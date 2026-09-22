package com.genui.aiapp.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.genui.aiapp.core.model.ApiProviderType
import com.genui.aiapp.core.model.GenUIModelConfigData
import com.genui.aiapp.net.GenUIModelListResult
import com.genui.aiapp.viewmodel.GenUIModelConfigViewModel
import kotlinx.coroutines.launch

/**
 * 模型配置界面
 *
 * @param viewModel 配置 ViewModel
 * @param onBack 返回回调
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenUIModelConfigScreen(
    viewModel: GenUIModelConfigViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val cfg by viewModel.cfg.collectAsState()
    val modelList by viewModel.modelList.collectAsState()
    val isFetching by viewModel.isFetchingModels.collectAsState()
    val isTesting by viewModel.isTesting.collectAsState()
    val testResult by viewModel.testResult.collectAsState()

    var showProviderSheet by remember { mutableStateOf(false) }
    var showModelPage by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = BackgroundColor,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "模型配置",
                        style = TextStyle(
                            fontSize = 17.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Line2),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "返回",
                                tint = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = SurfaceWhite.copy(alpha = 0.95f),
                    scrolledContainerColor = SurfaceWhite.copy(alpha = 0.95f)
                ),
                actions = {},
                modifier = Modifier
                    .background(SurfaceWhite.copy(alpha = 0.95f))
                    .then(
                        // 底部细分割线
                        Modifier
                    )
            )
            // 底部分割线
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(0.5.dp)
                    .background(Line)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                horizontal = 16.dp,
                vertical = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 提供商选择
            item {
                ChapterLabel("01", "API 提供商")
                Spacer(modifier = Modifier.height(8.dp))
                SettingsCard {
                    SettingsSelectorRow(
                        title = "提供商类型",
                        subtitle = ApiProviderType.entries.firstOrNull { it.providerId == cfg.provider }?.displayName ?: cfg.provider,
                        color = Accent,
                        onClick = { showProviderSheet = true }
                    )
                }
            }

            // 连接设置
            item {
                ChapterLabel("02", "连接设置")
                Spacer(modifier = Modifier.height(8.dp))
                SettingsCard {
                    Column {
                        GenUIField(
                            label = "Base URL",
                            value = cfg.baseUrl,
                            onValueChange = { viewModel.update { c -> c.copy(baseUrl = it) } }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        ApiKeyField(
                            value = cfg.apiKey,
                            onValueChange = { viewModel.update { c -> c.copy(apiKey = it) } }
                        )
                    }
                }
            }

            // 模型选择
            item {
                ChapterLabel("03", "模型")
                Spacer(modifier = Modifier.height(8.dp))
                SettingsCard {
                    Column {
                        SettingsSelectorRow(
                            title = "模型名称",
                            subtitle = cfg.model.ifBlank { "点击选择或输入" },
                            color = Accent,
                            onClick = {
                                showModelPage = true
                                if (modelList == null) {
                                    viewModel.fetchModels()
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        SecondaryButton(
                            text = "获取模型列表",
                            onClick = { viewModel.fetchModels() },
                            enabled = !isFetching
                        ) {
                            if (isFetching) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    color = Accent,
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                            } else {
                                Icon(
                                    Icons.Default.Refresh,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                            }
                        }
                    }
                }
            }

            // 参数设置
            item {
                ChapterLabel("04", "参数设置")
                Spacer(modifier = Modifier.height(8.dp))
                SettingsCard {
                    Column {
                        TemperatureRow(
                            value = cfg.temperature,
                            onValueChange = { viewModel.update { c -> c.copy(temperature = it) } }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        StepperField(
                            label = "最大输出 Tokens",
                            value = cfg.maxTokens,
                            onValueChange = { viewModel.update { c -> c.copy(maxTokens = it) } }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        StepperField(
                            label = "上下文窗口",
                            value = cfg.contextWindow,
                            onValueChange = { viewModel.update { c -> c.copy(contextWindow = it) } }
                        )
                    }
                }
            }

            // 工具设置
            item {
                ChapterLabel("05", "工具调用")
                Spacer(modifier = Modifier.height(8.dp))
                SettingsCard {
                    Column {
                        SwitchRow(
                            label = "启用工具调用",
                            checked = cfg.enableTools,
                            onCheckedChange = { viewModel.update { c -> c.copy(enableTools = it) } }
                        )
                        if (cfg.enableTools) {
                            Spacer(modifier = Modifier.height(12.dp))
                            StepperField(
                                label = "最大工具调用轮次",
                                value = cfg.maxToolRounds,
                                onValueChange = { viewModel.update { c -> c.copy(maxToolRounds = it) } }
                            )
                        }
                    }
                }
            }

            // 连接测试
            item {
                ChapterLabel("06", "连接测试")
                Spacer(modifier = Modifier.height(8.dp))
                SettingsCard {
                    Column {
                        PrimaryButton(
                            text = "测试连接",
                            onClick = { viewModel.testConnection() },
                            enabled = !isTesting && cfg.baseUrl.isNotBlank() && cfg.model.isNotBlank()
                        ) {
                            if (isTesting) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                            }
                        }
                        if (testResult != null) {
                            Spacer(modifier = Modifier.height(12.dp))
                            val isSuccess = (testResult ?: "").startsWith("✅")
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSuccess) SuccessSoft else ErrorSoft)
                                    .padding(horizontal = 14.dp, vertical = 12.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = if (isSuccess) "✓" else "✕",
                                        style = TextStyle(
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSuccess) Success else Error
                                        )
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = testResult ?: "",
                                        style = TextStyle(
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = if (isSuccess) Success else Error
                                        ),
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    // 提供商选择底部弹窗
    if (showProviderSheet) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = { showProviderSheet = false },
            containerColor = SurfaceWhite,
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            dragHandle = {
                Box(
                    modifier = Modifier
                        .padding(top = 12.dp, bottom = 8.dp)
                        .width(40.dp)
                        .height(4.dp)
                        .clip(CircleShape)
                        .background(Line)
                )
            }
        ) {
            ProviderSelectorSheet(
                selected = cfg.provider,
                onSelect = { provider ->
                    viewModel.update { c -> c.copy(provider = provider.providerId) }
                    scope.launch { sheetState.hide() }.invokeOnCompletion {
                        showProviderSheet = false
                    }
                }
            )
        }
    }

    // 模型选择 — 全屏页面
    if (showModelPage) {
        ModelPickerPage(
            selected = cfg.model,
            modelList = modelList,
            isLoading = isFetching,
            customInput = cfg.model,
            onSelect = { model ->
                viewModel.update { c -> c.copy(model = model) }
                showModelPage = false
            },
            onCustomInput = { model ->
                viewModel.update { c -> c.copy(model = model) }
            },
            onRefresh = { viewModel.fetchModels() },
            onBack = { showModelPage = false }
        )
    }
}

// ==================== 温度滑块 ====================

@Composable
private fun TemperatureRow(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Temperature",
                style = TextStyle(
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
            Text(
                text = String.format("%.2f", value),
                style = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Accent
                )
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        androidx.compose.material3.Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = 0f..2f,
            steps = 19,
            colors = androidx.compose.material3.SliderDefaults.colors(
                activeTrackColor = Accent,
                thumbColor = Accent,
                inactiveTrackColor = Line2,
                activeTickColor = Accent.copy(alpha = 0.3f),
                inactiveTickColor = Color.Transparent
            )
        )
    }
}

// ==================== 开关行 ====================

@Composable
private fun SwitchRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = TextStyle(
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Accent,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Line
            )
        )
    }
}

// ==================== 提供商选择弹窗 ====================

@Composable
private fun ProviderSelectorSheet(
    selected: String,
    onSelect: (ApiProviderType) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "选择 API 提供商",
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            ),
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .padding(horizontal = 12.dp)
        ) {
            items(ApiProviderType.entries, key = { it.providerId }) { provider ->
                val isSelected = provider.providerId == selected
                val providerColor = getProviderColor(provider)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) AccentSoft.copy(alpha = 0.4f) else Color.Transparent)
                        .clickable { onSelect(provider) }
                        .padding(horizontal = 12.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 选中时左侧深青色竖条
                    if (isSelected) {
                        Box(
                            modifier = Modifier
                                .width(3.dp)
                                .height(28.dp)
                                .clip(CircleShape)
                                .background(Accent)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                    } else {
                        Spacer(modifier = Modifier.width(15.dp))
                    }
                    ProviderAvatar(
                        iconUri = null,
                        letter = provider.displayName.first().toString(),
                        size = 40.dp,
                        bgColor = providerColor.copy(alpha = 0.12f),
                        textColor = providerColor
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = provider.displayName,
                            style = TextStyle(
                                fontSize = 15.sp,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                                color = if (isSelected) Accent else MaterialTheme.colorScheme.onBackground
                            )
                        )
                    }
                    if (isSelected) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = null,
                            tint = Accent,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

// ==================== 模型选择全屏页面 ====================

/**
 * 模型选择全屏页面
 * 替代原来的底部弹窗 — 确保模型列表完全可见
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ModelPickerPage(
    selected: String,
    modelList: GenUIModelListResult?,
    isLoading: Boolean,
    customInput: String,
    onSelect: (String) -> Unit,
    onCustomInput: (String) -> Unit,
    onRefresh: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var searchText by remember { mutableStateOf("") }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = BackgroundColor,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "选择模型",
                        style = TextStyle(
                            fontSize = 17.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Line2),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "返回",
                                tint = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = onRefresh, enabled = !isLoading) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Accent,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                Icons.Default.Refresh,
                                contentDescription = "刷新",
                                tint = Accent,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = SurfaceWhite.copy(alpha = 0.95f),
                    scrolledContainerColor = SurfaceWhite.copy(alpha = 0.95f)
                )
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(0.5.dp)
                    .background(Line)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // 搜索框
            if (modelList is GenUIModelListResult.Success && modelList.models.size > 10) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Line2)
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null,
                        tint = Muted,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    BasicTextField(
                        value = searchText,
                        onValueChange = { searchText = it },
                        textStyle = TextStyle(
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        ),
                        modifier = Modifier.weight(1f),
                        decorationBox = { inner ->
                            Box {
                                if (searchText.isEmpty()) {
                                    Text(
                                        text = "搜索模型...",
                                        style = TextStyle(fontSize = 14.sp, color = Muted)
                                    )
                                }
                                inner()
                            }
                        }
                    )
                    if (searchText.isNotEmpty()) {
                        IconButton(
                            onClick = { searchText = "" },
                            modifier = Modifier.size(20.dp)
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "清除",
                                tint = Muted,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            // 自定义模型输入卡片
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                SettingsCard {
                    Column {
                        GenUIField(
                            label = "自定义模型名",
                            value = customInput,
                            onValueChange = onCustomInput
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        PrimaryButton(
                            text = "使用自定义模型",
                            onClick = { onSelect(customInput) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 模型列表 — 全屏滚动
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                when {
                    isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                CircularProgressIndicator(color = Accent)
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "正在获取模型列表...",
                                    style = TextStyle(fontSize = 14.sp, color = Muted)
                                )
                            }
                        }
                    }
                    modelList is GenUIModelListResult.Success -> {
                        val models = if (searchText.isBlank()) {
                            modelList.models
                        } else {
                            modelList.models.filter { it.id.contains(searchText, ignoreCase = true) }
                        }
                        if (models.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "未找到匹配的模型",
                                    style = TextStyle(fontSize = 14.sp, color = Muted)
                                )
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                                    horizontal = 16.dp,
                                    vertical = 8.dp
                                ),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                // 显示模型数量
                                item {
                                    Text(
                                        text = "共 ${modelList.models.size} 个模型" +
                                            if (searchText.isNotBlank()) "（筛选后 ${models.size} 个）" else "",
                                        style = TextStyle(
                                            fontSize = 12.sp,
                                            color = Muted,
                                            fontWeight = FontWeight.Medium
                                        ),
                                        modifier = Modifier.padding(vertical = 4.dp)
                                    )
                                }
                                items(models, key = { it.id }) { model ->
                                    val isSelected = model.id == selected
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(SurfaceWhite)
                                            .border(
                                                width = if (isSelected) 1.5.dp else 1.dp,
                                                color = if (isSelected) Accent else Line,
                                                shape = RoundedCornerShape(12.dp)
                                            )
                                            .clickable { onSelect(model.id) }
                                            .padding(horizontal = 14.dp, vertical = 12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = model.id,
                                                style = TextStyle(
                                                    fontSize = 14.sp,
                                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                                                    color = if (isSelected) Accent else MaterialTheme.colorScheme.onBackground
                                                ),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            if (model.contextLength > 0) {
                                                Spacer(modifier = Modifier.height(2.dp))
                                                Text(
                                                    text = "${formatTokens(model.contextLength)} tokens",
                                                    style = TextStyle(
                                                        fontSize = 12.sp,
                                                        color = Muted
                                                    )
                                                )
                                            }
                                        }
                                        if (isSelected) {
                                            Box(
                                                modifier = Modifier
                                                    .size(22.dp)
                                                    .clip(CircleShape)
                                                    .background(Accent),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    Icons.Default.Check,
                                                    contentDescription = null,
                                                    tint = Color.White,
                                                    modifier = Modifier.size(14.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                                item { Spacer(modifier = Modifier.height(24.dp)) }
                            }
                        }
                    }
                    modelList is GenUIModelListResult.Error -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(24.dp)
                            ) {
                                Text(
                                    text = "获取失败",
                                    style = TextStyle(
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Error
                                    )
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = modelList.message,
                                    style = TextStyle(fontSize = 13.sp, color = Muted),
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                PrimaryButton(
                                    text = "重试",
                                    onClick = onRefresh,
                                    modifier = Modifier.width(140.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Refresh,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                }
                            }
                        }
                    }
                    else -> {
                        // 初始状态 — 还没拉取过
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(horizontal = 32.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(64.dp)
                                        .clip(CircleShape)
                                        .background(AccentSoft),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("📡", fontSize = 32.sp)
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "获取模型列表",
                                    style = TextStyle(
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "点击右上角刷新按钮或下方按钮获取模型列表",
                                    style = TextStyle(fontSize = 13.sp, color = Muted),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                                PrimaryButton(
                                    text = "获取模型列表",
                                    onClick = onRefresh,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(
                                        Icons.Default.Refresh,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/** 格式化 token 数量为友好显示 */
private fun formatTokens(tokens: Int): String {
    return when {
        tokens >= 1_000_000 -> "${tokens / 1_000_000}M"
        tokens >= 1_000 -> "${tokens / 1_000}K"
        else -> tokens.toString()
    }
}
