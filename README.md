<div align="center">

# 🧩 GenUI Agent

**AI 生成式 UI 安卓智能体 — 让大模型直接生成交互界面**

[![Kotlin](https://img.shields.io/badge/Kotlin-2.0-7F52FF)](https://kotlinlang.org)
[![Compose](https://img.shields.io/badge/Jetpack%20Compose-Material%203-4285F4)](https://developer.android.com/jetpack/compose)
[![License](https://img.shields.io/badge/License-Apache%202.0-green)](LICENSE)
[![API](https://img.shields.io/badge/API-26%2B-brightgreen)](https://android-arsenal.com/api?level=26)

</div>

## 这是什么

GenUI Agent 是一个安卓端 **AI Agent 应用 + 生成式 UI SDK**：用户与 AI 对话，AI 直接生成**可交互的原生界面**（不是文本，是真正能点的 App 界面），并支持表单数据回流（`collectFrom` 聚合提交）。

```
用户：「帮我做一个 BMI 计算器」
  ↓
AI 生成 GenUI JSON → SDK 渲染原生 Compose 界面
  ↓
用户在界面里输入身高体重 → 点提交
  ↓
输入值自动聚合回传 AI → AI 返回结果页
```

## 核心能力

### 🧩 SDK（`sdk/` 模块，可直接复用）
| 能力 | 说明 |
| --- | --- |
| **530+ 组件类型** | 10 大领域（布局/文本/按钮/输入/展示/导航/反馈/数据/媒体/容器） |
| **变体系统** | 12 配色板 × 3 密度 × 5 情绪 = 单组件 14,400 变体 |
| **效果引擎 v2** | 渐变 / 辉光 / 纹理 / 旋转 / 透明度（style 声明式） |
| **形状系统三层** | M3 圆角刻度（xs~xxl + 侧向角 + cut 切角）· Expressive 35 趣味形（heart/cookie9/flower…纯 Path 零依赖）· 胶囊家族 15 组件 |
| **通知组件家族** | 30 类型（进度卡/聚合堆栈/社交爆发/系统警示…） |
| **语法高亮** | 11 语言零依赖引擎（关键字/字符串/注释/数字着色 + 复制 + 行号） |
| **GFM Markdown** | 任务列表/表格/删除线/Alerts/引用块/自动链接，流式安全 |
| **互动表单** | `collectFrom` 状态总线：输入实时聚合，提交回传 AI |
| **动作系统** | navigate / openUrl / clipboard / haptic / dialog / custom(handlerId) |
| **容错管线** | 未知动作丢弃不炸页 · JSON 脏数据修复 · 流式渐进渲染 |

### 📱 应用（`app/` 模块）
- **四通道渲染**：GenUI（主力）/ A2UI（FlatDoc，桥接 GenUI 引擎）/ Markdown / HTML（WebView + CDN 外部依赖）
- **右侧抽屉**：四锁手势（位置锁/轴向锁/方向锁/速度锁），竖滑零误触
- **漂浮宠物**：可拖动、可换装（JSON 导入导出）、AI 状态播报（思考/调用工具/生成中）
- **层栈导航**：单一 layerStack，任何按钮压一层，返回弹一层
- **宠物管理 / 灵魂系统 / 多模型配置 / 界面回放**

## 快速开始

```kotlin
// 1. 渲染一段 GenUI JSON
val spec = DslParser.parse(json)
GenUI.Screen(spec = spec, host = myActionHost)

// 2. 处理表单提交（collectFrom 聚合）
val host = LambdaActionHost(customHandler = { id, payload ->
    // id = "submit_form", payload 含表单各 id 的当前值
})
```

GenUI JSON 示例：
```json
{
  "root": {
    "type": "column",
    "children": [
      {"type": "heading", "properties": {"text": "BMI 计算器"}},
      {"type": "text_field", "id": "height", "properties": {"label": "身高(cm)"}},
      {"type": "slider", "id": "weight", "properties": {"value": 65, "min": 30, "max": 150}},
      {"type": "pill_button", "properties": {"text": "计算"},
       "events": {"onClick": {"actions": [
         {"action": "custom", "handlerId": "submit_form",
          "payload": {"collectFrom": ["height", "weight"]}}]}}}
    ]
  }
}
```

## 致谢 / 相关项目

本项目的部分交互与架构模式参考了以下开源项目（各项目保留其自身版权）：

- [ZorvAI](https://github.com/Quor-a/ZorvAI) — 侧边栏抽屉 / 思考面板 / 语音球交互模式
- [llm-ui](https://github.com/llm-ui/llm-ui) — LLM 输出流式渲染思路
- [A2UI](https://developers.googleblog.com) — 可移植生成式 UI 规范
- [Vercel AI SDK](https://ai-sdk.dev) — Generative UI 范式
- [multiplatform-markdown-renderer](https://github.com/mikepenz/multiplatform-markdown-renderer) — GFM 特性集参考

## 许可证

[Apache License 2.0](LICENSE)

```
Copyright 2026 GenUI Agent Contributors

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
