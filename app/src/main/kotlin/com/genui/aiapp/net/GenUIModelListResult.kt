package com.genui.aiapp.net

/**
 * 模型列表获取结果
 */
sealed interface GenUIModelListResult {
    /**
     * 成功结果
     *
     * @property models 模型列表
     */
    data class Success(val models: List<GenUIModelInfo>) : GenUIModelListResult

    /**
     * 错误结果
     *
     * @property message 错误信息
     */
    data class Error(val message: String) : GenUIModelListResult
}
