package com.genui.aiapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.genui.aiapp.core.model.ApiProviderConfigs
import com.genui.aiapp.core.model.ApiProviderType
import com.genui.aiapp.core.model.GenUIModelConfigData
import com.genui.aiapp.core.model.GenUIModelConfigDataRepository
import com.genui.aiapp.net.GenUILlmClient
import com.genui.aiapp.net.GenUIModelListFetcher
import com.genui.aiapp.net.GenUIModelListResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 模型配置 ViewModel
 * 管理模型配置的编辑、保存、模型列表获取、连接测试等
 */
class GenUIModelConfigViewModel(context: Context) : ViewModel() {

    private val repo: GenUIModelConfigDataRepository
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val fetcher = GenUIModelListFetcher()
    private val llmClient = GenUILlmClient()

    private val _cfg: MutableStateFlow<GenUIModelConfigData>
    val cfg: StateFlow<GenUIModelConfigData>

    private val _modelList = MutableStateFlow<GenUIModelListResult?>(null)
    val modelList: StateFlow<GenUIModelListResult?> = _modelList.asStateFlow()

    private val _isFetchingModels = MutableStateFlow(false)
    val isFetchingModels: StateFlow<Boolean> = _isFetchingModels.asStateFlow()

    private val _isTesting = MutableStateFlow(false)
    val isTesting: StateFlow<Boolean> = _isTesting.asStateFlow()

    private val _testResult = MutableStateFlow<String?>(null)
    val testResult: StateFlow<String?> = _testResult.asStateFlow()

    init {
        val appContext = context.applicationContext
        repo = GenUIModelConfigDataRepository(appContext)
        val initialConfig = repo.load()
        _cfg = MutableStateFlow(initialConfig)
        cfg = _cfg.asStateFlow()

        // 加载缓存的模型列表
        _modelList.value = repo.loadModelListCache(initialConfig.baseUrl)
    }

    fun getRepo(): GenUIModelConfigDataRepository = repo

    /**
     * 更新配置
     * 如果 baseUrl 变化，自动检测提供商类型
     */
    fun update(block: (GenUIModelConfigData) -> GenUIModelConfigData) {
        val newConfig = block(_cfg.value)

        // 检测 baseUrl 变化，自动设置提供商
        if (newConfig.baseUrl != _cfg.value.baseUrl && newConfig.baseUrl.isNotBlank()) {
            val detected = ApiProviderConfigs.detectProviderFromUrl(newConfig.baseUrl)
            if (detected.name != newConfig.provider) {
                _cfg.value = newConfig.copy(provider = detected.name)
                repo.save(_cfg.value)
                return
            }
        }

        _cfg.value = newConfig
        repo.save(newConfig)
    }

    /** 保存当前配置 */
    fun save() {
        repo.save(_cfg.value)
    }

    /** 重新加载配置 */
    fun reload() {
        _cfg.value = repo.load()
    }

    /** 加载缓存的模型列表 */
    fun loadCachedModels() {
        _modelList.value = repo.loadModelListCache(_cfg.value.baseUrl)
    }

    /**
     * 远程获取模型列表
     */
    fun fetchModels() {
        _isFetchingModels.value = true
        _modelList.value = null

        scope.launch {
            val current = _cfg.value
            val result = fetcher.fetch(current.baseUrl, current.apiKey)
            _modelList.value = result
            _isFetchingModels.value = false

            // 缓存结果
            if (result is GenUIModelListResult.Success) {
                repo.saveModelListCache(current.baseUrl, result)
            }
        }
    }

    /** 清空模型列表 */
    fun clearModelList() {
        _modelList.value = null
    }

    /**
     * 测试连接
     */
    fun testConnection() {
        val current = _cfg.value
        if (current.baseUrl.isBlank() || current.model.isBlank()) {
            _testResult.value = "❌ 请先填写 Base URL 和模型名"
            return
        }

        _isTesting.value = true
        _testResult.value = null

        scope.launch {
            val error = llmClient.testConnection(
                baseUrl = current.baseUrl,
                apiKey = current.apiKey,
                model = current.model
            )
            _testResult.value = if (error == null) {
                "✅ 连接成功！模型响应正常。"
            } else {
                "❌ 连接失败：$error"
            }
            _isTesting.value = false
        }
    }

    /** 清除测试结果 */
    fun clearTestResult() {
        _testResult.value = null
    }

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }

    /**
     * ViewModel Factory 工厂方法
     * 接收 Context 参数，不再依赖全局单例
     */
    companion object {
        fun factory(context: Context): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return GenUIModelConfigViewModel(context.applicationContext) as T
            }
        }
    }
}
