package com.kai.planet.gateway.auth

import cn.dev33.satoken.exception.NotLoginException
import cn.dev33.satoken.exception.NotRoleException
import cn.dev33.satoken.exception.SaTokenException
import cn.dev33.satoken.exception.StopMatchException
import cn.dev33.satoken.reactor.context.SaReactorHolder
import cn.dev33.satoken.reactor.context.SaReactorSyncHolder
import cn.dev33.satoken.reactor.filter.SaReactorFilter
import cn.dev33.satoken.router.SaRouter
import cn.dev33.satoken.router.SaRouterStaff
import cn.dev33.satoken.stp.StpUtil
import com.alibaba.fastjson2.JSON
import com.kai.planet.common.domain.response.R
import com.kai.planet.common.exception.GlobalExceptionCode
import org.springframework.http.HttpHeaders
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

/**
 *
 * @since 10/10/2024 11:46 PM
 * @author 29002
 * @version 1.0.0
 */


class MySaReactorFilter : SaReactorFilter() {


    init {
        addInclude("/**")
        addExclude("/favicon.ico")
        setAuth {
            SaRouter.match("/**", "/user/sign-in") { _: SaRouterStaff? -> StpUtil.checkLogin() }
        }
    }

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        exchange.attributes["WEB_FILTER_CHAIN_KEY"] = chain

        return try {
            SaReactorSyncHolder.setContext(exchange)
            this.beforeAuth.run(null)

            SaRouter.match(this.includeList).notMatch(this.excludeList).check { _ ->
                this.auth.run(null)
            }

            chain.filter(exchange).contextWrite { ctx ->
                ctx.put(SaReactorHolder.CONTEXT_KEY, exchange)
            }.doFinally {
                SaReactorSyncHolder.clearContext()
            }
        } catch (e: StopMatchException) {
            chain.filter(exchange).contextWrite { ctx ->
                ctx.put(SaReactorHolder.CONTEXT_KEY, exchange)
            }.doFinally {
                SaReactorSyncHolder.clearContext()
            }
        } catch (e: Exception) {

            val result = handleSaTokenException(e)

            if (exchange.response.headers.getFirst(HttpHeaders.CONTENT_TYPE) == null) {
                exchange.response.headers.set(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8")
            }

            exchange.response.writeWith(
                Mono.just(
                    exchange.response.bufferFactory().wrap(JSON.toJSONBytes(result))
                )
            )
        } finally {
            SaReactorSyncHolder.clearContext()
        }
    }

    private fun handleSaTokenException(exception: Exception): R<Void> {
        val exceptionMap = HashMap<Class<out SaTokenException>, GlobalExceptionCode>()
        // Create a map with the exception class and its corresponding code
        exceptionMap[NotLoginException::class.java] = GlobalExceptionCode.NOT_LOGIN_ERROR
        exceptionMap[NotRoleException::class.java] = GlobalExceptionCode.NOT_ROLE_ERROR

        println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>.")
        println(exceptionMap[exception::class.java])
        val exceptionCode = exceptionMap[exception::class.java] ?: GlobalExceptionCode.PERMISSION_ERROR
        return R.fail(exceptionCode.msg, exceptionCode.code)
    }
}
