package com.kai.planet.mail.service.impl

import com.kai.planet.common.domain.request.email.SendSimpleMailRequest
import com.kai.planet.mail.service.EmailService
import jakarta.mail.internet.MimeMessage
import org.springframework.mail.MailException
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service


@Service
class EmailServiceImpl(
    private val javaMailSender: JavaMailSender
) : EmailService {

    override fun sendSimpleMail(request: SendSimpleMailRequest) {
        try {
            // 创建邮件对象
            val message: MimeMessage = javaMailSender.createMimeMessage()

            // 使用 MimeMessageHelper 来设置邮件内容
            val helper = MimeMessageHelper(message, true)  // true 表示支持多媒体邮件（如附件）

            // 设置邮件的基本信息
            helper.setTo(request.to)                      // 收件人
            helper.setSubject(request.subject)            // 邮件主题
            helper.setText(request.body, true)             // 邮件正文，true 表示支持 HTML 格式
            helper.setFrom("Planet  <2643354082@qq.com>")

            // 设置抄送和密送
            request.cc?.let { helper.setCc(it) }
            request.bcc?.let { helper.setBcc(it) }

            // 设置回复地址
            request.replyTo?.let { helper.setReplyTo(it) }

            // 处理附件
            request.attachments?.forEach { attachment ->
                helper.addAttachment(attachment, java.io.File(attachment))  // 添加附件，路径作为文件
            }

            // 发送邮件
            javaMailSender.send(message)

            println("Email sent successfully.")
        } catch (e: MailException) {
            println("Failed to send email: ${e.message}")
            throw e  // 可抛出异常或者进行相应的错误处理
        }
    }
}
