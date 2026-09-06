package com.example.emulator

object JavaScriptPlayground {

    // A real custom runtime evaluator of custom linear JS code written by users
    fun evaluateCustomJSCode(code: String): Pair<String, Map<String, String>> {
        val vars = mutableMapOf<String, String>()
        val output = StringBuilder()
        
        val lines = code.split("\n")
        var currentLineNo = 0
        var hasError = false
        
        try {
            // First pass: Global syntax checks
            validateGlobalJsSyntax(code)

            // Second pass: Step execution line by line
            for (rawLine in lines) {
                currentLineNo++
                val line = rawLine.trim()
                if (line.isEmpty() || line.startsWith("//") || line.startsWith("/*") || line.startsWith("*")) continue

                // Safety sandbox checks
                val lowerLine = line.lowercase()
                if (lowerLine.contains("window.") || lowerLine.contains("document.") || lowerLine.contains("eval(") || lowerLine.contains("require(")) {
                    throw Exception("全域沙箱防護：不開放全域 Document DOM 操作或系統級 eval/require 函式。請專注於 JavaScript 變數與基礎邏輯！")
                }

                // Clean semicolon at the end
                var cleanLine = line
                if (cleanLine.endsWith(";")) {
                    cleanLine = cleanLine.substring(0, cleanLine.length - 1).trim()
                }

                // 1. Check for console.log(...)
                if (cleanLine.startsWith("console.log(") || cleanLine.startsWith("console.log ")) {
                    if (!cleanLine.endsWith(")")) {
                        throw Exception("console.log() 括號未閉合，缺少右括號 ')'")
                    }
                    val innerExpr = cleanLine.substring(12, cleanLine.length - 1).trim()
                    val evaluatedText = parseLogArguments(innerExpr, vars)
                    output.append(evaluatedText).append("\n")
                } 
                // 2. Check for variable declaration: let / const / var x = value
                else if (cleanLine.contains("=")) {
                    val isLet = cleanLine.startsWith("let ")
                    val isConst = cleanLine.startsWith("const ")
                    val isVar = cleanLine.startsWith("var ")

                    if (!isLet && !isConst && !isVar && !cleanLine.contains("==") && !cleanLine.contains("===")) {
                        // Check if assigning to existing var or new undeclared var
                        val rawVar = cleanLine.substringBefore("=").trim()
                        if (!isValidIdentifier(rawVar)) {
                            throw Exception("不合法的變數名稱或賦值語法: `$rawVar`")
                        }
                    }

                    // Strip let/const/var prefix
                    val rawAssignment = when {
                        isLet -> cleanLine.substring(4).trim()
                        isConst -> cleanLine.substring(6).trim()
                        isVar -> cleanLine.substring(4).trim()
                        else -> cleanLine
                    }

                    if (!rawAssignment.contains("=")) {
                        throw Exception("賦值表達式不完整，缺少 '=' 與賦值內容")
                    }

                    val parts = rawAssignment.split("=", limit = 2)
                    val varName = parts[0].trim()
                    val varValueExpr = parts[1].trim()
                    
                    if (varName.isEmpty()) {
                        throw Exception("變數名稱不可為空")
                    }
                    if (varValueExpr.isEmpty()) {
                        throw Exception("變數 `$varName` 未指定賦值內容")
                    }

                    if (isValidIdentifier(varName)) {
                        val evaluatedVal = evaluateExpression(varValueExpr, vars)
                        vars[varName] = evaluatedVal
                    } else {
                        throw Exception("不合法的 JavaScript 變數名稱: `$varName`（變數開頭不能為數字，且不可包含非法符號或關鍵字）")
                    }
                } else if (cleanLine.startsWith("let ") || cleanLine.startsWith("const ") || cleanLine.startsWith("var ")) {
                    // Variable declared without initial value or assignment
                    val parts = cleanLine.split("\\s+".toRegex())
                    val varName = if (parts.size >= 2) parts[1].trim() else ""
                    if (varName.isNotEmpty() && isValidIdentifier(varName)) {
                        vars[varName] = "undefined"
                    } else {
                        throw Exception("不合法的變數宣告語法")
                    }
                } else {
                    // Standalone expression or unknown syntax
                    throw Exception("未知的 JavaScript 語法或無效的指令表達式: `$cleanLine`")
                }
            }
        } catch (e: Exception) {
            hasError = true
            output.append("\n❌ ❌ ❌ 程式模擬器已停止執行 ❌ ❌ ❌\n")
            output.append("【JavaScript 語法/執行錯誤 - 第 $currentLineNo 行】\n")
            output.append("錯誤原因：${e.message}\n")
        }

        val completedOutput = if (output.isEmpty() && !hasError) {
            "（執行完成，無主控台 console.log 輸出。可以嘗試使用 console.log() 語法印出結果！）"
        } else {
            output.toString()
        }

        return Pair(completedOutput, vars)
    }

    private fun validateGlobalJsSyntax(code: String) {
        var parenCount = 0
        var braceCount = 0
        var bracketCount = 0
        var inSingleQuote = false
        var inDoubleQuote = false
        var inBacktick = false

        for (i in 0 until code.length) {
            val char = code[i]
            val prevChar = if (i > 0) code[i - 1] else ' '

            if (char == '\'' && prevChar != '\\' && !inDoubleQuote && !inBacktick) {
                inSingleQuote = !inSingleQuote
            } else if (char == '"' && prevChar != '\\' && !inSingleQuote && !inBacktick) {
                inDoubleQuote = !inDoubleQuote
            } else if (char == '`' && prevChar != '\\' && !inSingleQuote && !inDoubleQuote) {
                inBacktick = !inBacktick
            }

            if (!inSingleQuote && !inDoubleQuote && !inBacktick) {
                when (char) {
                    '(' -> parenCount++
                    ')' -> parenCount--
                    '{' -> braceCount++
                    '}' -> braceCount--
                    '[' -> bracketCount++
                    ']' -> bracketCount--
                }
                if (parenCount < 0) throw Exception("括號多餘或未匹配：發現多餘的右括號 ')'")
                if (braceCount < 0) throw Exception("大括號多餘或未匹配：發現多餘的右大括號 '}'")
                if (bracketCount < 0) throw Exception("中括號多餘或未匹配：發現多餘的右中括號 ']'")
            }
        }

        if (inSingleQuote) throw Exception("單引號 `'` 未成對閉合")
        if (inDoubleQuote) throw Exception("雙引號 `\"` 未成對閉合")
        if (inBacktick) throw Exception("反引號 `` ` `` (Template Literal) 未成對閉合")
        if (parenCount > 0) throw Exception("小括號 `()` 未正確閉合，缺少 $parenCount 個右括號 ')'")
        if (braceCount > 0) throw Exception("大括號 `{}` 未正確閉合，缺少 $braceCount 個右大括號 '}'")
        if (bracketCount > 0) throw Exception("中括號 `[]` 未正確閉合，缺少 $bracketCount 個右中括號 ']'")
    }

    private fun isValidIdentifier(id: String): Boolean {
        if (id.isEmpty()) return false
        val reservedKeywords = setOf("let", "const", "var", "function", "if", "else", "for", "while", "return", "class", "import", "export")
        if (reservedKeywords.contains(id)) return false

        val firstChar = id[0]
        if (!firstChar.isLetter() && firstChar != '_' && firstChar != '$') return false
        for (i in 1 until id.length) {
            val char = id[i]
            if (!char.isLetterOrDigit() && char != '_' && char != '$') return false
        }
        return true
    }

    private fun parseLogArguments(expr: String, vars: Map<String, String>): String {
        // Handle Backtick Template Literals first
        if (expr.startsWith("`") && expr.endsWith("`")) {
            return evaluateTemplateLiteral(expr, vars)
        }

        // Simple splitter by comma, taking care of quotes
        val args = mutableListOf<String>()
        var currentArg = StringBuilder()
        var insideQuotes = false
        var quoteChar = ' '

        for (i in 0 until expr.length) {
            val char = expr[i]
            if ((char == '"' || char == '\'') && (i == 0 || expr[i - 1] != '\\')) {
                if (!insideQuotes) {
                    insideQuotes = true
                    quoteChar = char
                } else if (char == quoteChar) {
                    insideQuotes = false
                } else {
                    currentArg.append(char)
                }
            } else if (char == ',' && !insideQuotes) {
                args.add(currentArg.toString().trim())
                currentArg = StringBuilder()
            } else {
                currentArg.append(char)
            }
        }
        args.add(currentArg.toString().trim())

        return args.map { arg ->
            if (arg.isEmpty()) return@map ""
            
            // Check if it's a quoted string literal
            if ((arg.startsWith("\"") && arg.endsWith("\"")) || (arg.startsWith("'") && arg.endsWith("'"))) {
                arg.substring(1, arg.length - 1)
            } else if (vars.containsKey(arg)) {
                vars[arg] ?: ""
            } else {
                // Try expression evaluation
                try {
                    evaluateExpression(arg, vars)
                } catch (e: Exception) {
                    arg // Fallback
                }
            }
        }.joinToString(" ")
    }

    private fun evaluateTemplateLiteral(expr: String, vars: Map<String, String>): String {
        // Strip backticks
        val template = expr.substring(1, expr.length - 1)
        var result = template
        
        // Find all ${variableName} or ${expression} inside
        val regex = "\\$\\{([^}]+)}".toRegex()
        result = regex.replace(result) { matchResult ->
            val inner = matchResult.groups[1]?.value?.trim() ?: ""
            if (vars.containsKey(inner)) {
                vars[inner] ?: ""
            } else {
                try {
                    evaluateExpression(inner, vars)
                } catch (e: Exception) {
                    inner
                }
            }
        }
        return result
    }

    private fun evaluateExpression(expr: String, vars: Map<String, String>): String {
        var cleanExpr = expr.trim()
        
        // Handles backtick template literals
        if (cleanExpr.startsWith("`") && cleanExpr.endsWith("`")) {
            return evaluateTemplateLiteral(cleanExpr, vars)
        }

        // Remove outer quotes if it's a string assignment
        if ((cleanExpr.startsWith("\"") && cleanExpr.endsWith("\"")) || (cleanExpr.startsWith("'") && cleanExpr.endsWith("'"))) {
            return cleanExpr.substring(1, cleanExpr.length - 1)
        }

        // Replace variable references with their values
        for ((name, value) in vars) {
            val regex = "\\b$name\\b".toRegex()
            cleanExpr = cleanExpr.replace(regex, value)
        }

        // Check strict equality e.g. 5 === "5"
        if (cleanExpr.contains("===")) {
            val parts = cleanExpr.split("===")
            if (parts.size == 2) {
                val left = parts[0].trim()
                val right = parts[1].trim()
                
                val leftIsString = (left.startsWith("\"") && left.endsWith("\"")) || (left.startsWith("'") && left.endsWith("'"))
                val rightIsString = (right.startsWith("\"") && right.endsWith("\"")) || (right.startsWith("'") && right.endsWith("'"))
                
                if (leftIsString != rightIsString) {
                    return "false"
                }
                
                val leftClean = if (leftIsString) left.substring(1, left.length - 1) else left
                val rightClean = if (rightIsString) right.substring(1, right.length - 1) else right
                return (leftClean == rightClean).toString()
            }
        }

        // Check loose equality e.g. 5 == "5"
        if (cleanExpr.contains("==")) {
            val parts = cleanExpr.split("==")
            if (parts.size == 2) {
                val left = parts[0].trim()
                val right = parts[1].trim()
                val leftClean = if ((left.startsWith("\"") && left.endsWith("\"")) || (left.startsWith("'") && left.endsWith("'"))) left.substring(1, left.length - 1) else left
                val rightClean = if ((right.startsWith("\"") && right.endsWith("\"")) || (right.startsWith("'") && right.endsWith("'"))) right.substring(1, right.length - 1) else right
                return (leftClean == rightClean).toString()
            }
        }

        // Try standard calculation or string addition
        if (cleanExpr.contains("+") && (cleanExpr.contains("\"") || cleanExpr.contains("'"))) {
            return cleanExpr.split("+").joinToString("") { part ->
                val trimmed = part.trim()
                if ((trimmed.startsWith("\"") && trimmed.endsWith("\"")) || (trimmed.startsWith("'") && trimmed.endsWith("'"))) {
                    trimmed.substring(1, trimmed.length - 1)
                } else {
                    trimmed
                }
            }
        }

        return try {
            val result = simpleEvaluateMath(cleanExpr)
            if (result % 1.0 == 0.0) {
                result.toInt().toString()
            } else {
                result.toString()
            }
        } catch (e: Exception) {
            cleanExpr
        }
    }

    private fun simpleEvaluateMath(expr: String): Double {
        val clean = expr.replace("\\s".toRegex(), "")
        val operators = listOf("+", "-", "*", "/", "%")
        for (op in operators) {
            val idx = clean.indexOf(op)
            if (idx > 0 && idx < clean.lastIndex) {
                val left = clean.substring(0, idx).toDoubleOrNull()
                val right = clean.substring(idx + 1).toDoubleOrNull()
                if (left != null && right != null) {
                    return when (op) {
                        "+" -> left + right
                        "-" -> left - right
                        "*" -> left * right
                        "/" -> if (right != 0.0) left / right else throw Exception("數學錯誤：除數不可為零 (Division by Zero)")
                        "%" -> left % right
                        else -> 0.0
                    }
                }
            }
        }
        return clean.toDoubleOrNull() ?: throw Exception("無法解析的數值運算: `$expr`")
    }
}

