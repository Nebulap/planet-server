package com.kai.planet.common.domain.request.server

import com.kai.planet.common.domain.entity.server.Remote
import java.time.LocalDateTime


/**
 *
 * @since 2024/11/12 9:46
 * @author 29002
 * @version 1.0.0
 */


data class AddServerRequest (
    val name: String, // 服务器名称
    val os: String?, // 操作系统（如：Ubuntu 20.04, Windows Server）
    val ip: String, // 公网 IP 地址
    val region: String?, // 数据中心区域（如：北京，上海）
    val instanceType: String?, // 实例类型（如：轻量级服务器）

    val createdAt: LocalDateTime? = LocalDateTime.now(), // 创建时间（ISO 格式）
    val updatedAt: LocalDateTime? = LocalDateTime.now(), // 最后更新时间（ISO 格式）

    val tags: String?, // 服务器标签，用逗号分隔

    val billingMode: String?, // 计费模式 ("hourly", "monthly", "prepaid")
    val sshKeys: String?, // 与服务器关联的 SSH 密钥 ID 数组，用逗号分隔

    val remote: Remote, // 远程服务器配置
)
