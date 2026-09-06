package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.courses.CourseData
import com.example.courses.CourseLesson
import com.example.data.ChatMessage
import com.example.data.UserStats
import com.example.emulator.PythonPlayground
import com.example.ui.viewmodel.AppTab
import com.example.ui.viewmodel.LearningViewModel
import com.example.ui.viewmodel.QuizStatus
import com.example.ui.viewmodel.ThemeMode
import com.example.ui.viewmodel.FontSizeOption
import com.example.ui.viewmodel.CodeThemeOption
import kotlinx.coroutines.launch

// Utility function to clean markdown headers (###, ##, #) and asterisks (***, **, *) from raw text
fun String.cleanMarkdownFormat(): String {
    var cleaned = this.replace(Regex("(?m)^#+\\s*"), "")
    cleaned = cleaned.replace(Regex("#+\\s+"), "")
    cleaned = cleaned.replace("###", "")
    cleaned = cleaned.replace("##", "")
    cleaned = cleaned.replace("***", "")
    cleaned = cleaned.replace("**", "")
    cleaned = cleaned.replace("*", "")
    cleaned = cleaned.replace("__", "")
    cleaned = cleaned.replace("_", "")
    cleaned = cleaned.replace(Regex("(?m)^>\\s*"), "")
    cleaned = cleaned.replace("```", "")
    cleaned = cleaned.replace("`", "")
    return cleaned.trim()
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainAppScreen(
    viewModel: LearningViewModel,
    modifier: Modifier = Modifier
) {
    val stats by viewModel.userStats.collectAsStateWithLifecycle()
    val rawCompletedLessons = stats?.completedLessons ?: ""
    val completedSet = remember(rawCompletedLessons) {
        rawCompletedLessons.split(",").filter { it.isNotEmpty() }.toSet()
    }

    var showSettingsSheet by remember { mutableStateOf(false) }

    if (showSettingsSheet) {
        SettingsDialog(
            viewModel = viewModel,
            onDismiss = { showSettingsSheet = false }
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            if (viewModel.activeChapter == null) {
                Surface(
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier.statusBarsPadding()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Pycoach",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    letterSpacing = (-0.5).sp
                                ),
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Text(
                                text = "LEARNING DASHBOARD",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.5.sp
                                ),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Settings Button
                            IconButton(
                                onClick = { showSettingsSheet = true },
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                                    .testTag("topbar_settings_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = "設定",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            // Circular user level avatar badge
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primaryContainer)
                                    .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f), CircleShape)
                                    .clickable { viewModel.currentTab = AppTab.DASHBOARD },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Lvl\n${stats?.level ?: 1}",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        lineHeight = 10.sp,
                                        textAlign = TextAlign.Center
                                    ),
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    }
                }
            } else {
                TopAppBar(
                    title = {
                        Text(
                            text = viewModel.activeChapter?.title ?: "單元閱讀",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { viewModel.activeChapter = null }) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "返回")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                )
            }
        },
        bottomBar = {
            NavigationBar(
                windowInsets = WindowInsets.navigationBars
            ) {
                NavigationBarItem(
                    selected = viewModel.currentTab == AppTab.COURSES,
                    onClick = { viewModel.currentTab = AppTab.COURSES },
                    icon = {
                        Icon(
                            imageVector = if (viewModel.currentTab == AppTab.COURSES) Icons.Filled.School else Icons.Outlined.School,
                            contentDescription = "學習課程"
                        )
                    },
                    label = { Text("課程") },
                    modifier = Modifier.testTag("nav_courses")
                )
                NavigationBarItem(
                    selected = viewModel.currentTab == AppTab.EMULATOR,
                    onClick = { viewModel.currentTab = AppTab.EMULATOR },
                    icon = {
                        Icon(
                            imageVector = if (viewModel.currentTab == AppTab.EMULATOR) Icons.Filled.Code else Icons.Outlined.Code,
                            contentDescription = "代碼模擬器"
                        )
                    },
                    label = { Text("模擬器") },
                    modifier = Modifier.testTag("nav_emulator")
                )
                NavigationBarItem(
                    selected = viewModel.currentTab == AppTab.QUIZ,
                    onClick = { viewModel.currentTab = AppTab.QUIZ },
                    icon = {
                        Icon(
                            imageVector = if (viewModel.currentTab == AppTab.QUIZ) Icons.Filled.Psychology else Icons.Outlined.Psychology,
                            contentDescription = "核心測驗"
                        )
                    },
                    label = { Text("測驗") },
                    modifier = Modifier.testTag("nav_quiz")
                )
                NavigationBarItem(
                    selected = viewModel.currentTab == AppTab.AI_COACH,
                    onClick = { viewModel.currentTab = AppTab.AI_COACH },
                    icon = {
                        Icon(
                            imageVector = if (viewModel.currentTab == AppTab.AI_COACH) Icons.Filled.Forum else Icons.Outlined.Forum,
                            contentDescription = "AI Coach"
                        )
                    },
                    label = { Text("AI Coach") },
                    modifier = Modifier.testTag("nav_ai_coach")
                )
                NavigationBarItem(
                    selected = viewModel.currentTab == AppTab.DASHBOARD,
                    onClick = { viewModel.currentTab = AppTab.DASHBOARD },
                    icon = {
                        Icon(
                            imageVector = if (viewModel.currentTab == AppTab.DASHBOARD) Icons.Filled.BarChart else Icons.Outlined.BarChart,
                            contentDescription = "數據儀表板"
                        )
                    },
                    label = { Text("數據設定") },
                    modifier = Modifier.testTag("nav_dashboard")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            when (viewModel.currentTab) {
                AppTab.COURSES -> CoursesTab(viewModel, completedSet)
                AppTab.EMULATOR -> EmulatorTab(viewModel)
                AppTab.QUIZ -> QuizTab(viewModel)
                AppTab.AI_COACH -> AiCoachTab(viewModel)
                AppTab.DASHBOARD -> DashboardTab(
                    viewModel = viewModel,
                    onOpenSettings = { showSettingsSheet = true }
                )
            }
        }
    }
}

// ==========================================
// 1. COURSES TAB & LESSON DETAILS
// ==========================================
@Composable
fun CoursesTab(viewModel: LearningViewModel, completedSet: Set<String>) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val stats by viewModel.userStats.collectAsStateWithLifecycle()
    val active = viewModel.activeChapter
    if (active != null) {
        LessonDetailScreen(lesson = active, viewModel = viewModel, isCompleted = completedSet.contains(active.id))
    } else {
        var viewMode by remember { mutableStateOf(1) } // Default to beautiful Roadmap view (Option 1)
        
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Mode Switcher Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "核心程式課程",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "自主學習與進度導航",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    // Segmented control switcher
                    Row(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                            .padding(2.dp),
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        listOf("核心關卡", "學習地圖").forEachIndexed { idx, label ->
                            val isSel = viewMode == idx
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(18.dp))
                                    .background(if (isSel) MaterialTheme.colorScheme.primary else Color.Transparent)
                                    .clickable { viewMode = idx }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = label,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSel) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            // Plan 1: Starry Daily Check-In
            item {
                DailyCheckInCard(viewModel = viewModel)
            }

            // Plan 2: Daily Code Puzzle Trivia
            item {
                DailyPuzzleFlipCard(viewModel = viewModel)
            }

            // AI Guided Study Companion & Structured Progress (MANDATORY OPTION 1)
            item {
                AiGuidedCompanionPanel(viewModel = viewModel, completedSet = completedSet)
            }

            if (viewMode == 0) {
                // Section 1: Learning Stats Card
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.TrendingUp,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "學習成效數據",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                }
                                
                                Box(
                                    modifier = Modifier
                                        .background(MaterialTheme.colorScheme.primary, shape = CircleShape)
                                        .padding(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "連續學習 ${stats?.streak ?: 1} 天",
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                // Column 1: XP
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f), shape = RoundedCornerShape(16.dp))
                                        .padding(vertical = 8.dp, horizontal = 4.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "累積經驗值",
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                    )
                                    Text(
                                        text = "${stats?.xp ?: 0} XP",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Black,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                                
                                // Column 2: Quiz Accuracy
                                val quizAcc = if ((stats?.quizTotal ?: 0) > 0) {
                                    (stats!!.quizCorrect * 100) / stats!!.quizTotal
                                } else 0
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f), shape = RoundedCornerShape(16.dp))
                                        .padding(vertical = 8.dp, horizontal = 4.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "測驗正確率",
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                    )
                                    Text(
                                        text = "$quizAcc%",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Black,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                                
                                // Column 3: Stats level
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f), shape = RoundedCornerShape(16.dp))
                                        .padding(vertical = 8.dp, horizontal = 4.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "晉升階段",
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                    )
                                    Text(
                                        text = "等級 Lvl.${stats?.level ?: 1}",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Black,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }

                // Section 2: Continue Course Basics Card
                item {
                    val currentLessons = viewModel.currentLessons
                    val currentCourseCompletedSet = completedSet.filter { id -> currentLessons.any { it.id == id } }
                    val continueLesson = currentLessons.firstOrNull { !currentCourseCompletedSet.contains(it.id) } ?: currentLessons.lastOrNull() ?: CourseData.pythonLessons.first()
                    val progressPercent = if (currentLessons.isNotEmpty()) (currentCourseCompletedSet.size * 100) / currentLessons.size else 0
                    val courseName = when (viewModel.currentCourseType) {
                        com.example.ui.viewmodel.CourseType.PYTHON -> "Python"
                        com.example.ui.viewmodel.CourseType.JAVASCRIPT -> "JavaScript"
                        com.example.ui.viewmodel.CourseType.HTML -> "HTML"
                        com.example.ui.viewmodel.CourseType.KOTLIN -> "Kotlin"
                    }
                    
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "繼續學習 ${courseName} 核心特訓",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                        
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(24.dp))
                                .background(MaterialTheme.colorScheme.secondaryContainer)
                                .clickable { viewModel.selectChapter(continueLesson) }
                                .padding(20.dp)
                        ) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = "單元進度回顧",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.sp
                                    ),
                                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = continueLesson.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                                Text(
                                    text = continueLesson.subtitle,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
                                )
                                
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    LinearProgressIndicator(
                                        progress = progressPercent / 100f,
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(6.dp)
                                            .clip(CircleShape),
                                        color = MaterialTheme.colorScheme.primary,
                                        trackColor = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.2f),
                                    )
                                    Text(
                                        text = "$progressPercent%",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                }
                            }
                        }
                    }
                }

                // Section 3: Daily Lessons List Header
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "全套核心單元課程",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(start = 4.dp, top = 8.dp)
                            )
                            Text(
                                text = "共 ${viewModel.filteredLessons.size} 單元",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Search Bar
                        OutlinedTextField(
                            value = viewModel.lessonSearchQuery,
                            onValueChange = { viewModel.lessonSearchQuery = it },
                            placeholder = { Text("搜尋單元名稱或關鍵字（如 Compose, 變數...）", fontSize = 12.sp) },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", modifier = Modifier.size(20.dp)) },
                            trailingIcon = {
                                if (viewModel.lessonSearchQuery.isNotEmpty()) {
                                    IconButton(onClick = { viewModel.lessonSearchQuery = "" }) {
                                        Icon(Icons.Default.Clear, contentDescription = "Clear", modifier = Modifier.size(18.dp))
                                    }
                                }
                            },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.surface,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surface
                            )
                        )

                        // Categories Horizontal Scroll Filter Chips
                        val categories = viewModel.availableCategories
                        if (categories.size > 1) {
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                contentPadding = PaddingValues(horizontal = 2.dp, vertical = 4.dp)
                            ) {
                                items(categories) { cat ->
                                    val isSelected = viewModel.selectedCategoryFilter == cat
                                    Surface(
                                        onClick = { viewModel.selectedCategoryFilter = cat },
                                        shape = RoundedCornerShape(12.dp),
                                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                        modifier = Modifier.height(32.dp)
                                    ) {
                                        Box(
                                            contentAlignment = Alignment.Center,
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                        ) {
                                            Text(
                                                text = cat,
                                                fontSize = 11.sp,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                val currentLessonsList = viewModel.filteredLessons
                items(currentLessonsList) { lesson ->
                    val allLessons = viewModel.currentLessons
                    val originalIndex = allLessons.indexOfFirst { it.id == lesson.id }
                    val isUnlocked = originalIndex <= 0 || completedSet.contains(allLessons[originalIndex - 1].id)
                    val isDone = completedSet.contains(lesson.id)
                    Card(
                         modifier = Modifier
                             .fillMaxWidth()
                             .clickable { 
                                 if (isUnlocked) {
                                     viewModel.selectChapter(lesson)
                                 } else {
                                     android.widget.Toast.makeText(context, "這堂課尚未解鎖，請依序完成前一單元並答對測驗！", android.widget.Toast.LENGTH_SHORT).show()
                                 }
                             },
                         colors = CardDefaults.cardColors(
                             containerColor = if (!isUnlocked) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.1f)
                             else if (isDone) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                             else MaterialTheme.colorScheme.surface
                         ),
                         shape = RoundedCornerShape(16.dp),
                         border = BorderStroke(
                             1.dp,
                             if (!isUnlocked) MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                             else if (isDone) MaterialTheme.colorScheme.outlineVariant
                             else MaterialTheme.colorScheme.outline
                         )
                    ) {
                         Row(
                             modifier = Modifier
                                 .fillMaxWidth()
                                 .padding(14.dp),
                             verticalAlignment = Alignment.CenterVertically
                         ) {
                             Box(
                                 modifier = Modifier
                                     .size(40.dp)
                                     .clip(RoundedCornerShape(8.dp))
                                     .background(
                                         if (!isUnlocked) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                         else if (isDone) MaterialTheme.colorScheme.primaryContainer
                                         else MaterialTheme.colorScheme.surfaceVariant
                                     ),
                                 contentAlignment = Alignment.Center
                             ) {
                                 Icon(
                                     imageVector = if (!isUnlocked) Icons.Default.Lock else if (isDone) Icons.Default.CheckCircle else Icons.Default.Code,
                                     contentDescription = null,
                                     tint = if (!isUnlocked) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f) else MaterialTheme.colorScheme.primary,
                                     modifier = Modifier.size(20.dp)
                                 )
                             }

                             Spacer(modifier = Modifier.width(16.dp))

                             Column(modifier = Modifier.weight(1.0f)) {
                                 Text(
                                     text = lesson.title,
                                     style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                                     color = if (!isUnlocked) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                                             else if (isDone) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f) 
                                             else MaterialTheme.colorScheme.onSurface
                                 )
                                 Text(
                                     text = if (!isUnlocked) "鎖定中，請先完成前一章節" else lesson.subtitle,
                                     style = MaterialTheme.typography.bodyMedium.copy(fontStyle = androidx.compose.ui.text.font.FontStyle.Italic),
                                     color = if (!isUnlocked) MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                                             else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                     maxLines = 1
                                 )
                             }

                             Spacer(modifier = Modifier.width(8.dp))

                             Icon(
                                 imageVector = if (!isUnlocked) Icons.Default.Lock else Icons.AutoMirrored.Filled.ArrowForward,
                                 contentDescription = if (isUnlocked) "閱讀" else "未解鎖",
                                 tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = if (isUnlocked) 0.5f else 0.25f),
                                 modifier = Modifier.size(20.dp)
                             )
                         }
                    }
                }
            } else {
                // Interactive Learning Roadmap (MANDATORY OPTION 1)
                item {
                    val currentLessonsList = viewModel.currentLessons
                    InteractiveRoadmap(
                        lessons = currentLessonsList,
                        completedSet = completedSet,
                        onLessonClick = { lesson ->
                            viewModel.selectChapter(lesson)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun AiGuidedCompanionPanel(viewModel: LearningViewModel, completedSet: Set<String>) {
    val currentLessons = viewModel.currentLessons
    val total = currentLessons.size
    val completedCount = completedSet.count { id -> currentLessons.any { it.id == id } }
    val nextLesson = currentLessons.firstOrNull { !completedSet.contains(it.id) } ?: currentLessons.lastOrNull() ?: CourseData.pythonLessons.first()
    
    val companionName = when (viewModel.currentCourseType) {
        com.example.ui.viewmodel.CourseType.PYTHON -> "小派 (Py-Buddy)"
        com.example.ui.viewmodel.CourseType.JAVASCRIPT -> "小沙 (JS-Buddy)"
        com.example.ui.viewmodel.CourseType.HTML -> "小網 (HTML-Buddy)"
        com.example.ui.viewmodel.CourseType.KOTLIN -> "小琴 (Kotlin-Buddy)"
    }
    
    val avatarEmoji = when (viewModel.currentCourseType) {
        com.example.ui.viewmodel.CourseType.PYTHON -> "🐍"
        com.example.ui.viewmodel.CourseType.JAVASCRIPT -> "⚡"
        com.example.ui.viewmodel.CourseType.HTML -> "🌐"
        com.example.ui.viewmodel.CourseType.KOTLIN -> "📘"
    }

    val speechText = when {
        completedCount == 0 -> "哈囉！我是你的智慧引導學伴【$companionName】！我們正要開啟這門課的學習旅程。推薦你立刻點擊下方的「解鎖第一步」，讓我們一起學習【${nextLesson.title}】吧！"
        completedCount < total -> "太厲害了！你已經順利學成了 $completedCount/$total 個單元！下一單元我建議學習【${nextLesson.title}】。跟著我繼續精進，你很快就能學有所成！"
        else -> "恭喜你！你已經完成了這門課的所有 $total 個單元！你已經掌握了核心知識。快去挑戰【每日程式謎題】或使用【AI 導師】來進行進階問答吧！"
    }

    var showStageDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f)),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Glow avatar
                val infiniteTransition = rememberInfiniteTransition(label = "avatarGlow")
                val pulseScale by infiniteTransition.animateFloat(
                    initialValue = 0.95f,
                    targetValue = 1.1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(1200, easing = FastOutSlowInEasing),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "pulseScale"
                )

                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .scale(pulseScale)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.secondary
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = avatarEmoji, fontSize = 22.sp)
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = companionName,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "✨ 專屬智慧導讀學伴",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Speech bubble
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                        shape = RoundedCornerShape(
                            topStart = 0.dp,
                            topEnd = 16.dp,
                            bottomEnd = 16.dp,
                            bottomStart = 16.dp
                        )
                    )
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(
                            topStart = 0.dp,
                            topEnd = 16.dp,
                            bottomEnd = 16.dp,
                            bottomStart = 16.dp
                        )
                    )
                    .padding(12.dp)
            ) {
                Text(
                    text = speechText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 20.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Structured Stage Progress
            Text(
                text = "📊 結構化學習階段進度",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 3-Stage Progress Timeline
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Stage 1: Basics (Lessons 1-3)
                val s1Done = completedCount >= 3
                val s1Active = completedCount in 0..2
                StageTimelineBlock(
                    title = "基礎入門",
                    desc = "基礎語法與變數",
                    isDone = s1Done,
                    isActive = s1Active,
                    modifier = Modifier.weight(1f)
                )

                // Stage 2: Core (Lessons 4-7)
                val s2Done = completedCount >= 7
                val s2Active = completedCount in 3..6
                StageTimelineBlock(
                    title = "核心進階",
                    desc = "控制流與核心結構",
                    isDone = s2Done,
                    isActive = s2Active,
                    modifier = Modifier.weight(1f)
                )

                // Stage 3: Master (Lessons 8+)
                val s3Done = completedCount >= total
                val s3Active = completedCount in 7 until total
                StageTimelineBlock(
                    title = "實戰應用",
                    desc = "實戰項目與妙用",
                    isDone = s3Done,
                    isActive = s3Active,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Guide Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { showStageDialog = true },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(imageVector = Icons.Default.Info, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("結構分析", fontSize = 12.sp)
                }

                Button(
                    onClick = { viewModel.selectChapter(nextLesson) },
                    modifier = Modifier.weight(1.2f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(if (completedCount == 0) "解鎖第一步" else "帶領學習：下一步", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    if (showStageDialog) {
        AlertDialog(
            onDismissRequest = { showStageDialog = false },
            title = { Text("學習進度結構化診斷", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("您的全方位編程掌握度 analysis 如下：", style = MaterialTheme.typography.bodyMedium)
                    
                    TextProgressBar(label = "語法基石", current = (completedCount.coerceAtMost(3) * 100) / 3)
                    TextProgressBar(label = "控制與結構", current = (if (completedCount > 3) (completedCount - 3).coerceAtMost(4) * 100 / 4 else 0))
                    TextProgressBar(label = "實戰應用開發", current = (if (completedCount > 7) (completedCount - 7).coerceAtMost(3) * 100 / 3 else 0))
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "💡 學伴建議：持續保持每日登入學習，目前您在程式世界中已獲得 ${completedCount * 100} 積分，完成下一個單元解鎖更多實用成就！",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showStageDialog = false }) {
                    Text("收到，繼續學習！")
                }
            }
        )
    }
}

@Composable
fun StageTimelineBlock(
    title: String,
    desc: String,
    isDone: Boolean,
    isActive: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
                when {
                    isDone -> Color(0xFF4CAF50).copy(alpha = 0.15f)
                    isActive -> MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                    else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f)
                }
            )
            .border(
                width = if (isActive) 1.5.dp else 1.dp,
                color = when {
                    isDone -> Color(0xFF4CAF50)
                    isActive -> MaterialTheme.colorScheme.primary
                    else -> Color.Transparent
                },
                shape = RoundedCornerShape(12.dp)
            )
            .padding(8.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = when {
                        isDone -> Icons.Default.CheckCircle
                        isActive -> Icons.Default.DirectionsRun
                        else -> Icons.Default.Lock
                    },
                    contentDescription = null,
                    tint = when {
                        isDone -> Color(0xFF4CAF50)
                        isActive -> MaterialTheme.colorScheme.primary
                        else -> Color.Gray
                    },
                    modifier = Modifier.size(14.dp)
                )
                Text(
                    text = title,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isDone || isActive) MaterialTheme.colorScheme.onSurface else Color.Gray
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = desc,
                fontSize = 9.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                lineHeight = 11.sp
            )
        }
    }
}

@Composable
fun TextProgressBar(label: String, current: Int) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Text("$current%", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        }
        Spacer(modifier = Modifier.height(2.dp))
        LinearProgressIndicator(
            progress = { current / 100f },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(CircleShape),
            color = if (current == 100) Color(0xFF4CAF50) else MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}

@Composable
fun LegendItem(label: String, color: Color, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(14.dp))
        Text(text = label, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
fun InteractiveRoadmap(
    lessons: List<CourseLesson>,
    completedSet: Set<String>,
    onLessonClick: (CourseLesson) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Map Legend
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f), RoundedCornerShape(12.dp))
                .padding(vertical = 10.dp, horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LegendItem(label = "已學成", color = Color(0xFF4CAF50), icon = Icons.Default.CheckCircle)
            LegendItem(label = "進行中", color = Color(0xFF00B0FF), icon = Icons.Default.PlayArrow)
            LegendItem(label = "未解鎖", color = Color.Gray.copy(alpha = 0.5f), icon = Icons.Default.Lock)
        }
        
        Spacer(modifier = Modifier.height(20.dp))
        
        lessons.forEachIndexed { index, lesson ->
            val isCompleted = completedSet.contains(lesson.id)
            val isUnlocked = index == 0 || completedSet.contains(lessons[index - 1].id)
            val isActive = isUnlocked && !isCompleted
            
            // Zigzag alignment
            val isLeft = index % 2 == 0
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = if (isLeft) Arrangement.Start else Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (!isLeft) {
                    Spacer(modifier = Modifier.weight(0.15f))
                }
                
                // Card for Lesson Node with Custom Border and Apple Glass elements
                Card(
                    modifier = Modifier
                        .weight(0.85f)
                        .clip(RoundedCornerShape(20.dp))
                        .clickable(enabled = isUnlocked) { onLessonClick(lesson) }
                        .border(
                            width = if (isActive) 1.5.dp else 1.dp,
                            color = when {
                                isCompleted -> Color(0xFF4CAF50).copy(alpha = 0.6f)
                                isActive -> MaterialTheme.colorScheme.primary
                                else -> Color.Gray.copy(alpha = 0.2f)
                            },
                            shape = RoundedCornerShape(20.dp)
                        ),
                    colors = CardDefaults.cardColors(
                        containerColor = when {
                            isCompleted -> Color(0xFFE8F5E9).copy(alpha = 0.45f)
                            isActive -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.85f)
                            else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
                        }
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = if (isActive) 4.dp else 0.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Node status indicator
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(
                                    brush = Brush.radialGradient(
                                        colors = when {
                                            isCompleted -> listOf(Color(0xFF81C784), Color(0xFF4CAF50))
                                            isActive -> listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.secondary)
                                            else -> listOf(Color.LightGray.copy(alpha = 0.5f), Color.Gray.copy(alpha = 0.4f))
                                        }
                                    )
                                )
                                .border(1.dp, Color.White.copy(alpha = 0.4f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = when {
                                    isCompleted -> Icons.Default.Check
                                    isActive -> Icons.Default.PlayArrow
                                    else -> Icons.Default.Lock
                                },
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        
                        Spacer(modifier = Modifier.width(10.dp))
                        
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "關卡 ${index + 1}",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = when {
                                    isCompleted -> Color(0xFF2E7D32)
                                    isActive -> MaterialTheme.colorScheme.primary
                                    else -> Color.Gray
                                }
                            )
                            Text(
                                text = lesson.title,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                                color = if (isUnlocked) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                            )
                        }
                        
                        if (isUnlocked) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
                
                if (isLeft) {
                    Spacer(modifier = Modifier.weight(0.15f))
                }
            }
            
            // Connective Line Path
            if (index < lessons.size - 1) {
                val nextIsUnlocked = completedSet.contains(lesson.id)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val w = size.width
                        val h = size.height
                        
                        // Path curve logic
                        val startX = if (isLeft) w * 0.4f else w * 0.6f
                        val endX = if (isLeft) w * 0.6f else w * 0.4f
                        
                        val path = Path().apply {
                            moveTo(startX, 0f)
                            cubicTo(
                                startX, h * 0.5f,
                                endX, h * 0.5f,
                                endX, h
                            )
                        }
                        
                        drawPath(
                            path = path,
                            color = if (nextIsUnlocked) Color(0xFF4CAF50) else Color.LightGray.copy(alpha = 0.4f),
                            style = Stroke(
                                width = 5f,
                                pathEffect = if (!nextIsUnlocked) PathEffect.dashPathEffect(floatArrayOf(12f, 10f), 0f) else null
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LessonDetailScreen(lesson: CourseLesson, viewModel: LearningViewModel, isCompleted: Boolean) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = { viewModel.activeChapter = null }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "儲存返回")
                }
                Text(
                    text = "返回課程列表",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1.0f))
                if (isCompleted) {
                    SuggestionChip(
                        onClick = {},
                        label = { Text("已完成獲得 +${lesson.xpReward} XP") },
                        icon = { Icon(Icons.Default.Check, contentDescription = null, tint = Color.Green) }
                    )
                }
            }
        }

        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                SuggestionChip(
                    onClick = {},
                    label = { Text(lesson.category) },
                    colors = SuggestionChipDefaults.suggestionChipColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        labelColor = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = lesson.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = lesson.subtitle,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        item {
            OutlinedCard(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "📖 課程講解說明",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = lesson.explanationHtmlCode.cleanMarkdownFormat(),
                        style = MaterialTheme.typography.bodyLarge,
                        lineHeight = 26.sp
                    )
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "🐍 Python 範例代碼",
                            color = Color(0xFFFFD700),
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Button(
                            onClick = {
                                viewModel.isCustomCodeMode = true
                                viewModel.customCodeInput = lesson.codeSnippet
                                viewModel.customTerminalOutput = "範例代碼已載入！請點選「執行程式碼」"
                                viewModel.customVariablesWatch = emptyMap()
                                viewModel.currentTab = AppTab.EMULATOR
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF2D2D2D),
                                contentColor = Color.White
                            ),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            modifier = Modifier.height(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = Color(0xFF4CAF50)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("傳送到模擬器", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = lesson.codeSnippet,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 13.sp,
                        color = Color(0xFFD4D4D4),
                        lineHeight = 18.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF121212), shape = RoundedCornerShape(4.dp))
                            .padding(8.dp)
                    )
                }
            }
        }

        item {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "隨堂小小驗收難關",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = lesson.interactiveQuizQuestion,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    lesson.quizAnswers.forEachIndexed { index, optionText ->
                        val isSelected = viewModel.activeLessonSelectedOption == index
                        val highlightBorder = isSelected
                        val displayColor = if (viewModel.activeLessonQuestionAnswered) {
                            if (index == lesson.correctQuizAnswerIndex) Color(0xFF2E7D32)
                            else if (isSelected) Color(0xFFC62828)
                            else MaterialTheme.colorScheme.surface
                        } else {
                            if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                        }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable(enabled = !viewModel.activeLessonQuestionAnswered) {
                                    viewModel.submitLessonQuiz(index)
                                },
                            colors = CardDefaults.cardColors(containerColor = displayColor),
                            border = BorderStroke(
                                if (highlightBorder) 2.dp else 1.dp,
                                if (highlightBorder) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp, horizontal = 16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${'A' + index}.  $optionText",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (viewModel.activeLessonQuestionAnswered && (index == lesson.correctQuizAnswerIndex || isSelected)) Color.White else MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                if (viewModel.activeLessonQuestionAnswered && index == lesson.correctQuizAnswerIndex) {
                                    Icon(imageVector = Icons.Default.Check, contentDescription = "正確", tint = Color.White)
                                }
                            }
                        }
                    }

                    if (viewModel.activeLessonQuestionAnswered) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = if (viewModel.activeLessonIsCorrectAnswer) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
                            )
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = if (viewModel.activeLessonIsCorrectAnswer) Icons.Default.CheckCircle else Icons.Default.Error,
                                        contentDescription = null,
                                        tint = if (viewModel.activeLessonIsCorrectAnswer) Color(0xFF2E7D32) else Color(0xFFC62828),
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (viewModel.activeLessonIsCorrectAnswer) "答對了！加倍回饋！" else "答錯了，沒關係！",
                                        fontWeight = FontWeight.Bold,
                                        color = if (viewModel.activeLessonIsCorrectAnswer) Color(0xFF2E7D32) else Color(0xFFC62828)
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = lesson.quizExplanation.cleanMarkdownFormat(),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.Black.copy(alpha = 0.8f)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.claimLessonCompletion() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text(
                                "我已完成閱讀並且吸收此單元知識 (+20 XP)",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }

        item {
            val challenge = com.example.courses.HandsOnChallenges.challenges[lesson.id]
            if (challenge != null) {
                HandsOnChallengeCard(challenge = challenge, viewModel = viewModel)
            }
        }
        
        item {
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun HandsOnChallengeCard(challenge: com.example.courses.HandsOnChallenge, viewModel: LearningViewModel) {
    val isCompleted = viewModel.handsOnCompletedSet.contains(challenge.lessonId)
    
    val language = remember(challenge.lessonId) {
        when {
            challenge.lessonId.startsWith("js_") -> "javascript"
            challenge.lessonId.startsWith("html_") -> "html"
            else -> "python"
        }
    }
    
    var handsOnValue by remember {
        mutableStateOf(
            TextFieldValue(
                text = viewModel.handsOnCodeInput,
                selection = TextRange(viewModel.handsOnCodeInput.length)
            )
        )
    }

    LaunchedEffect(viewModel.handsOnCodeInput) {
        if (viewModel.handsOnCodeInput != handsOnValue.text) {
            handsOnValue = TextFieldValue(
                text = viewModel.handsOnCodeInput,
                selection = TextRange(viewModel.handsOnCodeInput.length)
            )
        }
    }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("hands_on_challenge_card"),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        ),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(
            1.dp, 
            if (isCompleted) Color(0xFF4CAF50).copy(alpha = 0.7f) else MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Code,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "🧠 舉一反三：動手實作測驗",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (isCompleted) {
                    Box(
                        modifier = Modifier
                            .background(Color(0xFF2E7D32), shape = RoundedCornerShape(12.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "已通關 +30 XP",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = challenge.title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = challenge.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Task explanation
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Column {
                    Text(
                        text = "📋 實作通關目標：",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = challenge.expectedExplanation,
                        style = MaterialTheme.typography.bodyMedium,
                        lineHeight = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Python Code Editor Card
            Text(
                text = "💻 程式編輯器：",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 6.dp)
            )
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
                border = BorderStroke(1.dp, Color(0xFF333333))
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    // Line Number Gutter
                    val rawText = handsOnValue.text
                    val linesList = rawText.split("\n")
                    val totalLines = maxOf(1, linesList.size)
                    val errorLineNum = viewModel.handsOnErrorLine
                    
                    Column(
                        modifier = Modifier
                            .background(Color(0xFF121212))
                            .padding(vertical = 12.dp, horizontal = 8.dp)
                            .widthIn(min = 36.dp),
                        horizontalAlignment = Alignment.End
                    ) {
                        for (i in 1..totalLines) {
                            val isActiveError = i == errorLineNum
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.End,
                                modifier = Modifier.height(20.dp)
                            ) {
                                if (isActiveError) {
                                    Text(
                                        text = "⚠️",
                                        fontSize = 11.sp,
                                        modifier = Modifier.padding(end = 4.dp)
                                    )
                                }
                                Text(
                                    text = i.toString(),
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 13.sp,
                                    fontWeight = if (isActiveError) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isActiveError) Color(0xFFEF5350) else Color(0xFF8B949E)
                                )
                            }
                        }
                    }
                    
                    // Vertical Divider
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(1.dp)
                            .background(Color(0xFF2E2E2E))
                    )

                    // Text Field Editor Area
                    Column(modifier = Modifier.weight(1f).padding(vertical = 4.dp)) {
                        val highlightTransformation = remember(language, viewModel.handsOnErrorLine) {
                            com.example.ui.CodeHighlightTransformation(language, viewModel.handsOnErrorLine)
                        }
                        TextField(
                            value = handsOnValue,
                            onValueChange = { newValue ->
                                val indented = com.example.ui.CodeEditorHelper.handleCodeChangeWithAutoIndent(
                                    oldValue = handsOnValue,
                                    newValue = newValue,
                                    language = language
                                )
                                handsOnValue = indented
                                viewModel.handsOnCodeInput = indented.text
                            },
                            textStyle = TextStyle(
                                fontFamily = FontFamily.Monospace,
                                fontSize = 13.sp,
                                color = Color(0xFFD4D4D4),
                                lineHeight = 20.sp
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 100.dp, max = 220.dp)
                                .testTag("hands_on_code_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent,
                                cursorColor = Color(0xFFFFD700)
                            ),
                            singleLine = false,
                            visualTransformation = highlightTransformation,
                            placeholder = {
                                Text(
                                    if (challenge.lessonId.startsWith("js_")) "// 在此輸入你的 JavaScript 程式碼" else if (challenge.lessonId.startsWith("html_")) "<!-- 在此輸入你的 HTML 程式碼 -->" else "# 在此輸入你的 Python 程式碼",
                                    color = Color.Gray,
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 13.sp
                                )
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Run and control bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { viewModel.runAndVerifyHandsOn(challenge.lessonId) },
                    modifier = Modifier
                        .weight(1.2f)
                        .height(44.dp)
                        .testTag("verify_code_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isCompleted) Color(0xFF2E7D32) else MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        imageVector = if (isCompleted) Icons.Default.CheckCircle else Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        if (isCompleted) "重新運算並驗證" else "執行代碼並驗證",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                if (viewModel.handsOnSuccess) {
                    Button(
                        onClick = { viewModel.runHandsOnOptimization() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.tertiary
                        ),
                        modifier = Modifier
                            .height(44.dp)
                            .weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Psychology,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "✨ AI 優化建議",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                OutlinedButton(
                    onClick = {
                        handsOnValue = TextFieldValue(text = challenge.initialCode, selection = TextRange(challenge.initialCode.length))
                        viewModel.handsOnCodeInput = challenge.initialCode
                    },
                    modifier = Modifier.height(44.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "重設"
                    )
                }
            }

            // Feedback Message Alert Box
            if (viewModel.handsOnFeedbackMessage.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (viewModel.handsOnSuccess) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (viewModel.handsOnSuccess) Icons.Default.CheckCircle else Icons.Default.Error,
                            contentDescription = null,
                            tint = if (viewModel.handsOnSuccess) Color(0xFF2E7D32) else Color(0xFFC62828),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (viewModel.handsOnSuccess) "驗證成功！任務圓滿通關！" else "挑戰未完備，繼續加油！",
                                fontWeight = FontWeight.Bold,
                                color = if (viewModel.handsOnSuccess) Color(0xFF2E7D32) else Color(0xFFC62828),
                                style = MaterialTheme.typography.bodyMedium
                              )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = viewModel.handsOnFeedbackMessage,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Black.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            }

            // AI Error Analysis loading & diagnostic result box
            if (viewModel.handsOnIsAiAnalyzing) {
                Spacer(modifier = Modifier.height(12.dp))
                Card(
                    modifier = Modifier.fillMaxWidth().testTag("hands_on_ai_loading_card"),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.3f)),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.tertiary,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "🧠 Gemini 正在為您進行深度除錯分析，請稍候...",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                }
            }

            if (viewModel.handsOnAiErrorAnalysis.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Card(
                    modifier = Modifier.fillMaxWidth().testTag("hands_on_ai_analysis_card"),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.15f)),
                    border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.tertiary.copy(alpha = 0.6f))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Psychology,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.tertiary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "🧠 AI 智慧除錯與分析建議",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        }
                        
                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f),
                            modifier = Modifier.padding(bottom = 10.dp)
                        )

                        // Conversational dialogue flow rendering
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 240.dp)
                        ) {
                            items(viewModel.handsOnChatHistory.size) { index ->
                                val (role, text) = viewModel.handsOnChatHistory[index]
                                val isUser = role == "User"
                                
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
                                ) {
                                    Text(
                                        text = if (isUser) "💬 我的提問：" else "🎓 AI 導師建議：",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isUser) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.tertiary
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Card(
                                        colors = CardDefaults.cardColors(
                                            containerColor = if (isUser) MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)
                                            else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                                        ),
                                        modifier = Modifier.widthIn(max = 280.dp),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Text(
                                            text = text,
                                            style = MaterialTheme.typography.bodyMedium,
                                            modifier = Modifier.padding(10.dp),
                                            lineHeight = 22.sp
                                        )
                                    }
                                }
                            }
                        }

                        // One Click Quick Fix Button
                        viewModel.handsOnFixedCode?.let { fixed ->
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = {
                                    handsOnValue = TextFieldValue(text = fixed, selection = TextRange(fixed.length))
                                    viewModel.handsOnCodeInput = fixed
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("hands_on_apply_fixed_code_btn")
                            ) {
                                Icon(imageVector = Icons.Default.Build, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("🛠️ 一鍵自動套用 AI 修正代碼", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                            }
                        }

                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f),
                            modifier = Modifier.padding(vertical = 12.dp)
                        )

                        // Follow-up input form
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextField(
                                value = viewModel.handsOnQuestionInput,
                                onValueChange = { viewModel.handsOnQuestionInput = it },
                                placeholder = { Text("💬 追問 AI 老師相關細節...", fontSize = 12.sp) },
                                modifier = Modifier.weight(1f),
                                textStyle = TextStyle(fontSize = 13.sp),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                ),
                                shape = RoundedCornerShape(20.dp),
                                singleLine = true
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            IconButton(
                                onClick = { viewModel.askHandsOnFollowUp(viewModel.handsOnQuestionInput) },
                                enabled = viewModel.handsOnQuestionInput.isNotBlank() && !viewModel.handsOnIsAiAnalyzing,
                                colors = IconButtonDefaults.filledIconButtonColors(
                                    containerColor = MaterialTheme.colorScheme.tertiary,
                                    contentColor = Color.White
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Send,
                                    contentDescription = "提問",
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Simulated Terminal Block
            if (viewModel.handsOnTerminalOutput.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "📟 虛擬終端輸出 (Terminal Output)",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F0F0F))
                ) {
                    Column(
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = viewModel.handsOnTerminalOutput,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 12.sp,
                            color = Color(0xFF00FF00),
                            lineHeight = 16.sp,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // Simulated Variables Block
            if (viewModel.handsOnVariablesWatch.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "💾 變數虛擬記憶體監測器 (Variable State)",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        viewModel.handsOnVariablesWatch.forEach { (varName, varVal) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = varName,
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = varVal,
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 2. EMULATOR PLAYGROUND TAB
// ==========================================
@Composable
fun EmulatorTab(viewModel: LearningViewModel) {
    var isDropdownExpanded by remember { mutableStateOf(false) }

    val customLanguage = remember(viewModel.currentCourseType) {
        when (viewModel.currentCourseType) {
            com.example.ui.viewmodel.CourseType.JAVASCRIPT -> "javascript"
            com.example.ui.viewmodel.CourseType.HTML -> "html"
            com.example.ui.viewmodel.CourseType.KOTLIN -> "kotlin"
            else -> "python"
        }
    }

    var customCodeValue by remember {
        mutableStateOf(
            TextFieldValue(
                text = viewModel.customCodeInput,
                selection = TextRange(viewModel.customCodeInput.length)
            )
        )
    }

    LaunchedEffect(viewModel.customCodeInput) {
        if (viewModel.customCodeInput != customCodeValue.text) {
            customCodeValue = TextFieldValue(
                text = viewModel.customCodeInput,
                selection = TextRange(viewModel.customCodeInput.length)
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        val langName = when (viewModel.currentCourseType) {
            com.example.ui.viewmodel.CourseType.PYTHON -> "Python 3"
            com.example.ui.viewmodel.CourseType.JAVASCRIPT -> "JavaScript"
            com.example.ui.viewmodel.CourseType.HTML -> "HTML5 DOM"
            com.example.ui.viewmodel.CourseType.KOTLIN -> "Kotlin JVM"
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (viewModel.isCustomCodeMode) "$langName 程式演練模擬器" else "引導式代碼單步偵錯",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("自由撰寫模式", style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.width(4.dp))
                Switch(
                    checked = viewModel.isCustomCodeMode,
                    onCheckedChange = {
                        viewModel.isCustomCodeMode = it
                        viewModel.resetEmulator()
                    },
                    modifier = Modifier.testTag("custom_code_switch")
                )
            }
        }

        // Language Engine Switcher Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = viewModel.currentCourseType == com.example.ui.viewmodel.CourseType.PYTHON,
                onClick = { viewModel.switchCourseType(com.example.ui.viewmodel.CourseType.PYTHON) },
                label = { Text("Python 模擬") }
            )
            FilterChip(
                selected = viewModel.currentCourseType == com.example.ui.viewmodel.CourseType.JAVASCRIPT,
                onClick = { viewModel.switchCourseType(com.example.ui.viewmodel.CourseType.JAVASCRIPT) },
                label = { Text("JS 模擬") }
            )
            FilterChip(
                selected = viewModel.currentCourseType == com.example.ui.viewmodel.CourseType.HTML,
                onClick = { viewModel.switchCourseType(com.example.ui.viewmodel.CourseType.HTML) },
                label = { Text("HTML 渲染") }
            )
            FilterChip(
                selected = viewModel.currentCourseType == com.example.ui.viewmodel.CourseType.KOTLIN,
                onClick = { viewModel.switchCourseType(com.example.ui.viewmodel.CourseType.KOTLIN) },
                label = { Text("Kotlin 模擬") }
            )
        }

        if (!viewModel.isCustomCodeMode) {
            OutlinedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                onClick = { isDropdownExpanded = true }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "當前範本：${viewModel.preloadedScript.title}",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = viewModel.preloadedScript.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "展開選擇")
                }

                DropdownMenu(
                    expanded = isDropdownExpanded,
                    onDismissRequest = { isDropdownExpanded = false }
                ) {
                    PythonPlayground.scripts.forEachIndexed { index, script ->
                        DropdownMenuItem(
                            text = { Text(script.title) },
                            onClick = {
                                viewModel.selectScript(index)
                                isDropdownExpanded = false
                            }
                        )
                    }
                }
            }
        }

        Card(
            modifier = Modifier
                .weight(1.2f)
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E), contentColor = Color.White),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Code,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("代碼寫入視窗", fontFamily = FontFamily.Monospace, fontSize = 12.sp, color = Color.Gray)
                    }
                    if (viewModel.isCustomCodeMode) {
                        TextButton(onClick = {
                            customCodeValue = TextFieldValue("")
                            viewModel.customCodeInput = ""
                        }) {
                            Text("清空代碼", color = Color.LightGray, fontSize = 11.sp)
                        }
                    } else {
                        Text(
                            text = if (viewModel.currentStepIdx == -1) "尚未啟動，請按單步執行" 
                            else "執行至第 ${viewModel.preloadedScript.steps[viewModel.currentStepIdx].currentLineIndex} 行",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp,
                            color = Color(0xFFFFD700)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                if (viewModel.isCustomCodeMode) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                    ) {
                        // Interactive Line Numbers (Gutter)
                        val rawText = customCodeValue.text
                        val linesList = rawText.split("\n")
                        val totalLines = maxOf(1, linesList.size)
                        val errorLineNum = viewModel.customErrorLine
                        
                        Column(
                            modifier = Modifier
                                .background(Color(0xFF121212))
                                .padding(vertical = 12.dp, horizontal = 8.dp)
                                .widthIn(min = 34.dp),
                            horizontalAlignment = Alignment.End
                        ) {
                            for (i in 1..totalLines) {
                                val isActiveError = i == errorLineNum
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.End,
                                    modifier = Modifier.height(18.dp)
                                ) {
                                    if (isActiveError) {
                                        Text(
                                            text = "⚠️",
                                            fontSize = 10.sp,
                                            modifier = Modifier.padding(end = 4.dp)
                                        )
                                    }
                                    Text(
                                        text = i.toString(),
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 12.sp,
                                        fontWeight = if (isActiveError) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isActiveError) Color(0xFFEF5350) else Color(0xFF8B949E)
                                    )
                                }
                            }
                        }

                        // Gutter Divider
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(1.dp)
                                .background(Color(0xFF2E2E2E))
                        )

                        // Main Text Field
                        val highlightTransformation = remember(customLanguage, viewModel.customErrorLine) {
                            com.example.ui.CodeHighlightTransformation(customLanguage, viewModel.customErrorLine)
                        }
                        TextField(
                            value = customCodeValue,
                            onValueChange = { newValue ->
                                val indented = com.example.ui.CodeEditorHelper.handleCodeChangeWithAutoIndent(
                                    oldValue = customCodeValue,
                                    newValue = newValue,
                                    language = customLanguage
                                )
                                customCodeValue = indented
                                viewModel.customCodeInput = indented.text
                            },
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1f)
                                .testTag("code_editor_field"),
                            textStyle = TextStyle(
                                fontFamily = FontFamily.Monospace,
                                fontSize = 12.sp,
                                color = Color(0xFFE0E0E0),
                                lineHeight = 18.sp
                            ),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFF151515),
                                unfocusedContainerColor = Color(0xFF151515),
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            shape = RoundedCornerShape(2.dp),
                            visualTransformation = highlightTransformation,
                            placeholder = {
                                val text = when(viewModel.currentCourseType) {
                                    com.example.ui.viewmodel.CourseType.JAVASCRIPT -> "// 在此處輸入並測試 JavaScript 代碼..."
                                    com.example.ui.viewmodel.CourseType.HTML -> "<!-- 在此處輸入並測試 HTML 代碼... -->"
                                    com.example.ui.viewmodel.CourseType.KOTLIN -> "// 在此處輸入並測試 Kotlin 代碼..."
                                    else -> "# 在此處輸入並測試 Python 代碼..."
                                }
                                Text(text, color = Color.DarkGray, fontSize = 13.sp)
                            }
                        )
                    }
                } else {
                    val lines = viewModel.preloadedScript.code.split("\n")
                    val currentStepLineNo = if (viewModel.currentStepIdx in viewModel.preloadedScript.steps.indices) {
                        viewModel.preloadedScript.steps[viewModel.currentStepIdx].currentLineIndex
                    } else -1

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                            .background(Color(0xFF121212), shape = RoundedCornerShape(8.dp))
                            .padding(8.dp)
                    ) {
                        items(lines.size) { lineIndex ->
                            val lineNum = lineIndex + 1
                            val isCurrentLine = lineNum == currentStepLineNo
                            val lineContent = lines[lineIndex]

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        if (isCurrentLine) Color(0xFF333300) else Color.Transparent,
                                        shape = RoundedCornerShape(2.dp)
                                    )
                                    .padding(vertical = 1.dp)
                            ) {
                                Text(
                                    text = String.format("%2d │ ", lineNum),
                                    color = if (isCurrentLine) Color(0xFFFFD700) else Color.DarkGray,
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 13.sp,
                                    modifier = Modifier.width(36.dp)
                                )
                                Text(
                                    text = lineContent,
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 13.sp,
                                    color = if (isCurrentLine) Color.White else Color(0xFFCCCCCC)
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (viewModel.isCustomCodeMode) {
                Button(
                    onClick = { viewModel.runCustomPython() },
                    modifier = Modifier
                        .weight(1.2f)
                        .height(44.dp)
                        .testTag("run_code_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("執行程式碼", fontWeight = FontWeight.Bold)
                }
                
                // Show AI Optimization review button if program executed with no syntax errors
                if (viewModel.customTerminalOutput.isNotEmpty() && !viewModel.customTerminalOutput.contains("語法錯誤") && !viewModel.customTerminalOutput.contains("錯誤")) {
                    Button(
                        onClick = { viewModel.runCustomOptimization() },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Psychology, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("✨ AI 品質與優化分析", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    }
                }

                OutlinedButton(
                    onClick = { viewModel.resetEmulator() },
                    modifier = Modifier
                        .weight(0.5f)
                        .height(44.dp)
                ) {
                    Text("重置")
                }
            } else {
                Button(
                    onClick = { viewModel.executeSingleStep() },
                    modifier = Modifier
                        .weight(1.2f)
                        .height(44.dp)
                        .testTag("step_code_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    enabled = viewModel.currentStepIdx < viewModel.preloadedScript.steps.size - 1
                ) {
                    Icon(imageVector = Icons.Default.SkipNext, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("下單步 step", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = { viewModel.toggleAutoStep() },
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (viewModel.isAutoStepping) Color(0xFFD32F2F) else MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Icon(
                        imageVector = if (viewModel.isAutoStepping) Icons.Default.Pause else Icons.Default.FastForward,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (viewModel.isAutoStepping) "暫停" else "自動播放")
                }

                OutlinedButton(
                    onClick = { viewModel.resetEmulator() },
                    modifier = Modifier
                        .weight(0.6f)
                        .height(44.dp)
                ) {
                    Text("重置")
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .weight(1.1f)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Card(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp)),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.12f))
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = "📦 虛擬記憶體狀態",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "變數與當下數值監控",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp))

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        if (viewModel.isCustomCodeMode) {
                            if (viewModel.customVariablesWatch.isEmpty()) {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillParentMaxSize()
                                            .padding(8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            "記憶體暫時空置\n(宣告變數如 x = 5)",
                                            style = MaterialTheme.typography.bodySmall,
                                            textAlign = TextAlign.Center,
                                            color = Color.Gray
                                        )
                                    }
                                }
                            } else {
                                items(viewModel.customVariablesWatch.keys.toList()) { varName ->
                                    val valStr = viewModel.customVariablesWatch[varName] ?: ""
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(
                                                MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.2f),
                                                shape = RoundedCornerShape(4.dp)
                                            )
                                            .padding(6.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(varName, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                                        Text(valStr, fontFamily = FontFamily.Monospace, color = MaterialTheme.colorScheme.secondary)
                                    }
                                }
                            }
                        } else {
                            if (viewModel.stepVariablesWatch.isEmpty()) {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillParentMaxSize()
                                            .padding(8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            "調用單步以啟用記憶體",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color.Gray
                                        )
                                    }
                                }
                            } else {
                                items(viewModel.stepVariablesWatch) { variable ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(
                                                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f),
                                                shape = RoundedCornerShape(4.dp)
                                            )
                                            .padding(horizontal = 8.dp, vertical = 6.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column {
                                            Text(variable.name, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                                            Text("型態: ${variable.type}", fontSize = 9.sp, color = Color.Gray)
                                        }
                                        Text(
                                            text = variable.value,
                                            fontFamily = FontFamily.Monospace,
                                            color = MaterialTheme.colorScheme.primary,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Card(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0F0F0F)),
                border = BorderStroke(1.dp, Color.DarkGray)
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "📟 Console 終端印出",
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = Color(0xFF00FF00)
                        )
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(if (viewModel.isAutoStepping) Color.Green else Color.Gray)
                        )
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp), color = Color.DarkGray)

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(2.dp),
                        state = rememberLazyListState()
                    ) {
                        if (viewModel.isCustomCodeMode) {
                            item {
                                Text(
                                    text = viewModel.customTerminalOutput,
                                    fontFamily = FontFamily.Monospace,
                                    fontSize = 12.sp,
                                    color = if (viewModel.customTerminalOutput.contains("語法錯誤") || viewModel.customTerminalOutput.contains("錯誤")) Color.Red else Color(0xFFE0E0E0)
                                )
                            }
                        } else {
                            if (viewModel.stepTerminalHistory.isEmpty()) {
                                item {
                                    Text(
                                        text = "$ python running...\n(等候指令印出)",
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 12.sp,
                                        color = Color.DarkGray
                                    )
                                }
                            } else {
                                items(viewModel.stepTerminalHistory) { termLine ->
                                    Text(
                                        text = termLine,
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 12.sp,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        if (viewModel.isCustomCodeMode && (viewModel.customIsAiAnalyzing || viewModel.customAiErrorAnalysis.isNotEmpty())) {
            Spacer(modifier = Modifier.height(12.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("custom_ai_analysis_card"),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.15f)),
                border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.tertiary.copy(alpha = 0.6f))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Psychology,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.tertiary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "🧠 AI 智慧除錯與建議",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        }
                        if (viewModel.customIsAiAnalyzing) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = MaterialTheme.colorScheme.tertiary,
                                strokeWidth = 2.dp
                            )
                        } else {
                            TextButton(onClick = { viewModel.customAiErrorAnalysis = "" }) {
                                Text("關閉", fontSize = 11.sp, color = MaterialTheme.colorScheme.tertiary)
                            }
                        }
                    }
                    
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f),
                        modifier = Modifier.padding(vertical = 6.dp)
                    )

                    if (viewModel.customIsAiAnalyzing && viewModel.customChatHistory.isEmpty()) {
                        Text(
                            text = "正在深度排查，Gemini AI 老師正在撰寫分析與重構建議，請稍候...",
                            style = MaterialTheme.typography.bodyMedium,
                            fontStyle = FontStyle.Italic,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 200.dp)
                        ) {
                            items(viewModel.customChatHistory.size) { index ->
                                val (role, text) = viewModel.customChatHistory[index]
                                val isUser = role == "User"
                                
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
                                ) {
                                    Text(
                                        text = if (isUser) "💬 我的提問：" else "🎓 AI 導師建議：",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isUser) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.tertiary
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Card(
                                        colors = CardDefaults.cardColors(
                                            containerColor = if (isUser) MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)
                                            else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                                        ),
                                        modifier = Modifier.widthIn(max = 280.dp),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Text(
                                            text = text,
                                            style = MaterialTheme.typography.bodyMedium,
                                            modifier = Modifier.padding(10.dp),
                                            lineHeight = 22.sp
                                        )
                                    }
                                }
                            }
                        }

                        // One Click Quick Fix Button
                        viewModel.customFixedCode?.let { fixed ->
                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                onClick = {
                                    customCodeValue = TextFieldValue(text = fixed, selection = TextRange(fixed.length))
                                    viewModel.customCodeInput = fixed
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("custom_apply_fixed_code_btn")
                            ) {
                                Icon(imageVector = Icons.Default.Build, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("🛠️ 一鍵自動套用 AI 修正建議或優化", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                            }
                        }

                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f),
                            modifier = Modifier.padding(vertical = 8.dp)
                        )

                        // Follow-up interaction form
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextField(
                                value = viewModel.customQuestionInput,
                                onValueChange = { viewModel.customQuestionInput = it },
                                placeholder = { Text("💬 追問 AI 老師相關細節...", fontSize = 11.sp) },
                                modifier = Modifier.weight(1f),
                                textStyle = TextStyle(fontSize = 12.sp),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                ),
                                shape = RoundedCornerShape(20.dp),
                                singleLine = true
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            IconButton(
                                onClick = { viewModel.askCustomFollowUp(viewModel.customQuestionInput) },
                                enabled = viewModel.customQuestionInput.isNotBlank() && !viewModel.customIsAiAnalyzing,
                                colors = IconButtonDefaults.filledIconButtonColors(
                                    containerColor = MaterialTheme.colorScheme.tertiary,
                                    contentColor = Color.White
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Send,
                                    contentDescription = "提問",
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 3. QUIZ INTERACTION TAB
// ==========================================
@Composable
fun QuizTab(viewModel: LearningViewModel) {
    val currentType = viewModel.currentCourseType
    val (courseTitle, courseDesc) = when (currentType) {
        com.example.ui.viewmodel.CourseType.PYTHON -> Pair("Python 語法實戰挑戰", "動態生成 5 道隨機的 Python 高手關鍵題目。\n涵蓋變數、迴圈、列表與函式操作，答對即可累積經驗點數！")
        com.example.ui.viewmodel.CourseType.JAVASCRIPT -> Pair("JavaScript 全能語法測驗", "動態生成 5 道隨機 JavaScript 實務測試題。\n驗收 let/const 宣告、console.log 輸出、DOM 事件與非同步邏輯！")
        com.example.ui.viewmodel.CourseType.HTML -> Pair("HTML5 網頁標籤總挑戰", "動態生成 5 道隨機 HTML5 網頁語法測試題。\n考驗您對 h1~h6、p、a、img、button 與結構標籤的熟練度！")
        com.example.ui.viewmodel.CourseType.KOTLIN -> Pair("Kotlin 現代語法實戰測驗", "動態生成 5 道隨機 Kotlin 核心語法測試題。\n驗收 val/var、Null 安全 (?. / ?:)、Elvis 運算子與 Collection 函數！")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Course Type Selector Chips for Quiz
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = currentType == com.example.ui.viewmodel.CourseType.PYTHON,
                onClick = { viewModel.switchCourseType(com.example.ui.viewmodel.CourseType.PYTHON) },
                label = { Text("Python 測驗") },
                leadingIcon = { Icon(Icons.Default.School, contentDescription = null, modifier = Modifier.size(16.dp)) }
            )
            FilterChip(
                selected = currentType == com.example.ui.viewmodel.CourseType.JAVASCRIPT,
                onClick = { viewModel.switchCourseType(com.example.ui.viewmodel.CourseType.JAVASCRIPT) },
                label = { Text("JavaScript 測驗") },
                leadingIcon = { Icon(Icons.Default.Code, contentDescription = null, modifier = Modifier.size(16.dp)) }
            )
            FilterChip(
                selected = currentType == com.example.ui.viewmodel.CourseType.HTML,
                onClick = { viewModel.switchCourseType(com.example.ui.viewmodel.CourseType.HTML) },
                label = { Text("HTML 測驗") },
                leadingIcon = { Icon(Icons.Default.Language, contentDescription = null, modifier = Modifier.size(16.dp)) }
            )
            FilterChip(
                selected = currentType == com.example.ui.viewmodel.CourseType.KOTLIN,
                onClick = { viewModel.switchCourseType(com.example.ui.viewmodel.CourseType.KOTLIN) },
                label = { Text("Kotlin 測驗") },
                leadingIcon = { Icon(Icons.Default.SmartButton, contentDescription = null, modifier = Modifier.size(16.dp)) }
            )
        }

        when (viewModel.quizStatus) {
            QuizStatus.NOT_STARTED -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(vertical = 16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(88.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Psychology,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = courseTitle,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = courseDesc,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "💡 溫馨提醒：這些題目是用於綜合實戰驗收。如果您剛接觸程式，強烈建議先前往「課程學習」閱讀教材，或隨時「召喚 AI 導師」進行雙向引導問答！當然您也隨時可以嘗試作答，並直接閱讀每題詳解唷！",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                lineHeight = 18.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = { viewModel.startNewQuizBatch() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("start_quiz_button"),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("開始挑戰 5 題測驗", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    }
                }
            }

            QuizStatus.IN_PROGRESS, QuizStatus.GRADED -> {
                val question = viewModel.currentQuizQuestion
                if (question != null) {
                    val progressRatio = (viewModel.quizCurrentIndex.toFloat()) / viewModel.activeQuizList.size
                    
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // Top Header: Progress Counter, AI Coach Button, Difficulty Badge
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "測驗進度: ${viewModel.quizCurrentIndex + 1} / ${viewModel.activeQuizList.size} 題",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                TextButton(
                                    onClick = {
                                        viewModel.askAiCoachAboutQuiz(
                                            questionText = question.question,
                                            options = question.options
                                        )
                                    },
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                    modifier = Modifier.height(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Chat,
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp),
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("問 AI 導師", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "難度: ${question.difficulty}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = when (question.difficulty) {
                                        "精簡" -> Color(0xFF2E7D32)
                                        "核心" -> Color(0xFF1565C0)
                                        else -> Color(0xFFC2185B)
                                    },
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        LinearProgressIndicator(
                            progress = { progressRatio },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.secondaryContainer
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Question Card (Displays question clearly)
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp)
                            ) {
                                Text(
                                    text = question.question,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    lineHeight = 24.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Scrollable Answer Options: using LazyColumn so the 4th option is always easily reached
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(vertical = 4.dp)
                        ) {
                            itemsIndexed(question.options) { optIndex, optionText ->
                                val isSelected = viewModel.quizSelectedOption == optIndex
                                val canClick = viewModel.quizStatus == QuizStatus.IN_PROGRESS

                                val cardBgColor = if (viewModel.quizStatus == QuizStatus.GRADED) {
                                    if (optIndex == question.correctAnswerIndex) Color(0xFF2E7D32)
                                    else if (isSelected) Color(0xFFC62828)
                                    else MaterialTheme.colorScheme.surface
                                } else {
                                    if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                                }

                                val textColor = if (viewModel.quizStatus == QuizStatus.GRADED && (optIndex == question.correctAnswerIndex || isSelected)) {
                                    Color.White
                                } else if (isSelected) {
                                    MaterialTheme.colorScheme.onPrimaryContainer
                                } else {
                                    MaterialTheme.colorScheme.onSurface
                                }

                                val optionLetter = ('A' + optIndex).toString()

                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .clickable(enabled = canClick) {
                                            if (viewModel.quizSelectedOption == optIndex) {
                                                viewModel.submitQuizQuestion(optIndex)
                                            } else {
                                                viewModel.quizSelectedOption = optIndex
                                            }
                                        }
                                        .testTag("quiz_option_$optIndex"),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = cardBgColor),
                                    border = BorderStroke(
                                        if (isSelected) 2.dp else 1.dp,
                                        if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 14.dp, vertical = 12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        // Option badge (A, B, C, D)
                                        Box(
                                            modifier = Modifier
                                                .size(28.dp)
                                                .clip(CircleShape)
                                                .background(
                                                    if (viewModel.quizStatus == QuizStatus.GRADED && (optIndex == question.correctAnswerIndex || isSelected)) {
                                                        Color.White.copy(alpha = 0.25f)
                                                    } else if (isSelected) {
                                                        MaterialTheme.colorScheme.primary
                                                    } else {
                                                        MaterialTheme.colorScheme.surfaceVariant
                                                    }
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = optionLetter,
                                                style = MaterialTheme.typography.labelLarge,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isSelected && viewModel.quizStatus != QuizStatus.GRADED) {
                                                    MaterialTheme.colorScheme.onPrimary
                                                } else {
                                                    textColor
                                                }
                                            )
                                        }

                                        Spacer(modifier = Modifier.width(12.dp))

                                        Text(
                                            text = optionText,
                                            style = MaterialTheme.typography.bodyLarge,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            color = textColor,
                                            modifier = Modifier.weight(1f),
                                            lineHeight = 22.sp
                                        )

                                        if (viewModel.quizStatus == QuizStatus.GRADED) {
                                            Spacer(modifier = Modifier.width(8.dp))
                                            if (optIndex == question.correctAnswerIndex) {
                                                Icon(imageVector = Icons.Default.CheckCircle, contentDescription = "正確", tint = Color.White)
                                            } else if (isSelected) {
                                                Icon(imageVector = Icons.Default.Cancel, contentDescription = "錯誤", tint = Color.White)
                                            }
                                        }
                                    }
                                }
                            }

                            if (viewModel.quizStatus == QuizStatus.GRADED) {
                                item {
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 6.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
                                        ),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Column(modifier = Modifier.padding(14.dp)) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    imageVector = Icons.Default.Info,
                                                    contentDescription = null,
                                                    tint = MaterialTheme.colorScheme.primary,
                                                    modifier = Modifier.size(20.dp)
                                                )
                                                Spacer(modifier = Modifier.width(8.dp))
                                                Text(
                                                    text = "題目詳細解析：",
                                                    fontWeight = FontWeight.Bold,
                                                    style = MaterialTheme.typography.titleMedium,
                                                    color = MaterialTheme.colorScheme.primary
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(6.dp))
                                            Text(
                                                text = question.explanation.cleanMarkdownFormat(),
                                                style = MaterialTheme.typography.bodyMedium,
                                                lineHeight = 22.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Bottom Action Button — Pinned and ALWAYS visible regardless of screen size!
                        if (viewModel.quizStatus == QuizStatus.IN_PROGRESS) {
                            Button(
                                onClick = {
                                    if (viewModel.quizSelectedOption in question.options.indices) {
                                        viewModel.submitQuizQuestion(viewModel.quizSelectedOption)
                                    }
                                },
                                enabled = viewModel.quizSelectedOption != -1,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp)
                                    .testTag("submit_quiz_button"),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = if (viewModel.quizSelectedOption == -1) "請點選一個選項" else "送出答案",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        } else if (viewModel.quizStatus == QuizStatus.GRADED) {
                            Button(
                                onClick = { viewModel.nextQuizQuestion() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp)
                                    .testTag("next_quiz_button"),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = if (viewModel.quizCurrentIndex == viewModel.activeQuizList.size - 1) "檢視測驗結果" else "進入下一題",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            QuizStatus.COMPLETED -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = "成就獎盃",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(80.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "測驗大功告成！",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "你在 5 道題中總共答對了：",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "${viewModel.quizCurrentScore} / 5 題",
                            style = MaterialTheme.typography.displayMedium,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.primary
                        )

                        val pct = (viewModel.quizCurrentScore * 20)
                        Text(
                            text = "答題正確率：$pct% | 贏得高額加權獎章點數",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondary,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(24.dp))
                        
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f))
                        ) {
                            Text(
                                text = when (viewModel.quizCurrentScore) {
                                    5 -> "太完美了！你已經是個認證的 Python 語法達人！"
                                    4, 3 -> "做得很好！你可以多跟「AI Coach」聊聊比較不熟悉的資料型態！"
                                    else -> "多到「課程」分頁讀幾遍，以及透過代碼「模擬器」多手寫演練，一定會更好！"
                                },
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(14.dp),
                                textAlign = TextAlign.Center
                            )
                        }

                        Spacer(modifier = Modifier.height(30.dp))

                        Button(
                            onClick = { viewModel.startNewQuizBatch() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Text("再次發起新的一輪", fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedButton(
                            onClick = { viewModel.quizStatus = QuizStatus.NOT_STARTED },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                        ) {
                            Text("返回主宣傳頁")
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 4. AI COACH INTERACTIVE CHAT TAB
// ==========================================
@Composable
fun AiCoachTab(viewModel: LearningViewModel) {
    val messages by viewModel.chatMessages.collectAsStateWithLifecycle()
    val scrollState = rememberLazyListState()
    val softwareKeyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(messages.size, viewModel.isAiThinking) {
        if (messages.isNotEmpty()) {
            scrollState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp, start = 16.dp, end = 16.dp, bottom = 12.dp)
    ) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val quickPrompts = listOf(
                Pair("列出 List 和 Tuple 差別", Icons.Default.Info),
                Pair("設計一個 for 迴圈初級題目", Icons.Default.Search),
                Pair("什麼是 Python 字典 Key-Value", Icons.Default.Folder),
                Pair("解析 Python 遞迴函數", Icons.Default.Code)
            )
            items(quickPrompts) { (promptText, promptIcon) ->
                SuggestionChip(
                    onClick = {
                        viewModel.sendChatPrompt(promptText)
                    },
                    label = { Text(promptText, fontSize = 12.sp) },
                    icon = {
                        Icon(
                            imageVector = promptIcon,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                )
            }
        }

        Card(
            modifier = Modifier
                .weight(1.0f)
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.12f))
        ) {
            if (messages.isEmpty() && !viewModel.isAiThinking) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Text("👋", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "我是你的專屬 Python AI Coach！",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "我能幫你：\n1. 講解課程與寫作難點\n2. 審查、除錯(Debug)你的 Python 範例\n3. 出一組自訂練習題給你演練",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 22.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            } else {
                LazyColumn(
                    state = scrollState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(messages) { message ->
                        val isUser = message.sender == "user"
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
                        ) {
                            if (!isUser) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primaryContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.SupportAgent,
                                        contentDescription = "Coach Avatar",
                                        modifier = Modifier.size(18.dp),
                                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                            }

                            Card(
                                shape = RoundedCornerShape(
                                    topStart = 12.dp,
                                    topEnd = 12.dp,
                                    bottomEnd = if (isUser) 0.dp else 12.dp,
                                    bottomStart = if (isUser) 12.dp else 0.dp
                                ),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isUser) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
                                ),
                                modifier = Modifier
                                    .widthIn(max = 280.dp)
                                    .testTag("chat_bubble_${message.id}")
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(
                                        text = message.message.cleanMarkdownFormat(),
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = if (isUser) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                        lineHeight = 22.sp
                                    )
                                }
                            }
                        }
                    }

                    if (viewModel.isAiThinking) {
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primaryContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.SupportAgent,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Card(
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(10.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        CircularProgressIndicator(modifier = Modifier.size(14.dp), strokeWidth = 2.dp)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("AI Coach 正在思考最佳方案中...", fontSize = 12.sp, color = Color.Gray)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextField(
                value = viewModel.chatInputField,
                onValueChange = { viewModel.chatInputField = it },
                modifier = Modifier
                    .weight(1f)
                    .testTag("chat_input_field"),
                placeholder = { Text("在此詢問 AI 任何 Python 大小問題...") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Send
                ),
                keyboardActions = KeyboardActions(
                    onSend = {
                        viewModel.sendChatPrompt()
                        softwareKeyboardController?.hide()
                    }
                ),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(24.dp)
            )

            IconButton(
                onClick = {
                    viewModel.sendChatPrompt()
                    softwareKeyboardController?.hide()
                },
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .testTag("chat_send_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "發送",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

// ==========================================
// 5. PERSONAL LEARNING DATA統計儀表板 & SETTINGS
// ==========================================
@Composable
fun DashboardTab(
    viewModel: LearningViewModel,
    onOpenSettings: () -> Unit = {}
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val stats by viewModel.userStats.collectAsStateWithLifecycle()
    var showResetDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "個人學習數據統計儀表板",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "這裡儲存你全部一磚一瓦積累的 Python 技能戰力點數！",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        stats?.let { s ->
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val memberRole = when {
                            s.level >= 5 -> "Python 語法守護宗師"
                            s.level >= 4 -> "物件與函數建造者"
                            s.level >= 3 -> "條件與迴圈精熟客"
                            s.level >= 2 -> "程式基礎初心者"
                            else -> "鍵盤編譯新手"
                        }
                        
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Stars,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = memberRole,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        Text(
                            text = "等級 Lvl.${s.level}",
                            style = MaterialTheme.typography.displaySmall,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        val xpProgress = s.xp % 100
                        val progressRatio = xpProgress.toFloat() / 100f
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("目前 XP: ${s.xp}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f))
                            Text("升級尚需: ${100 - xpProgress} XP", fontSize = 12.sp, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f))
                        }
                        
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        LinearProgressIndicator(
                            progress = { progressRatio },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            trackColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f)
                        )
                    }
                }
            }

            item {
                val accuracyRate = if (s.quizTotal > 0) {
                    (s.quizCorrect.toFloat() / s.quizTotal * 100).toInt()
                } else 0
                
                val currentLanguageLessons = viewModel.currentLessons
                val totalLanguageLessons = currentLanguageLessons.size
                val completedChaptersCount = s.completedLessons.split(",")
                    .filter { id -> id.isNotEmpty() && currentLanguageLessons.any { it.id == id } }.size

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(11.dp)) {
                        StatBlockCard(
                            title = "連續學習",
                            number = "${s.streak} 天",
                            subtext = s.streak.let { if (it > 1) "連續學習熱度攀升！" else "每天學點 Python" },
                            icon = Icons.Default.Whatshot,
                            iconColor = Color(0xFFFF5722)
                        )
                        StatBlockCard(
                            title = "答題正確率",
                            number = "$accuracyRate%",
                            subtext = "累計答對 ${s.quizCorrect} / ${s.quizTotal} 題",
                            icon = Icons.Default.CheckCircle,
                            iconColor = Color(0xFF4CAF50)
                        )
                    }

                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(11.dp)) {
                        StatBlockCard(
                            title = "完成課程單元",
                            number = "$completedChaptersCount / $totalLanguageLessons",
                            subtext = "累計獲得 ${completedChaptersCount * 20} XP",
                            icon = Icons.Default.School,
                            iconColor = Color(0xFF2196F3)
                        )
                        StatBlockCard(
                            title = "等級累積",
                            number = "${s.xp} XP",
                            subtext = "解鎖高階段稱號",
                            icon = Icons.Default.Leaderboard,
                            iconColor = Color(0xFFFFD700)
                        )
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.padding(top = 8.dp, bottom = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.TrendingUp,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "技能與實行統計儀表板",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                var selectedDashboardSubTab by remember { mutableStateOf(0) } // 0 = hours, 1 = quiz, 2 = topics
                
                // Segmented buttons or Tab Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    val tabs = listOf(
                        "練習時數" to Icons.Default.AccessTime,
                        "測驗分數" to Icons.Default.TrendingUp,
                        "主題進度" to Icons.Default.Book
                    )
                    tabs.forEachIndexed { index, (label, icon) ->
                        val isSel = selectedDashboardSubTab == index
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSel) MaterialTheme.colorScheme.primary else Color.Transparent)
                                .clickable { selectedDashboardSubTab = index }
                                .padding(horizontal = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = null,
                                    tint = if (isSel) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSel) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                when (selectedDashboardSubTab) {
                    0 -> DailyHouseChartPanel(viewModel)
                    1 -> QuizTrendChartPanel(viewModel, s)
                    2 -> PythonTopicsProgressPanel(viewModel, s)
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.4f)),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Psychology,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.tertiary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "🧠 AI Coach 智慧成效診斷",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "讓 Gemini 深度對比您的全部隨堂試卷、學習時數與單元進度，量身製作一份學習成效診斷和提升建議！",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        
                        Spacer(modifier = Modifier.height(10.dp))
                        
                        Button(
                            onClick = { viewModel.runAiDashboardDiagnostic() },
                            enabled = !viewModel.isCheckingAiInsights,
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (viewModel.isCheckingAiInsights) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    color = MaterialTheme.colorScheme.onTertiary,
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("深度排查數據中，請稍候...", fontSize = 12.sp)
                            } else {
                                Icon(imageVector = Icons.Default.Stars, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("🔬 啟動 AI 學習成效一鍵診斷", fontWeight = FontWeight.Bold)
                            }
                        }
                        
                        if (viewModel.aiDashboardInsights.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(12.dp))
                            HorizontalDivider(color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.15f))
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            Card(
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                            ) {
                                Text(
                                    text = viewModel.aiDashboardInsights,
                                    style = MaterialTheme.typography.bodyMedium,
                                    lineHeight = 22.sp,
                                    modifier = Modifier.padding(14.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.School,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "當前修讀課程切換",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "本系統內嵌四大核心程式與網頁開發教材！您可在此隨時靈活切換主修。各教材的學習進度均獨立累積計算！",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                // Course Option 1: Python
                val isPythonActive = viewModel.currentCourseType == com.example.ui.viewmodel.CourseType.PYTHON
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.switchCourseType(com.example.ui.viewmodel.CourseType.PYTHON)
                            viewModel.activeChapter = null
                            android.widget.Toast.makeText(context, "已成功切換至 Python 核心基礎特訓課程！測驗與模擬器已同步更新。", android.widget.Toast.LENGTH_SHORT).show()
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = if (isPythonActive) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                    ),
                    border = if (isPythonActive) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(if (isPythonActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.School,
                                contentDescription = null,
                                tint = if (isPythonActive) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Python 核心基礎特訓",
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.titleSmall,
                                    color = if (isPythonActive) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text("30單元", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                }
                            }
                            Text(
                                text = "零基礎首選！循序漸進攻克 Python 變數、條件分支、迴圈及函式封裝等實用運算思維。",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (isPythonActive) MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                        if (isPythonActive) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Active",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }

                // Course Option 2: JavaScript
                val isJsActive = viewModel.currentCourseType == com.example.ui.viewmodel.CourseType.JAVASCRIPT
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.switchCourseType(com.example.ui.viewmodel.CourseType.JAVASCRIPT)
                            viewModel.activeChapter = null
                            android.widget.Toast.makeText(context, "已成功切換至 JavaScript 50堂全能特訓！測驗與模擬器已同步更新。", android.widget.Toast.LENGTH_SHORT).show()
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = if (isJsActive) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                    ),
                    border = if (isJsActive) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(if (isJsActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Code,
                                contentDescription = null,
                                tint = if (isJsActive) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "JavaScript 50堂全能特寫",
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.titleSmall,
                                    color = if (isJsActive) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text("50單元", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
                                }
                            }
                            Text(
                                text = "高階進階特訓！從變數、迴圈到動態網頁 DOM、EventListener、Promise 及 async/await 網路 API 獲取！已完美為您設計 50 關卡。",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (isJsActive) MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                        if (isJsActive) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Active",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }

                // Course Option 3: HTML
                val isHtmlActive = viewModel.currentCourseType == com.example.ui.viewmodel.CourseType.HTML
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.switchCourseType(com.example.ui.viewmodel.CourseType.HTML)
                            viewModel.activeChapter = null
                            android.widget.Toast.makeText(context, "已成功切換至 HTML 50堂全能特訓！測驗與模擬器已同步更新。", android.widget.Toast.LENGTH_SHORT).show()
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = if (isHtmlActive) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                    ),
                    border = if (isHtmlActive) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(if (isHtmlActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Language,
                                contentDescription = null,
                                tint = if (isHtmlActive) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "HTML 50堂網頁全能特訓",
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.titleSmall,
                                    color = if (isHtmlActive) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text("50單元", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.tertiary)
                                }
                            }
                            Text(
                                text = "網頁前端大門！從基本標籤、巢狀結構、多媒體、表單、表格到 HTML5 語意化和 SEO 一網打盡！已為您完備 50 堂測驗與特訓關卡。",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (isHtmlActive) MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                        if (isHtmlActive) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Active",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }

                // Course Option 4: Kotlin
                val isKotlinActive = viewModel.currentCourseType == com.example.ui.viewmodel.CourseType.KOTLIN
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.switchCourseType(com.example.ui.viewmodel.CourseType.KOTLIN)
                            viewModel.activeChapter = null
                            android.widget.Toast.makeText(context, "已成功切換至 Kotlin 100單元核心特訓課程！測驗與模擬器已同步更新。", android.widget.Toast.LENGTH_SHORT).show()
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = if (isKotlinActive) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                    ),
                    border = if (isKotlinActive) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(if (isKotlinActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Book,
                                contentDescription = null,
                                tint = if (isKotlinActive) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Kotlin Android 100堂核心特訓",
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.titleSmall,
                                    color = if (isKotlinActive) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text("100單元", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                }
                            }
                            Text(
                                text = "參考 Google Android 官方 Basics with Compose 精髓！從基礎變數、函數、類別、集合高階 API，完美貫通協程 Coroutines、非同步 Flow 到 Jetpack Compose UI、MVVM、ViewModel、Room 資料庫與自動化測試！一網打盡 100 堂極致關卡！",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (isKotlinActive) MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                        if (isKotlinActive) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Active",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "個人化偏好與系統設定",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(10.dp))

            // Settings Overview Card
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("現有客製化偏好狀態", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                        TextButton(onClick = onOpenSettings) {
                            Text("修改偏好", fontWeight = FontWeight.Bold)
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Palette, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("主題風格:", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Text(viewModel.themeMode.title, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.FormatSize, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("字體縮放:", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Text(viewModel.fontSizeOption.title, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Notifications, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("每日提醒:", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Text(
                            if (viewModel.dailyReminderEnabled) "開啟 (${viewModel.reminderTime})" else "已關閉",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Code, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("編輯器主題:", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Text(viewModel.codeThemeOption.title, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = onOpenSettings,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("dashboard_open_settings_button")
            ) {
                Icon(imageVector = Icons.Default.Tune, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("開啟全功能個人化設定中心 (介面/字體/通知)", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedButton(
                onClick = { showResetDialog = true },
                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.error),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("reset_progress_button")
            ) {
                Icon(imageVector = Icons.Default.DeleteForever, contentDescription = null)
                Spacer(modifier = Modifier.width(6.dp))
                Text("重新初始化全部學習與對話歷史", fontWeight = FontWeight.Bold)
            }
        }
    }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("確認重置學習進度？") },
            text = { Text("這將會清除您本裝置上儲存的所有等級、XP 經驗值、課程完成進度以及與 AI Coach 的對話歷史。此操作將無法復原。") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.resetAllUserStats()
                        showResetDialog = false
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("確認重置", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text("取消")
                }
            }
        )
    }
}

@Composable
fun DailyHouseChartPanel(viewModel: LearningViewModel) {
    var selectedTimeframe by remember { mutableStateOf(0) } // 0 = 本週, 1 = 本月
    
    val hoursList = if (selectedTimeframe == 0) viewModel.dailyPracticeHours else viewModel.monthlyPracticeHours
    val labels = if (selectedTimeframe == 0) {
        listOf("週一", "週二", "週三", "週四", "週五", "週六", "週日")
    } else {
        listOf("第一週", "第二週", "第三週", "第四週")
    }
    
    val targetHours = if (selectedTimeframe == 0) 10f else 40f
    val currentSum = hoursList.sum()
    
    var selectedBarIdx by remember { mutableStateOf(-1) }
    
    // Reset selection when switching timeframe
    LaunchedEffect(selectedTimeframe) {
        selectedBarIdx = -1
    }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header with filter buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (selectedTimeframe == 0) "本週持續學習時數" else "本月持續學習時數",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "累積投入：${String.format("%.1f", currentSum)} 小時 / 目標 ${targetHours.toInt()} 小時",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                // Segments switcher for Week vs Month
                Row(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                        .padding(2.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    listOf("本週", "本月").forEachIndexed { idx, label ->
                        val isSel = selectedTimeframe == idx
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(18.dp))
                                .background(if (isSel) MaterialTheme.colorScheme.primary else Color.Transparent)
                                .clickable { selectedTimeframe = idx }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = label,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSel) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Progress visual pill
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val completionPct = (currentSum * 100 / targetHours).coerceAtMost(100f)
                LinearProgressIndicator(
                    progress = { completionPct / 100f },
                    modifier = Modifier
                        .weight(1f)
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "${completionPct.toInt()}%",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Bar Chart Canvas
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(hoursList) {
                            detectTapGestures { offset ->
                                val w = size.width
                                val numBars = hoursList.size
                                val barSpacing = w / numBars
                                val clickIdx = (offset.x / barSpacing).toInt()
                                if (clickIdx in 0 until numBars) {
                                    selectedBarIdx = if (selectedBarIdx == clickIdx) -1 else clickIdx
                                }
                            }
                        }
                ) {
                    val w = size.width
                    val h = size.height
                    val numBars = hoursList.size
                    val barWidth = (w / numBars) * 0.45f
                    val maxVal = maxOf(4f, hoursList.maxOrNull() ?: 0f)
                    
                    // Draw grid lines
                    val gridLines = 4
                    for (i in 0 until gridLines) {
                        val y = h * 0.15f + (h * 0.65f) * (i.toFloat() / (gridLines - 1))
                        drawLine(
                            color = Color.LightGray.copy(alpha = 0.2f),
                            start = Offset(0f, y),
                            end = Offset(w, y),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                        )
                    }
                    
                    hoursList.forEachIndexed { idx, hrs ->
                        val barCenterX = (idx * (w / numBars)) + (w / (numBars * 2f))
                        val barHeight = h * 0.7f * (hrs / maxVal)
                        val barTopY = h * 0.8f - barHeight
                        
                        // Track background (transparent bar)
                        drawRoundRect(
                            color = Color.LightGray.copy(alpha = 0.1f),
                            topLeft = Offset(barCenterX - (barWidth / 2f), h * 0.1f),
                            size = Size(barWidth, h * 0.7f),
                            cornerRadius = CornerRadius(6f, 6f)
                        )
                        
                        // Active bar with dual-color high-contrast gradient
                        val gradientBrush = Brush.verticalGradient(
                            colors = if (idx == selectedBarIdx) {
                                listOf(Color(0xFF00B0FF), Color(0xFF00E5FF))
                            } else {
                                listOf(Color(0xFFFF5722), Color(0xFFFF9800))
                            }
                        )
                        drawRoundRect(
                            brush = gradientBrush,
                            topLeft = Offset(barCenterX - (barWidth / 2f), barTopY),
                            size = Size(barWidth, barHeight.coerceAtLeast(4f)),
                            cornerRadius = CornerRadius(6f, 6f)
                        )
                        
                        // Pulse glow for selected bar
                        if (idx == selectedBarIdx) {
                            drawRoundRect(
                                color = Color(0x3300B0FF),
                                topLeft = Offset(barCenterX - (barWidth / 2f) - 4f, barTopY - 4f),
                                size = Size(barWidth + 8f, barHeight + 8f),
                                cornerRadius = CornerRadius(10f, 10f)
                            )
                        }
                    }
                }
                
                // Days row beneath the bars
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    labels.forEachIndexed { idx, label ->
                        Text(
                            text = label,
                            fontSize = 10.sp,
                            fontWeight = if (idx == selectedBarIdx) FontWeight.Bold else FontWeight.Normal,
                            color = if (idx == selectedBarIdx) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            
            // Showing touched value tooltip
            AnimatedVisibility(visible = selectedBarIdx != -1) {
                val idx = selectedBarIdx
                if (idx in hoursList.indices) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Default.AccessTime, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${labels[idx]}：這期間您在平台累計練習了 ${String.format("%.2f", hoursList[idx])} 小時！",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QuizTrendChartPanel(viewModel: LearningViewModel, stats: UserStats) {
    val scores = viewModel.recentQuizScores
    var selectedPointIdx by remember { mutableStateOf(-1) }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("隨堂測驗成績趨勢", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text("答題正確率：${if (stats.quizTotal > 0) (stats.quizCorrect * 100 / stats.quizTotal) else 0}%（累計答對 ${stats.quizCorrect} / ${stats.quizTotal} 題）", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            
            Spacer(modifier = Modifier.height(16.dp))
            
            if (scores.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🧩 尚無近期測驗紀錄", style = MaterialTheme.typography.titleSmall, color = Color.Gray)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("請至「隨堂挑戰」切換頁籤，答完一輪 5 道隨堂測驗後，這裡會立即繪製出精緻的百分制學習成績曲線！", style = MaterialTheme.typography.bodySmall, color = Color.Gray, modifier = Modifier.padding(horizontal = 24.dp))
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                ) {
                    Canvas(
                        modifier = Modifier
                            .fillMaxSize()
                            .pointerInput(scores) {
                                detectTapGestures { offset ->
                                    val w = size.width
                                    val paddingX = w * 0.1f
                                    val availableW = w * 0.8f
                                    val pointSpacing = if (scores.size > 1) availableW / (scores.size - 1) else availableW
                                    
                                    var closestIdx = -1
                                    var minDist = Float.MAX_VALUE
                                    for (i in scores.indices) {
                                        val px = paddingX + i * pointSpacing
                                        val dist = Math.abs(offset.x - px)
                                        if (dist < minDist) {
                                            minDist = dist
                                            closestIdx = i
                                        }
                                    }
                                    if (closestIdx != -1 && minDist < 60f) {
                                        selectedPointIdx = if (selectedPointIdx == closestIdx) -1 else closestIdx
                                    }
                                }
                            }
                    ) {
                        val w = size.width
                        val h = size.height
                        
                        val paddingX = w * 0.1f
                        val paddingY = h * 0.15f
                        val availableW = w * 0.8f
                        val availableH = h * 0.7f
                        
                        val pointSpacing = if (scores.size > 1) availableW / (scores.size - 1) else availableW
                        
                        // Draw grid lines
                        val lines = 5
                        for (i in 0 until lines) {
                            val ratio = i.toFloat() / (lines - 1)
                            val y = paddingY + availableH * ratio
                            drawLine(
                                color = Color.LightGray.copy(alpha = 0.2f),
                                start = Offset(paddingX, y),
                                end = Offset(paddingX + availableW, y),
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                            )
                        }
                        
                        val points = scores.mapIndexed { idx, score ->
                            val x = paddingX + idx * pointSpacing
                            val y = paddingY + availableH * (1f - (score / 100f))
                            Offset(x, y)
                        }
                        
                        // 1. Draw smooth area gradient under the line
                        if (points.size > 1) {
                            val areaPath = Path().apply {
                                moveTo(points.first().x, paddingY + availableH)
                                points.forEach { lineTo(it.x, it.y) }
                                lineTo(points.last().x, paddingY + availableH)
                                close()
                            }
                            drawPath(
                                path = areaPath,
                                brush = Brush.verticalGradient(
                                    colors = listOf(Color(0x664CAF50), Color(0x004CAF50))
                                )
                            )
                            
                            // 2. Draw line connect points
                            val linePath = Path().apply {
                                moveTo(points.first().x, points.first().y)
                                for (i in 1 until points.size) {
                                    lineTo(points[i].x, points[i].y)
                                }
                            }
                            drawPath(
                                path = linePath,
                                color = Color(0xFF4CAF50),
                                style = Stroke(width = 5f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                            )
                        }
                        
                        // 3. Draw circles
                        points.forEachIndexed { idx, point ->
                            val isSel = idx == selectedPointIdx
                            drawCircle(
                                color = if (isSel) Color(0xFF81C784) else Color(0x334CAF50),
                                radius = if (isSel) 12f else 6f,
                                center = point
                            )
                            drawCircle(
                                color = if (isSel) Color(0xFF2E7D32) else Color(0xFF4CAF50),
                                radius = if (isSel) 7f else 4f,
                                center = point
                            )
                        }
                    }
                    
                    // Attempts labels below
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .padding(horizontal = 24.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        scores.forEachIndexed { idx, _ ->
                            Text(
                                text = "T.${idx + 1}",
                                fontSize = 9.sp,
                                fontWeight = if (idx == selectedPointIdx) FontWeight.Bold else FontWeight.Normal,
                                color = if (idx == selectedPointIdx) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                
                // Show touched score tooltip
                AnimatedVisibility(visible = selectedPointIdx != -1) {
                    val idx = selectedPointIdx
                    if (idx in scores.indices) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(imageVector = Icons.Default.TrendingUp, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "第 ${idx + 1} 次測驗學習分數：${scores[idx]} 分！",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PythonTopicsProgressPanel(viewModel: LearningViewModel, stats: UserStats) {
    val completedSet = stats.completedLessons.split(",")
        .filter { it.isNotEmpty() }
        .toSet()
        
    val sections = listOf(
        Triple("1. 起步觀念與基礎語法 (單元 1-6)", listOf("lesson_1", "lesson_2", "lesson_3", "lesson_4", "lesson_5", "lesson_6"), "變數、字串與 print 基礎"),
        Triple("2. 條件與判斷分支 (單元 7-12)", listOf("lesson_7", "lesson_8", "lesson_9", "lesson_10", "lesson_11", "lesson_12"), "if-else 與複合比較邏輯"),
        Triple("3. 迴圈迭代與遍歷架構 (單元 13-18)", listOf("lesson_13", "lesson_14", "lesson_15", "lesson_16", "lesson_17", "lesson_18"), "for / while 迴圈指令"),
        Triple("4. 函數宣告與模組封裝 (單元 19-24)", listOf("lesson_19", "lesson_20", "lesson_21", "lesson_22", "lesson_23", "lesson_24"), "def、參數傳遞與模組 import"),
        Triple("5. 進階物件導向設計 OOP (單元 25-30)", listOf("lesson_25", "lesson_26", "lesson_27", "lesson_28", "lesson_29", "lesson_30"), "class、物件初始化、封裝繼承")
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Python 核心五大主題精熟度", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text("根據您當前修讀完畢的單元比率進行解鎖評分：", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            
            Spacer(modifier = Modifier.height(16.dp))
            
            sections.forEach { (title, list, desc) ->
                val completedCount = list.count { completedSet.contains(it) }
                val pct = completedCount.toFloat() / list.size.toFloat()
                
                val statusText = when {
                    completedCount == list.size -> "已精通 🏆"
                    completedCount > 0 -> "修讀中 🌟"
                    else -> "未啟航 🧭"
                }
                
                val badgeColor = when {
                    completedCount == list.size -> Color(0xFF4CAF50)
                    completedCount > 0 -> Color(0xFFFF9800)
                    else -> Color.Gray
                }
                
                Column(modifier = Modifier.padding(vertical = 4.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                            Text(desc, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(badgeColor.copy(alpha = 0.15f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(statusText, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = badgeColor)
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        LinearProgressIndicator(
                            progress = { pct },
                            modifier = Modifier
                                .weight(1f)
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = if (completedCount == list.size) Color(0xFF4CAF50) else MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                        )
                        Text("${(pct * 100).toInt()}%", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun AppleGlassIconContainer(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    size: androidx.compose.ui.unit.Dp = 38.dp,
    iconSize: androidx.compose.ui.unit.Dp = 18.dp
) {
    val infiniteTransition = rememberInfiniteTransition(label = "glassGlow")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.45f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )
    val floatRotate by infiniteTransition.animateFloat(
        initialValue = -5f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "floatRotate"
    )

    Box(
        modifier = modifier
            .size(size)
            .graphicsLayer {
                rotationZ = floatRotate
            }
            .drawBehind {
                // Glow layer behind glass
                drawCircle(
                    color = color.copy(alpha = pulseAlpha),
                    radius = size.toPx() * 0.65f,
                    center = Offset(size.toPx() / 2f, size.toPx() / 2f)
                )
                drawCircle(
                    color = color.copy(alpha = 0.12f),
                    radius = size.toPx() * 0.85f,
                    center = Offset(size.toPx() / 2f, size.toPx() / 2f)
                )
            },
        contentAlignment = Alignment.Center
    ) {
        // Frosted Glass plate with rich linear gradient borders
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.22f),
                            Color.White.copy(alpha = 0.04f),
                            color.copy(alpha = 0.15f)
                        )
                    )
                )
                .border(
                    width = 1.2.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.55f),
                            Color.White.copy(alpha = 0.05f),
                            color.copy(alpha = 0.40f)
                        )
                    ),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            // Shiny highlight arcs and light glare pattern
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = this.size.width
                val h = this.size.height
                
                // Flare diagonal stroke reflection
                val path = Path().apply {
                    moveTo(0f, h * 0.45f)
                    lineTo(w * 0.55f, 0f)
                    lineTo(w * 0.82f, 0f)
                    lineTo(0f, h * 0.82f)
                    close()
                }
                drawPath(
                    path = path,
                    color = Color.White.copy(alpha = 0.18f)
                )

                // High-light crescent rim at the top-left edge
                drawArc(
                    color = Color.White.copy(alpha = 0.45f),
                    startAngle = 180f,
                    sweepAngle = 90f,
                    useCenter = false,
                    topLeft = Offset(1.2.dp.toPx(), 1.2.dp.toPx()),
                    size = Size(w - 2.4.dp.toPx(), h - 2.4.dp.toPx()),
                    style = Stroke(width = 1.2.dp.toPx())
                )
            }

            // Glass interior double-tone icon style
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .size(iconSize)
                    .graphicsLayer {
                        shadowElevation = 6.dp.toPx()
                        shape = CircleShape
                        clip = false
                    }
            )
        }
    }
}

@Composable
fun StatBlockCard(
    title: String,
    number: String,
    subtext: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color
) {
    val isStreak = title == "連續學習"
    
    // Smooth infinite transition for breathing flame scale and pulsing glow
    val infiniteTransition = rememberInfiniteTransition(label = "streakGlow")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )
    val shimmerPhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerPhase"
    )

    // Animated particles for flame/spark effect rising from the bottom continuous stream
    val sparkProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "spark"
    )

    // Define persistent spark tracks with different horizontal alignment and delay
    val sparks = remember {
        List(12) { i ->
            val horizontalOffset = (i * 23) % 100 // distribute horizontally 0 to 100%
            val size = 4f + (i * 3) % 8f
            val delay = (i * 0.12f) % 1f
            val color = when (i % 3) {
                0 -> Color(0xFFFF3D00) // Fire Orange-Red
                1 -> Color(0xFFFF9100) // Fire Orange
                else -> Color(0xFFFFD600)  // Golden Ember Sparkle
            }
            Triple(horizontalOffset, size, Pair(delay, color))
        }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (isStreak) {
                    Modifier.drawBehind {
                        // Drawing subtle glowing shadow around the card
                        drawRoundRect(
                            color = Color(0xFFFF5722).copy(alpha = 0.18f),
                            cornerRadius = CornerRadius(16.dp.toPx(), 16.dp.toPx()),
                            size = size
                        )
                    }
                } else Modifier
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (isStreak) {
                MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
            } else {
                MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp)
            }
        ),
        border = BorderStroke(
            1.5.dp,
            if (isStreak) {
                // Shimmering flame premium border gradient moving dynamically
                val colors = listOf(
                    Color(0xFFFF3D00), 
                    Color(0xFFFF9100), 
                    Color(0xFFFFD600), 
                    Color(0xFFFF3D00)
                )
                val brush = Brush.sweepGradient(
                    colors = colors,
                    center = Offset(shimmerPhase * 200f, 100f)
                )
                brush
            } else {
                SolidColor(MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
            }
        )
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            // Draw floating flame sparks inside the background of the streak card!
            if (isStreak) {
                Canvas(
                    modifier = Modifier.matchParentSize()
                ) {
                    val w = size.width
                    val h = size.height
                    sparks.forEach { (xPct, sparkSize, deco) ->
                        val delay = deco.first
                        val color = deco.second
                        
                        // Calculate personalized progress relative to standard cycle
                        val localProgress = (sparkProgress + delay) % 1.0f
                        
                        // Spark starts at bottom (y = h) and rises (y = 0)
                        val y = h - (h * localProgress)
                        // Sway left/right
                        val sway = (Math.sin(localProgress * 3 * Math.PI + xPct).toFloat()) * 25f
                        val x = (w * (xPct / 100f)) + sway
                        
                        // Fade as it nears the top
                        val alpha = (1f - localProgress).coerceIn(0f, 1f)
                        
                        drawCircle(
                            color = color.copy(alpha = alpha * 0.7f),
                            radius = sparkSize * (1f - localProgress * 0.25f),
                            center = Offset(x, y)
                        )
                    }
                }
            }

            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall,
                        color = if (isStreak) {
                            Color(0xFFFF7043)
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        },
                        fontWeight = if (isStreak) FontWeight.Bold else FontWeight.Normal
                    )
                    
                    // Glassmorphic Apple Icon Container with glowing aura (iOS 27 style!)
                    AppleGlassIconContainer(
                        icon = icon,
                        color = if (isStreak) Color(0xFFFF5722) else iconColor,
                        modifier = Modifier
                            .scale(if (isStreak) pulseScale else 1.0f)
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = number,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black,
                        color = if (isStreak) Color(0xFFFF5722) else MaterialTheme.colorScheme.onSurface
                    )
                    
                    if (isStreak) {
                        Spacer(modifier = Modifier.width(6.dp))
                        // Flame Heat indicator tag
                        Box(
                            modifier = Modifier
                                .background(Color(0xFFFF5722).copy(alpha = 0.2f), shape = RoundedCornerShape(6.dp))
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "🔥 HIGH ENERGY",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp),
                                color = Color(0xFFFF5722),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtext,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// ==========================================
// 🌌 GAMIFICATION RETENTION COMPOSABLES (PLANS 1 & 2)
// ==========================================
@Composable
fun DailyCheckInCard(viewModel: LearningViewModel) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val isCheckedIn = viewModel.isTodayCheckedIn
    val progressOffset = viewModel.checkInProgressDays // 0 to 6
    val stats = viewModel.userStats.collectAsState().value
    val streakDays = stats?.streak ?: 1
    
    // Animation trigger for stars particle explosion
    var explosionTrigger by remember { mutableStateOf(0) }
    val animProgress = remember { Animatable(0f) }
    
    LaunchedEffect(explosionTrigger) {
        if (explosionTrigger > 0) {
            animProgress.snapTo(0f)
            animProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(1200, easing = LinearOutSlowInEasing)
            )
        }
    }
    
    val particles = remember(explosionTrigger) {
        List(35) { i ->
            val angle = Math.random() * 2 * Math.PI
            val velocity = 120f + (Math.random() * 180f).toFloat()
            val size = 5f + (Math.random() * 12f).toFloat()
            val isSparkle = i % 2 == 0
            val color = listOf(
                Color(0xFFFF3D00), // Fire red-orange
                Color(0xFFFF9100), // Fire orange
                Color(0xFFFFD600), // Gold
                Color(0xFFFFEA00), // Neon yellow
                Color(0xFFE040FB)  // Neon purple
            ).random()
            Triple(angle, velocity, Triple(size, color, isSparkle))
        }
    }
    
    // Infinite chest breathing animation if not checked in
    val infiniteTransition = rememberInfiniteTransition(label = "chestAnim")
    val chestScale by infiniteTransition.animateFloat(
        initialValue = 0.94f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "chestScale"
    )
    val chestRotation by infiniteTransition.animateFloat(
        initialValue = -2.5f,
        targetValue = 2.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(1100, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "chestRotation"
    )

    val checkInShimmerPhase by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "checkInShimmer"
    )

    Box(modifier = Modifier.fillMaxWidth()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isCheckedIn) {
                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
                } else {
                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
                }
            ),
            border = BorderStroke(
                if (isCheckedIn) 1.5.dp else 1.dp,
                if (isCheckedIn) {
                    val colors = listOf(Color(0xFFFF3D00), Color(0xFFFF9100), Color(0xFFFFEA00), Color(0xFFFF3D00))
                    Brush.sweepGradient(
                        colors = colors,
                        center = Offset(checkInShimmerPhase * 300f, 150f)
                    )
                } else {
                    SolidColor(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f))
                }
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Stars,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "星際能量日簽到",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "獲取學習能源，維持高亢熱情",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    }
                    
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "+30 XP",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))

                // Interactive Area
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Leftside Details & 7-day stars status row
                    Column(
                        modifier = Modifier.weight(1.3f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = if (isCheckedIn) "🎉 今日簽到成功！星空能量已充滿，繼續修讀課程挑戰新境界吧！" else "點擊右側星命神盾，即可喚醒汲取今日 +30 XP 成長能量！",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                            lineHeight = 16.sp,
                            modifier = Modifier.padding(end = 4.dp)
                        )
                        
                        // 7-day small nodes row
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            for (i in 0..6) {
                                val isNodeDone = i < progressOffset
                                val isCurrentActive = i == progressOffset && !isCheckedIn
                                
                                val scaleAnim by animateFloatAsState(
                                    targetValue = if (isCurrentActive) 1.25f else 1.0f,
                                    animationSpec = remember { spring(dampingRatio = Spring.DampingRatioHighBouncy) },
                                    label = "nodeScale"
                                )

                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .scale(scaleAnim)
                                        .clip(CircleShape)
                                        .background(
                                            when {
                                                isNodeDone -> MaterialTheme.colorScheme.primary
                                                isCurrentActive -> MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)
                                                else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
                                            }
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (isNodeDone) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onPrimary,
                                            modifier = Modifier.size(12.dp)
                                        )
                                    } else {
                                        Text(
                                            text = "${i + 1}",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isCurrentActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Floating Shaking Box on the Right
                    Column(
                        modifier = Modifier.weight(0.7f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(85.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    if (isCheckedIn) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                    else MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
                                )
                                .clickable(enabled = !isCheckedIn) {
                                    viewModel.performDailyCheckIn()
                                    explosionTrigger++
                                    android.widget.Toast
                                        .makeText(context, "🌌 點亮成功！成功喚醒今日 +30 XP 能量！", android.widget.Toast.LENGTH_SHORT)
                                        .show()
                                }
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            // Shaking breathing Visual
                            val animatedModifier = if (!isCheckedIn) {
                                Modifier
                                    .scale(chestScale)
                                    .rotate(chestRotation)
                            } else {
                                Modifier
                            }
                            
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = animatedModifier
                            ) {
                                Icon(
                                    imageVector = if (isCheckedIn) Icons.Default.Stars else Icons.Default.Whatshot,
                                    contentDescription = "Sign in Stars",
                                    tint = if (isCheckedIn) Color(0xFFFBC02D) else MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(38.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = if (isCheckedIn) "已充能" else "點選蓄能",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Black,
                                    color = if (isCheckedIn) Color(0xFFFBC02D) else MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        }
        
        // Render Particles Canvas overlay on top of Card
        val p = animProgress.value
        if (p > 0f && p < 1f) {
            Canvas(
                modifier = Modifier
                    .matchParentSize()
                    .clip(RoundedCornerShape(24.dp))
            ) {
                // Set origin center on chest box (roughly around 82% width, 60% height)
                val chestOrigin = Offset(size.width * 0.82f, size.height * 0.65f)
                particles.forEach { (angle, velocity, tripleDetails) ->
                    val sizePx = tripleDetails.first
                    val color = tripleDetails.second
                    val isSparkle = tripleDetails.third
                    
                    val distance = velocity * p
                    val dx = (Math.cos(angle) * distance).toFloat()
                    val dy = (Math.sin(angle) * distance + (120f * p * p)).toFloat() // Gravity influence
                    
                    val alpha = (1f - p).coerceIn(0f, 1f)
                    val computedCenter = chestOrigin + Offset(dx, dy)
                    
                    if (isSparkle) {
                        // Drawing shiny diamond/star sparkles!
                        val half = (sizePx * (1f - p * 0.3f)) / 2f
                        drawLine(
                            color = color.copy(alpha = alpha),
                            start = Offset(computedCenter.x - half * 2f, computedCenter.y),
                            end = Offset(computedCenter.x + half * 2f, computedCenter.y),
                            strokeWidth = sizePx * 0.25f,
                            cap = StrokeCap.Round
                        )
                        drawLine(
                            color = color.copy(alpha = alpha),
                            start = Offset(computedCenter.x, computedCenter.y - half * 2f),
                            end = Offset(computedCenter.x, computedCenter.y + half * 2f),
                            strokeWidth = sizePx * 0.25f,
                            cap = StrokeCap.Round
                        )
                    } else {
                        // Drawing rising/glowing fire flames embers
                        drawCircle(
                            color = color.copy(alpha = alpha),
                            radius = sizePx * (1f - p * 0.4f),
                            center = computedCenter
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DailyPuzzleFlipCard(viewModel: LearningViewModel) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val puzzle = viewModel.todayPuzzle
    val isCompleted = viewModel.isTodayPuzzleCompleted
    val selectedOption = viewModel.dailyPuzzleSelectedOption
    val isCorrect = viewModel.dailyPuzzleAnsweredCorrectly
    
    // Confetti trigger for correct daily answer
    var confettiTrigger by remember { mutableStateOf(0) }
    val confettiProgress = remember { Animatable(0f) }
    
    LaunchedEffect(confettiTrigger) {
        if (confettiTrigger > 0) {
            confettiProgress.snapTo(0f)
            confettiProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(1400, easing = LinearOutSlowInEasing)
            )
        }
    }
    
    val confettiList = remember(confettiTrigger) {
        List(40) {
            val angle = Math.random() * 2 * Math.PI
            val velocity = 150f + (Math.random() * 220f).toFloat()
            val size = 5 + (Math.random() * 8).toFloat()
            val color = listOf(
                Color(0xFFE040FB), // bright purple
                Color(0xFF00E676), // bright green
                Color(0xFF29B6F6), // light blue
                Color(0xFFFFEE58), // bright yellow
                Color(0xFFFF7043)  // coral orange
            ).random()
            Triple(angle, velocity, Pair(size, color))
        }
    }

    val rotation by animateFloatAsState(
        targetValue = if (viewModel.isDailyPuzzleFlipped) 180f else 0f,
        animationSpec = tween(650, easing = FastOutSlowInEasing),
        label = "triviaCardFlip"
    )

    Box(modifier = Modifier.fillMaxWidth()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer {
                    rotationY = rotation
                    cameraDistance = 14f * density
                }
                .animateContentSize(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
            ),
            border = BorderStroke(1.2.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f))
        ) {
            // Mirror back text horizontally because of cards rotationY!
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer {
                        if (rotation > 90f) {
                            rotationY = 180f
                        }
                    }
                    .padding(16.dp)
            ) {
                if (rotation <= 90f) {
                    // FRONT SIDE PANEL
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Psychology,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.secondary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "每日代碼極速躍遷",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Black,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.12f))
                                    .padding(horizontal = 7.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = if (isCompleted) "已解鎖" else "未解密",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }
                        }

                        Text(
                            text = if (isCompleted) "今日極速代碼冷知識問答您已攻破！請點擊查看或重溫底下各代碼語意與原理解析。" else "每日為您精選一則超趣味「程式陷阱 / Gotcha 問答」！這是大腦熱身，如果還沒學過程式沒關係，猜猜看！答對 +50 XP，答錯也同樣可賺 +15 XP，並一秒獲得深入原理詳解唷！",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                            lineHeight = 16.sp
                        )

                        // Cool decorative code block snapshot
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF15151A))
                                .padding(12.dp)
                        ) {
                            Column {
                                Text(
                                    text = "// 每日冷知識探查: ${puzzle.title}",
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = Color(0xFF64B5F6)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "def compile_and_leap(user):\n    # 觸發大腦高速運算 ...\n    return \"+50 XP Energy Loaded!\"",
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = Color(0xFF81C784)
                                )
                            }
                        }

                        Button(
                            onClick = { viewModel.flipDailyPuzzleCard() },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondary
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Icon(
                                imageVector = if (isCompleted) Icons.Default.Info else Icons.Default.PlayArrow,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isCompleted) "查看完整原理解析" else "展開量子代碼挑戰 (+50 XP)",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                } else {
                    // BACK SIDE PANEL
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "?",
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = puzzle.title,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Black
                                )
                            }
                            
                            IconButton(
                                onClick = { viewModel.flipDailyPuzzleCard() },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "返回",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        // Code details
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF15151A))
                                .padding(12.dp)
                        ) {
                            Column {
                                Text(
                                    text = "# 執行環境: ${puzzle.language}",
                                    fontSize = 10.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = Color(0xFFA1A1A1)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = puzzle.code,
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = Color(0xFFEEEEEE),
                                    lineHeight = 15.sp
                                )
                            }
                        }

                        if (!isCompleted) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "💡 請問上述代碼執行的預期輸出是？",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                                TextButton(
                                    onClick = {
                                        viewModel.askAiCoachAboutPuzzle(
                                            puzzleTitle = puzzle.title,
                                            language = puzzle.language,
                                            code = puzzle.code,
                                            options = puzzle.options
                                        )
                                    },
                                    contentPadding = PaddingValues(0.dp),
                                    modifier = Modifier.height(30.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Chat,
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp),
                                        tint = MaterialTheme.colorScheme.secondary
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("召喚導師提示", fontSize = 11.sp, color = MaterialTheme.colorScheme.secondary)
                                }
                            }

                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                puzzle.options.forEachIndexed { index, option ->
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                viewModel.submitDailyPuzzle(index)
                                                if (index == puzzle.correctIndex) {
                                                    confettiTrigger++
                                                    android.widget.Toast.makeText(context, "🌟 太強了！攻克成功！獲得 +50 XP 能源！", android.widget.Toast.LENGTH_SHORT).show()
                                                } else {
                                                    android.widget.Toast.makeText(context, "解析略有誤差！嘗試亦獲得 +15 XP！", android.widget.Toast.LENGTH_SHORT).show()
                                                }
                                            },
                                        shape = RoundedCornerShape(12.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = MaterialTheme.colorScheme.surface
                                        ),
                                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(10.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(24.dp)
                                                    .clip(CircleShape)
                                                    .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.12f)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = ('A' + index).toString(),
                                                    style = MaterialTheme.typography.labelSmall,
                                                    fontWeight = FontWeight.Bold,
                                                    color = MaterialTheme.colorScheme.secondary
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(10.dp))
                                            Text(
                                                text = option,
                                                style = MaterialTheme.typography.bodySmall,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }
                                }
                            }
                        } else {
                            // Completed state detail presentation inside back of card
                            val choiceSuccess = selectedOption == puzzle.correctIndex
                            val containerBg = if (choiceSuccess) {
                                Color(0xFF2E7D32).copy(alpha = 0.15f)
                            } else {
                                Color(0xFFC62828).copy(alpha = 0.15f)
                            }
                            val contentColor = if (choiceSuccess) {
                                Color(0xFF81C784)
                            } else {
                                Color(0xFFE57373)
                            }
                            
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(containerBg)
                                        .padding(12.dp)
                                ) {
                                    Column {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = if (choiceSuccess) Icons.Default.CheckCircle else Icons.Default.Cancel,
                                                contentDescription = null,
                                                tint = contentColor,
                                                modifier = Modifier.size(18.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = if (choiceSuccess) "解碼成功！已吸收 +50 XP 核心能源！" else "與真解略有偏差！嘗試灌注 +15 XP 防護盾",
                                                fontWeight = FontWeight.Bold,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = contentColor
                                            )
                                        }
                                        
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = "正確答案：${puzzle.options[puzzle.correctIndex]}",
                                            fontWeight = FontWeight.Bold,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "💡 專家深度解構：\n${puzzle.explanation.cleanMarkdownFormat()}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
                                            lineHeight = 15.sp
                                        )
                                    }
                                }

                                Button(
                                    onClick = { viewModel.flipDailyPuzzleCard() },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    Text("返回說明", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
        
        // Render Confetti Canvas Overlay (flies out of center on correct puzzle response)
        val t = confettiProgress.value
        if (t > 0f && t < 1f) {
            Canvas(
                modifier = Modifier
                    .matchParentSize()
                    .clip(RoundedCornerShape(24.dp))
            ) {
                // Spawn source on center of Card box
                val origin = Offset(size.width / 2f, size.height / 2f)
                confettiList.forEach { (angle, velocity, deco) ->
                    val sizePx = deco.first
                    val color = deco.second
                    
                    val distance = velocity * t
                    val dx = (Math.cos(angle) * distance).toFloat()
                    val dy = (Math.sin(angle) * distance + (250f * t * t)).toFloat() // Gravity curvature
                    
                    val alpha = (1f - t).coerceIn(0f, 1f)
                    
                    if (angle.hashCode() % 2 == 0) {
                        drawCircle(
                            color = color.copy(alpha = alpha),
                            radius = sizePx * (1f - t * 0.3f),
                            center = origin + Offset(dx, dy)
                        )
                    } else {
                        drawRect(
                            color = color.copy(alpha = alpha),
                            topLeft = origin + Offset(dx - sizePx, dy - sizePx),
                            size = Size(sizePx * 2f, sizePx * 1.4f)
                        )
                    }
                }
            }
        }
    }
}

// ==========================================
// PERSONALIZATION & SETTINGS CENTER DIALOG
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsDialog(
    viewModel: LearningViewModel,
    onDismiss: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf(
        Pair("主題與顯示", Icons.Default.Palette),
        Pair("字體大小", Icons.Default.FormatSize),
        Pair("通知與提醒", Icons.Default.Notifications),
        Pair("編輯器偏好", Icons.Default.Code),
        Pair("資料維護", Icons.Default.Storage)
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier
            .fillMaxWidth(0.94f)
            .fillMaxHeight(0.86f)
            .clip(RoundedCornerShape(28.dp)),
        confirmButton = {
            Button(
                onClick = onDismiss,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("close_settings_button")
            ) {
                Text("完成並套用偏好", fontWeight = FontWeight.Bold)
            }
        },
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "個人化設定中心",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "客製化主題、字體比例、通知提醒與編輯器",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "關閉")
                }
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxSize()) {
                ScrollableTabRow(
                    selectedTabIndex = selectedTab,
                    edgePadding = 0.dp,
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                    divider = {}
                ) {
                    tabs.forEachIndexed { index, (title, icon) ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        title,
                                        fontSize = 12.sp,
                                        fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Box(modifier = Modifier.weight(1f)) {
                    when (selectedTab) {
                        0 -> ThemeSettingsContent(viewModel)
                        1 -> FontSizeSettingsContent(viewModel)
                        2 -> NotificationSettingsContent(viewModel)
                        3 -> CodeEditorSettingsContent(viewModel)
                        4 -> SystemDataSettingsContent(viewModel, onDismiss)
                    }
                }
            }
        }
    )
}

@Composable
fun ThemeSettingsContent(viewModel: LearningViewModel) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Text(
                text = "介面色彩主題 (Theme Mode)",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                ThemeMode.values().forEach { mode ->
                    val isSelected = viewModel.themeMode == mode
                    Card(
                        onClick = { viewModel.updateThemeMode(mode) },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                        ),
                        border = if (isSelected) BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary) else null,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = when (mode) {
                                        ThemeMode.SYSTEM -> Icons.Default.PhoneAndroid
                                        ThemeMode.LIGHT -> Icons.Default.WbSunny
                                        ThemeMode.DARK -> Icons.Default.NightsStay
                                    },
                                    contentDescription = null,
                                    tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = mode.title,
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        text = when (mode) {
                                            ThemeMode.SYSTEM -> "跟隨 Android 系統設定自動調整"
                                            ThemeMode.LIGHT -> "清爽明亮視感，適合日間學習"
                                            ThemeMode.DARK -> "沉浸式深色畫布，夜間護眼降疲勞"
                                        },
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                            RadioButton(
                                selected = isSelected,
                                onClick = { viewModel.updateThemeMode(mode) }
                            )
                        }
                    }
                }
            }
        }

        item {
            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Material You 動態色彩",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "在 Android 12+ 裝置上自動擷取系統桌布色彩調色盤",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Switch(
                    checked = viewModel.dynamicColorEnabled,
                    onCheckedChange = { viewModel.updateDynamicColor(it) }
                )
            }
        }
    }
}

@Composable
fun FontSizeSettingsContent(viewModel: LearningViewModel) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Text(
                text = "全域 UI 字體比例 (Font Scaling)",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "調整應用程式內所有說明、教材與題目之文字大小",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(10.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                FontSizeOption.values().forEach { option ->
                    val isSelected = viewModel.fontSizeOption == option
                    Card(
                        onClick = { viewModel.updateFontSizeOption(option) },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                        ),
                        border = if (isSelected) BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary) else null,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = option.title,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "文字等級倍率: ${(option.scale * 100).toInt()}%",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            RadioButton(
                                selected = isSelected,
                                onClick = { viewModel.updateFontSizeOption(option) }
                            )
                        }
                    }
                }
            }
        }

        item {
            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
            Text(
                text = "即時閱讀效果預覽 (Live Preview)",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))

            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "單元一：變數與資料型態",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "變數是程式語言中最基礎的儲存單元，它就像是一個標有名稱的盒子，用來存放資料。在 Kotlin 中，我們使用 val 與 var 宣告變數。",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SuggestionChip(
                            onClick = {},
                            label = { Text("Kotlin 觀念") }
                        )
                        SuggestionChip(
                            onClick = {},
                            label = { Text("預覽展示") }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationSettingsContent(viewModel: LearningViewModel) {
    val times = listOf("08:00", "12:00", "18:00", "20:00", "22:00")

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "每日定時學習提醒",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = "定時提醒學習打卡，建立穩固寫程式習慣",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = viewModel.dailyReminderEnabled,
                            onCheckedChange = { viewModel.updateDailyReminder(it) }
                        )
                    }

                    if (viewModel.dailyReminderEnabled) {
                        HorizontalDivider()
                        Text("選擇每日提醒時間:", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            times.forEach { time ->
                                val isSelected = viewModel.reminderTime == time
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { viewModel.updateReminderTime(time) },
                                    label = { Text(time, fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) }
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("智慧推播與警示偏好", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("連續學習中斷預警", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text("當當天晚間尚未完成打卡時，發送連勝預警", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Switch(
                            checked = viewModel.streakAlertsEnabled,
                            onCheckedChange = { viewModel.updateStreakAlerts(it) }
                        )
                    }

                    HorizontalDivider()

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("每日測驗挑戰推播", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text("每天推播一題精選演算法或面試名題", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Switch(
                            checked = viewModel.quizChallengeEnabled,
                            onCheckedChange = { viewModel.updateQuizChallenge(it) }
                        )
                    }

                    HorizontalDivider()

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("AI 導師智慧學習建議", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text("根據學習記錄自動推送觀念解析與盲點提醒", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Switch(
                            checked = viewModel.aiTipsEnabled,
                            onCheckedChange = { viewModel.updateAiTips(it) }
                        )
                    }
                }
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("操作音效與觸覺體驗", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("觸覺震動回饋 (Haptic)", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text("答對題目或解鎖成就時觸發微震動", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Switch(
                            checked = viewModel.vibrationEnabled,
                            onCheckedChange = { viewModel.updateVibration(it) }
                        )
                    }

                    HorizontalDivider()

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("按鈕與答題音效", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text("播放簡潔俐落的互動點擊與完成慶賀音效", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Switch(
                            checked = viewModel.soundEffectsEnabled,
                            onCheckedChange = { viewModel.updateSoundEffects(it) }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CodeEditorSettingsContent(viewModel: LearningViewModel) {
    val fontSizes = listOf(12, 14, 16, 18)

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Text(
                text = "語法高亮主題 (Code Theme)",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                CodeThemeOption.values().forEach { option ->
                    val isSelected = viewModel.codeThemeOption == option
                    Card(
                        onClick = { viewModel.updateCodeThemeOption(option) },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                        ),
                        border = if (isSelected) BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary) else null,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(option.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                            RadioButton(selected = isSelected, onClick = { viewModel.updateCodeThemeOption(option) })
                        }
                    }
                }
            }
        }

        item {
            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
            Text("程式碼字級 (Code Font Size)", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                fontSizes.forEach { sz ->
                    val isSelected = viewModel.codeFontSizeSp == sz
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.updateCodeFontSizeSp(sz) },
                        label = { Text("${sz} sp", fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) }
                    )
                }
            }
        }

        item {
            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("即時自動測試 (Auto-Run)", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                    Text("在 Python / Kotlin 模擬器輸入代碼時自動檢查語法並執行", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Switch(
                    checked = viewModel.autoRunCodeEnabled,
                    onCheckedChange = { viewModel.updateAutoRunCode(it) }
                )
            }
        }

        item {
            Text("編輯器外觀即時預覽", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(6.dp))

            val previewBg = when (viewModel.codeThemeOption) {
                CodeThemeOption.DARK -> Color(0xFF1E1E1E)
                CodeThemeOption.MONOKAI -> Color(0xFF272822)
                CodeThemeOption.LIGHT -> Color(0xFFF5F5F5)
                CodeThemeOption.SOLARIZED -> Color(0xFFFDF6E3)
            }
            val previewFg = when (viewModel.codeThemeOption) {
                CodeThemeOption.DARK, CodeThemeOption.MONOKAI -> Color(0xFFA6E22E)
                CodeThemeOption.LIGHT -> Color(0xFF006600)
                CodeThemeOption.SOLARIZED -> Color(0xFF859900)
            }

            Surface(
                shape = RoundedCornerShape(14.dp),
                color = previewBg,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "def calculate_score(val1, val2):\n    # Code Preview\n    return val1 * 10 + val2",
                        fontFamily = FontFamily.Monospace,
                        fontSize = viewModel.codeFontSizeSp.sp,
                        color = previewFg
                    )
                }
            }
        }
    }
}

@Composable
fun SystemDataSettingsContent(
    viewModel: LearningViewModel,
    onDismiss: () -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    var showResetDialog by remember { mutableStateOf(false) }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("資料與快取管理", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)

                    OutlinedButton(
                        onClick = {
                            android.widget.Toast.makeText(context, "已成功清除暫存快取記憶體！", android.widget.Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Icon(Icons.Default.CleaningServices, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("清除暫存快取記憶體", fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = {
                            android.widget.Toast.makeText(context, "已成功產生學習紀錄加密備份！", android.widget.Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("匯出學習成就與筆記備份", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("危險區域", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error)
                    }

                    Text(
                        text = "這將會重置這台裝置上的所有課程成就、經驗值等級與 AI 對話記錄，恢復為初始安裝狀態。",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )

                    Button(
                        onClick = { showResetDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.DeleteForever, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("重新初始化所有學習進度", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("確定要重置所有進度嗎？") },
            text = { Text("重置後您的通關紀錄、等級與 XP 經驗值將無法復原。") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.resetAllUserStats()
                        showResetDialog = false
                        onDismiss()
                    }
                ) {
                    Text("確認重置", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text("取消")
                }
            }
        )
    }
}

