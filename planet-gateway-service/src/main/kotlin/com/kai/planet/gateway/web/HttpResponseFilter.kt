package com.kai.planet.gateway.web

import com.alibaba.fastjson2.JSON
import com.alibaba.fastjson2.JSONObject
import com.kai.planet.common.domain.response.R
import org.reactivestreams.Publisher
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.cloud.gateway.filter.NettyWriteResponseFilter
import org.springframework.core.Ordered
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.core.io.buffer.DataBufferFactory
import org.springframework.core.io.buffer.DataBufferUtils
import org.springframework.core.io.buffer.DefaultDataBufferFactory
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.http.server.reactive.ServerHttpResponseDecorator
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.nio.charset.Charset


/**
 *
 * @since 10/7/2024 4:01 PM
 * @author 29002
 * @version 1.0.0
 */


//@Component
class HttpResponseFilter : GlobalFilter, Ordered {

    override fun filter(exchange: ServerWebExchange, chain: GatewayFilterChain): Mono<Void> {
        return requestDecorator(exchange, chain)
    }

    private fun requestDecorator(exchange: ServerWebExchange, chain: GatewayFilterChain): Mono<Void> {
        // Get response body
        val serverHttpResponseDecorator = CacheServerHttpResponseDecorator(exchange.response)
        return chain.filter(exchange.mutate().response(serverHttpResponseDecorator).build())
    }

    class CacheServerHttpResponseDecorator(
        response: ServerHttpResponse
    ) : ServerHttpResponseDecorator(response) {
        override fun writeWith(body: Publisher<out DataBuffer>): Mono<Void> {
            if (body !is Flux<*>) {
                return super.writeWith(body)
            }
            val fluxBody = body as Flux<out DataBuffer>

            val publisher = fluxBody.buffer().map { dataBuffer -> processDataBuffer(dataBuffer) }
            return super.writeWith(publisher)
        }

        override fun writeAndFlushWith(body: Publisher<out Publisher<out DataBuffer>>): Mono<Void> {
            return writeWith(Flux.from(body).flatMapSequential { p -> p })
        }

        private fun processDataBuffer(dataBuffers: List<DataBuffer>): DataBuffer {
            val dataBufferFactory: DataBufferFactory = DefaultDataBufferFactory()
            val join = dataBufferFactory.join(dataBuffers)
            val content = ByteArray(join.readableByteCount())
            join.read(content);
            DataBufferUtils.release(join);
            val responseString = String(content, Charset.forName("UTF-8"))

//            val content = ByteArray(dataBuffer.readableByteCount())
//            dataBuffer.read(content)
//            DataBufferUtils.release(dataBuffer)

            println(">>>>>>>>>>>>>>>>>>>>>>>封装")
//            val responseString = String(content, StandardCharsets.UTF_8)
            println(responseString)
            // 移除控制字符
            val sanitizedJson = responseString.replace(Regex("[\\x00-\\x1F\\x7F]"), "")
            var r: R<*>?
            try {
                r = JSON.parseObject(sanitizedJson, R::class.java)
            } catch (e: Exception) {
                if (sanitizedJson.startsWith("[")) {
                    println("列表")
                    r = R.ok(JSON.parseArray(sanitizedJson, JSONObject::class.java))
                } else if (sanitizedJson.startsWith("{")) {
                    println("对象")
                    r = R.ok(JSON.parseObject(sanitizedJson, JSONObject::class.java))
                } else {
                    println("字符串")
                    r = R.ok(sanitizedJson)
                }
            }
            return dataBufferFactory.wrap(JSON.toJSONBytes(r))
        }
    }

    override fun getOrder(): Int {
        return NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER - 1
    }
}


