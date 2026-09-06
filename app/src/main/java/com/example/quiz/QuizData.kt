package com.example.quiz

import com.example.ui.viewmodel.CourseType

data class QuizQuestion(
    val id: String,
    val question: String,
    val options: List<String>,
    val correctAnswerIndex: Int,
    val explanation: String,
    val difficulty: String // "精簡", "核心", "挑戰"
)

object QuizData {

    val pythonQuestions = listOf(
        QuizQuestion(
            id = "py_q1",
            question = "請問 print(10 % 3) 輸出的結果是什麼？",
            options = listOf("3", "1", "3.33", "0"),
            correctAnswerIndex = 1,
            explanation = "「%」符號指的是「求餘數（Modulus）」。10 除以 3 等於 3 餘 1，因此答案是 1。",
            difficulty = "精簡"
        ),
        QuizQuestion(
            id = "py_q2",
            question = "Python 中，下列哪一個是合法的變數名稱？",
            options = listOf("2_user_name", "user-name", "user_name", "import"),
            correctAnswerIndex = 2,
            explanation = "變數名稱不能以數字開頭 (1 錯誤)、不能含有破折號 (2 錯誤)、不能使用系統保留關鍵字 (4 錯誤)。只有 user_name 是合法的下底線命名。",
            difficulty = "精簡"
        ),
        QuizQuestion(
            id = "py_q3",
            question = "x = [1, 2, 3]，執行 x.append(4) 後，接著印出 len(x) 的值會是多少？",
            options = listOf("3", "4", "5", "會產生 Error 錯誤"),
            correctAnswerIndex = 1,
            explanation = "append() 會往清單(List)的最後部塞入新元素。插入 4 後，清單變成 [1, 2, 3, 4]，所以長度 len(x) 為 4。",
            difficulty = "核心"
        ),
        QuizQuestion(
            id = "py_q4",
            question = "哪一個資料結構使用大括號 `{}` 且內部裝有鍵值對（Key-Value pairs）？",
            options = listOf("List (列表)", "Tuple (元組)", "Dictionary (字典)", "Set (集合)"),
            correctAnswerIndex = 2,
            explanation = "在 Python 中，Dictionary (字典) 使用 {} 定義，且包含鍵值對（如 `{'name': 'Alex'}`）。",
            difficulty = "核心"
        ),
        QuizQuestion(
            id = "py_q5",
            question = "在 Python 當中，以下哪一樣描述是「Tuple（元組）」的正確特徵？",
            options = listOf("它是可任意修改的（Mutable）", "它是不可修改的（Immutable）", "它不允許重複的元素", "它必須使用雙引號宣告"),
            correctAnswerIndex = 1,
            explanation = "Tuple 一經宣告即「不可修改」（Immutable），用於保障資料在流通時不被意外篡改。",
            difficulty = "挑戰"
        ),
        QuizQuestion(
            id = "py_q6",
            question = "下列程式執行完後：\nx = 5\nif x > 3:\n    x = x + 2\nif x > 6:\n    x = x * 2\nprint(x) 的輸出結果是多少？",
            options = listOf("14", "10", "7", "5"),
            correctAnswerIndex = 0,
            explanation = "第一個條件 `x > 3` 成立 (5 > 3)，x 變成 7。第二個條件 `x > 6` (7 > 6) 也成立！x 被 7 * 2，得到 14。",
            difficulty = "挑戰"
        ),
        QuizQuestion(
            id = "py_q7",
            question = "如果要打破（提前結束）一個對應的 `for` 迴圈，該使用哪一個關鍵字？",
            options = listOf("continue", "break", "exit", "stop"),
            correctAnswerIndex = 1,
            explanation = "`break` 關鍵字可以即刻跳出、強制終止當前的迴圈結構；`continue` 是跳過當次並進入下一次循環。",
            difficulty = "核心"
        ),
        QuizQuestion(
            id = "py_q8",
            question = "如果要將一個浮點數字串 `float_str = '3.14'` 轉換成小數，應呼叫哪一個函數？",
            options = listOf("int()", "str()", "float()", "number()"),
            correctAnswerIndex = 2,
            explanation = "使用 `float('3.14')` 即可返回對應的浮點數 3.14。",
            difficulty = "精簡"
        )
    )

    val javascriptQuestions = listOf(
        QuizQuestion(
            id = "js_q1",
            question = "JavaScript 中，`let` 與 `const` 的主要差異是什麼？",
            options = listOf("let 是全域變數，const 是區域變數", "let 宣告後可重新賦值，const 為常數不可重新賦值", "const 只能存數字，let 只能存字串", "兩者完全相同無任何差異"),
            correctAnswerIndex = 1,
            explanation = "`let` 允許變數重新賦值 (Re-assignment)；而 `const` 宣告常數，一旦初始化後就不可重新給值。",
            difficulty = "精簡"
        ),
        QuizQuestion(
            id = "js_q2",
            question = "在 JavaScript 當中，執行 `console.log(\"5\" + 3)` 的輸出結果會是多少？",
            options = listOf("8", "\"53\"", "NaN", "TypeError 錯誤"),
            correctAnswerIndex = 1,
            explanation = "JavaScript 當字串與數字使用 `+` 相加時，會自動觸發隱式型別轉換 (String Coercion)，將數字轉換為字串並進行拼接，結果為 \"53\"。",
            difficulty = "精簡"
        ),
        QuizQuestion(
            id = "js_q3",
            question = "JavaScript 的嚴格比較運算子 `===` 與普通比較 `==` 的差異在於？",
            options = listOf("=== 只比較值，不比較資料型態", "=== 會同時比較「值」與「資料型態」", "== 比較速度比 === 快 10 倍", "=== 是賦值符號而非比較符號"),
            correctAnswerIndex = 1,
            explanation = "`===` (Strict Equality) 必須在「資料型態」與「數值」完全相同時才傳回 true；`==` 則會先試圖轉換型態再進行數值比較。",
            difficulty = "核心"
        ),
        QuizQuestion(
            id = "js_q4",
            question = "Template Literals (樣板字串) 嵌入變數時，使用的符號與語法是哪一個？",
            options = listOf("單引號 ' 與 %s", "雙引號 \" 與 {var}", "反引號 ` 與 \${var}", "大括號 {} 與 #var"),
            correctAnswerIndex = 2,
            explanation = "JavaScript 樣板字串必須包裹在反引號 `` ` `` 中，並透過 `\${expression}` 動態插入變數或運算式。",
            difficulty = "核心"
        ),
        QuizQuestion(
            id = "js_q5",
            question = "執行 `[1, 2, 3].map(x => x * 2)` 運算後，傳回的新陣列會是多少？",
            options = listOf("[1, 2, 3, 2]", "[2, 4, 6]", "6", "[1, 4, 9]"),
            correctAnswerIndex = 1,
            explanation = "`Array.prototype.map()` 會遍歷陣列每一個元素並執行回調函式，對 1, 2, 3 分別乘以 2，傳回新陣列 [2, 4, 6]。",
            difficulty = "核心"
        ),
        QuizQuestion(
            id = "js_q6",
            question = "在 JavaScript 程式碼中，最常用於在主控台 (Console) 印出測試訊息的語法是？",
            options = listOf("System.out.println()", "print()", "console.log()", "echo()"),
            correctAnswerIndex = 2,
            explanation = "`console.log()` 是 JavaScript 與網頁瀏覽器開發者工具最標準的日誌列印函數。",
            difficulty = "精簡"
        ),
        QuizQuestion(
            id = "js_q7",
            question = "宣告一個不用給值且預設代表「無值或未初始化」的 JavaScript 變數，其預設型態值為？",
            options = listOf("null", "undefined", "void", "0"),
            correctAnswerIndex = 1,
            explanation = "在 JS 中，宣告了變數但尚未賦予任何值時，其預設值就是 `undefined`；而 `null` 通常是開發者主動給予的空值標記。",
            difficulty = "挑戰"
        )
    )

    val htmlQuestions = listOf(
        QuizQuestion(
            id = "html_q1",
            question = "HTML 文件中，用來表示網頁「最一級主標題」的標籤是哪一個？",
            options = listOf("<header>", "<h1>", "<title>", "<head>"),
            correctAnswerIndex = 1,
            explanation = "`<h1>` 代表 Heading 1（一級主標題），是網頁中語意權重最高的主標題標籤。",
            difficulty = "精簡"
        ),
        QuizQuestion(
            id = "html_q2",
            question = "在 HTML 中建立「超連結 (Hyperlink)」時，必須搭配哪一個屬性來指定目標網址？",
            options = listOf("src=\"...\"", "href=\"...\"", "link=\"...\"", "target=\"...\""),
            correctAnswerIndex = 1,
            explanation = "`<a>` 標籤代表 Anchor，搭配 `href=\"URL\"` (Hypertext Reference) 指定跳轉連結位置。",
            difficulty = "精簡"
        ),
        QuizQuestion(
            id = "html_q3",
            question = "HTML 的 `<img>` 圖片標籤中，`alt` 屬性的主要作用是什麼？",
            options = listOf("設定圖片的邊框顏色", "當圖片載入失敗或螢幕閱讀器讀取時提供的替代文字說明", "指定圖片的像素寬度", "設定圖片點擊後的跳轉網址"),
            correctAnswerIndex = 1,
            explanation = "`alt` (Alternative Text) 提供無障礙讀屏器與圖片失效時的替代文字，對網頁 accessibility 與 SEO 非常重要。",
            difficulty = "核心"
        ),
        QuizQuestion(
            id = "html_q4",
            question = "若要建立「項目符號點點」的無序列表，正確的 HTML 標籤組合是哪一個？",
            options = listOf("<ol> 包含 <li>", "<ul> 包含 <li>", "<list> 包含 <item>", "<dl> 包含 <dt>"),
            correctAnswerIndex = 1,
            explanation = "`<ul>` 代表 Unordered List（無序列表），內部每一個項目由 `<li>` (List Item) 標籤包覆。",
            difficulty = "核心"
        ),
        QuizQuestion(
            id = "html_q5",
            question = "下列何者是 HTML 註解 (Comment) 的正確寫法？",
            options = listOf("// 這是一行註解", "/* 這是一行註解 */", "<!-- 這是一行註解 -->", "# 這是一行註解"),
            correctAnswerIndex = 2,
            explanation = "HTML 使用 `<!-- 註解內容 -->` 語法包覆網頁註解，瀏覽器不會將其渲染在畫面上。",
            difficulty = "精簡"
        ),
        QuizQuestion(
            id = "html_q6",
            question = "用來建立可供使用者點擊互動按鈕的語意化 HTML 標籤是？",
            options = listOf("<button>", "<input type=\"click\">", "<clickable>", "<action>"),
            correctAnswerIndex = 0,
            explanation = "`<button>` 是標準的 HTML 互動按鈕標籤，可透過 JavaScript 監聽其點擊事件。",
            difficulty = "精簡"
        ),
        QuizQuestion(
            id = "html_q7",
            question = "關於 HTML5 語意化標籤，下列哪一個標籤專門用來包覆網頁頂部的導覽選單（Navigation）？",
            options = listOf("<menu>", "<nav>", "<aside>", "<section>"),
            correctAnswerIndex = 1,
            explanation = "`<nav>` (Navigation) 是 HTML5 推出的語意化標籤，專門用於示範主選單與導覽連結列。",
            difficulty = "挑戰"
        )
    )

    val kotlinQuestions = listOf(
        QuizQuestion(
            id = "kt_q1",
            question = "Kotlin 中，宣告「唯讀不可變 (Immutable)」變數的關鍵字是哪一個？",
            options = listOf("var", "val", "const", "let"),
            correctAnswerIndex = 1,
            explanation = "`val` (Value) 宣告唯讀變數，賦值後不可再修改；`var` (Variable) 則宣告可變變數。",
            difficulty = "精簡"
        ),
        QuizQuestion(
            id = "kt_q2",
            question = "Kotlin 的 Elvis 運算子 `?:` 主要用途是什麼？",
            options = listOf("進行三元邏輯判斷", "當左側表達式為 null 時，返回右側預設值", "進行字串格式化比對", "建立範圍 Range 區間"),
            correctAnswerIndex = 1,
            explanation = "Elvis 運算子 `left ?: right` 表示：若 left 不為 null 則取 left，否則取 right 預設備用值。",
            difficulty = "核心"
        ),
        QuizQuestion(
            id = "kt_q3",
            question = "在 Kotlin 中，哪一種呼叫方式是「Null 安全呼叫 (Safe Call)」？",
            options = listOf("str!!.length", "str?.length", "str->length", "str:length"),
            correctAnswerIndex = 1,
            explanation = "`?.` 為 Safe Call，若變數為 null 則整個表達式安全傳回 null 而不會引發 NullPointerException；`!!` 則是強行斷言不為 null。",
            difficulty = "核心"
        ),
        QuizQuestion(
            id = "kt_q4",
            question = "Kotlin 中印出一行文字並自動於末端換行的標準函數是？",
            options = listOf("System.out.print()", "println()", "console.log()", "echo()"),
            correctAnswerIndex = 1,
            explanation = "`println()` 是 Kotlin 的頂級標準函數，會將內容列印至控制台並在末尾加上換行符號。",
            difficulty = "精簡"
        ),
        QuizQuestion(
            id = "kt_q5",
            question = "宣告一個可以儲存 Null 值的字串變數 (Nullable String)，正確的型態語法是？",
            options = listOf("val str: String", "val str: String?", "val str: NullableString", "val str: String = null"),
            correctAnswerIndex = 1,
            explanation = "Kotlin 的型態系統嚴格區分 Nullable 與 Non-null。在型態後面加上 `?`（如 `String?`）才可接受 null。",
            difficulty = "核心"
        ),
        QuizQuestion(
            id = "kt_q6",
            question = "Kotlin 中建立唯讀不可變列表 (List) 的標準庫函數是哪一個？",
            options = listOf("mutableListOf()", "listOf()", "arrayListOf()", "newList()"),
            correctAnswerIndex = 1,
            explanation = "`listOf()` 建立不可變 List，無法進行 add/remove 修改；若需要修改則應使用 `mutableListOf()`。",
            difficulty = "精簡"
        ),
        QuizQuestion(
            id = "kt_q7",
            question = "在 Kotlin 當中，執行 `for (i in 1..5)` 迴圈會循環幾次？",
            options = listOf("4 次 (1 到 4)", "5 次 (1 到 5)", "6 次", "0 次"),
            correctAnswerIndex = 1,
            explanation = "Kotlin 的 `..` 區間運算子是雙向包含的 (Closed Range)，`1..5` 包含了 1, 2, 3, 4, 5 共 5 次。",
            difficulty = "精簡"
        )
    )

    fun getQuestionsForCourse(courseType: CourseType): List<QuizQuestion> {
        return when (courseType) {
            CourseType.PYTHON -> pythonQuestions
            CourseType.JAVASCRIPT -> javascriptQuestions
            CourseType.HTML -> htmlQuestions
            CourseType.KOTLIN -> kotlinQuestions
        }
    }

    val questions: List<QuizQuestion>
        get() = pythonQuestions
}

