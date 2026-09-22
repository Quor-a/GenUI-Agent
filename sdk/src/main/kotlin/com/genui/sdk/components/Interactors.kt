package com.genui.sdk.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.genui.sdk.dsl.UIComponent
import com.genui.sdk.render.RenderContext
import com.genui.sdk.render.StyleResolver

/**
 * A2UI 式本地交互器（客户端自持状态，零网络往返）：
 *
 * A2UI 协议的核心洞见之一——UI 微交互（开关/展开/步进/勾选/页签）不需要 AI 参与，
 * 状态留在客户端即可。GenUI 渲染时这些组件完全本地响应，即时反馈。
 * 若声明 onChange 事件，状态变化时才回传 AI（可选）。
 */
@Composable
fun InteractorRenderer(component: UIComponent, ctx: RenderContext, kind: String, modifier: Modifier = Modifier) {
    val props = component.properties
    fun str(k: String, def: String = ""): String =
        (props[k] as? kotlinx.serialization.json.JsonPrimitive)?.content?.takeIf { it.isNotBlank() } ?: def

    val title = str("text", str("title"))
    val accent = StyleResolver.resolveColor(
        component.style.textColor, ctx.theme.colorScheme, ctx.theme.colorScheme.primary
    )

    when (kind) {
        // ── 点按切换 ──
        "toggle" -> {
            var on by rememberSaveable(component.id) { mutableStateOf(str("value") == "true") }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { on = !on }
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Text(title, fontSize = 14.sp, modifier = Modifier.weight(1f))
                Box(
                    Modifier
                        .width(44.dp)
                        .height(24.dp)
                        .clip(CircleShape)
                        .padding(2.dp)
                ) {
                    androidx.compose.foundation.layout.BoxWithConstraints {
                        val knob = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .align(if (on) Alignment.CenterEnd else Alignment.CenterStart)
                            .padding(0.dp)
                        Surface(
                            color = if (on) accent else Color(0xFF8B93A7),
                            modifier = Modifier.fillMaxWidth().height(24.dp).clip(CircleShape)
                        ) {}
                        Surface(color = Color.White, modifier = knob) {}
                    }
                }
            }
        }

        // ── 展开/收起 ──
        "expand" -> {
            var expanded by rememberSaveable(component.id) { mutableStateOf(str("expanded") == "true") }
            Column(modifier.fillMaxWidth()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { expanded = !expanded }
                        .padding(horizontal = 14.dp, vertical = 12.dp)
                ) {
                    Text(title, fontSize = 14.sp, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
                    Icon(
                        if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = null,
                        tint = accent
                    )
                }
                AnimatedVisibility(visible = expanded) {
                    Column(Modifier.padding(horizontal = 14.dp)) {
                        component.children.forEach { child ->
                            com.genui.sdk.render.RenderNode(child, ctx)
                        }
                    }
                }
            }
        }

        // ── 步进器 ──
        "counter" -> {
            var value by rememberSaveable(component.id) {
                mutableIntStateOf((props["value"] as? kotlinx.serialization.json.JsonPrimitive)?.content?.toIntOrNull() ?: 0)
            }
            val min = (props["min"] as? kotlinx.serialization.json.JsonPrimitive)?.content?.toIntOrNull() ?: Int.MIN_VALUE
            val max = (props["max"] as? kotlinx.serialization.json.JsonPrimitive)?.content?.toIntOrNull() ?: Int.MAX_VALUE
            val step = (props["step"] as? kotlinx.serialization.json.JsonPrimitive)?.content?.toIntOrNull() ?: 1
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = modifier.padding(vertical = 4.dp)
            ) {
                if (title.isNotBlank()) Text(title, fontSize = 14.sp, modifier = Modifier.weight(1f))
                Surface(
                    color = accent.copy(alpha = 0.15f),
                    shape = CircleShape,
                    modifier = Modifier.size(30.dp).clickable { if (value - step >= min) value -= step }
                ) {
                    Box(contentAlignment = Alignment.Center) { Text("−", fontSize = 18.sp, color = accent) }
                }
                Text("$value", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Surface(
                    color = accent.copy(alpha = 0.15f),
                    shape = CircleShape,
                    modifier = Modifier.size(30.dp).clickable { if (value + step <= max) value += step }
                ) {
                    Box(contentAlignment = Alignment.Center) { Text("+", fontSize = 18.sp, color = accent) }
                }
            }
        }

        // ── 勾选 ──
        "check" -> {
            var checked by rememberSaveable(component.id) { mutableStateOf(str("value") == "true") }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { checked = !checked }
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Box(
                    Modifier
                        .size(22.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .padding(0.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        color = if (checked) accent else Color(0x33888888),
                        modifier = Modifier.size(22.dp),
                        shape = RoundedCornerShape(6.dp)
                    ) {}
                    if (checked) Text("✓", fontSize = 13.sp, color = Color.White, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.width(10.dp))
                Text(title, fontSize = 14.sp)
            }
        }

        // ── 页签（本地切换内容）──
        "tabs" -> {
            var selected by rememberSaveable(component.id) { mutableIntStateOf(0) }
            val labels = component.children.map { c ->
                (c.properties["text"] as? kotlinx.serialization.json.JsonPrimitive)?.content
                    ?: (c.properties["title"] as? kotlinx.serialization.json.JsonPrimitive)?.content ?: "Tab"
            }
            Column(modifier.fillMaxWidth()) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    labels.forEachIndexed { i, label ->
                        Surface(
                            color = if (i == selected) accent.copy(alpha = 0.18f) else Color(0x11888888),
                            shape = RoundedCornerShape(50),
                            modifier = Modifier.clickable { selected = i }
                        ) {
                            Text(
                                label, fontSize = 12.sp,
                                color = if (i == selected) accent else Color(0xFF8B93A7),
                                fontWeight = if (i == selected) FontWeight.SemiBold else FontWeight.Normal,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
                Spacer(Modifier.height(10.dp))
                component.children.getOrNull(selected)?.let { tab ->
                    com.genui.sdk.render.RenderNode(tab, ctx)
                }
            }
        }
    }
}
