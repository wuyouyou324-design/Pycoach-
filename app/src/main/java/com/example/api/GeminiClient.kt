package com.example.api

import android.util.Log
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object GeminiClient {
    private const val TAG = "GeminiClient"
    
    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    suspend fun getAIResponse(prompt: String, chatHistory: List<Pair<String, String>> = emptyList()): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext "請先在 AI Studio 的 Secrets 設定面板中配置真正的 GEMINI_API_KEY 方能與 AI 教練對話！"
        }

        // 1. 本地安全沙箱事前審查
        if (isPotentiallyHarmful(prompt)) {
            return@withContext "安全防護提示：本系統致力於提供安全、專業的 Python 學習體驗。AI 教練不開放關於設計/執行惡意程式（如木馬、勒索代碼、系統破壞、憑證竊取、或未經授權之網路與系統存取）的學術外操作指導。請專注於學習標準變數、迴圈與基礎程式邏輯，讓我們共同維護資訊安全！"
        }

        val models = listOf("gemini-3.5-flash", "gemini-2.5-flash", "gemini-1.5-flash")
        var lastErrorMsg = ""

        for (model in models) {
            val url = "https://generativelanguage.googleapis.com/v1beta/models/$model:generateContent?key=$apiKey"
            
            try {
                val contentsArray = JSONArray()
                
                // Add previous chat history
                for (turn in chatHistory) {
                    val role = if (turn.first == "user") "user" else "model"
                    val contentObj = JSONObject().apply {
                        put("role", role)
                        val partsArray = JSONArray().apply {
                            put(JSONObject().apply { put("text", turn.second) })
                        }
                        put("parts", partsArray)
                    }
                    contentsArray.put(contentObj)
                }
                
                // Add current question
                val currentTurn = JSONObject().apply {
                    put("role", "user")
                    val partsArray = JSONArray().apply {
                        put(JSONObject().apply { put("text", prompt) })
                    }
                    put("parts", partsArray)
                }
                contentsArray.put(currentTurn)

                // System Instruction (帶有強大安全教育審查約束的系統提示，並且不含任何情緒及 emoji，極具專業導師風範)
                val systemInstructionText = """
                    你是一位專業且極具素養的 Pycoach Python 程式設計導師。說話親切、嚴謹，並且統一使用繁體中文(Traditional Chinese)回答。
                    如果是程式代碼問題，請提供結構良好、安全、且富有教育意義的 Python 範例碼，並加上關鍵註解。請多用引導式提問，幫助學員一步一步掌握程式思維。

                    【安全邊界與教學沙箱規範 (極重要)】
                    當使用者提出可能涉及系統破壞、網絡攻擊、惡意軟體或敏感資訊截流的「不安全代碼」或「探測行為」時：
                    1. 絕不編寫、不提供、亦不解鎖任何有害或惡性用途的程式代碼（包括但不限於：鍵盤側錄、網絡爆破、自動木馬植入、自動提權、惡意勒索）。
                    2. 任何涉及底層資源讀寫、破壞作業系統文件、或執行破壞性指令（如 sh / rm -rf）的請求，均必須委婉指出其潛在安全風險與危害，引導其使用安全的形式或安全的學術角度探討，並隨即轉移至常規且健康的 Python 理論與應用學習。
                    3. 請秉持專業資訊人員之素養，將安全思維（Secure Coding）作為教育的一部分，倡導與實踐健康的軟體開發理念。
                """.trimIndent()

                val systemInstructionObj = JSONObject().apply {
                    val partsArray = JSONArray().apply {
                        put(JSONObject().apply { 
                            put("text", systemInstructionText) 
                        })
                    }
                    put("parts", partsArray)
                }

                // Combined Request Body
                val requestBodyJson = JSONObject().apply {
                    put("contents", contentsArray)
                    put("systemInstruction", systemInstructionObj)
                }

                val mediaType = "application/json; charset=utf-8".toMediaType()
                val requestBody = requestBodyJson.toString().toRequestBody(mediaType)

                val request = Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build()

                client.newCall(request).execute().use { response ->
                    if (!response.isSuccessful) {
                        val errBody = response.body?.string() ?: ""
                        Log.e(TAG, "Model $model request failed: Code ${response.code}, Body $errBody")
                        lastErrorMsg = "與 AI 伺服器聯絡出錯 (型號: $model, 代碼: ${response.code})。請確認 API 金鑰有效與網路連接正常。"
                        // Continue to next model in loop
                    } else {
                        val resBody = response.body?.string() ?: return@withContext "AI 伺服器傳回空數據"
                        val rootJson = JSONObject(resBody)
                        val candidates = rootJson.getJSONArray("candidates")
                        val firstCandidate = candidates.getJSONObject(0)
                        val responseContent = firstCandidate.getJSONObject("content")
                        val parts = responseContent.getJSONArray("parts")
                        
                        return@withContext parts.getJSONObject(0).getString("text")
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Exception during API call for model $model", e)
                lastErrorMsg = "AI 教練暫時無法服務 (型號: $model)：${e.localizedMessage ?: "請檢查您的網路連接"}"
                // Continue to next model in loop
            }
        }

        // If all models failed, return the last error message
        lastErrorMsg.ifEmpty { "AI 教練休息中，請稍後再試！" }
    }

    private fun isPotentiallyHarmful(prompt: String): Boolean {
        val lowerPrompt = prompt.lowercase()
        
        // 1. 常見惡意、破壞性、或滲透性關鍵字
        val maliciousKeywords = listOf(
            "malware", "ransomware", "backdoor", "trojan", "spyware", "computer virus",
            "勒索軟體", "惡意軟體", "後門程式", "特洛伊木馬", "電腦病毒", "木馬程式",
            "ddos attack", "denial of service", "阻斷服務攻擊", "injection exploit", "sql injection",
            "privilege escalation", "提權", "credential harvesting", "憑證竊取", "steal password",
            "fork bomb", "叉子炸彈", "攻擊網站", "crack security", "crack software", "ddos 攻擊"
        )
        for (keyword in maliciousKeywords) {
            if (lowerPrompt.contains(keyword)) {
                return true
            }
        }
        
        // 2. 破壞性操作與敏感標的之惡意搭配限制 (防止對重要硬體、根目錄或系統金鑰執行寫入或破壞，但不影響純粹的 OS 模組教學)
        val destructiveVerbs = listOf(
            "delete", "remove", "rm -rf", "destroy", "wipe", "format", "shred", "overwriting",
            "刪除", "清除", "破壞", "格式化"
        )
        val sensitiveAssets = listOf(
            "system file", "root directory", "keystore", "debug.keystore", "build.gradle", "metadata.json",
            "系統檔案", "根目錄", "密鑰庫", "環境變數"
        )
        
        val containsVerb = destructiveVerbs.any { lowerPrompt.contains(it) }
        val containsAsset = sensitiveAssets.any { lowerPrompt.contains(it) }
        if (containsVerb && containsAsset) {
            return true
        }
        
        return false
    }
}
