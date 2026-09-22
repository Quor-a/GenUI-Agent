package com.genui.aiapp.core.model

/**
 * 保存的配置档案
 * 用于快速切换不同的模型配置
 *
 * @property id 档案ID
 * @property name 档案名称
 * @property provider 提供商类型
 * @property baseUrl API基础URL
 * @property apiKey API密钥
 * @property model 模型名称
 * @property temperature 温度参数
 * @property maxTokens 最大输出token数
 * @property enableTools 是否启用工具
 * @property maxToolRounds 最大工具调用轮次
 * @property contextWindow 上下文窗口大小
 * @property customProviderName 自定义提供商名称
 */
data class GenUISavedProfile(
    val id: String,
    val name: String,
    val provider: String,
    val baseUrl: String,
    val apiKey: String,
    val model: String,
    val temperature: Float,
    val maxTokens: Int,
    val enableTools: Boolean,
    val maxToolRounds: Int,
    val contextWindow: Int,
    val customProviderName: String = ""
)
