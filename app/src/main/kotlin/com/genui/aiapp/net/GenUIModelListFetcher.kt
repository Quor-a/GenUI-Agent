package com.genui.aiapp.net

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.util.Locale
import java.util.concurrent.TimeUnit

/**
 * 模型列表获取器
 * 从 OpenAI 兼容的 /models 端点获取模型列表
 * 同时支持 Ollama /api/tags 格式
 *
 * @param connectTimeout 连接超时（秒）
 * @param readTimeout 读取超时（秒）
 * @param client OkHttpClient 实例
 */
class GenUIModelListFetcher(
    connectTimeout: Long = 30L,
    readTimeout: Long = 60L,
    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(connectTimeout, TimeUnit.SECONDS)
        .readTimeout(readTimeout, TimeUnit.SECONDS)
        .build()
) {

    /**
     * 获取模型列表
     *
     * @param baseUrl API 基础 URL
     * @param apiKey API 密钥
     * @return 模型列表结果
     */
    suspend fun fetch(baseUrl: String, apiKey: String): GenUIModelListResult =
        withContext(Dispatchers.IO) {
            try {
                val isOllama = isLikelyOllama(baseUrl)
                val url = if (isOllama) {
                    "$baseUrl/api/tags"
                } else {
                    "$baseUrl/models".trimEnd('/')
                }

                val requestBuilder = Request.Builder().url(url).get()
                if (apiKey.isNotBlank()) {
                    requestBuilder.addHeader("Authorization", "Bearer $apiKey")
                }
                val request = requestBuilder.build()

                client.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) {
                        val code = response.code
                        val body = response.body?.string() ?: ""
                        GenUIModelListResult.Error("HTTP $code: ${body.ifBlank { response.message }}")
                    } else {
                        val body = response.body?.string() ?: ""
                        val models = if (isOllama) {
                            parseOllamaTags(body)
                        } else {
                            parseModels(body)
                        }
                        GenUIModelListResult.Success(models)
                    }
                }
            } catch (e: Exception) {
                GenUIModelListResult.Error(e.message ?: "Unknown error")
            }
        }

    /**
     * 解析 OpenAI 兼容格式的模型列表
     */
    fun parseModels(json: String): List<GenUIModelInfo> {
        val data = JSONObject(json).getJSONArray("data")
        val result = mutableListOf<GenUIModelInfo>()
        for (i in 0 until data.length()) {
            val obj = data.getJSONObject(i)
            val id = obj.optString("id", "").trim()
            if (id.isNotEmpty()) {
                var contextLength = obj.optInt("context_length", 0)
                if (contextLength <= 0) {
                    contextLength = obj.optInt("context_window", 0)
                }
                result.add(GenUIModelInfo(id, contextLength))
            }
        }
        return result
    }

    /**
     * 解析 Ollama 格式的模型标签
     * 失败时回退到 parseModels
     */
    fun parseOllamaTags(json: String): List<GenUIModelInfo> {
        return try {
            val models = JSONObject(json).getJSONArray("models")
            val result = mutableListOf<GenUIModelInfo>()
            for (i in 0 until models.length()) {
                val name = models.getJSONObject(i).optString("name", "").trim()
                if (name.isNotEmpty()) {
                    result.add(GenUIModelInfo(name, 0))
                }
            }
            result
        } catch (_: Exception) {
            parseModels(json)
        }
    }

    /** 判断是否可能是 Ollama 服务 */
    private fun isLikelyOllama(url: String): Boolean {
        val lower = url.lowercase(Locale.ROOT)
        return ":11434" in lower || "ollama" in lower
    }

    /** 判断是否为回环地址 */
    private fun isLoopback(url: String): Boolean {
        val lower = url.lowercase(Locale.ROOT)
        return "localhost" in lower || "127.0.0.1" in lower || "10.0.2.2" in lower || "[::1]" in lower
    }
}
