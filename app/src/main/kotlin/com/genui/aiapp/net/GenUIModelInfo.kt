package com.genui.aiapp.net

/**
 * 模型信息数据类
 * 表示从 API 获取的单个模型的信息
 *
 * @property id 模型唯一标识（模型名称）
 * @property contextLength 上下文窗口长度（token 数），0 表示未知
 */
data class GenUIModelInfo(
    val id: String,
    val contextLength: Int = 0
)
