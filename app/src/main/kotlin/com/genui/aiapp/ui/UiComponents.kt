package com.genui.aiapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.genui.aiapp.core.model.ApiProviderType

// ==================== 颜色常量 ====================

val Accent = Color(0xFF0D9488)
val AccentSoft = Color(0xFFCCFBF1)
val AccentAmber = Color(0xFFF59E0B)
val AccentAmberSoft = Color(0xFFFEF3C7)
val Line = Color(0xFFE7E5E4)
val Line2 = Color(0xFFF5F5F4)
val Muted = Color(0xFF78716C)
val Success = Color(0xFF10B981)
val SuccessSoft = Color(0xFFD1FAE5)
val Error = Color(0xFFE11D48)
val ErrorSoft = Color(0xFFFFE4E6)
val SurfaceWhite = Color(0xFFFFFFFF)
val BackgroundColor = Color(0xFFFAFAF9)

// ==================== 提供商工具方法 ====================

/** 获取提供商显示名称 */
fun getProviderDisplayName(type: ApiProviderType): String = type.displayName

/** 获取提供商主题色 */
fun getProviderColor(type: ApiProviderType): Color {
    return when (type) {
        ApiProviderType.OPENAI -> Color(0xFF10A37F)
        ApiProviderType.OPENAI_RESPONSES -> Color(0xFF10A37F)
        ApiProviderType.OPENAI_RESPONSES_GENERIC -> Color(0xFF10A37F)
        ApiProviderType.OPENAI_GENERIC -> Color(0xFF10A37F)
        ApiProviderType.ANTHROPIC -> Color(0xFFD97757)
        ApiProviderType.ANTHROPIC_GENERIC -> Color(0xFFD97757)
        ApiProviderType.GOOGLE -> Color(0xFF4285F4)
        ApiProviderType.GEMINI_GENERIC -> Color(0xFF4285F4)
        ApiProviderType.DEEPSEEK -> Color(0xFF4D6BFE)
        ApiProviderType.BAIDU -> Color(0xFF2932E1)
        ApiProviderType.ALIYUN -> Color(0xFFFF6A00)
        ApiProviderType.XUNFEI -> Color(0xFF00B2FF)
        ApiProviderType.ZHIPU -> Color(0xFF3366FF)
        ApiProviderType.BAICHUAN -> Color(0xFF0055FF)
        ApiProviderType.MOONSHOT -> Color(0xFF266AFF)
        ApiProviderType.MIMO -> Color(0xFFFF6900)
        ApiProviderType.MISTRAL -> Color(0xFFF97316)
        ApiProviderType.SILICONFLOW -> Color(0xFF536DFE)
        ApiProviderType.IFLOW -> Color(0xFF00C6FF)
        ApiProviderType.OPENROUTER -> Color(0xFF777777)
        ApiProviderType.FOUR_ROUTER -> Color(0xFF6366F1)
        ApiProviderType.NOUS_PORTAL -> Color(0xFF8B5CF6)
        ApiProviderType.INFINIAI -> Color(0xFF10B981)
        ApiProviderType.ALIPAY_BAILING -> Color(0xFF1677FF)
        ApiProviderType.DOUBAO -> Color(0xFF3370FF)
        ApiProviderType.NVIDIA -> Color(0xFF76B900)
        ApiProviderType.LMSTUDIO -> Color(0xFF007BFF)
        ApiProviderType.OLLAMA -> Color(0xFF000000)
        ApiProviderType.OPENAI_LOCAL -> Color(0xFF10A37F)
        ApiProviderType.PPINFRA -> Color(0xFF6366F1)
        ApiProviderType.NOVITA -> Color(0xFFEC4899)
        ApiProviderType.OPENCODE_GO -> Color(0xFF10B981)
        ApiProviderType.MNN -> Color(0xFFFF6F00)
        ApiProviderType.LLAMA_CPP -> Color(0xFF8B4513)
        ApiProviderType.OTHER -> Accent
    }
}

// ==================== 章节标签 ====================

/**
 * 章节标签
 * 左侧深青色小圆点 + 数字 + 标题
 */
@Composable
fun ChapterLabel(
    num: String,
    title: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(Accent)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = num,
            style = TextStyle(
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Accent
            )
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = title,
            style = TextStyle(
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )
        )
    }
}

// ==================== 设置卡片容器 ====================

/**
 * 设置卡片容器
 * 白色卡片，16dp 圆角，1dp 阴影
 */
@Composable
fun SettingsCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 1.dp,
                spotColor = Color.Black.copy(alpha = 0.05f),
                ambientColor = Color.Black.copy(alpha = 0.05f),
                shape = RoundedCornerShape(16.dp)
            )
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceWhite)
            .padding(16.dp)
    ) {
        content()
    }
}

// ==================== 输入框 ====================

/**
 * 通用输入字段
 * 圆角卡片样式，浮动标签效果
 */
@Composable
fun GenUIField(
    label: String,
    value: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            keyboardOptions = keyboardOptions,
            textStyle = TextStyle(
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onBackground
            ),
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { isFocused = it.isFocused },
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .border(
                            width = 1.dp,
                            color = if (isFocused) Accent else Line,
                            shape = RoundedCornerShape(14.dp)
                        )
                        .background(SurfaceWhite)
                        .padding(horizontal = 14.dp, vertical = 12.dp)
                ) {
                    Column {
                        Text(
                            text = label,
                            style = TextStyle(
                                fontSize = if (isFocused || value.isNotEmpty()) 11.sp else 14.sp,
                                color = if (isFocused) Accent else Muted,
                                fontWeight = if (isFocused) FontWeight.Medium else FontWeight.Normal
                            )
                        )
                        if (isFocused || value.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(2.dp))
                        }
                        innerTextField()
                    }
                }
            }
        )
    }
}

/**
 * API Key 输入框
 * 圆角卡片样式，带显示/隐藏切换按钮
 */
@Composable
fun ApiKeyField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var visible by remember { mutableStateOf(false) }
    var isFocused by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onBackground
            ),
            visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { isFocused = it.isFocused },
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .border(
                            width = 1.dp,
                            color = if (isFocused) Accent else Line,
                            shape = RoundedCornerShape(14.dp)
                        )
                        .background(SurfaceWhite)
                        .padding(horizontal = 14.dp, vertical = 12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "API Key",
                                style = TextStyle(
                                    fontSize = if (isFocused || value.isNotEmpty()) 11.sp else 14.sp,
                                    color = if (isFocused) Accent else Muted,
                                    fontWeight = if (isFocused) FontWeight.Medium else FontWeight.Normal
                                )
                            )
                            if (isFocused || value.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(2.dp))
                            }
                            innerTextField()
                        }
                        IconButton(
                            onClick = { visible = !visible },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = if (visible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = if (visible) "隐藏" else "显示",
                                tint = Muted,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        )
    }
}

// ==================== 设置选择行 ====================

/**
 * 设置选择器行
 * 点击可展开选择器的行，右侧带 Chevron 箭头
 */
@Composable
fun SettingsSelectorRow(
    title: String,
    subtitle: String,
    color: Color = Accent,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 4.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
            if (subtitle.isNotEmpty()) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    style = TextStyle(
                        fontSize = 13.sp,
                        color = color,
                        fontWeight = FontWeight.Medium
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = Muted,
            modifier = Modifier.size(20.dp)
        )
    }
}

// ==================== 步进器 ====================

/**
 * 步进数字字段
 * 用于调整数字参数
 */
@Composable
fun StepperField(
    label: String,
    value: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = TextStyle(
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Line2)
        ) {
            IconButton(
                onClick = { onValueChange((value - 1).coerceAtLeast(0)) },
                modifier = Modifier.size(36.dp)
            ) {
                Text("−", fontSize = 18.sp, color = Accent, fontWeight = FontWeight.Medium)
            }
            Text(
                text = value.toString(),
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Accent
                ),
                modifier = Modifier.width(48.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            IconButton(
                onClick = { onValueChange(value + 1) },
                modifier = Modifier.size(36.dp)
            ) {
                Text("+", fontSize = 18.sp, color = Accent, fontWeight = FontWeight.Medium)
            }
        }
    }
}

// ==================== 提供商头像 ====================

/**
 * 提供商头像
 * 显示提供商首字母或图标
 */
@Composable
fun ProviderAvatar(
    iconUri: String?,
    letter: String,
    size: androidx.compose.ui.unit.Dp = 36.dp,
    bgColor: Color = AccentSoft,
    textColor: Color = Accent,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(bgColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = letter.take(1).uppercase(),
            style = TextStyle(
                fontSize = (size.value * 0.4f).sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        )
    }
}

// ==================== 主按钮 ====================

/**
 * 主按钮
 * 深青背景 + 白色文字 + 14dp 圆角 + 阴影
 */
@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable () -> Unit = {}
) {
    androidx.compose.material3.Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .shadow(
                elevation = 2.dp,
                spotColor = Accent.copy(alpha = 0.3f),
                ambientColor = Accent.copy(alpha = 0.1f),
                shape = RoundedCornerShape(14.dp)
            ),
        shape = RoundedCornerShape(14.dp),
        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
            containerColor = Accent,
            contentColor = Color.White,
            disabledContainerColor = Accent.copy(alpha = 0.4f),
            disabledContentColor = Color.White.copy(alpha = 0.7f)
        )
    ) {
        content()
        Text(text, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
    }
}

// ==================== 次按钮 ====================

/**
 * 次按钮
 * 浅青背景 + 深青文字 + 14dp 圆角
 */
@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable () -> Unit = {}
) {
    androidx.compose.material3.Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp),
        shape = RoundedCornerShape(14.dp),
        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
            containerColor = AccentSoft,
            contentColor = Accent,
            disabledContainerColor = AccentSoft.copy(alpha = 0.5f),
            disabledContentColor = Accent.copy(alpha = 0.4f)
        )
    ) {
        content()
        Text(text, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}
