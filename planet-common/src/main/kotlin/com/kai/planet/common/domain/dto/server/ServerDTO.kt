package com.kai.planet.common.domain.dto.server

import com.kai.planet.common.constants.server.ServerState
import java.time.LocalDateTime

/**
 *
 * @since 2024/11/10 22:36
 * @author 29002
 * @version 1.0.0
 */


open class ServerDTO(
    val id: Long, // 服务器的唯一标识符

    val name: String?, // 服务器名称
    val os: String?, // 操作系统（如：Ubuntu 20.04, Windows Server）
    val ip: String?, // 公网 IP 地址
    val region: String?, // 数据中心区域（如：北京，上海）
    val instanceType: String?, // 实例类型（如：t2.micro, m5.large）

    val createdAt: LocalDateTime = LocalDateTime.now(), // 创建时间（ISO 格式）
    val updatedAt: LocalDateTime = LocalDateTime.now(), // 最后更新时间（ISO 格式）

    val tags: String?, // 服务器标签，用逗号分隔
    val cpuCores: Int?, // CPU 核心数
    val memory: Int?, // 总内存（MB 或 GB）
    val disk: Int?, // 磁盘总容量（GB）

    val billingMode: String?, // 计费模式 ("hourly", "monthly", "prepaid")
    val sshKeys: String?, // 与服务器关联的 SSH 密钥 ID 数组，用逗号分隔
    val restartCount: Int?,// 重启次数

    var state: String? = ServerState.LOADING, // 服务器的状态
)
