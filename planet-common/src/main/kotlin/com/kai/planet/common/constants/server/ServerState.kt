package com.kai.planet.common.constants.server

/**
 *
 * @since 2024/11/10 21:53
 * @author 29002
 * @version 1.0.0
 */


object ServerState {
    const val RUNNING = "running"  // 运行中
    const val STOPPED = "stopped" // 已停止
    const val STARTING = "starting" // 启动中
    const val STOPPING = "stopping" // 停止中
    const val RESTARTING = "restarting" // 重启中
    const val ERROR = "error" // 异常
    const val UNKNOWN = "unknown" // 未知
    const val LOADING = "loading" // 加载中
}
