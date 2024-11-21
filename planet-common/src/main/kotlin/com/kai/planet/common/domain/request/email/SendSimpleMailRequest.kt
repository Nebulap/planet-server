package com.kai.planet.common.domain.request.email

/**
 *
 * @since 2024/11/18 21:02
 * @author 29002
 * @version 1.0.0
 */


open class SendSimpleMailRequest(
    val to: String = "",                 // 收件人地址
    val cc: String? = null,         // 抄送地址（可选）
    val bcc: String? = null,        // 密送地址（可选）
    val subject: String = "",            // 邮件主题
    val body: String = "",               // 邮件正文
    val attachments: List<String>? = null,  // 附件路径（可选）
    val priority: String? = null,   // 优先级（如 High, Normal, Low）（可选）
    val replyTo: String? = null,    // 回复地址（可选）
    val from: String? = null        // 发件人地址（可选）
)

