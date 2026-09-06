package com.example.emulator

object KotlinPlayground {
    fun evaluateCustomKotlinCode(code: String): Pair<String, Map<String, String>> {
        val vars = mutableMapOf<String, String>()
        val output = StringBuilder()
        
        val lines = code.split("\n")
        var currentLineNo = 0
        try {
            var idx = 0
            while (idx < lines.size) {
                currentLineNo = idx + 1
                val rawLine = lines[idx]
                val line = rawLine.trim()
                idx++

                if (line.isEmpty() || line.startsWith("//") || line.startsWith("/*") || line.startsWith("*")) continue

                // Safety checks
                if (line.startsWith("import ")) {
                    throw Exception("安全沙箱防護：本系統不開放外部模組匯入。請專注於 Kotlin 變數、迴圈與基礎邏輯語法！")
                }
                
                val lowerLine = line.lowercase()
                if (lowerLine.contains("java.io") || lowerLine.contains("java.nio") || lowerLine.contains("reflect")) {
                    throw Exception("安全沙箱防護：不支援底層系統存取。請優先使用標準變數與基礎邏輯！")
                }

                // Parse fun main() or other brackets
                if (line.startsWith("fun main") || line == "{" || line == "}" || line.endsWith("{")) {
                    // Check if it's a for loop: for (i in 1..5)
                    if (line.startsWith("for (") || line.startsWith("for(")) {
                        val loopContent = line.substringAfter("(").substringBefore(")").trim()
                        if (loopContent.contains(" in ")) {
                            val varName = loopContent.substringBefore(" in ").trim()
                            val rangeStr = loopContent.substringAfter(" in ").trim()
                            
                            val loopValues = parseRangeOrList(rangeStr, vars)
                            
                            // Collect loop body until matching '}'
                            val bodyLines = mutableListOf<String>()
                            var depth = 1
                            while (idx < lines.size && depth > 0) {
                                val bLine = lines[idx].trim()
                                idx++
                                if (bLine == "{") { depth++ }
                                else if (bLine == "}") { depth--; if (depth == 0) break }
                                else if (bLine.endsWith("{")) { depth++ }
                                bodyLines.add(bLine)
                            }
                            
                            // Execute loop body
                            for (v in loopValues) {
                                vars[varName] = v
                                for (bLine in bodyLines) {
                                    if (bLine.isEmpty() || bLine.startsWith("//")) continue
                                    executeSingleLine(bLine, vars, output)
                                }
                            }
                        }
                    }
                    continue
                }

                executeSingleLine(line, vars, output)
            }
        } catch (e: Exception) {
            output.append("【語法錯誤，位於第 $currentLineNo 行】: ${e.message}\n")
        }

        val completedOutput = if (output.isEmpty()) {
            "（執行完成，無終端機輸出內容。可以嘗試使用 println() 語法印出結果！）"
        } else {
            output.toString()
        }

        return Pair(completedOutput, vars)
    }

    private fun parseRangeOrList(rangeStr: String, vars: Map<String, String>): List<String> {
        val clean = rangeStr.trim()
        if (clean.contains("..")) {
            val parts = clean.split("..")
            val start = evaluateArithmetic(parts[0], vars).toIntOrNull() ?: 1
            val end = evaluateArithmetic(parts[1], vars).toIntOrNull() ?: 5
            return (start..end).map { it.toString() }
        } else if (clean.contains(" until ")) {
            val parts = clean.split(" until ")
            val start = evaluateArithmetic(parts[0], vars).toIntOrNull() ?: 1
            val end = evaluateArithmetic(parts[1], vars).toIntOrNull() ?: 5
            return (start until end).map { it.toString() }
        } else if (clean.startsWith("listOf(") || clean.startsWith("mutableListOf(")) {
            val inner = clean.substringAfter("(").substringBeforeLast(")").trim()
            return inner.split(",").map { evaluateArithmetic(it, vars) }
        } else if (vars.containsKey(clean)) {
            val v = vars[clean] ?: ""
            if (v.startsWith("[") && v.endsWith("]")) {
                return v.substring(1, v.length - 1).split(",").map { it.trim() }
            }
        }
        return listOf("1", "2", "3")
    }

    private fun executeSingleLine(line: String, vars: MutableMap<String, String>, output: StringBuilder) {
        // 1. Check for println/print statement
        if ((line.startsWith("println(") || line.startsWith("print(")) && line.endsWith(")")) {
            val isPrintln = line.startsWith("println(")
            val startIdx = if (isPrintln) 8 else 6
            val innerExpr = line.substring(startIdx, line.length - 1).trim()
            val evaluatedText = parsePrintArguments(innerExpr, vars)
            output.append(evaluatedText).append("\n")
        } 
        // 2. Check for variable assignment: val x = value or var x = value
        else if (line.contains("=")) {
            var cleanLine = line
            if (cleanLine.startsWith("val ")) {
                cleanLine = cleanLine.substring(4).trim()
            } else if (cleanLine.startsWith("var ")) {
                cleanLine = cleanLine.substring(4).trim()
            }
            
            val parts = cleanLine.split("=", limit = 2)
            var varName = parts[0].trim()
            val varValueExpr = parts[1].trim()
            
            // Support explicit types: x: Int = 5
            if (varName.contains(":")) {
                varName = varName.split(":")[0].trim()
            }
            
            if (isValidIdentifier(varName)) {
                val evaluatedVal = evaluateValueExpr(varValueExpr, vars)
                vars[varName] = evaluatedVal
            } else {
                throw Exception("不合法的變數名稱: $varName")
            }
        }
    }

    private fun evaluateValueExpr(expr: String, vars: Map<String, String>): String {
        val clean = expr.trim()

        // Handle Elvis operator: x ?: "default"
        if (clean.contains("?:")) {
            val parts = clean.split("?:", limit = 2)
            val leftVal = evaluateValueExpr(parts[0], vars)
            if (leftVal != "null" && leftVal.isNotEmpty()) {
                return leftVal
            }
            return evaluateValueExpr(parts[1], vars)
        }

        // Handle Safe call or String method: x?.length or x.uppercase()
        if (clean.contains("?.length") || clean.contains(".length")) {
            val varName = clean.substringBefore("?").substringBefore(".").trim()
            val strVal = vars[varName]
            if (strVal == null || strVal == "null") return "null"
            return strVal.length.toString()
        }

        if (clean.contains(".uppercase()")) {
            val varName = clean.substringBefore(".").trim()
            val strVal = vars[varName] ?: evaluateArithmetic(varName, vars)
            return strVal.uppercase()
        }

        if (clean.contains(".lowercase()")) {
            val varName = clean.substringBefore(".").trim()
            val strVal = vars[varName] ?: evaluateArithmetic(varName, vars)
            return strVal.lowercase()
        }

        if (clean.startsWith("listOf(") || clean.startsWith("mutableListOf(")) {
            val inner = clean.substringAfter("(").substringBeforeLast(")").trim()
            val items = inner.split(",").map { evaluateValueExpr(it, vars) }
            return "[${items.joinToString(", ")}]"
        }

        return evaluateArithmetic(clean, vars)
    }

    private fun isValidIdentifier(id: String): Boolean {
        if (id.isEmpty()) return false
        val firstChar = id[0]
        if (!firstChar.isLetter() && firstChar != '_') return false
        for (i in 1 until id.length) {
            val char = id[i]
            if (!char.isLetterOrDigit() && char != '_') return false
        }
        return true
    }

    private fun parsePrintArguments(expr: String, vars: Map<String, String>): String {
        // Handle Kotlin style string templates: "x = $x" or "sum = ${a + b}"
        if (expr.startsWith("\"") && expr.endsWith("\"")) {
            var content = expr.substring(1, expr.length - 1)
            
            // Handle ${a + b} or ${x}
            val templateRegex = "\\$\\{([^}]+)\\}".toRegex()
            content = templateRegex.replace(content) { matchResult ->
                val innerExpr = matchResult.groupValues[1].trim()
                evaluateValueExpr(innerExpr, vars)
            }

            // Replace $name
            for ((name, value) in vars) {
                content = content.replace("$$name", value)
            }
            return content
        }
        
        // Simple expression check
        if (vars.containsKey(expr)) {
            return vars[expr] ?: ""
        }
        
        try {
            return evaluateValueExpr(expr, vars)
        } catch (e: Exception) {
            return expr
        }
    }

    private fun evaluateArithmetic(expr: String, vars: Map<String, String>): String {
        var cleanExpr = expr.trim()
        
        if ((cleanExpr.startsWith("\"") && cleanExpr.endsWith("\"")) || (cleanExpr.startsWith("'") && cleanExpr.endsWith("'"))) {
            return cleanExpr.substring(1, cleanExpr.length - 1)
        }

        if (cleanExpr == "true") return "true"
        if (cleanExpr == "false") return "false"
        if (cleanExpr == "null") return "null"

        for ((name, value) in vars) {
            val regex = "\\b$name\\b".toRegex()
            cleanExpr = cleanExpr.replace(regex, value)
        }

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
            result.toString()
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
                        "/" -> if (right != 0.0) left / right else throw Exception("除以零錯誤")
                        "%" -> left % right
                        else -> 0.0
                    }
                }
            }
        }
        return clean.toDoubleOrNull() ?: throw Exception("無法解析的數學運算: $expr")
    }
}

