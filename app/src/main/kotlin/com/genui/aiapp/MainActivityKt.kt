package com.genui.aiapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.genui.aiapp.ui.ChatScreen
import com.genui.aiapp.ui.GenUIModelConfigScreen
import com.genui.aiapp.viewmodel.ChatViewModel
import com.genui.aiapp.viewmodel.GenUIModelConfigViewModel

/**
 * 应用导航路由常量
 */
object Routes {
    const val CHAT = "chat"
    const val SETTINGS = "settings"
}

/**
 * 应用导航图
 *
 * @param navController 导航控制器
 * @param startDestination 起始目标
 */
@Composable
fun AppNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Routes.CHAT
) {
    val context = LocalContext.current
    val chatViewModel: ChatViewModel = viewModel()
    val configViewModel: GenUIModelConfigViewModel = viewModel(
        factory = GenUIModelConfigViewModel.factory(context.applicationContext)
    )

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Routes.CHAT) {
            ChatScreen(
                viewModel = chatViewModel,
                onSettingsClick = {
                    navController.navigate(Routes.SETTINGS)
                }
            )
        }

        composable(Routes.SETTINGS) {
            GenUIModelConfigScreen(
                viewModel = configViewModel,
                onBack = {
                    navController.popBackStack()
                    // 返回聊天页面时刷新配置
                    chatViewModel.reloadConfig()
                }
            )
        }
    }
}
