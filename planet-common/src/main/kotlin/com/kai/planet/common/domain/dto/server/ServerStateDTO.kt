package com.kai.planet.common.domain.dto.server

import com.fasterxml.jackson.annotation.JsonProperty
import com.kai.planet.common.constants.server.ServerState
import java.time.LocalDateTime

/**
 *
 * @since 2024/11/10 22:15
 * @author 29002
 * @version 1.0.0
 */


open class ServerStateDTO(
    @JsonProperty("status") var status: String? = ServerState.UNKNOWN, // 服务器状态
    @JsonProperty("cpuUsage") val cpuUsage: String = "--",  // 当前 CPU 使用率（百分比）
    @JsonProperty("memTotal") val memUsage: String = "--",   // 内存总量（MB）
    @JsonProperty("memFree") val memFree: String = "--",   // 当前空闲内存（MB）
    @JsonProperty("diskTotal") val diskTotal: String = "--",   // 磁盘空间总量 (GB)
    @JsonProperty("diskUsed") val diskUsage: String = "--",   // 已用磁盘空间（GB）
//    @JsonProperty("networkIn") val networkIn: Long, // 入站网络流量（字节）
//    @JsonProperty("networkOut") val networkOut: Long,    // 出站网络流量（字节）
    @JsonProperty("lastUpdated") var lastUpdated: LocalDateTime? = LocalDateTime.now()  // 最后更新时间（ISO 格式）
)
