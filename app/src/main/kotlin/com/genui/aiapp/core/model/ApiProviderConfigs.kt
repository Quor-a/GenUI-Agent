package com.genui.aiapp.core.model

/**
 * API 提供商类型枚举
 *
 * @property providerId 提供商ID标识
 * @property displayName 显示名称
 */
enum class ApiProviderType(
    val providerId: String,
    val displayName: String
) {
    OPENAI("OPENAI", "OpenAI"),
    OPENAI_RESPONSES("OPENAI_RESPONSES", "OpenAI Responses"),
    OPENAI_RESPONSES_GENERIC("OPENAI_RESPONSES_GENERIC", "OpenAI Responses (Generic)"),
    OPENAI_GENERIC("OPENAI_GENERIC", "OpenAI Compatible"),
    ANTHROPIC("ANTHROPIC", "Anthropic"),
    ANTHROPIC_GENERIC("ANTHROPIC_GENERIC", "Anthropic (Generic)"),
    GOOGLE("GOOGLE", "Google"),
    GEMINI_GENERIC("GEMINI_GENERIC", "Gemini (Generic)"),
    DEEPSEEK("DEEPSEEK", "DeepSeek"),
    BAIDU("BAIDU", "Baidu"),
    ALIYUN("ALIYUN", "Aliyun"),
    XUNFEI("XUNFEI", "Xunfei"),
    ZHIPU("ZHIPU", "Zhipu"),
    BAICHUAN("BAICHUAN", "Baichuan"),
    MOONSHOT("MOONSHOT", "Moonshot"),
    MIMO("MIMO", "MiMo"),
    MISTRAL("MISTRAL", "Mistral"),
    SILICONFLOW("SILICONFLOW", "SiliconFlow"),
    IFLOW("IFLOW", "iFlow"),
    OPENROUTER("OPENROUTER", "OpenRouter"),
    FOUR_ROUTER("FOUR_ROUTER", "4Router"),
    NOUS_PORTAL("NOUS_PORTAL", "Nous Portal"),
    INFINIAI("INFINIAI", "InfiniAI"),
    ALIPAY_BAILING("ALIPAY_BAILING", "Alipay Bailing"),
    DOUBAO("DOUBAO", "Doubao"),
    NVIDIA("NVIDIA", "NVIDIA"),
    LMSTUDIO("LMSTUDIO", "LM Studio"),
    OLLAMA("OLLAMA", "Ollama"),
    OPENAI_LOCAL("OPENAI_LOCAL", "OpenAI Local"),
    PPINFRA("PPINFRA", "PPInfra"),
    NOVITA("NOVITA", "Novita"),
    OPENCODE_GO("OPENCODE_GO", "OpenCode Go"),
    MNN("MNN", "MNN"),
    LLAMA_CPP("LLAMA_CPP", "llama.cpp"),
    OTHER("OTHER", "Other");

    companion object {
        /** 根据 provider ID 查找枚举值 */
        fun fromProviderTypeId(id: String): ApiProviderType? {
            return entries.firstOrNull { it.providerId == id }
        }
    }
}

/**
 * 提供商端点选项
 *
 * @property endpoint 端点URL
 * @property label 显示标签
 */
data class ProviderEndpointOption(
    val endpoint: String,
    val label: String
)

/**
 * 提供商 API 配置
 *
 * @property providerType 提供商类型
 * @property defaultModelName 默认模型名
 * @property defaultApiEndpoint 默认API端点
 * @property endpointOptions 可选端点列表
 * @property requiresApiKey 是否需要API Key
 */
data class ProviderApiConfig(
    val providerType: ApiProviderType,
    val defaultModelName: String = "",
    val defaultApiEndpoint: String = "",
    val endpointOptions: List<ProviderEndpointOption> = emptyList(),
    val requiresApiKey: Boolean = true
)

/**
 * API 提供商配置单例
 * 包含所有支持的提供商配置，以及URL检测、回环检测等工具方法
 */
object ApiProviderConfigs {

    private val configs: Map<ApiProviderType, ProviderApiConfig> = mapOf(
        ApiProviderType.OPENAI to ProviderApiConfig(
            ApiProviderType.OPENAI,
            "gpt-4o",
            "https://api.openai.com/v1"
        ),
        ApiProviderType.OPENAI_RESPONSES to ProviderApiConfig(
            ApiProviderType.OPENAI_RESPONSES,
            "gpt-4o",
            "https://api.openai.com/v1/responses"
        ),
        ApiProviderType.OPENAI_RESPONSES_GENERIC to ProviderApiConfig(
            ApiProviderType.OPENAI_RESPONSES_GENERIC
        ),
        ApiProviderType.OPENAI_GENERIC to ProviderApiConfig(
            ApiProviderType.OPENAI_GENERIC
        ),
        ApiProviderType.ANTHROPIC to ProviderApiConfig(
            ApiProviderType.ANTHROPIC,
            "claude-3-opus-20240229",
            "https://api.anthropic.com/v1/messages"
        ),
        ApiProviderType.ANTHROPIC_GENERIC to ProviderApiConfig(
            ApiProviderType.ANTHROPIC_GENERIC
        ),
        ApiProviderType.GOOGLE to ProviderApiConfig(
            ApiProviderType.GOOGLE,
            "gemini-2.0-flash",
            "https://generativelanguage.googleapis.com/v1beta/models"
        ),
        ApiProviderType.GEMINI_GENERIC to ProviderApiConfig(
            ApiProviderType.GEMINI_GENERIC,
            "gemini-2.0-flash"
        ),
        ApiProviderType.DEEPSEEK to ProviderApiConfig(
            ApiProviderType.DEEPSEEK,
            "deepseek-v4-flash",
            "https://api.deepseek.com/v1"
        ),
        ApiProviderType.BAIDU to ProviderApiConfig(
            ApiProviderType.BAIDU,
            "ernie-bot-4",
            "https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat"
        ),
        ApiProviderType.ALIYUN to ProviderApiConfig(
            ApiProviderType.ALIYUN,
            "qwen-max",
            "https://dashscope.aliyuncs.com/compatible-mode/v1"
        ),
        ApiProviderType.XUNFEI to ProviderApiConfig(
            ApiProviderType.XUNFEI,
            "spark3.5",
            "https://spark-api-open.xf-yun.com/v2"
        ),
        ApiProviderType.ZHIPU to ProviderApiConfig(
            ApiProviderType.ZHIPU,
            "glm-4.5",
            "https://open.bigmodel.cn/api/paas/v4",
            listOf(
                ProviderEndpointOption("https://open.bigmodel.cn/api/paas/v4", "CN standard"),
                ProviderEndpointOption("https://open.bigmodel.cn/api/coding/paas/v4", "CN coding"),
                ProviderEndpointOption("https://api.z.ai/api/paas/v4", "International standard"),
                ProviderEndpointOption("https://api.z.ai/api/coding/paas/v4", "International coding")
            )
        ),
        ApiProviderType.BAICHUAN to ProviderApiConfig(
            ApiProviderType.BAICHUAN,
            "baichuan4",
            "https://api.baichuan-ai.com/v1"
        ),
        ApiProviderType.MOONSHOT to ProviderApiConfig(
            ApiProviderType.MOONSHOT,
            "moonshot-v1-128k",
            "https://api.moonshot.cn/v1",
            listOf(
                ProviderEndpointOption("https://api.moonshot.cn/v1", "China (moonshot.cn)"),
                ProviderEndpointOption("https://api.moonshot.ai/v1", "International (moonshot.ai)"),
                ProviderEndpointOption("https://api.kimi.com/coding/v1", "Kimi Code (api.kimi.com)")
            )
        ),
        ApiProviderType.MIMO to ProviderApiConfig(
            ApiProviderType.MIMO,
            "mimo-v2.5-pro",
            "https://api.xiaomimimo.com/v1"
        ),
        ApiProviderType.MISTRAL to ProviderApiConfig(
            ApiProviderType.MISTRAL,
            "codestral-latest",
            "https://codestral.mistral.ai/v1"
        ),
        ApiProviderType.SILICONFLOW to ProviderApiConfig(
            ApiProviderType.SILICONFLOW,
            "yi-1.5-34b",
            "https://api.siliconflow.cn/v1"
        ),
        ApiProviderType.IFLOW to ProviderApiConfig(
            ApiProviderType.IFLOW,
            "TBStars2-200B-A13B",
            "https://apis.iflow.cn/v1"
        ),
        ApiProviderType.OPENROUTER to ProviderApiConfig(
            ApiProviderType.OPENROUTER,
            "google/gemini-pro",
            "https://openrouter.ai/api/v1"
        ),
        ApiProviderType.FOUR_ROUTER to ProviderApiConfig(
            ApiProviderType.FOUR_ROUTER,
            "gpt-5.4-mini",
            "https://4router.net/v1"
        ),
        ApiProviderType.NOUS_PORTAL to ProviderApiConfig(
            ApiProviderType.NOUS_PORTAL,
            defaultApiEndpoint = "https://inference-api.nousresearch.com/v1"
        ),
        ApiProviderType.INFINIAI to ProviderApiConfig(
            ApiProviderType.INFINIAI,
            "infini-mini",
            "https://cloud.infini-ai.com/maas/v1"
        ),
        ApiProviderType.ALIPAY_BAILING to ProviderApiConfig(
            ApiProviderType.ALIPAY_BAILING,
            "Ling-1T",
            "https://api.tbox.cn/api/llm/v1"
        ),
        ApiProviderType.DOUBAO to ProviderApiConfig(
            ApiProviderType.DOUBAO,
            "Doubao-pro-4k",
            "https://ark.cn-beijing.volces.com/api/v3",
            listOf(
                ProviderEndpointOption("https://ark.cn-beijing.volces.com/api/v3", "CN standard"),
                ProviderEndpointOption("https://ark.cn-beijing.volces.com/api/coding/v3", "CN coding")
            )
        ),
        ApiProviderType.NVIDIA to ProviderApiConfig(
            ApiProviderType.NVIDIA,
            "nvidia/nemotron-3-nano-30b-a3b",
            "https://integrate.api.nvidia.com/v1"
        ),
        ApiProviderType.LMSTUDIO to ProviderApiConfig(
            ApiProviderType.LMSTUDIO,
            "meta-llama-3.1-8b-instruct",
            "http://localhost:1234/v1",
            requiresApiKey = false
        ),
        ApiProviderType.OLLAMA to ProviderApiConfig(
            ApiProviderType.OLLAMA,
            defaultApiEndpoint = "http://localhost:11434/v1",
            requiresApiKey = false
        ),
        ApiProviderType.OPENAI_LOCAL to ProviderApiConfig(
            ApiProviderType.OPENAI_LOCAL,
            defaultApiEndpoint = "http://localhost:8000/v1",
            requiresApiKey = false
        ),
        ApiProviderType.MNN to ProviderApiConfig(
            ApiProviderType.MNN,
            requiresApiKey = false
        ),
        ApiProviderType.LLAMA_CPP to ProviderApiConfig(
            ApiProviderType.LLAMA_CPP,
            requiresApiKey = false
        ),
        ApiProviderType.PPINFRA to ProviderApiConfig(
            ApiProviderType.PPINFRA,
            "gpt-4o-mini",
            "https://api.ppinfra.com/openai/v1"
        ),
        ApiProviderType.NOVITA to ProviderApiConfig(
            ApiProviderType.NOVITA,
            "moonshotai/kimi-k2.5",
            "https://api.novita.ai/openai/v1",
            listOf(
                ProviderEndpointOption("https://api.novita.ai/openai/v1", "OpenAI-compatible"),
                ProviderEndpointOption("https://api.novita.ai/anthropic/v1/messages", "Anthropic-compatible")
            )
        ),
        ApiProviderType.OPENCODE_GO to ProviderApiConfig(
            ApiProviderType.OPENCODE_GO,
            "deepseek-v4-flash",
            "https://opencode.ai/zen/go/v1",
            listOf(
                ProviderEndpointOption("https://opencode.ai/zen/go/v1", "OpenAI 兼容 (/v1)")
            ),
            requiresApiKey = true
        ),
        ApiProviderType.OTHER to ProviderApiConfig(
            ApiProviderType.OTHER
        )
    )

    /**
     * 根据URL自动检测提供商类型
     */
    fun detectProviderFromUrl(url: String): ApiProviderType {
        val lower = url.lowercase()
        return when {
            "api.openai.com" in lower -> ApiProviderType.OPENAI
            "api.anthropic.com" in lower -> ApiProviderType.ANTHROPIC
            "generativelanguage.googleapis.com" in lower -> ApiProviderType.GOOGLE
            "api.deepseek.com" in lower -> ApiProviderType.DEEPSEEK
            "aip.baidubce.com" in lower -> ApiProviderType.BAIDU
            "dashscope.aliyuncs.com" in lower -> ApiProviderType.ALIYUN
            "spark-api-open.xf-yun.com" in lower -> ApiProviderType.XUNFEI
            "open.bigmodel.cn" in lower -> ApiProviderType.ZHIPU
            "api.baichuan-ai.com" in lower -> ApiProviderType.BAICHUAN
            "api.moonshot.cn" in lower || "api.moonshot.ai" in lower -> ApiProviderType.MOONSHOT
            "api.xiaomimimo.com" in lower -> ApiProviderType.MIMO
            "codestral.mistral.ai" in lower -> ApiProviderType.MISTRAL
            "api.siliconflow.cn" in lower -> ApiProviderType.SILICONFLOW
            "apis.iflow.cn" in lower -> ApiProviderType.IFLOW
            "openrouter.ai" in lower -> ApiProviderType.OPENROUTER
            "4router.net" in lower -> ApiProviderType.FOUR_ROUTER
            "inference-api.nousresearch.com" in lower -> ApiProviderType.NOUS_PORTAL
            "cloud.infini-ai.com" in lower -> ApiProviderType.INFINIAI
            "api.tbox.cn" in lower -> ApiProviderType.ALIPAY_BAILING
            "ark.cn-beijing.volces.com" in lower -> ApiProviderType.DOUBAO
            "integrate.api.nvidia.com" in lower -> ApiProviderType.NVIDIA
            "lmstudio" in lower || "localhost:1234" in lower -> ApiProviderType.LMSTUDIO
            "localhost:11434" in lower || "127.0.0.1:11434" in lower -> ApiProviderType.OLLAMA
            "api.ppinfra.com" in lower -> ApiProviderType.PPINFRA
            "api.novita.ai" in lower -> ApiProviderType.NOVITA
            else -> ApiProviderType.OPENAI_GENERIC
        }
    }

    /** 获取指定提供商的配置 */
    operator fun get(providerType: ApiProviderType): ProviderApiConfig {
        return configs[providerType] ?: ProviderApiConfig(providerType)
    }

    /** 获取默认模型名 */
    fun getDefaultModelName(providerType: ApiProviderType): String {
        return get(providerType).defaultModelName
    }

    /** 获取默认API端点 */
    fun getDefaultApiEndpoint(providerType: ApiProviderType): String {
        return get(providerType).defaultApiEndpoint
    }

    /** 获取端点选项列表，如无可选返回null */
    fun getEndpointOptions(providerType: ApiProviderType): List<ProviderEndpointOption>? {
        val options = get(providerType).endpointOptions
        return options.ifEmpty { null }
    }

    /**
     * 判断是否需要API Key
     * 本地回环地址即使配置了也不需要
     */
    fun requiresApiKey(providerType: ApiProviderType, apiEndpoint: String = ""): Boolean {
        if (!get(providerType).requiresApiKey) return false
        return !isLoopbackEndpoint(apiEndpoint)
    }

    /** 根据providerTypeId判断是否需要API Key */
    fun requiresApiKey(providerTypeId: String, apiEndpoint: String = ""): Boolean {
        val type = ApiProviderType.fromProviderTypeId(providerTypeId)
            ?: return !isLoopbackEndpoint(apiEndpoint)
        return requiresApiKey(type, apiEndpoint)
    }

    /** 判断模型名是否为某个提供商的默认值 */
    fun isDefaultModelName(modelName: String): Boolean {
        return configs.values.any { it.defaultModelName == modelName }
    }

    /** 判断端点是否为某个提供商的默认值 */
    fun isDefaultApiEndpoint(endpoint: String): Boolean {
        return configs.values.any { it.defaultApiEndpoint == endpoint }
    }

    /** 判断是否为回环（本地）端点 */
    fun isLoopbackEndpoint(apiEndpoint: String): Boolean {
        if (apiEndpoint.isBlank()) return false
        val lower = apiEndpoint.trim().lowercase()
        return try {
            val host = java.net.URI(apiEndpoint).host
            if (host != null) isLoopbackHost(host) else isLoopbackEndpointText(lower)
        } catch (_: Exception) {
            isLoopbackEndpointText(lower)
        }
    }

    private fun isLoopbackHost(host: String): Boolean {
        return when (host.lowercase().trim('[', ']')) {
            "localhost", "127.0.0.1", "0.0.0.0", "10.0.2.2", "::1" -> true
            else -> false
        }
    }

    private fun isLoopbackEndpointText(text: String): Boolean {
        return text.startsWith("localhost:") ||
                text.startsWith("127.0.0.1:") ||
                text.startsWith("[::1]:") ||
                text.startsWith("::1:") ||
                text.startsWith("0.0.0.0:") ||
                text.startsWith("10.0.2.2:")
    }
}
