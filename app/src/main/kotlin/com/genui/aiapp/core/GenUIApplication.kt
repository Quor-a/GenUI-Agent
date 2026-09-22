package com.genui.aiapp.core

import android.app.Application

/**
 * 自定义 Application 类
 * 在所有 Activity/ViewModel 之前初始化全局 Context
 */
class GenUIApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // 必须在此初始化，保证 ViewModelFactory 创建时可用
        QuuroApplication.appCtx = applicationContext
    }
}
