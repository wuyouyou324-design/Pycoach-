package com.example.emulator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

// Representation of a Variable in Memory
data class ProgramVariable(val name: String, val type: String, val value: String)

// Represents a step-by-step visual line execution
data class ExecutionStep(
    val currentLineIndex: Int,
    val variables: List<ProgramVariable>,
    val outputLine: String?
)

// A pre-loaded course script
data class PreloadedScript(
    val title: String,
    val description: String,
    val code: String,
    val steps: List<ExecutionStep>
)

object PythonPlayground {

    val scripts = listOf(
        PreloadedScript(
            title = "變數交換 (Variable Swap)",
            description = "學習如何使用暫存變數 temp 交換兩個變數的值。",
            code = """# 宣告兩個變數
x = 5
y = 10
print("交換前: x =", x, "y =", y)

# 進行交換
temp = x
x = y
y = temp
print("交換後: x =", x, "y =", y)""",
            steps = listOf(
                ExecutionStep(1, emptyList(), "定義 x 為 5"),
                ExecutionStep(2, listOf(ProgramVariable("x", "int", "5")), null),
                ExecutionStep(3, listOf(ProgramVariable("x", "int", "5"), ProgramVariable("y", "int", "10")), null),
                ExecutionStep(4, listOf(ProgramVariable("x", "int", "5"), ProgramVariable("y", "int", "10")), "控制台輸出: 交換前: x = 5 y = 10"),
                ExecutionStep(6, listOf(ProgramVariable("x", "int", "5"), ProgramVariable("y", "int", "10"), ProgramVariable("temp", "int", "5")), "將 x 的值給暫存變數 temp"),
                ExecutionStep(7, listOf(ProgramVariable("x", "int", "10"), ProgramVariable("y", "int", "10"), ProgramVariable("temp", "int", "5")), "將 y 的值覆蓋到 x"),
                ExecutionStep(8, listOf(ProgramVariable("x", "int", "10"), ProgramVariable("y", "int", "5"), ProgramVariable("temp", "int", "5")), "將 temp 的值給 y 完成交換"),
                ExecutionStep(9, listOf(ProgramVariable("x", "int", "10"), ProgramVariable("y", "int", "5"), ProgramVariable("temp", "int", "5")), "控制台輸出: 交換後: x = 10 y = 5")
            )
        ),
        PreloadedScript(
            title = "費波那契數列 (Fibonacci)",
            description = "使用循環輸出前 5 個 Fibonacci 數字 (1, 1, 2, 3, 5)。",
            code = """a = 1
b = 1
print("第 1 個數:", a)
print("第 2 個數:", b)

for i in range(3):
    temp = a + b
    a = b
    b = temp
    print("下一個數:", b)""",
            steps = listOf(
                ExecutionStep(1, emptyList(), "初始化 a = 1"),
                ExecutionStep(2, listOf(ProgramVariable("a", "int", "1")), "初始化 b = 1"),
                ExecutionStep(3, listOf(ProgramVariable("a", "int", "1"), ProgramVariable("b", "int", "1")), "控制台輸出: 第 1 個數: 1"),
                ExecutionStep(4, listOf(ProgramVariable("a", "int", "1"), ProgramVariable("b", "int", "1")), "控制台輸出: 第 2 個數: 1"),
                // Loop i=0
                ExecutionStep(6, listOf(ProgramVariable("a", "int", "1"), ProgramVariable("b", "int", "1"), ProgramVariable("i", "int", "0")), "進入循環，i = 0"),
                ExecutionStep(7, listOf(ProgramVariable("a", "int", "1"), ProgramVariable("b", "int", "1"), ProgramVariable("i", "int", "0"), ProgramVariable("temp", "int", "2")), "計算 a + b 並存入 temp = 2"),
                ExecutionStep(8, listOf(ProgramVariable("a", "int", "1"), ProgramVariable("b", "int", "1"), ProgramVariable("i", "int", "0"), ProgramVariable("temp", "int", "2")), "a 設定為前一個 b = 1"),
                ExecutionStep(9, listOf(ProgramVariable("a", "int", "1"), ProgramVariable("b", "int", "2"), ProgramVariable("i", "int", "0"), ProgramVariable("temp", "int", "2")), "b 設定為 temp = 2"),
                ExecutionStep(10, listOf(ProgramVariable("a", "int", "1"), ProgramVariable("b", "int", "2"), ProgramVariable("i", "int", "0"), ProgramVariable("temp", "int", "2")), "控制台輸出: 下一個數: 2"),
                // Loop i=1
                ExecutionStep(6, listOf(ProgramVariable("a", "int", "1"), ProgramVariable("b", "int", "2"), ProgramVariable("i", "int", "1"), ProgramVariable("temp", "int", "2")), "循環，i = 1"),
                ExecutionStep(7, listOf(ProgramVariable("a", "int", "1"), ProgramVariable("b", "int", "2"), ProgramVariable("i", "int", "1"), ProgramVariable("temp", "int", "3")), "計算 a + b 並存入 temp = 3"),
                ExecutionStep(8, listOf(ProgramVariable("a", "int", "2"), ProgramVariable("b", "int", "2"), ProgramVariable("i", "int", "1"), ProgramVariable("temp", "int", "3")), "a 設定為前一個 b = 2"),
                ExecutionStep(9, listOf(ProgramVariable("a", "int", "2"), ProgramVariable("b", "int", "3"), ProgramVariable("i", "int", "1"), ProgramVariable("temp", "int", "3")), "b 設定為 temp = 3"),
                ExecutionStep(10, listOf(ProgramVariable("a", "int", "2"), ProgramVariable("b", "int", "3"), ProgramVariable("i", "int", "1"), ProgramVariable("temp", "int", "3")), "控制台輸出: 下一個數: 3"),
                // Loop i=2
                ExecutionStep(6, listOf(ProgramVariable("a", "int", "2"), ProgramVariable("b", "int", "3"), ProgramVariable("i", "int", "2"), ProgramVariable("temp", "int", "3")), "循環，i = 2 (最後一次)"),
                ExecutionStep(7, listOf(ProgramVariable("a", "int", "2"), ProgramVariable("b", "int", "3"), ProgramVariable("i", "int", "2"), ProgramVariable("temp", "int", "5")), "計算 a + b 並存入 temp = 5"),
                ExecutionStep(8, listOf(ProgramVariable("a", "int", "3"), ProgramVariable("b", "int", "3"), ProgramVariable("i", "int", "2"), ProgramVariable("temp", "int", "5")), "a 設定為前一個 b = 3"),
                ExecutionStep(9, listOf(ProgramVariable("a", "int", "3"), ProgramVariable("b", "int", "5"), ProgramVariable("i", "int", "2"), ProgramVariable("temp", "int", "5")), "b 設定為 temp = 5"),
                ExecutionStep(10, listOf(ProgramVariable("a", "int", "3"), ProgramVariable("b", "int", "5"), ProgramVariable("i", "int", "2"), ProgramVariable("temp", "int", "5")), "控制台輸出: 下一個數: 5")
            )
        ),
        PreloadedScript(
            title = "FizzBuzz 趣味遊戲",
            description = "如果數字能被 3 整除輸出 Fizz，被 5 整除輸出 Buzz；同時整除輸出 FizzBuzz 否則輸出數字本身。",
            code = """for num in range(1, 6):
    if num % 3 == 0 and num % 5 == 0:
        print("FizzBuzz")
    elif num % 3 == 0:
        print("Fizz")
    elif num % 5 == 0:
        print("Buzz")
    else:
        print(num)""",
            steps = listOf(
                ExecutionStep(1, emptyList(), "定義循環 range(1, 6)..."),
                ExecutionStep(2, listOf(ProgramVariable("num", "int", "1")), "當 num = 1，測試是否可被3和5整除"),
                ExecutionStep(8, listOf(ProgramVariable("num", "int", "1")), "1 均無法整除，執行 else"),
                ExecutionStep(9, listOf(ProgramVariable("num", "int", "1")), "控制台輸出: 1"),
                ExecutionStep(1, listOf(ProgramVariable("num", "int", "1")), "當 num = 2..."),
                ExecutionStep(8, listOf(ProgramVariable("num", "int", "2")), "2 均無法整除，執行 else"),
                ExecutionStep(9, listOf(ProgramVariable("num", "int", "2")), "控制台輸出: 2"),
                ExecutionStep(1, listOf(ProgramVariable("num", "int", "2")), "當 num = 3..."),
                ExecutionStep(4, listOf(ProgramVariable("num", "int", "3")), "3 可被 3 整除，命中 elif (num % 3 == 0)"),
                ExecutionStep(5, listOf(ProgramVariable("num", "int", "3")), "控制台輸出: Fizz"),
                ExecutionStep(1, listOf(ProgramVariable("num", "int", "3")), "當 num = 4..."),
                ExecutionStep(8, listOf(ProgramVariable("num", "int", "4")), "4 均無法整除，執行 else"),
                ExecutionStep(9, listOf(ProgramVariable("num", "int", "4")), "控制台輸出: 4"),
                ExecutionStep(1, listOf(ProgramVariable("num", "int", "4")), "當 num = 5..."),
                ExecutionStep(6, listOf(ProgramVariable("num", "int", "5")), "5 可被 5 整除，命中 elif (num % 5 == 0)"),
                ExecutionStep(7, listOf(ProgramVariable("num", "int", "5")), "控制台輸出: Buzz")
            )
        )
    )

    // A real runtime evaluator of custom linear python code written by users
    fun evaluateCustomPythonCode(code: String): Pair<String, Map<String, String>> {
        val vars = mutableMapOf<String, String>()
        val output = StringBuilder()
        
        val lines = code.split("\n")
        var currentLineNo = 0
        try {
            for (rawLine in lines) {
                currentLineNo++
                val line = rawLine.trim()
                if (line.isEmpty() || line.startsWith("#")) continue

                // 安全沙箱審查：防止高風險操作與未授權匯入
                if (line.startsWith("import ") || line.startsWith("from ")) {
                    val imported = line.substringAfter("import").substringBefore("as").trim()
                    throw Exception("安全沙箱防護：本系統不開放 `${imported}` 模組匯入。為確保教學環境安全性，不允許執行外部系統存取與檔案讀寫。請專注於 Python 變數、迴圈與基礎邏輯語法！")
                }
                
                val lowerLine = line.lowercase()
                if (lowerLine.contains("open(") || lowerLine.contains("eval(") || lowerLine.contains("exec(")) {
                    throw Exception("安全沙箱防護：不支援底層系統或動態執行調用（例如 `open()`, `eval()`, `exec()`）。請優先使用標準變數指派與基礎邏輯！")
                }

                // 1. Check for print statement
                if (line.startsWith("print(") && line.endsWith(")")) {
                    val innerExpr = line.substring(6, line.length - 1).trim()
                    // Handles quotes strings, variables, or expressions
                    val evaluatedText = parsePrintArguments(innerExpr, vars)
                    output.append(evaluatedText).append("\n")
                } 
                // 2. Check for variable assignment: x = value
                else if (line.contains("=")) {
                    val parts = line.split("=", limit = 2)
                    val varName = parts[0].trim()
                    val varValueExpr = parts[1].trim()
                    
                    if (isValidIdentifier(varName)) {
                        val evaluatedVal = evaluateArithmetic(varValueExpr, vars)
                        vars[varName] = evaluatedVal
                    } else {
                        throw Exception("不合法的變數名稱: $varName")
                    }
                } else {
                    // Let's ignore complex indentation or other statements to keep the engine from crashing on random input,
                    // but provide general feedback.
                }
            }
        } catch (e: Exception) {
            output.append("【語法錯誤，位於第 $currentLineNo 行】: ${e.message}\n")
        }

        val completedOutput = if (output.isEmpty()) {
            "（執行完成，無終端機輸出內容。可以嘗試使用 print() 語法印出結果！）"
        } else {
            output.toString()
        }

        return Pair(completedOutput, vars)
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
                // Try arithmetic evaluation
                try {
                    evaluateArithmetic(arg, vars)
                } catch (e: Exception) {
                    arg // Fallback
                }
            }
        }.joinToString(" ")
    }

    private fun evaluateArithmetic(expr: String, vars: Map<String, String>): String {
        var cleanExpr = expr.trim()
        
        // Remove outer quotes if it's a string assignment
        if ((cleanExpr.startsWith("\"") && cleanExpr.endsWith("\"")) || (cleanExpr.startsWith("'") && cleanExpr.endsWith("'"))) {
            return cleanExpr.substring(1, cleanExpr.length - 1)
        }

        // Check boolean literals
        if (cleanExpr == "True") return "True"
        if (cleanExpr == "False") return "False"

        // Replace variable references with their values
        for ((name, value) in vars) {
            // Regex replace to match variable bound on whole word boundaries only
            val regex = "\\b$name\\b".toRegex()
            cleanExpr = cleanExpr.replace(regex, value)
        }

        // Try standard calculation or string addition
        if (cleanExpr.contains("+") && (cleanExpr.contains("\"") || cleanExpr.contains("'"))) {
            // String concatenation
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
            // Simple integer math evaluator for + - * / %
            val result = simpleEvaluateMath(cleanExpr)
            result.toString()
        } catch (e: Exception) {
            // Fail safe, return expr as a string literal
            cleanExpr
        }
    }

    private fun simpleEvaluateMath(expr: String): Double {
        // Regex patterns to find basic math
        val clean = expr.replace("\\s".toRegex(), "")
        
        // Let's do simple calculation: Left operand [+-/*%] Right operand
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
        
        // If it's a simple number
        return clean.toDoubleOrNull() ?: throw Exception("無法解析的數學運算: $expr")
    }
}
