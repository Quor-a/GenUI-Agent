package com.genui.aiapp.tools

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.util.DisplayMetrics
import android.view.WindowManager
import org.json.JSONArray
import org.json.JSONObject

/**
 * 设备信息工具
 *
 * 获取当前设备的屏幕尺寸、密度、系统版本等信息，
 * 用于 AI 设计适配设备的 UI 界面。
 *
 * 参数：无（空对象）
 */
class DeviceInfoTool(private val context: Context) : GenUITool {

    override val name: String = "get_device_info"

    override val description: String =
        "获取当前设备的屏幕尺寸、密度、系统版本等信息，用于设计适配设备的 UI 界面。" +
        "在生成 UI 之前调用此工具了解设备屏幕大小和能力。"

    override val parametersJson: String = """
        {
          "type": "object",
          "properties": {},
          "required": []
        }
    """.trimIndent()

    override suspend fun execute(arguments: String): String {
        return try {
            val result = collectDeviceInfo()
            result.toString()
        } catch (e: Exception) {
            JSONObject().apply {
                put("error", "获取设备信息失败: ${e.message}")
            }.toString()
        }
    }

    private fun collectDeviceInfo(): JSONObject {
        val displayMetrics: DisplayMetrics = context.resources.displayMetrics

        val screenWidthPx = displayMetrics.widthPixels
        val screenHeightPx = displayMetrics.heightPixels
        val density = displayMetrics.density
        val densityDpi = displayMetrics.densityDpi

        val screenWidthDp = (screenWidthPx / density).toInt()
        val screenHeightDp = (screenHeightPx / density).toInt()

        // 状态栏高度
        val statusBarHeightDp = getStatusBarHeightDp()

        // 导航栏高度
        val navigationBarHeightDp = getNavigationBarHeightDp()

        // Android 版本
        val androidVersion = Build.VERSION.RELEASE
        val androidSdkInt = Build.VERSION.SDK_INT

        // 设备信息
        val deviceModel = Build.MODEL
        val deviceManufacturer = Build.MANUFACTURER

        // 是否平板
        val isTablet = isTabletDevice()

        // 当前方向
        val orientation = when (context.resources.configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> "landscape"
            Configuration.ORIENTATION_PORTRAIT -> "portrait"
            else -> "portrait"
        }

        // 支持的能力列表
        val supportedFeatures = getSupportedFeatures()

        return JSONObject().apply {
            put("screen_width_px", screenWidthPx)
            put("screen_height_px", screenHeightPx)
            put("screen_width_dp", screenWidthDp)
            put("screen_height_dp", screenHeightDp)
            put("density", density)
            put("density_dpi", densityDpi)
            put("status_bar_height_dp", statusBarHeightDp)
            put("navigation_bar_height_dp", navigationBarHeightDp)
            put("android_version", androidVersion)
            put("android_sdk_int", androidSdkInt)
            put("device_model", deviceModel)
            put("device_manufacturer", deviceManufacturer)
            put("is_tablet", isTablet)
            put("orientation", orientation)
            put("supported_features", JSONArray(supportedFeatures))
        }
    }

    /**
     * 获取状态栏高度（dp）
     */
    private fun getStatusBarHeightDp(): Int {
        val resourceId = context.resources.getIdentifier(
            "status_bar_height", "dimen", "android"
        )
        return if (resourceId > 0) {
            val px = context.resources.getDimensionPixelSize(resourceId)
            (px / context.resources.displayMetrics.density).toInt()
        } else {
            24 // 默认 24dp
        }
    }

    /**
     * 获取导航栏高度（dp）
     */
    private fun getNavigationBarHeightDp(): Int {
        val resourceId = context.resources.getIdentifier(
            "navigation_bar_height", "dimen", "android"
        )
        return if (resourceId > 0) {
            val px = context.resources.getDimensionPixelSize(resourceId)
            (px / context.resources.displayMetrics.density).toInt()
        } else {
            0
        }
    }

    /**
     * 判断是否为平板设备
     */
    private fun isTabletDevice(): Boolean {
        val config = context.resources.configuration
        val screenLayout = config.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK
        return screenLayout >= Configuration.SCREENLAYOUT_SIZE_LARGE
    }

    /**
     * 获取设备支持的能力列表
     */
    private fun getSupportedFeatures(): List<String> {
        val features = mutableListOf<String>()

        // 基础反馈能力
        features.add("haptic")      // 触觉反馈
        features.add("vibrate")     // 震动
        features.add("toast")       // Toast 提示
        features.add("share")       // 分享
        features.add("clipboard")   // 剪贴板
        features.add("dialog")      // 对话框

        // 检查系统功能
        val pm = context.packageManager

        // 相机
        if (pm.hasSystemFeature(android.content.pm.PackageManager.FEATURE_CAMERA_ANY)) {
            features.add("camera")
        }

        // 蓝牙
        if (pm.hasSystemFeature(android.content.pm.PackageManager.FEATURE_BLUETOOTH)) {
            features.add("bluetooth")
        }

        // NFC
        if (pm.hasSystemFeature(android.content.pm.PackageManager.FEATURE_NFC)) {
            features.add("nfc")
        }

        // GPS 定位
        if (pm.hasSystemFeature(android.content.pm.PackageManager.FEATURE_LOCATION_GPS)) {
            features.add("gps")
        }

        // 传感器
        if (pm.hasSystemFeature(android.content.pm.PackageManager.FEATURE_SENSOR_ACCELEROMETER)) {
            features.add("accelerometer")
        }
        if (pm.hasSystemFeature(android.content.pm.PackageManager.FEATURE_SENSOR_GYROSCOPE)) {
            features.add("gyroscope")
        }

        // 指纹/生物识别
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
            pm.hasSystemFeature(android.content.pm.PackageManager.FEATURE_FINGERPRINT)
        ) {
            features.add("fingerprint")
        }

        // 麦克风
        if (pm.hasSystemFeature(android.content.pm.PackageManager.FEATURE_MICROPHONE)) {
            features.add("microphone")
        }

        // 触摸屏
        if (pm.hasSystemFeature(android.content.pm.PackageManager.FEATURE_TOUCHSCREEN)) {
            features.add("touchscreen")
        }

        // WiFi
        if (pm.hasSystemFeature(android.content.pm.PackageManager.FEATURE_WIFI)) {
            features.add("wifi")
        }

        // 网络
        features.add("network")

        return features
    }
}
