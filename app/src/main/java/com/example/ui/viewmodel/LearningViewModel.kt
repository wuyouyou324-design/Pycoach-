package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.GeminiClient
import com.example.courses.CourseData
import com.example.courses.CourseLesson
import com.example.data.*
import com.example.emulator.ExecutionStep
import com.example.emulator.PreloadedScript
import com.example.emulator.PythonPlayground
import com.example.quiz.QuizData
import com.example.quiz.QuizQuestion
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppTab {
    COURSES, EMULATOR, QUIZ, AI_COACH, DASHBOARD
}

enum class QuizStatus {
    NOT_STARTED, IN_PROGRESS, GRADED, COMPLETED
}

enum class CourseType {
    PYTHON, JAVASCRIPT, HTML, KOTLIN
}

enum class ThemeMode(val title: String) {
    SYSTEM("系統預設"),
    LIGHT("亮色模式"),
    DARK("深色模式")
}

enum class FontSizeOption(val title: String, val scale: Float) {
    SMALL("緊湊 (85%)", 0.85f),
    STANDARD("標準 (100%)", 1.0f),
    LARGE("放大 (115%)", 1.15f),
    EXTRA_LARGE("特大 (130%)", 1.30f)
}

enum class CodeThemeOption(val title: String) {
    DARK("深色語法"),
    MONOKAI("Monokai 高對比"),
    LIGHT("淺色簡約"),
    SOLARIZED("Solarized 暖色")
}

class LearningViewModel(application: Application) : AndroidViewModel(application) {

    private val database = PythonAppDatabase.getDatabase(application)
    private val repository = LearningRepository(database.learningDao())
    private val settingsPrefs = application.getSharedPreferences("app_settings_prefs", Context.MODE_PRIVATE)

    // Customization Settings State
    var themeMode by mutableStateOf(ThemeMode.SYSTEM)
    var dynamicColorEnabled by mutableStateOf(true)
    var fontSizeOption by mutableStateOf(FontSizeOption.STANDARD)

    // Notification Preferences State
    var dailyReminderEnabled by mutableStateOf(true)
    var reminderTime by mutableStateOf("20:00")
    var streakAlertsEnabled by mutableStateOf(true)
    var quizChallengeEnabled by mutableStateOf(true)
    var aiTipsEnabled by mutableStateOf(true)
    var soundEffectsEnabled by mutableStateOf(true)
    var vibrationEnabled by mutableStateOf(true)

    // Code & Editor Settings State
    var codeThemeOption by mutableStateOf(CodeThemeOption.DARK)
    var codeFontSizeSp by mutableStateOf(14)
    var autoRunCodeEnabled by mutableStateOf(false)

    init {
        loadSettingsFromPrefs()
    }

    private fun loadSettingsFromPrefs() {
        val themeName = settingsPrefs.getString("theme_mode", ThemeMode.SYSTEM.name) ?: ThemeMode.SYSTEM.name
        themeMode = try { ThemeMode.valueOf(themeName) } catch (e: Exception) { ThemeMode.SYSTEM }

        dynamicColorEnabled = settingsPrefs.getBoolean("dynamic_color", true)

        val fontName = settingsPrefs.getString("font_size_option", FontSizeOption.STANDARD.name) ?: FontSizeOption.STANDARD.name
        fontSizeOption = try { FontSizeOption.valueOf(fontName) } catch (e: Exception) { FontSizeOption.STANDARD }

        dailyReminderEnabled = settingsPrefs.getBoolean("daily_reminder", true)
        reminderTime = settingsPrefs.getString("reminder_time", "20:00") ?: "20:00"
        streakAlertsEnabled = settingsPrefs.getBoolean("streak_alerts", true)
        quizChallengeEnabled = settingsPrefs.getBoolean("quiz_challenge", true)
        aiTipsEnabled = settingsPrefs.getBoolean("ai_tips", true)
        soundEffectsEnabled = settingsPrefs.getBoolean("sound_effects", true)
        vibrationEnabled = settingsPrefs.getBoolean("vibration", true)

        val codeThemeName = settingsPrefs.getString("code_theme", CodeThemeOption.DARK.name) ?: CodeThemeOption.DARK.name
        codeThemeOption = try { CodeThemeOption.valueOf(codeThemeName) } catch (e: Exception) { CodeThemeOption.DARK }
        codeFontSizeSp = settingsPrefs.getInt("code_font_size", 14)
        autoRunCodeEnabled = settingsPrefs.getBoolean("auto_run_code", false)
    }

    fun updateThemeMode(mode: ThemeMode) {
        themeMode = mode
        settingsPrefs.edit().putString("theme_mode", mode.name).apply()
    }

    fun updateDynamicColor(enabled: Boolean) {
        dynamicColorEnabled = enabled
        settingsPrefs.edit().putBoolean("dynamic_color", enabled).apply()
    }

    fun updateFontSizeOption(option: FontSizeOption) {
        fontSizeOption = option
        settingsPrefs.edit().putString("font_size_option", option.name).apply()
    }

    fun updateDailyReminder(enabled: Boolean) {
        dailyReminderEnabled = enabled
        settingsPrefs.edit().putBoolean("daily_reminder", enabled).apply()
    }

    fun updateReminderTime(time: String) {
        reminderTime = time
        settingsPrefs.edit().putString("reminder_time", time).apply()
    }

    fun updateStreakAlerts(enabled: Boolean) {
        streakAlertsEnabled = enabled
        settingsPrefs.edit().putBoolean("streak_alerts", enabled).apply()
    }

    fun updateQuizChallenge(enabled: Boolean) {
        quizChallengeEnabled = enabled
        settingsPrefs.edit().putBoolean("quiz_challenge", enabled).apply()
    }

    fun updateAiTips(enabled: Boolean) {
        aiTipsEnabled = enabled
        settingsPrefs.edit().putBoolean("ai_tips", enabled).apply()
    }

    fun updateSoundEffects(enabled: Boolean) {
        soundEffectsEnabled = enabled
        settingsPrefs.edit().putBoolean("sound_effects", enabled).apply()
    }

    fun updateVibration(enabled: Boolean) {
        vibrationEnabled = enabled
        settingsPrefs.edit().putBoolean("vibration", enabled).apply()
    }

    fun updateCodeThemeOption(option: CodeThemeOption) {
        codeThemeOption = option
        settingsPrefs.edit().putString("code_theme", option.name).apply()
    }

    fun updateCodeFontSizeSp(size: Int) {
        codeFontSizeSp = size
        settingsPrefs.edit().putInt("code_font_size", size).apply()
    }

    fun updateAutoRunCode(enabled: Boolean) {
        autoRunCodeEnabled = enabled
        settingsPrefs.edit().putBoolean("auto_run_code", enabled).apply()
    }

    // Current Course Type
    var currentCourseType by mutableStateOf(CourseType.PYTHON)

    fun switchCourseType(newType: CourseType) {
        currentCourseType = newType
        selectedCategoryFilter = "全部"
        lessonSearchQuery = ""
        
        // 1. Instantly switch quiz questions to match selected course
        startNewQuizBatch()
        
        // 2. Switch code emulator template and rules to match selected course
        customCodeInput = getDefaultCodeForCourse(newType)
        customTerminalOutput = ""
        customVariablesWatch = emptyMap()
        customAiErrorAnalysis = ""
    }

    private fun getDefaultCodeForCourse(type: CourseType): String {
        return when (type) {
            CourseType.PYTHON -> """# Python 3 程式範例
msg = "哈囉，歡迎來到 Python 課程！"
x = 7
y = 12

print(msg)
print("x + y =", x + y)"""
            CourseType.JAVASCRIPT -> """// JavaScript 網頁動態腳本範例
let developer = "Alex";
const level = 5;

console.log(`Hello JavaScript, ${'$'}{developer}!`);
console.log("現有分數:", level * 20);"""
            CourseType.HTML -> """<!-- HTML5 網頁結構範例 -->
<h1>歡迎來到 HTML 網頁教室</h1>
<p>這是一個<strong>粗體強調的段落</strong>說明文字。</p>
<button>點擊測試互動按鈕</button>
<ul>
  <li>HTML5 標籤結構</li>
  <li>CSS3 樣式渲染</li>
</ul>"""
            CourseType.KOTLIN -> """// Kotlin Android 開發範例
val appName = "PyCoachApp"
var activeUsers = 120

println("應用名稱: ${'$'}appName")
println("目前活躍用戶數: ${'$'}activeUsers")"""
        }
    }

    // Course Search & Category Filter
    var lessonSearchQuery by mutableStateOf("")
    var selectedCategoryFilter by mutableStateOf("全部")

    val currentLessons: List<CourseLesson>
        get() = when (currentCourseType) {
            CourseType.PYTHON -> CourseData.pythonLessons
            CourseType.JAVASCRIPT -> CourseData.javascriptLessons
            CourseType.HTML -> CourseData.htmlLessons
            CourseType.KOTLIN -> CourseData.kotlinLessons
        }

    val availableCategories: List<String>
        get() {
            val cats = currentLessons.map { it.category }.distinct()
            return listOf("全部") + cats
        }

    val filteredLessons: List<CourseLesson>
        get() {
            var lessons = currentLessons
            if (selectedCategoryFilter != "全部") {
                lessons = lessons.filter { it.category == selectedCategoryFilter }
            }
            if (lessonSearchQuery.isNotBlank()) {
                val q = lessonSearchQuery.trim().lowercase()
                lessons = lessons.filter {
                    it.title.lowercase().contains(q) ||
                    it.category.lowercase().contains(q) ||
                    it.description.lowercase().contains(q)
                }
            }
            return lessons
        }

    // UI Active Navigation Tab
    var currentTab by mutableStateOf(AppTab.COURSES)

    // User statistics fetched from Room DB
    val userStats: StateFlow<UserStats?> = repository.userStatsFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    // Code emulator state
    var isCustomCodeMode by mutableStateOf(false)
    var customCodeInput by mutableStateOf(getDefaultCodeForCourse(CourseType.PYTHON))

    // Quiz properties initialized before init block
    val activeQuizList = mutableListOf<QuizQuestion>()
    var quizCurrentIndex by mutableStateOf(0)
    var quizSelectedOption by mutableStateOf(-1)
    var quizStatus by mutableStateOf(QuizStatus.NOT_STARTED)
    var quizCurrentScore by mutableStateOf(0)

    init {
        // Initialize default quiz batch for active course
        startNewQuizBatch()

        viewModelScope.launch {
            // Trigger creation / retrieval of default stats
            repository.getOrCreateUserStats()
            // Track learning streaks
            repository.recordStreak()
            // Load daily retention data
            initializeRetentionData()
        }
    }

    // ==========================================
    // 1. COURSE LESSONS TAB STUFF
    // ==========================================
    var activeChapter: CourseLesson? by mutableStateOf(null)
    var activeLessonSelectedOption by mutableStateOf(-1)
    var activeLessonQuestionAnswered by mutableStateOf(false)
    var activeLessonIsCorrectAnswer by mutableStateOf(false)

    // Hands-On coding challenge states
    var handsOnCodeInput by mutableStateOf("")
    var handsOnTerminalOutput by mutableStateOf("")
    var handsOnVariablesWatch by mutableStateOf<Map<String, String>>(emptyMap())
    var handsOnFeedbackMessage by mutableStateOf("")
    var handsOnSuccess by mutableStateOf(false)
    var handsOnCompletedSet by mutableStateOf<Set<String>>(emptySet()) // set of lessonIds which hands-on are passed in this session

    // AI Error Analysis states
    var handsOnAiErrorAnalysis by mutableStateOf("")
    var handsOnIsAiAnalyzing by mutableStateOf(false)
    var handsOnChatHistory by mutableStateOf<List<Pair<String, String>>>(emptyList())
    var handsOnQuestionInput by mutableStateOf("")

    val handsOnErrorLine: Int?
        get() {
            if (handsOnTerminalOutput.isEmpty()) return null
            val regex = "位於第\\s*(\\d+)\\s*行".toRegex()
            val match = regex.find(handsOnTerminalOutput)
            return match?.groupValues?.get(1)?.toIntOrNull()
        }

    val handsOnFixedCode: String?
        get() {
            val match = "\\[FIXED_CODE\\]([\\s\\S]*?)\\[/FIXED_CODE\\]".toRegex().find(handsOnAiErrorAnalysis)
            return match?.groupValues?.get(1)?.trim()
        }

    fun setupHandsOnChallenge(lessonId: String, initialCode: String) {
        if (handsOnFeedbackMessage.isEmpty() || !handsOnCompletedSet.contains(lessonId)) {
            handsOnCodeInput = initialCode
            handsOnTerminalOutput = ""
            handsOnVariablesWatch = emptyMap()
            handsOnFeedbackMessage = ""
            handsOnSuccess = false
            handsOnAiErrorAnalysis = ""
            handsOnIsAiAnalyzing = false
            handsOnChatHistory = emptyList()
            handsOnQuestionInput = ""
        }
    }

    fun runAndVerifyHandsOn(lessonId: String) {
        val challenge = com.example.courses.HandsOnChallenges.challenges[lessonId] ?: return
        
        handsOnAiErrorAnalysis = ""
        handsOnIsAiAnalyzing = false
        
        // Evaluate python or javascript code depending on the lesson category / lessonId
        val evaluatorResult = if (lessonId.startsWith("js_")) {
            com.example.emulator.JavaScriptPlayground.evaluateCustomJSCode(handsOnCodeInput)
        } else if (lessonId.startsWith("html_")) {
            com.example.emulator.HtmlPlayground.evaluateCustomHtmlCode(handsOnCodeInput)
        } else {
            com.example.emulator.PythonPlayground.evaluateCustomPythonCode(handsOnCodeInput)
        }
        handsOnTerminalOutput = evaluatorResult.first
        handsOnVariablesWatch = evaluatorResult.second
        
        // Validation check
        val validationResult = challenge.validate(handsOnCodeInput, evaluatorResult.first, evaluatorResult.second)
        handsOnSuccess = validationResult.first
        handsOnFeedbackMessage = validationResult.second
        
        logStudyHours(0.2f) // Auto-log 0.2 hours for solving a hands-on task
        
        // If correct and not rewarded in this session, give XP!
        if (validationResult.first && !handsOnCompletedSet.contains(lessonId)) {
            val updatedSet = handsOnCompletedSet.toMutableSet()
            updatedSet.add(lessonId)
            handsOnCompletedSet = updatedSet
            
            viewModelScope.launch {
                repository.addXp(30) // extra reward XP for practical hands-on challenge completion!
            }
        } else if (!validationResult.first) {
            // Automatically launch Gemini diagnostic feedback
            runHandsOnAiAnalysis(challenge.title, challenge.description, challenge.expectedExplanation)
        }
    }

    private fun runHandsOnAiAnalysis(challengeTitle: String, challengeDesc: String, expected: String) {
        handsOnAiErrorAnalysis = ""
        handsOnIsAiAnalyzing = true
        handsOnChatHistory = emptyList()
        
        viewModelScope.launch {
            try {
                val errorDetails = if (handsOnErrorLine != null) {
                    "第 $handsOnErrorLine 行發生語法錯誤或執行失敗。\n終端機輸出：$handsOnTerminalOutput"
                } else {
                    "執行驗證未通過。\n終端機輸出：$handsOnTerminalOutput\n驗證回饋：$handsOnFeedbackMessage"
                }
                
                val prompt = """
                    【動手實作硬核挑戰偵錯分析】
                    - 語言類型：${if (activeChapter?.id?.startsWith("js_") == true) "JavaScript" else if (activeChapter?.id?.startsWith("html_") == true) "HTML" else "Python"}
                    - 任務主題：$challengeTitle
                    - 任務描述：$challengeDesc
                    - 預期通關目標：$expected
                    
                    【使用者編寫的原始程式碼】
                    $handsOnCodeInput
                    
                    【執行與驗證錯誤詳情】
                    $errorDetails
                    
                    請身為最專業、溫柔耐心的程式導師，為學員進行這段代碼的「AI 錯誤分析與偵錯建議」。
                    
                    【回覆規範與重點】
                    1. 標註出錯行數：你必須在分析的最開始，清晰且醒目、高對比地告訴學員：
                       「⚠️ 本次程式碼執行之偵錯發現：問題核心可能出在【第 X 行】」或是「⚠️ 本次執行未通過挑戰目標要求，請特別檢視第 Y 行前後。」
                    2. 解構錯誤原因：用淺顯易懂、引導提問的方式，告訴學員為什麼這行程式碼無法運作、或是為什麼不符合實作目標的要求。
                    3. 給予代碼修正提示，並「務必且唯一」將你建議的完整、可直接執行的修正後原始碼包裹在 [FIXED_CODE] 與 [/FIXED_CODE] 標籤中，如下所示（切勿在標籤內再添加多餘的 markdown 三反引號）：
                       [FIXED_CODE]
                       你的完整修正代碼
                       [/FIXED_CODE]
                    
                    請簡明扼要、層次分明地用繁體中文回答。請不要顯示任何 systemId 或環境路徑。
                """.trimIndent()
                
                val result = GeminiClient.getAIResponse(prompt)
                handsOnAiErrorAnalysis = result
                handsOnChatHistory = listOf("AI" to result)
            } catch (e: Exception) {
                handsOnAiErrorAnalysis = "AI 錯誤分析調用失敗：${e.localizedMessage}"
            } finally {
                handsOnIsAiAnalyzing = false
            }
        }
    }

    fun askHandsOnFollowUp(question: String) {
        if (question.isBlank() || handsOnIsAiAnalyzing) return
        val updatedHistory = handsOnChatHistory.toMutableList()
        updatedHistory.add("User" to question)
        handsOnChatHistory = updatedHistory
        handsOnQuestionInput = ""
        handsOnIsAiAnalyzing = true
        
        viewModelScope.launch {
            try {
                val challenge = com.example.courses.HandsOnChallenges.challenges[activeChapter?.id] ?: return@launch
                
                // Build a conversation context for the follow up
                val conversationStr = handsOnChatHistory.joinToString("\n") { (role, msg) ->
                    if (role == "User") "學員提問：$msg" else "導師回答：$msg"
                }
                
                val prompt = """
                    【動手實作追問補習對話】
                    - 任務主題：${challenge.title}
                    - 語言類型：${if (activeChapter?.id?.startsWith("js_") == true) "JavaScript" else if (activeChapter?.id?.startsWith("html_") == true) "HTML" else "Python"}
                    - 當前編寫的代碼：
                    $handsOnCodeInput
                    
                    【過往對話歷程】
                    $conversationStr
                    
                    請身為溫和、善於啟發學員思考的程式導師，針對學員最新的提問進行詳細、有教育意義的回覆。
                    - 保持專業、親切的互動口吻。
                    - 如果有產生修正後的完整代碼，同樣「務必」將其包裹在 [FIXED_CODE] 與 [/FIXED_CODE] 中，以便學員一鍵套用。
                    
                    請用繁體中文回答。
                """.trimIndent()
                
                val reply = GeminiClient.getAIResponse(prompt)
                val finalHistory = handsOnChatHistory.toMutableList()
                finalHistory.add("AI" to reply)
                handsOnChatHistory = finalHistory
                
                // Update final analysis so the apply fixed code button can capture the latest fixed code if any!
                if (reply.contains("[FIXED_CODE]")) {
                    handsOnAiErrorAnalysis = reply
                }
            } catch (e: Exception) {
                val finalHistory = handsOnChatHistory.toMutableList()
                finalHistory.add("AI" to "抱歉，連線失敗或調用發生異常：${e.localizedMessage}")
                handsOnChatHistory = finalHistory
            } finally {
                handsOnIsAiAnalyzing = false
            }
        }
    }

    fun runHandsOnOptimization() {
        val challenge = com.example.courses.HandsOnChallenges.challenges[activeChapter?.id] ?: return
        handsOnAiErrorAnalysis = ""
        handsOnIsAiAnalyzing = true
        handsOnChatHistory = emptyList()
        
        viewModelScope.launch {
            try {
                val prompt = """
                    【自訂程式碼成功通關優化審查】
                    - 語言類型：${if (activeChapter?.id?.startsWith("js_") == true) "JavaScript" else if (activeChapter?.id?.startsWith("html_") == true) "HTML" else "Python"}
                    - 任務主題：${challenge.title}
                    - 任務描述：${challenge.description}
                    
                    【使用者編寫的通關程式碼】
                    $handsOnCodeInput
                    
                    這段代碼已經順利通過了我們系統的執行與語法驗證！請身為世界級的高級技術專家、乾淨程式碼（Clean Code）倡導者與效能極致大師，為使用者提供學術且實用的「程式碼效能優化與美化評鑑建議」。
                    
                    【回覆規範】
                    1. 先給與讚美與評分（如：代碼質量：A+）。
                    2. 提供可讀性、時間/空間複雜度、邊界條件、或命名細節上的精進建議。
                    3. 提供更優雅、精簡且效能卓越的重構版本，並務必且唯一包裹在 [FIXED_CODE] 與 [/FIXED_CODE] 標籤中，如下：
                       [FIXED_CODE]
                       你的完整重構優化代碼
                       [/FIXED_CODE]
                    
                    請以專業、富教育啟發性的繁體中文回答。
                """.trimIndent()
                
                val result = GeminiClient.getAIResponse(prompt)
                handsOnAiErrorAnalysis = result
                handsOnChatHistory = listOf("AI" to result)
            } catch (e: Exception) {
                handsOnAiErrorAnalysis = "調用 AI 優化分析失敗：${e.localizedMessage}"
            } finally {
                handsOnIsAiAnalyzing = false
            }
        }
    }

    fun selectChapter(lesson: CourseLesson) {
        activeChapter = lesson
        activeLessonSelectedOption = -1
        activeLessonQuestionAnswered = false
        activeLessonIsCorrectAnswer = false

        // Set up hands-on test if exists
        val challenge = com.example.courses.HandsOnChallenges.challenges[lesson.id]
        if (challenge != null) {
            setupHandsOnChallenge(lesson.id, challenge.initialCode)
        }
    }

    fun submitLessonQuiz(selectedIdx: Int) {
        val chapter = activeChapter ?: return
        activeLessonSelectedOption = selectedIdx
        activeLessonQuestionAnswered = true
        val isCorrect = selectedIdx == chapter.correctQuizAnswerIndex
        activeLessonIsCorrectAnswer = isCorrect
        logStudyHours(0.1f) // Auto-log 0.1 hours for answering lesson check
    }

    fun claimLessonCompletion() {
        val chapter = activeChapter ?: return
        viewModelScope.launch {
            repository.completeLesson(chapter.id, chapter.xpReward)
            logStudyHours(0.6f) // Auto-log 0.6 hours upon main lesson completion
            // Go back
            activeChapter = null
        }
    }

    // ==========================================
    // 2. PYTHON / JS / HTML / KOTLIN EMULATOR TAB STUFF
    // ==========================================
    var customTerminalOutput by mutableStateOf("")
    var customVariablesWatch by mutableStateOf<Map<String, String>>(emptyMap())

    // AI Error Analysis states
    var customAiErrorAnalysis by mutableStateOf("")
    var customIsAiAnalyzing by mutableStateOf(false)
    var customChatHistory by mutableStateOf<List<Pair<String, String>>>(emptyList())
    var customQuestionInput by mutableStateOf("")

    val customErrorLine: Int?
        get() {
            if (customTerminalOutput.isEmpty()) return null
            val regex = "位於第\\s*(\\d+)\\s*行".toRegex()
            val match = regex.find(customTerminalOutput)
            return match?.groupValues?.get(1)?.toIntOrNull()
        }

    val customFixedCode: String?
        get() {
            val match = "\\[FIXED_CODE\\]([\\s\\S]*?)\\[/FIXED_CODE\\]".toRegex().find(customAiErrorAnalysis)
            return match?.groupValues?.get(1)?.trim()
        }

    // Preloaded script state
    var selectedScriptIdx by mutableStateOf(0)
    val preloadedScript: PreloadedScript
        get() = PythonPlayground.scripts[selectedScriptIdx]

    var currentStepIdx by mutableStateOf(-1)
    var stepTerminalHistory = mutableListOf<String>()
    var stepVariablesWatch = mutableListOf<com.example.emulator.ProgramVariable>()
    var isAutoStepping by mutableStateOf(false)
    private var autoStepJob: Job? = null

    fun selectScript(idx: Int) {
        selectedScriptIdx = idx
        resetEmulator()
    }

    fun resetEmulator() {
        if (isCustomCodeMode) {
            customTerminalOutput = ""
            customVariablesWatch = emptyMap()
            customAiErrorAnalysis = ""
            customIsAiAnalyzing = false
            customChatHistory = emptyList()
            customQuestionInput = ""
        } else {
            stopAutoStep()
            currentStepIdx = -1
            stepTerminalHistory.clear()
            stepVariablesWatch.clear()
        }
    }

    fun executeSingleStep() {
        val script = preloadedScript
        if (currentStepIdx < script.steps.size - 1) {
            currentStepIdx++
            val step = script.steps[currentStepIdx]
            
            // Increment variables
            stepVariablesWatch.clear()
            stepVariablesWatch.addAll(step.variables)
            
            // Add console output
            step.outputLine?.let { output ->
                stepTerminalHistory.add(output)
            }
        } else {
            stopAutoStep()
        }
    }

    fun toggleAutoStep() {
        if (isAutoStepping) {
            stopAutoStep()
        } else {
            isAutoStepping = true
            autoStepJob = viewModelScope.launch {
                while (currentStepIdx < preloadedScript.steps.size - 1) {
                    executeSingleStep()
                    delay(1500)
                }
                isAutoStepping = false
            }
        }
    }

    private fun stopAutoStep() {
        isAutoStepping = false
        autoStepJob?.cancel()
        autoStepJob = null
    }

    fun runCustomPython() {
        customAiErrorAnalysis = ""
        customIsAiAnalyzing = false
        logStudyHours(0.12f) // Auto-log 0.12 hours upon custom script execution
        
        val evaluatorResult = when (currentCourseType) {
            CourseType.JAVASCRIPT -> com.example.emulator.JavaScriptPlayground.evaluateCustomJSCode(customCodeInput)
            CourseType.HTML -> com.example.emulator.HtmlPlayground.evaluateCustomHtmlCode(customCodeInput)
            CourseType.KOTLIN -> com.example.emulator.KotlinPlayground.evaluateCustomKotlinCode(customCodeInput)
            else -> PythonPlayground.evaluateCustomPythonCode(customCodeInput)
        }
        customTerminalOutput = evaluatorResult.first
        customVariablesWatch = evaluatorResult.second
        
        if (customTerminalOutput.contains("語法錯誤") || customTerminalOutput.contains("錯誤")) {
            runCustomAiAnalysis()
        }
    }

    private fun runCustomAiAnalysis() {
        customAiErrorAnalysis = ""
        customIsAiAnalyzing = true
        customChatHistory = emptyList()
        
        viewModelScope.launch {
            try {
                val languageName = when (currentCourseType) {
                    CourseType.JAVASCRIPT -> "JavaScript"
                    CourseType.HTML -> "HTML"
                    CourseType.KOTLIN -> "Kotlin"
                    else -> "Python"
                }
                
                val errorDetails = if (customErrorLine != null) {
                    "於第 $customErrorLine 行偵測到語法或解析錯誤。\n終端機輸出：$customTerminalOutput"
                } else {
                    "程式執行完畢或出錯。\n終端機輸出：$customTerminalOutput"
                }
                
                val prompt = """
                    【自訂程式碼終端機執行偵錯分析】
                    - 語言類型：$languageName
                    
                    【使用者編寫的原始程式碼】
                    $customCodeInput
                    
                    【終端機偵測之錯誤詳情】
                    $errorDetails
                    
                    請身為最專業、溫和有耐心的程式設計偵錯大師，為使用者進行「自訂代碼 AI 錯誤分析與重構建議」。
                    
                    【回覆規範與重點】
                    1. 標註出錯行數：你必須在分析的最開始，極其醒目且突出地標註：「⚠️ 出錯行數分析：問題可能發生在【第 X 行】」（如果代碼為空或無法定位具體行，則說明一般性除錯位置）。
                    2. 核心錯誤原因：解構問題原因（如語法錯誤、不合法變量名稱、未閉合括號等）。
                    3. 修改與建議，並「務必且唯一」將你建議的完整、可直接執行的正確代碼包裹在 [FIXED_CODE] 與 [/FIXED_CODE] 標籤中（切勿添加 markdown 三反引號），例如：
                       [FIXED_CODE]
                       你的完整代碼
                       [/FIXED_CODE]
                    
                    請條理清晰、富教育意義地用繁體中文回答。請不要顯示任何 systemId 或環境路徑。
                """.trimIndent()
                
                val result = GeminiClient.getAIResponse(prompt)
                customAiErrorAnalysis = result
                customChatHistory = listOf("AI" to result)
            } catch (e: Exception) {
                customAiErrorAnalysis = "AI 錯誤分析調用失敗：${e.localizedMessage}"
            } finally {
                customIsAiAnalyzing = false
            }
        }
    }

    fun askCustomFollowUp(question: String) {
        if (question.isBlank() || customIsAiAnalyzing) return
        val updatedHistory = customChatHistory.toMutableList()
        updatedHistory.add("User" to question)
        customChatHistory = updatedHistory
        customQuestionInput = ""
        customIsAiAnalyzing = true
        
        viewModelScope.launch {
            try {
                val languageName = when (currentCourseType) {
                    CourseType.JAVASCRIPT -> "JavaScript"
                    CourseType.HTML -> "HTML"
                    CourseType.KOTLIN -> "Kotlin"
                    else -> "Python"
                }
                
                val conversationStr = customChatHistory.joinToString("\n") { (role, msg) ->
                    if (role == "User") "學員提問：$msg" else "導師回答：$msg"
                }
                
                val prompt = """
                    【自訂程式碼演練追問對話】
                    - 語言類型：$languageName
                    - 當前編寫的自訂代碼：
                    $customCodeInput
                    
                    【過往對話歷程】
                    $conversationStr
                    
                    請身為溫和、善於啟發使用者的程式大師與資深導師，針對最新的提問進行詳細、解惑指引與教育意義的回覆。
                    - 保持專業、親切的對話口吻。
                    - 若解答包含修正後代碼，請同樣包裹在 [FIXED_CODE] 與 [/FIXED_CODE] 中，以便使用者一鍵套用。
                    
                    請用繁體中文回答。
                """.trimIndent()
                
                val reply = GeminiClient.getAIResponse(prompt)
                val finalHistory = customChatHistory.toMutableList()
                finalHistory.add("AI" to reply)
                customChatHistory = finalHistory
                
                if (reply.contains("[FIXED_CODE]")) {
                    customAiErrorAnalysis = reply
                }
            } catch (e: Exception) {
                val finalHistory = customChatHistory.toMutableList()
                finalHistory.add("AI" to "抱歉，連線失敗：${e.localizedMessage}")
                customChatHistory = finalHistory
            } finally {
                customIsAiAnalyzing = false
            }
        }
    }

    fun runCustomOptimization() {
        customAiErrorAnalysis = ""
        customIsAiAnalyzing = true
        customChatHistory = emptyList()
        
        viewModelScope.launch {
            try {
                val languageName = when (currentCourseType) {
                    CourseType.JAVASCRIPT -> "JavaScript"
                    CourseType.HTML -> "HTML"
                    CourseType.KOTLIN -> "Kotlin"
                    else -> "Python"
                }
                
                val prompt = """
                    【自訂程式碼效能與品質優化審查】
                    - 語言類型：$languageName
                    
                    【使用者編寫的自訂程式碼】
                    $customCodeInput
                    
                    這段代碼執行順利、沒有明顯的主語法錯誤。請身為最講究代碼美德與高效演算法的總架構師，不吝為使用者提供此設計的「效能優化評級與程式碼重構建議」。
                    
                    【回覆規範】
                    1. 評估其時間、空間複雜度及重構空間，並給予大師評等（如：S 級 / A 級 / B 級）。
                    2. 指出任何語意、冗餘定義或不乾淨的壞味道（Code Smell）。
                    3. 提供更優雅的最佳實踐重構代碼，並「務必且唯一」包裹在 [FIXED_CODE] 與 [/FIXED_CODE] 中以支持一鍵套用：
                       [FIXED_CODE]
                       你的優化重構代碼
                       [/FIXED_CODE]
                    
                    請用繁體中文以排版精美的 markdown 條列回答。
                """.trimIndent()
                
                val result = GeminiClient.getAIResponse(prompt)
                customAiErrorAnalysis = result
                customChatHistory = listOf("AI" to result)
            } catch (e: Exception) {
                customAiErrorAnalysis = "調用 AI 優化分析失敗：${e.localizedMessage}"
            } finally {
                customIsAiAnalyzing = false
            }
        }
    }

    // ==========================================
    // 3. CODE PRACTICE QUIZZES TAB STUFF
    // ==========================================
    val currentQuizQuestion: QuizQuestion?
        get() = if (quizCurrentIndex in activeQuizList.indices) activeQuizList[quizCurrentIndex] else null

    fun startNewQuizBatch() {
        // Load questions for current course type
        activeQuizList.clear()
        val courseQuestions = QuizData.getQuestionsForCourse(currentCourseType)
        activeQuizList.addAll(courseQuestions.shuffled().take(5))
        quizCurrentIndex = 0
        quizSelectedOption = -1
        quizStatus = QuizStatus.IN_PROGRESS
        quizCurrentScore = 0
    }

    fun submitQuizQuestion(optionIndex: Int) {
        val question = currentQuizQuestion ?: return
        quizSelectedOption = optionIndex
        quizStatus = QuizStatus.GRADED
        
        val isCorrect = optionIndex == question.correctAnswerIndex
        if (isCorrect) {
            quizCurrentScore++
        }
        
        viewModelScope.launch {
            // Reward statistics in DB
            repository.addQuizResult(isCorrect)
            logStudyHours(0.15f) // Auto-log 0.15 hours on quiz answer submission
        }
    }

    fun nextQuizQuestion() {
        if (quizCurrentIndex < activeQuizList.size - 1) {
            quizCurrentIndex++
            quizSelectedOption = -1
            quizStatus = QuizStatus.IN_PROGRESS
        } else {
            quizStatus = QuizStatus.COMPLETED
            val pct = if (activeQuizList.isNotEmpty()) (quizCurrentScore * 100) / activeQuizList.size else 100
            recordQuizScoreInHistory(pct)
        }
    }

    // ==========================================
    // 4. AI COACH TUTOR CHAT TAB STUFF
    // ==========================================
    val chatMessages: StateFlow<List<ChatMessage>> = repository.chatMessagesFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    var isAiThinking by mutableStateOf(false)
    var chatInputField by mutableStateOf("")

    fun sendChatPrompt(customPrompt: String? = null) {
        val promptToSend = (customPrompt ?: chatInputField).trim()
        if (promptToSend.isEmpty()) return

        if (customPrompt == null) {
            chatInputField = ""
        }

        isAiThinking = true
        viewModelScope.launch {
            // 1. Record user's query block in Room Database
            repository.insertChatMessage("user", promptToSend)

            // 2. Pack the last 8 chat interactions to pass context to Gemini API
            val currentList = chatMessages.value
            val historyContext = currentList.reversed().take(8).reversed().map {
                Pair(it.sender, it.message)
            }

            // 3. Query the Gemini tutor wrapper
            val response = GeminiClient.getAIResponse(promptToSend, historyContext)

            // 4. Record response block in local memory db
            repository.insertChatMessage("ai", response)
            isAiThinking = false
        }
    }

    fun clearChatHistory() {
        viewModelScope.launch {
            repository.clearChat()
        }
    }

    fun askAiCoachAboutQuiz(questionText: String, options: List<String>) {
        currentTab = AppTab.AI_COACH
        val optionsText = options.mapIndexed { index, opt -> "${'A' + index}. $opt" }.joinToString("\n")
        val prompt = "我正在完成隨堂測驗，遇到這道題目不太明白，請身為程式設計導師的您，在不直接透露唯一答案的情況下，用好玩的方式引導我、給予我思路 hints 吧！\n\n【題目內容】\n$questionText\n\n【選項】\n$optionsText"
        sendChatPrompt(prompt)
    }

    fun askAiCoachAboutPuzzle(puzzleTitle: String, language: String, code: String, options: List<String>) {
        currentTab = AppTab.AI_COACH
        val optionsText = options.mapIndexed { index, opt -> "${'A' + index}. $opt" }.joinToString("\n")
        val prompt = "我正在完成「每日代碼極速躍遷」的冷知識問答挑戰。這題語言是 $language，名為「$puzzleTitle」。\n\n【代碼內容】\n$code\n\n【選項】\n$optionsText\n\n請您用生動有趣、深入淺出的方式解構這行代碼背後的知識點與可能隱藏的語言陷阱，教導我如何正確分析它吧！"
        sendChatPrompt(prompt)
    }

    // ==========================================
    // EXTRA GAMIFICATION RETENTION FEATURES
    // ==========================================
    private val prefs by lazy { getApplication<Application>().getSharedPreferences("daily_v2_retention_prefs", Context.MODE_PRIVATE) }

    var isTodayCheckedIn by mutableStateOf(false)
        private set
    var checkInProgressDays by mutableStateOf(0) // 0 to 6 representing circular progress in a week
        private set
    var isTodayPuzzleCompleted by mutableStateOf(false)
        private set
    var todayPuzzle: CodePuzzle by mutableStateOf(dailyPuzzles[0])
        private set
    var dailyPuzzleSelectedOption by mutableStateOf(-1)
        private set
    var dailyPuzzleAnsweredCorrectly by mutableStateOf(false)
        private set
    var isDailyPuzzleFlipped by mutableStateOf(false) // For the flip transition!

    private fun getTodayDateString(): String {
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
        return sdf.format(java.util.Date())
    }

    private fun initializeRetentionData() {
        loadDashboardStats()
        val todayStr = getTodayDateString()
        val lastCheckInDate = prefs.getString("last_checkin_date", "") ?: ""
        isTodayCheckedIn = lastCheckInDate == todayStr
        
        var savedProgress = prefs.getInt("checkin_progress_days", 0)
        // If a new day stars and they already achieved 7-day streak, reset progress to 0 for a new week cycle
        if (lastCheckInDate != todayStr && savedProgress >= 7) {
            savedProgress = 0
            prefs.edit().putInt("checkin_progress_days", 0).apply()
        }
        checkInProgressDays = savedProgress

        val lastPuzzleDate = prefs.getString("last_puzzle_date", "") ?: ""
        isTodayPuzzleCompleted = lastPuzzleDate == todayStr

        // Select puzzle based on current day or date hash code so it is consistent but moves
        val dayHash = Math.abs(todayStr.hashCode())
        val puzzleIdx = dayHash % dailyPuzzles.size
        todayPuzzle = dailyPuzzles[puzzleIdx]

        // Load daily puzzle answer status if already answered
        dailyPuzzleSelectedOption = prefs.getInt("puzzle_selected_${todayStr}", -1)
        dailyPuzzleAnsweredCorrectly = prefs.getBoolean("puzzle_correct_${todayStr}", false)
    }

    fun performDailyCheckIn() {
        if (isTodayCheckedIn) return
        val todayStr = getTodayDateString()
        
        // Mark checked in
        prefs.edit().putString("last_checkin_date", todayStr).apply()
        
        // Increment weekly progress
        val newProgress = (checkInProgressDays + 1).coerceAtMost(7)
        prefs.edit().putInt("checkin_progress_days", newProgress).apply()
        
        isTodayCheckedIn = true
        checkInProgressDays = newProgress
        logStudyHours(0.3f) // Auto-log 0.3 hours upon check-in

        viewModelScope.launch {
            // Give 30 XP as check-in reward!
            repository.addXp(30)
        }
    }

    fun submitDailyPuzzle(selectedIndex: Int) {
        if (isTodayPuzzleCompleted) return
        val todayStr = getTodayDateString()
        
        val isCorrect = selectedIndex == todayPuzzle.correctIndex
        
        prefs.edit()
            .putString("last_puzzle_date", todayStr)
            .putInt("puzzle_selected_${todayStr}", selectedIndex)
            .putBoolean("puzzle_correct_${todayStr}", isCorrect)
            .apply()
            
        isTodayPuzzleCompleted = true
        dailyPuzzleSelectedOption = selectedIndex
        dailyPuzzleAnsweredCorrectly = isCorrect
        logStudyHours(0.2f) // Auto-log 0.2 hours for solving daily puzzle

        viewModelScope.launch {
            // Reward 50 XP if correct, 15 XP if wrong but tried!
            val xpReward = if (isCorrect) 50 else 15
            repository.addXp(xpReward)
        }
    }

    fun flipDailyPuzzleCard() {
        isDailyPuzzleFlipped = !isDailyPuzzleFlipped
    }

    // ==========================================
    // 5. USER SETTINGS AND PROGRESS RESETS
    // ==========================================
    fun resetAllUserStats() {
        viewModelScope.launch {
            repository.resetAll()
            // Soft reset other local state variables as well
            activeChapter = null
            resetEmulator()
            quizStatus = QuizStatus.NOT_STARTED
            
            // Clear preferences so that the user can play daily features again in the same sandboxed session!
            prefs.edit().clear().apply()
            isTodayCheckedIn = false
            checkInProgressDays = 0
            isTodayPuzzleCompleted = false
            dailyPuzzleSelectedOption = -1
            dailyPuzzleAnsweredCorrectly = false
            isDailyPuzzleFlipped = false
        }
    }

    // ==========================================
    // LEARNING STATS DASHBOARD METHODS & STATES
    // ==========================================
    var dailyPracticeHours by mutableStateOf<List<Float>>(emptyList())
    var monthlyPracticeHours by mutableStateOf<List<Float>>(emptyList())
    var recentQuizScores by mutableStateOf<List<Int>>(emptyList())
    var aiDashboardInsights by mutableStateOf("")
    var isCheckingAiInsights by mutableStateOf(false)

    private fun loadDashboardStats() {
        val hoursStr = prefs.getString("dashboard_daily_hours", "0.0,0.0,0.0,0.0,0.0,0.0,0.0") ?: "0.0,0.0,0.0,0.0,0.0,0.0,0.0"
        dailyPracticeHours = hoursStr.split(",").map { it.toFloatOrNull() ?: 0f }

        val monthlyHoursStr = prefs.getString("dashboard_monthly_hours", "0.0,0.0,0.0,0.0") ?: "0.0,0.0,0.0,0.0"
        monthlyPracticeHours = monthlyHoursStr.split(",").map { it.toFloatOrNull() ?: 0f }

        val scoresStr = prefs.getString("dashboard_quiz_scores", "") ?: ""
        recentQuizScores = if (scoresStr.isEmpty()) emptyList() else scoresStr.split(",").map { it.toIntOrNull() ?: 0 }

        aiDashboardInsights = prefs.getString("dashboard_ai_insights", "尚未產生診斷結果。點擊下方按鈕，AI 導師將深入診斷您在 Python 的學習進程，並生成專屬的學習藍圖！") ?: ""
    }

    fun logStudyHours(hours: Float) {
        if (hours <= 0f) return
        val todayIndex = java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_WEEK) - 1 // 0=Sunday to 6=Saturday
        val adjustedIndex = if (todayIndex <= 0) 6 else todayIndex - 1 // Mon=0, Tue=1, ..., Sun=6
        
        val mutable = dailyPracticeHours.toMutableList()
        // Ensure mutable has at least 7 elements
        while (mutable.size < 7) {
            mutable.add(0f)
        }
        if (adjustedIndex in mutable.indices) {
            mutable[adjustedIndex] = (mutable[adjustedIndex] + hours).coerceAtMost(12f)
        }
        dailyPracticeHours = mutable
        prefs.edit().putString("dashboard_daily_hours", mutable.joinToString(",")).apply()

        // Also update monthly hours representing weeks of the month (0 to 3)
        val weekOfMonth = java.util.Calendar.getInstance().get(java.util.Calendar.WEEK_OF_MONTH) - 1
        val adjustedWeekIndex = weekOfMonth.coerceIn(0, 3)
        val monthlyMutable = monthlyPracticeHours.toMutableList()
        while (monthlyMutable.size < 4) {
            monthlyMutable.add(0f)
        }
        if (adjustedWeekIndex in monthlyMutable.indices) {
            monthlyMutable[adjustedWeekIndex] = (monthlyMutable[adjustedWeekIndex] + hours).coerceAtMost(50f)
        }
        monthlyPracticeHours = monthlyMutable
        prefs.edit().putString("dashboard_monthly_hours", monthlyMutable.joinToString(",")).apply()
    }

    fun recordQuizScoreInHistory(scorePct: Int) {
        val mutable = recentQuizScores.toMutableList()
        mutable.add(scorePct)
        if (mutable.size > 8) {
            mutable.removeAt(0)
        }
        recentQuizScores = mutable
        prefs.edit().putString("dashboard_quiz_scores", mutable.joinToString(",")).apply()
    }

    fun runAiDashboardDiagnostic() {
        if (isCheckingAiInsights) return
        isCheckingAiInsights = true
        
        viewModelScope.launch {
            try {
                val stats = repository.getOrCreateUserStats()
                val completedList = stats.completedLessons.split(",").filter { it.isNotEmpty() }
                
                val prompt = """
                    【學員學習進度智能診斷診斷書】
                    - 目前主修：Python 核心基礎
                    - Lvl.${stats.level} | XP：${stats.xp}
                    - 累積學完單元數：${completedList.size} / 30 個單元
                    - 連續學習天數：${stats.streak} 天
                    - 答題正確率：${if (stats.quizTotal > 0) (stats.quizCorrect * 100 / stats.quizTotal) else 0}%（累計答對 ${stats.quizCorrect} / ${stats.quizTotal} 題）
                    - 最近 5 次隨堂測驗百分制分數：${recentQuizScores.joinToString(", ")} 
                    - 本週每日練習時數（週一到週日）：${dailyPracticeHours.joinToString(", ")} 小時
                    - 總計累計練習時數：${dailyPracticeHours.sum()} 小時
                    
                    請身為專屬一對一 AI 智慧學習指導教練，為這位學員產出一份有深度、句句扣緊數據、能徹底鼓舞人心並給出精準精進指南的「學習戰力大解析與精進白皮書」。
                    
                    【產出格式與重點】
                    1. 🎓 **總體評估 & 學習稱號**：根據數據，給予他目前學習風格的帥氣評估和稱號（例如：「晨曦型高頻實踐者」或「大步躍進的極限碼農」）。
                    2. 📈 **戰力長處剖析**：指出他表現最好的指標（例如答題正確率高、連續學習意念強、或是主動練就時數充足）。
                    3. ⚠️ **弱點精進地圖**：不指責、改以幽默且充滿技術大師語意的方式指出他的可改進點（例如是否單元修讀進度較慢、特定幾次測驗有谷底等），並給出具體的「下週精進 3 步走計畫」。
                    
                    請用精緻且層次分明的 Markdown 條列格式繁體中文回答，融入對 Python 知識的溫柔關懷與具體指引。不要包含任何 systemId 或環境位置。
                """.trimIndent()
                
                val result = GeminiClient.getAIResponse(prompt)
                aiDashboardInsights = result
                prefs.edit().putString("dashboard_ai_insights", result).apply()
            } catch (e: Exception) {
                aiDashboardInsights = "分析診斷失敗，請確認網路連線是否暢通：${e.localizedMessage}"
            } finally {
                isCheckingAiInsights = false
            }
        }
    }
}

// ==========================================
// CODE PUZZLE DEFINITIONS
// ==========================================
data class CodePuzzle(
    val id: String,
    val language: String,
    val title: String,
    val code: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)

val dailyPuzzles = listOf(
    CodePuzzle(
        id = "p1",
        language = "Python",
        title = "浮點數除法型態",
        code = "result = 1 / 2\nprint(type(result))",
        options = listOf("<class 'int'>", "<class 'float'>", "<class 'double'>", "SyntaxError"),
        correctIndex = 1,
        explanation = "在 Python 3 中，使用單斜線 `/` 進行除法運算一律會返回浮點數（float）型態，因此 `1 / 2` 會得到 `0.5`，其類型為 float！"
    ),
    CodePuzzle(
        id = "p2",
        language = "JavaScript",
        title = "經典寬鬆相等",
        code = "console.log([] == ![])",
        options = listOf("true", "false", "TypeError", "undefined"),
        correctIndex = 0,
        explanation = "超經典的 JS 奇葩題！因為 `![]` 會被評估為布林值 `false`（空陣列本身為 truthy），而 `[] == false` 會把兩側轉換為數字。`[]` 轉為 `0`，`false` 轉為 `0`，各轉完後 `0 == 0` 結果為 `true`！"
    ),
    CodePuzzle(
        id = "p3",
        language = "Python",
        title = "字串反轉切片",
        code = "text = \"Python\"\nprint(text[::-1])",
        options = listOf("Python", "nohtyP", "ythoP", "IndexError"),
        correctIndex = 1,
        explanation = "Python 的切片語法 `[start:stop:step]` 當 step 設為 `-1` 時，代表由後往前反向遍歷整個字串，因此輸出即為字串的反轉 `nohtyP`！"
    ),
    CodePuzzle(
        id = "p4",
        language = "JavaScript",
        title = "typeof 雙重解析",
        code = "console.log(typeof typeof 42)",
        options = listOf("\"number\"", "\"string\"", "\"undefined\"", "\"object\""),
        correctIndex = 1,
        explanation = "`typeof 42` 會先返回一個代表型態名稱的字串值 `\"number\"`。接著對該字串 `\"number\"` 再執行一次 `typeof`， since 字串本身是字串，所以必然輸出 `\"string\"`！"
    ),
    CodePuzzle(
        id = "p5",
        language = "Python",
        title = "字串相乘魔法",
        code = "print(3 * '3')",
        options = listOf("'9'", "'333'", "TypeError", "9"),
        correctIndex = 1,
        explanation = "在 Python 中，字串可以與整數相乘！這代表將該字串重複拼接指定的次數：`3 * '3'` 就是連續拼接 3 個 `'3'`，輸出得 `'333'`！"
    ),
    CodePuzzle(
        id = "p6",
        language = "JavaScript",
        title = "陣列指標參考",
        code = "let a = [1, 2];\nlet b = [1, 2];\nconsole.log(a == b);",
        options = listOf("true", "false", "undefined", "TypeError"),
        correctIndex = 1,
        explanation = "在 JavaScript 中，陣列（物件類型）的比較是基於「記憶體位址（Reference）」而非內容值。因為 `a` 和 `b` 指向兩個完全不同的記憶體實例，因此 `a == b` 為 `false`！"
    ),
    CodePuzzle(
        id = "p7",
        language = "Python",
        title = "布林值隱式轉換",
        code = "print(True + True)",
        options = listOf("True", "2", "TrueTrue", "TypeError"),
        correctIndex = 1,
        explanation = "在 Python 中，布林值 `True` 和 `False` 分別繼承自整數 `1` 與 `0`。進行算術加法時，`True` 會被隱式轉換為 `1`，所以 `1 + 1` 結果為 `2`！"
    ),
    CodePuzzle(
        id = "p8",
        language = "JavaScript",
        title = "神奇的 NaN 型態",
        code = "console.log(typeof NaN);",
        options = listOf("\"number\"", "\"NaN\"", "\"undefined\"", "\"object\""),
        correctIndex = 0,
        explanation = "NaN 代表 'Not a Number'（不是一個數字），但根據 JavaScript 規範，它的資料型態依舊屬於數值類型，因此 `typeof NaN` 會返回 `\"number\"`！"
    )
)
