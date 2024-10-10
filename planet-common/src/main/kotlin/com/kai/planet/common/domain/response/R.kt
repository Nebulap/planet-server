package com.kai.planet.common.domain.response

/**
 * A generic response class to encapsulate API responses.
 *
 * @param T The type of the data contained in the response.
 * @property code An integer representing the status code of the response.
 * @property msg A string containing the message related to the response.
 * @property data The data of type T returned in the response, can be null.
 *
 * @since 10/7/2024 12:31 AM
 * @author 29002
 * @version 1.0.0
 */
class R<T> {

    var code: Int = 0
    var msg: String = ""
    var data: T? = null

    constructor()

    constructor(code: Int, msg: String, data: T?) {
        this.code = code
        this.msg = msg
        this.data = data
    }

    companion object {
        /**
         * Creates a successful response with the given data.
         *
         * @param data The data to be included in the response.
         * @return An instance of R<T> representing a successful response.
         */
        fun <T> ok(data: T?): R<T> {
            return R(0, "ok", data)
        }

        /**
         * Creates a failed response with the specified message and code.
         *
         * @param msg The message describing the failure.
         * @param code The status code representing the type of failure.
         * @return An instance of R<Void> representing a failed response.
         */
        fun fail(msg: String, code: Int): R<Void> {
            return R(code, msg, null)
        }
    }
}
