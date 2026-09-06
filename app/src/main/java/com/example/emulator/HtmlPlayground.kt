package com.example.emulator

object HtmlPlayground {

    // Evaluate HTML code and check tags/content
    fun evaluateCustomHtmlCode(code: String): Pair<String, Map<String, String>> {
        val tagsDetected = mutableMapOf<String, String>()
        val output = StringBuilder()
        
        val lines = code.split("\n")
        var elementCount = 0
        var currentLineNo = 0
        var hasError = false
        
        output.append("🌐 瀏覽器虛擬渲染畫面 (Simulated Browser Paint):\n")
        output.append("──────────────────────────────────────────\n")
        
        try {
            // First pass: Global HTML tag matching validation
            validateGlobalHtmlTags(code)

            for (rawLine in lines) {
                currentLineNo++
                val line = rawLine.trim()
                if (line.isEmpty()) continue
                
                // Line-level HTML Syntax validation checks
                if (line.contains("<") && !line.contains(">")) {
                    throw Exception("HTML 標籤未完全閉合，缺少 '>' 符號。例如：`<h1>標題` 需補上 '>' 才能成為合法標籤！")
                }
                if (line.contains(">") && !line.contains("<") && !line.endsWith("-->")) {
                    val firstLt = line.indexOf("<")
                    val firstGt = line.indexOf(">")
                    if (firstLt == -1 || firstGt < firstLt) {
                        throw Exception("發現孤立或錯位的 '>' 符號，HTML 標籤結構損壞！")
                    }
                }
                val doubleQuoteCount = line.count { it == '"' }
                if (doubleQuoteCount % 2 != 0) {
                    throw Exception("HTML 屬性雙引號 `\"` 未成對閉合（例如：href=\"https://... 引號遺漏）")
                }
                
                // Let's check for basic tags
                if (line.contains("<h1>") && line.contains("</h1>")) {
                    val content = extractContent(line, "<h1>", "</h1>")
                    output.append("【H1 大標題】 $content\n")
                    elementCount++
                    tagsDetected["h1_$elementCount"] = content
                }
                else if (line.contains("<h2>") && line.contains("</h2>")) {
                    val content = extractContent(line, "<h2>", "</h2>")
                    output.append("【H2 中標題】 $content\n")
                    elementCount++
                    tagsDetected["h2_$elementCount"] = content
                }
                else if (line.contains("<p>") && line.contains("</p>")) {
                    val content = extractContent(line, "<p>", "</p>")
                    output.append("【段落文字】 $content\n")
                    elementCount++
                    tagsDetected["p_$elementCount"] = content
                }
                else if (line.contains("<strong>") && line.contains("</strong>")) {
                    val content = extractContent(line, "<strong>", "</strong>")
                    output.append("【粗體強調】 $content\n")
                    elementCount++
                    tagsDetected["strong_$elementCount"] = content
                }
                else if (line.contains("<em>") && line.contains("</em>")) {
                    val content = extractContent(line, "<em>", "</em>")
                    output.append("【斜體強調】 $content\n")
                    elementCount++
                    tagsDetected["em_$elementCount"] = content
                }
                else if (line.contains("<a ") && line.contains("</a>")) {
                    val content = extractContent(line, ">", "</a>")
                    val href = extractAttribute(line, "href")
                    output.append("【超連結】 🔗 $content (網址: $href)\n")
                    elementCount++
                    tagsDetected["a_$elementCount"] = "$content ($href)"
                }
                else if (line.contains("<img ")) {
                    val src = extractAttribute(line, "src")
                    val alt = extractAttribute(line, "alt")
                    output.append("【多媒體圖片】 🖼️ [網址: $src] (說明: $alt)\n")
                    elementCount++
                    tagsDetected["img_$elementCount"] = "src=$src, alt=$alt"
                }
                else if (line.contains("<button") && line.contains("</button>")) {
                    val content = extractContent(line, ">", "</button>")
                    output.append("【互動按鈕】 🔘 [ $content ]\n")
                    elementCount++
                    tagsDetected["button_$elementCount"] = content
                }
                else if (line.contains("<li>") && line.contains("</li>")) {
                    val content = extractContent(line, "<li>", "</li>")
                    output.append("  • 清單項目: $content\n")
                    elementCount++
                    tagsDetected["li_$elementCount"] = content
                }
                else if (line.contains("<ul>") || line.contains("</ul>") || line.contains("<ol>") || line.contains("</ol>")) {
                    val type = if (line.contains("<ul>")) "無序列表 <ul>" else if (line.contains("<ol>")) "有序列表 <ol>" else "列表閉合標籤"
                    tagsDetected["list_structure_$elementCount"] = type
                }
                else if (line.contains("<!--") && line.contains("-->")) {
                    val comment = extractContent(line, "<!--", "-->")
                    output.append("（🌿 網頁註解: $comment）\n")
                }
                else {
                    val cleanText = line.replace("<[^>]*>".toRegex(), "").trim()
                    if (cleanText.isNotEmpty()) {
                        output.append("$cleanText\n")
                    }
                }
            }
        } catch (e: Exception) {
            hasError = true
            output.append("\n❌ ❌ ❌ 瀏覽器 HTML 渲染模擬已停止 ❌ ❌ ❌\n")
            output.append("【HTML5 語法解析錯誤 - 第 $currentLineNo 行】\n")
            output.append("錯誤原因：${e.message}\n")
        }
        
        output.append("──────────────────────────────────────────\n")
        if (hasError) {
            output.append("⚠️ 網頁 DOM 渲染中止，請依照錯誤原因修正 HTML 標籤語法！\n")
        } else {
            output.append("總共成功渲染 $elementCount 個基礎 DOM 標籤物件。\n")
        }
        
        return Pair(output.toString(), tagsDetected)
    }

    private fun validateGlobalHtmlTags(code: String) {
        val tagRegex = "<(/?)(\\w+)[^>]*>".toRegex()
        val openTags = mutableListOf<String>()
        val selfClosingTags = setOf("img", "br", "hr", "input", "meta", "link")

        val matches = tagRegex.findAll(code)
        for (match in matches) {
            val isClosing = match.groupValues[1] == "/"
            val tagName = match.groupValues[2].lowercase()

            if (selfClosingTags.contains(tagName)) continue

            if (!isClosing) {
                openTags.add(tagName)
            } else {
                if (openTags.isEmpty()) {
                    throw Exception("標籤結構錯位或多餘閉合：發現多餘的閉合標籤 `</$tagName>`，前面沒有對應的開啟標籤")
                }
                val lastOpen = openTags.last()
                if (lastOpen == tagName) {
                    openTags.removeAt(openTags.lastIndex)
                } else {
                    throw Exception("HTML 標籤未正確嵌套閉合：`< $lastOpen >` 尚未閉合，卻先出現了 `</$tagName>`")
                }
            }
        }

        if (openTags.isNotEmpty()) {
            val unclosed = openTags.last()
            throw Exception("HTML 標籤未正確閉合：`< $unclosed >` 標籤缺少對應的閉合標籤 `</$unclosed>`")
        }
    }
    
    private fun extractContent(line: String, startTag: String, endTag: String): String {
        return try {
            val startIdx = line.indexOf(startTag)
            val endIdx = line.indexOf(endTag)
            if (startIdx != -1 && endIdx != -1 && endIdx > startIdx) {
                line.substring(startIdx + startTag.length, endIdx).trim()
            } else {
                val clean = line.replace("<[^>]*>".toRegex(), "").trim()
                clean
            }
        } catch (e: Exception) {
            ""
        }
    }
    
    private fun extractAttribute(line: String, attrName: String): String {
        return try {
            val search = "$attrName="
            val startIdx = line.indexOf(search)
            if (startIdx != -1) {
                val rem = line.substring(startIdx + search.length)
                if (rem.isNotEmpty()) {
                    val quote = rem[0]
                    if (quote == '"' || quote == '\'') {
                        val endIdx = rem.indexOf(quote, 1)
                        if (endIdx != -1) {
                            return rem.substring(1, endIdx)
                        }
                    }
                }
            }
            "未指定"
        } catch (e: Exception) {
            "未指定"
        }
    }
}

