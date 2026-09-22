package com.genui.aiapp.ui

import androidx.compose.foundation.background
import androidx.compose.material3.Icon
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * 设置页（GenUI · 生成式 UI 对话）
 * 入口：右上角 ⚙。包含：模型配置 / 灵魂注入 / 历史对话。
 */
@Composable
fun SettingsScreen(
    worksCount: Int,
    soulName: String?,
    personaName: String,
    onBack: () -> Unit,
    onOpenModelConfig: () -> Unit,
    onOpenSoul: () -> Unit,
    onOpenHistory: () -> Unit
) {
    Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
            Row(
                Modifier.fillMaxWidth().statusBarsPadding().padding(horizontal = 8.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("← 返回", color = MaterialTheme.colorScheme.primary, fontSize = 13.sp,
                    modifier = Modifier.clickable { onBack() }.padding(horizontal = 8.dp, vertical = 4.dp))
                Spacer(Modifier.weight(1f))
                Text("设置", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.weight(1f))
                Spacer(Modifier.width(64.dp))
            }

            Text(
                "GenUI · 生成式 UI 对话",
                fontSize = 11.sp, color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            Spacer(Modifier.height(12.dp))

            SettingsRow(
                iconRes = com.genui.aiapp.R.drawable.ic_set_model,
                iconDesc = "模型配置", title = "模型配置",
                subtitle = "API Key · Base URL · 模型名 · 参数",
                onClick = onOpenModelConfig
            )
            SettingsRow(
                iconRes = com.genui.aiapp.R.drawable.ic_set_soul,
                iconDesc = "灵魂注入", title = "灵魂注入",
                subtitle = if (soulName != null) "当前灵魂：$soulName · 点击进入灵魂屏" else "当前人格：$personaName · 点击孵化/进入灵魂屏",
                onClick = onOpenSoul
            )
            SettingsRow(
                iconRes = com.genui.aiapp.R.drawable.ic_set_history,
                iconDesc = "历史对话", title = "历史对话",
                subtitle = "共 $worksCount 个界面记录 · 点击回放与查看往来",
                onClick = onOpenHistory
            )

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun SettingsRow(iconRes: Int, iconDesc: String, title: String, subtitle: String, onClick: () -> Unit) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable { onClick() }
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = androidx.compose.ui.res.painterResource(iconRes),
                contentDescription = iconDesc,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(30.dp)
            )
            Column(Modifier.weight(1f).padding(start = 12.dp)) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text(subtitle, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 2.dp))
            }
            Text("›", fontSize = 20.sp, color = MaterialTheme.colorScheme.outline)
        }
    }
}
