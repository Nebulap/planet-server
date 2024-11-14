package com.kai.planet.common.domain.entity.server


import com.mybatisflex.annotation.Id
import com.mybatisflex.annotation.KeyType
import com.mybatisflex.annotation.Table
import java.time.LocalDateTime

/**
 *
 * @since 2024/11/10 21:41
 * @author 29002
 * @version 1.0.0
 */


@Table("t_server")
data class Server(
 @Id(keyType = KeyType.Auto)
 var id: Int? = null, // 服务器的唯一标识符

 var name: String?, // 服务器名称
 var os: String?, // 操作系统（如：Ubuntu 20.04, Windows Server）
 var ip: String?, // 公网 IP 地址
 var region: String?, // 数据中心区域（如：北京，上海）
 var instanceType: String?, // 实例类型（如：t2.micro, m5.large）

 var createdAt: LocalDateTime? = LocalDateTime.now(), // 创建时间（ISO 格式）
 var updatedAt: LocalDateTime? = LocalDateTime.now(), // 最后更新时间（ISO 格式）

 var tags: String?, // 服务器标签，用逗号分隔
 var cpuCores: Int?, // CPU 核心数
 var memory: Int?, // 总内存（MB 或 GB）
 var disk: Int?, // 磁盘总容量（GB）

 var billingMode: String?, // 计费模式 ("hourly", "monthly", "prepaid")
 var sshKeys: String?, // 与服务器关联的 SSH 密钥 ID 数组，用逗号分隔
 var restartCount: Int? // 重启次数
)

