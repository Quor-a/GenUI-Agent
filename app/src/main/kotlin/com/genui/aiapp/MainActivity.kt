package com.genui.aiapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.genui.aiapp.core.QuuroApplication
import com.genui.aiapp.ui.theme.GenUITheme

/**
 * 主 Activity
 * 应用的入口 Activity，承载 Compose 导航
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 初始化全局 Application Context（必须在 setContent 之前，
        // 因为 ViewModelFactory 在 Compose 重组时会访问 appCtx）
        QuuroApplication.appCtx = applicationContext

        setContent {
            GenUITheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavHost()
                }
            }
        }
    }
}
