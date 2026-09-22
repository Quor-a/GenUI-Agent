package com.genui.aiapp.core

import android.content.Context

/**
 * 应用级单例
 * 提供全局 Application Context 访问
 */
object QuuroApplication {
    @Volatile
    private var _appCtx: Context? = null

    /** 全局 Application Context */
    var appCtx: Context?
        get() = _appCtx
        set(value) {
            _appCtx = value
        }
}
