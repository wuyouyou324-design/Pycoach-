package com.example.ui

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.TextRange

data class StyleRange(val start: Int, val end: Int, val style: SpanStyle)

object CodeEditorHelper {

    fun highlightPython(text: String): AnnotatedString {
        val ranges = mutableListOf<StyleRange>()
        
        fun overlaps(start: Int, end: Int): Boolean {
            return ranges.any { existing ->
                maxOf(start, existing.start) < minOf(end, existing.end)
            }
        }
        
        fun addRangeSafely(start: Int, end: Int, style: SpanStyle) {
            if (!overlaps(start, end)) {
                ranges.add(StyleRange(start, end, style))
            }
        }

        // 1. Triple-quoted strings (match first to avoid single/double quote interference)
        val tripleQuoteRegex = "\"\"\"[\\s\\S]*?\"\"\"|'''[\\s\\S]*?'''".toRegex()
        tripleQuoteRegex.findAll(text).forEach { match ->
            ranges.add(StyleRange(match.range.first, match.range.last + 1, SpanStyle(color = Color(0xFF6A9955)))) // green comments/strings
        }

        // 2. Single-line strings
        val singleQuoteRegex = "\"(?:\\\\.|[^\"\\\\])*\"|'(?:\\\\.|[^'\\\\])*'".toRegex()
        singleQuoteRegex.findAll(text).forEach { match ->
            addRangeSafely(match.range.first, match.range.last + 1, SpanStyle(color = Color(0xFFCE9178))) // warm/ginger string color
        }

        // 3. Comments (from # to end of line)
        val commentRegex = "#.*".toRegex()
        commentRegex.findAll(text).forEach { match ->
            addRangeSafely(match.range.first, match.range.last + 1, SpanStyle(color = Color(0xFF6A9955), fontStyle = FontStyle.Italic)) // green comments
        }

        // 4. Decorators (@decorator)
        val decoratorRegex = "@[a-zA-Z_][a-zA-Z0-9_]*".toRegex()
        decoratorRegex.findAll(text).forEach { match ->
            addRangeSafely(match.range.first, match.range.last + 1, SpanStyle(color = Color(0xFFDCDCAA)))
        }

        // 5. Keywords
        val keywordRegex = "\\b(def|class|if|else|elif|while|for|in|return|import|from|pass|try|except|raise|and|or|not|is|as|lambda|assert|break|continue|yield|global|nonlocal|with|del)\\b".toRegex()
        keywordRegex.findAll(text).forEach { match ->
            addRangeSafely(match.range.first, match.range.last + 1, SpanStyle(color = Color(0xFFC586C0), fontWeight = FontWeight.Bold)) // magenta keyword
        }

        // 6. Built-ins / functions
        val builtInRegex = "\\b(print|len|range|str|int|float|list|dict|tuple|set|super|open|type|abs|all|any|dir|enumerate|zip|isinstance)\\b".toRegex()
        builtInRegex.findAll(text).forEach { match ->
            addRangeSafely(match.range.first, match.range.last + 1, SpanStyle(color = Color(0xFF569CD6))) // blue builtins
        }

        // 7. Numbers
        val numberRegex = "\\b\\d+(?:\\.\\d+)?\\b".toRegex()
        numberRegex.findAll(text).forEach { match ->
            addRangeSafely(match.range.first, match.range.last + 1, SpanStyle(color = Color(0xFFB5CEA8))) // pale green numbers
        }

        // 8. Class names from class declaration
        val classNameRegex = "class\\s+([a-zA-Z_][a-zA-Z0-9_]*)".toRegex()
        classNameRegex.findAll(text).forEach { match ->
            val group = match.groups[1]
            if (group != null) {
                addRangeSafely(group.range.first, group.range.last + 1, SpanStyle(color = Color(0xFF4EC9B0), fontWeight = FontWeight.Bold)) // teal class
            }
        }

        // 9. Function names from def declaration
        val funcNameRegex = "def\\s+([a-zA-Z_][a-zA-Z0-9_]*)".toRegex()
        funcNameRegex.findAll(text).forEach { match ->
            val group = match.groups[1]
            if (group != null) {
                addRangeSafely(group.range.first, group.range.last + 1, SpanStyle(color = Color(0xFFDCDCAA))) // yellow function
            }
        }

        return buildAnnotatedString {
            append(text)
            for (range in ranges) {
                addStyle(range.style, range.start, range.end)
            }
        }
    }

    fun highlightHtml(text: String): AnnotatedString {
        val ranges = mutableListOf<StyleRange>()
        
        fun overlaps(start: Int, end: Int): Boolean {
            return ranges.any { existing ->
                maxOf(start, existing.start) < minOf(end, existing.end)
            }
        }
        
        fun addRangeSafely(start: Int, end: Int, style: SpanStyle) {
            if (!overlaps(start, end)) {
                ranges.add(StyleRange(start, end, style))
            }
        }

        // 1. Comments
        val commentRegex = "<!--[\\s\\S]*?-->".toRegex()
        commentRegex.findAll(text).forEach { match ->
            ranges.add(StyleRange(match.range.first, match.range.last + 1, SpanStyle(color = Color(0xFF6A9955), fontStyle = FontStyle.Italic)))
        }

        // 2. Attribute values (quoted values within tags)
        val attrValueRegex = "=\\s*\"[^\"]*\"|=\\s*'[^']*'".toRegex()
        attrValueRegex.findAll(text).forEach { match ->
            val startIndex = match.value.indexOfAny(charArrayOf('"', '\''))
            if (startIndex != -1) {
                val valStart = match.range.first + startIndex
                addRangeSafely(valStart, match.range.last + 1, SpanStyle(color = Color(0xFFCE9178)))
            }
        }

        // 3. Tag names in tags (e.g. <div, </div, <p, </p)
        val tagNameRegex = "</?([a-zA-Z0-9:-]+)".toRegex()
        tagNameRegex.findAll(text).forEach { match ->
            val group = match.groups[1]
            if (group != null) {
                addRangeSafely(group.range.first, group.range.last + 1, SpanStyle(color = Color(0xFFE06C75), fontWeight = FontWeight.Bold))
            }
        }

        // 4. Tag brackets
        val bracketsRegex = "</?|/?>".toRegex()
        bracketsRegex.findAll(text).forEach { match ->
            addRangeSafely(match.range.first, match.range.last + 1, SpanStyle(color = Color(0xFF808080)))
        }

        // 5. Attribute names
        val attrNameRegex = "\\b(class|id|style|src|href|type|value|placeholder|target|name|rel|width|height|alt|lang|charset|onclick)\\b".toRegex()
        attrNameRegex.findAll(text).forEach { match ->
            addRangeSafely(match.range.first, match.range.last + 1, SpanStyle(color = Color(0xFF9CDCF0)))
        }

        return buildAnnotatedString {
            append(text)
            for (range in ranges) {
                addStyle(range.style, range.start, range.end)
            }
        }
    }

    fun highlightJavascript(text: String): AnnotatedString {
        val ranges = mutableListOf<StyleRange>()
        
        fun overlaps(start: Int, end: Int): Boolean {
            return ranges.any { existing ->
                maxOf(start, existing.start) < minOf(end, existing.end)
            }
        }
        
        fun addRangeSafely(start: Int, end: Int, style: SpanStyle) {
            if (!overlaps(start, end)) {
                ranges.add(StyleRange(start, end, style))
            }
        }

        // 1. Multi-line comments
        val multiLineCommentRegex = "/\\*[\\s\\S]*?\\*/".toRegex()
        multiLineCommentRegex.findAll(text).forEach { match ->
            ranges.add(StyleRange(match.range.first, match.range.last + 1, SpanStyle(color = Color(0xFF6A9955), fontStyle = FontStyle.Italic)))
        }

        // 2. Single-line comments
        val commentRegex = "//.*".toRegex()
        commentRegex.findAll(text).forEach { match ->
            addRangeSafely(match.range.first, match.range.last + 1, SpanStyle(color = Color(0xFF6A9955), fontStyle = FontStyle.Italic))
        }

        // 3. String literals (double, single, template quotes)
        val stringRegex = "\"(?:\\\\.|[^\"\\\\])*\"|'(?:\\\\.|[^'\\\\])*'|`(?:\\\\.|[^`\\\\])*`".toRegex()
        stringRegex.findAll(text).forEach { match ->
            addRangeSafely(match.range.first, match.range.last + 1, SpanStyle(color = Color(0xFFCE9178)))
        }

        // 4. Keywords
        val keywordRegex = "\\b(const|let|var|function|return|if|else|while|for|in|of|import|export|from|class|extends|super|new|this|try|catch|finally|throw|async|await|typeof|instanceof|switch|case|default|break|continue)\\b".toRegex()
        keywordRegex.findAll(text).forEach { match ->
            addRangeSafely(match.range.first, match.range.last + 1, SpanStyle(color = Color(0xFFC586C0), fontWeight = FontWeight.Bold))
        }

        // 5. Built-ins
        val builtInRegex = "\\b(console|log|warn|error|document|window|Math|Array|Object|String|Number|Boolean|JSON|parse|stringify|setTimeout|setInterval|alert)\\b".toRegex()
        builtInRegex.findAll(text).forEach { match ->
            addRangeSafely(match.range.first, match.range.last + 1, SpanStyle(color = Color(0xFF569CD6)))
        }

        // 6. Numbers
        val numberRegex = "\\b\\d+(?:\\.\\d+)?\\b".toRegex()
        numberRegex.findAll(text).forEach { match ->
            addRangeSafely(match.range.first, match.range.last + 1, SpanStyle(color = Color(0xFFB5CEA8)))
        }

        return buildAnnotatedString {
            append(text)
            for (range in ranges) {
                addStyle(range.style, range.start, range.end)
            }
        }
    }

    fun handleCodeChangeWithAutoIndent(
        oldValue: TextFieldValue,
        newValue: TextFieldValue,
        language: String
    ): TextFieldValue {
        val oldText = oldValue.text
        val newText = newValue.text
        
        // Check if length increased by exactly 1 character and cursor moved exactly by 1
        if (newText.length == oldText.length + 1 && newValue.selection.start == oldValue.selection.start + 1) {
            val insertedCharIdx = oldValue.selection.start
            if (newText[insertedCharIdx] == '\n') {
                // Find previous line content
                val textBeforeCursor = oldText.substring(0, insertedCharIdx)
                val linesBefore = textBeforeCursor.split("\n")
                val lastLine = linesBefore.lastOrNull() ?: ""
                
                // Keep the exact leading spaces of the previous line
                val baseIndent = lastLine.takeWhile { it == ' ' || it == '\t' }
                
                // Add nested indent block if needed
                val trimmedLastLine = lastLine.trim()
                val extraIndent = when {
                    language.lowercase() == "python" && trimmedLastLine.endsWith(":") -> "    "
                    language.lowercase() == "html" && (trimmedLastLine.endsWith(">") && !trimmedLastLine.startsWith("</") && !trimmedLastLine.endsWith("/>") && !trimmedLastLine.contains("</")) -> "    "
                    language.lowercase() == "javascript" && trimmedLastLine.endsWith("{") -> "    "
                    else -> ""
                }
                
                val autoIndentStr = baseIndent + extraIndent
                
                if (autoIndentStr.isNotEmpty()) {
                    val modifiedText = newText.substring(0, insertedCharIdx + 1) + 
                                      autoIndentStr + 
                                      newText.substring(insertedCharIdx + 1)
                    
                    val newCursorPos = insertedCharIdx + 1 + autoIndentStr.length
                    
                    return newValue.copy(
                        text = modifiedText,
                        selection = TextRange(newCursorPos)
                    )
                }
            }
        }
        return newValue
    }
}

class CodeHighlightTransformation(val language: String, val errorLine: Int? = null) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val highlighted = when (language.lowercase()) {
            "python" -> CodeEditorHelper.highlightPython(text.text)
            "html" -> CodeEditorHelper.highlightHtml(text.text)
            "javascript" -> CodeEditorHelper.highlightJavascript(text.text)
            else -> text
        }
        
        val finalAnnotated = if (errorLine != null && errorLine > 0) {
            val rawText = text.text
            val lines = rawText.split("\n")
            if (errorLine <= lines.size) {
                var startChar = 0
                for (i in 0 until errorLine - 1) {
                    startChar += lines[i].length + 1
                }
                val endChar = minOf(rawText.length, startChar + lines[errorLine - 1].length)
                
                buildAnnotatedString {
                    append(highlighted)
                    addStyle(
                        style = SpanStyle(
                            background = Color(0x33C62828), // 33% transparent soft warning dark red
                            fontWeight = FontWeight.Bold
                        ),
                        start = startChar,
                        end = endChar
                    )
                }
            } else {
                highlighted
            }
        } else {
            highlighted
        }
        
        return TransformedText(finalAnnotated, OffsetMapping.Identity)
    }
}
