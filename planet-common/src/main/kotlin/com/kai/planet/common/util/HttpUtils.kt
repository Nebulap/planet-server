package com.kai.planet.common.util

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper
import org.springframework.web.util.WebUtils
import java.nio.charset.StandardCharsets

object HttpUtils {

    /**
     * Get request body
     *
     * @param request  Request object
     * @return A string representation of the request body
     */
    fun getRequestBody(request: HttpServletRequest): String {
        val wrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper::class.java)
        return wrapper?.let {
            String(it.contentAsByteArray, StandardCharsets.UTF_8)
        } ?: ""
    }

    /**
     *  Get response body
     *
     * @param response  Response object
     * @return A string representation of the response body
     */
    fun getResponseBody(response: HttpServletResponse): String {
        val wrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper::class.java)
        return wrapper?.let {
            String(it.contentAsByteArray, StandardCharsets.UTF_8)
        } ?: ""
    }
}
