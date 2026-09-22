package com.genui.aiapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.genui.aiapp.soul.Soul
import com.genui.aiapp.soul.SoulStore
import com.genui.aiapp.viewmodel.ChatViewModel

private val Amber = Color(0xFFF59E0B)
private val Panel = Color(0xFFF5F5F7)
private val PanelUp = Color(0xFFECECEF)
private val Ink = Color(0xFF1A1A1A)
private val Dim = Color(0xFF8E8E93)
private val Green = Color(0xFF16A34A)
private val Red = Color(0xFFEF4444)

/**
 * 灵魂屏 —— AI 自动人格孵化（SoulScreen 模块）。
 * 人格卡由模型自己孵化撰写，可反复重新孵化；全字段手动微调；
 * 视觉签名注入每一次界面生成。
 */
@Composable
fun SoulScreen(viewModel: ChatViewModel, onBack: () -> Unit) {
    val state by viewModel.state.collectAsState()

    Scaffold(containerColor = Color(0xFFFAFAFA)) { pad ->
        Column(
            Modifier.padding(pad).fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)
        ) {
            // 顶栏
            Row(Modifier.fillMaxWidth().padding(bottom = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                TextButton(onClick = onBack) { Text("← 返回", color = Amber, fontSize = 13.sp) }
                Spacer(Modifier.weight(1f))
                Text("灵魂", color = Ink, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.weight(1f))
                Spacer(Modifier.width(72.dp))
            }

            Text("人格卡由模型自己孵化并持续保持一致；重新孵化 = 让它重写自己。", color = Dim, fontSize = 11.sp, lineHeight = 16.sp)
            Spacer(Modifier.height(14.dp))

            val soul = state.soul
            // 人格卡展示
            CardShape {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(44.dp).background(Amber, RoundedCornerShape(10.dp)), contentAlignment = Alignment.Center) {
                        Text((soul?.name ?: "G").take(1), color = Color.White, fontSize = 20.sp, fontFamily = FontFamily.Serif)
                    }
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(soul?.name ?: "GenUI（内置灵魂）", color = Ink, fontSize = 17.sp, fontFamily = FontFamily.Serif)
                        Text(soul?.tone ?: "点击下方孵化，让 AI 写下自己的灵魂", color = Dim, fontSize = 11.sp)
                    }
                }
                soul?.let { s ->
                    Spacer(Modifier.height(12.dp))
                    if (s.mission.isNotBlank()) { SectionLabel("使命"); Text(s.mission, color = Ink, fontSize = 12.sp); Spacer(Modifier.height(8.dp)) }
                    if (s.traits.isNotEmpty()) { SectionLabel("特质"); Text(s.traits.joinToString(" · "), color = Ink, fontSize = 12.sp); Spacer(Modifier.height(8.dp)) }
                    if (s.principles.isNotEmpty()) { SectionLabel("原则"); s.principles.forEach { Text("· $it", color = Ink, fontSize = 12.sp) }; Spacer(Modifier.height(8.dp)) }
                    if (s.taboos.isNotEmpty()) { SectionLabel("禁忌 · 绝不做"); s.taboos.forEach { Text("✗ $it", color = Red, fontSize = 12.sp) }; Spacer(Modifier.height(8.dp)) }
                    if (s.sample.isNotBlank()) { SectionLabel("语气样例"); Text("「${s.sample}」", color = Ink, fontSize = 12.sp, fontFamily = FontFamily.Serif); Spacer(Modifier.height(8.dp)) }
                }
            }

            // —— 全字段手动编辑 ——
            Spacer(Modifier.height(14.dp))
            var draftName by remember(soul?.name) { mutableStateOf(soul?.name ?: "") }
            var draftTone by remember(soul?.tone) { mutableStateOf(soul?.tone ?: "") }
            var draftMission by remember(soul?.mission) { mutableStateOf(soul?.mission ?: "") }
            var draftTraits by remember(soul?.traits) { mutableStateOf(soul?.traits?.joinToString("，") ?: "") }
            var draftPrinciples by remember(soul?.principles) { mutableStateOf(soul?.principles?.joinToString("；") ?: "") }
            var draftTaboos by remember(soul?.taboos) { mutableStateOf(soul?.taboos?.joinToString("；") ?: "") }
            var draftSample by remember(soul?.sample) { mutableStateOf(soul?.sample ?: "") }

            CardShape {
                Text("手动微调 · 全字段（保存后下一次生成生效）", color = Dim, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                Spacer(Modifier.height(10.dp))
                SoulField("名字", draftName) { draftName = it }
                SoulField("语气", draftTone) { draftTone = it }
                SoulField("使命", draftMission) { draftMission = it }
                SoulField("特质（逗号分隔）", draftTraits) { draftTraits = it }
                SoulField("原则（分号分隔）", draftPrinciples) { draftPrinciples = it }
                SoulField("禁忌（分号分隔）", draftTaboos) { draftTaboos = it }
                SoulField("语气样例", draftSample) { draftSample = it }
                Spacer(Modifier.height(6.dp))
                TextButton(onClick = {
                    val snapshot = soul
                    val base = snapshot ?: viewModel.defaultSoul()
                    val s2 = base.copy(
                        name = draftName.ifBlank { base.name },
                        tone = draftTone, mission = draftMission, sample = draftSample,
                        traits = draftTraits.split("，", ",").map { it.trim() }.filter { it.isNotBlank() },
                        principles = draftPrinciples.split("；", ";").map { it.trim() }.filter { it.isNotBlank() },
                        taboos = draftTaboos.split("；", ";").map { it.trim() }.filter { it.isNotBlank() }
                    )
                    viewModel.saveSoul(s2)
                }) { Text("保存人格卡", color = Amber, fontSize = 12.sp) }
            }

            // —— 视觉签名 ——
            Spacer(Modifier.height(14.dp))
            var styleEdit by remember(soul?.style) { mutableStateOf(soul?.style ?: "") }
            CardShape {
                Text("视觉签名 · 注入每一次界面生成", color = Dim, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                Spacer(Modifier.height(6.dp))
                Text("色彩倾向、版式气质、装饰母题……改完保存，下一次生成即生效。", color = Dim, fontSize = 10.sp)
                Spacer(Modifier.height(8.dp))
                BasicTextField(
                    value = styleEdit,
                    onValueChange = { styleEdit = it },
                    textStyle = TextStyle(color = Ink, fontSize = 12.sp, fontFamily = FontFamily.Monospace, lineHeight = 17.sp),
                    cursorBrush = SolidColor(Amber),
                    modifier = Modifier.fillMaxWidth().heightIn(min = 72.dp)
                        .background(PanelUp, RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 9.dp)
                )
                Spacer(Modifier.height(8.dp))
                TextButton(onClick = {
                    val base = soul ?: viewModel.defaultSoul()
                    viewModel.saveSoul(base.copy(style = styleEdit))
                }) { Text("保存视觉签名", color = Amber, fontSize = 12.sp) }
            }

            Spacer(Modifier.height(16.dp))
            Button(
                onClick = { viewModel.incubateSoul() },
                enabled = !state.soulIncubating,
                colors = ButtonDefaults.buttonColors(containerColor = Amber, contentColor = Color.White),
                modifier = Modifier.fillMaxWidth().height(46.dp)
            ) {
                Text(
                    if (state.soulIncubating) "🥚 孵化中（模型重写自己…）"
                    else if (soul != null) "重新孵化（模型重写自己）" else "孵化灵魂",
                    fontSize = 13.sp
                )
            }
            if (state.error != null && state.error!!.startsWith("孵化失败")) {
                Spacer(Modifier.height(10.dp))
                Text(state.error!!, color = Red, fontSize = 11.sp)
            }
            Spacer(Modifier.height(30.dp))
        }
    }
}

@Composable
private fun CardShape(content: @Composable ColumnScope.() -> Unit) {
    Column(Modifier.fillMaxWidth().background(Panel, RoundedCornerShape(14.dp)).padding(16.dp), content = content)
}

@Composable
private fun SectionLabel(text: String) {
    Text(text, color = Dim, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
    Spacer(Modifier.height(2.dp))
}

@Composable
private fun SoulField(label: String, value: String, onChange: (String) -> Unit) {
    Column {
        Text(label, color = Dim, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
        BasicTextField(
            value = value, onValueChange = onChange,
            textStyle = TextStyle(color = Ink, fontSize = 12.sp, fontFamily = FontFamily.Monospace, lineHeight = 16.sp),
            cursorBrush = SolidColor(Amber),
            modifier = Modifier.fillMaxWidth().heightIn(min = 34.dp)
                .background(PanelUp, RoundedCornerShape(8.dp))
                .padding(horizontal = 10.dp, vertical = 8.dp)
        )
        Spacer(Modifier.height(7.dp))
    }
}
