package com.kai.planet.gateway.web

import com.alibaba.fastjson2.JSON
import com.kai.planet.common.domain.response.R
import org.reactivestreams.Publisher
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.cloud.gateway.filter.NettyWriteResponseFilter
import org.springframework.core.Ordered
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.core.io.buffer.DataBufferFactory
import org.springframework.core.io.buffer.DataBufferUtils
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.http.server.reactive.ServerHttpResponseDecorator
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.nio.charset.StandardCharsets

/**
 *
 * @since 10/7/2024 4:01 PM
 * @author 29002
 * @version 1.0.0
 */


@Component
class HttpResponseFilter : GlobalFilter, Ordered {

    override fun filter(exchange: ServerWebExchange, chain: GatewayFilterChain): Mono<Void> {
        return requestDecorator(exchange, chain)
    }

    private fun requestDecorator(exchange: ServerWebExchange, chain: GatewayFilterChain): Mono<Void> {
        val bufferFactory: DataBufferFactory = exchange.response.bufferFactory()
        // Get response body
        val serverHttpResponseDecorator = CacheServerHttpResponseDecorator(exchange.response, bufferFactory)
        return chain.filter(exchange.mutate().response(serverHttpResponseDecorator).build())
    }

    class CacheServerHttpResponseDecorator(
        response: ServerHttpResponse,
        private val bufferFactory: DataBufferFactory
    ) : ServerHttpResponseDecorator(response) {
        override fun writeWith(body: Publisher<out DataBuffer>): Mono<Void> {
            if (body !is Flux<*>) {
                return super.writeWith(body)
            }
            val fluxBody = body as Flux<out DataBuffer>
            return super.writeWith(fluxBody.map { dataBuffer ->
                // Probably should reuse buffers
                val content = ByteArray(dataBuffer.readableByteCount())
                dataBuffer.read(content)
                DataBufferUtils.release(dataBuffer)
                // Convert response body
                val responseString = String(content, StandardCharsets.UTF_8)
                val r: R<*>? = try {
                    JSON.parseObject(responseString, R::class.java)
                }catch (e:Exception){
                    R.ok(JSON.parseObject(responseString))
                }
                bufferFactory.wrap(JSON.toJSONBytes(r))
            })
        }

        override fun writeAndFlushWith(body: Publisher<out Publisher<out DataBuffer>>): Mono<Void> {
            return writeWith(Flux.from(body).flatMapSequential { p -> p })
        }

    }

    override fun getOrder(): Int {
        return NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER - 1
    }
}
