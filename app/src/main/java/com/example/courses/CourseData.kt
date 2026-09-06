package com.example.courses

data class CourseLesson(
    val id: String,
    val title: String,
    val subtitle: String,
    val category: String,
    val readTimeMinutes: Int,
    val description: String,
    val explanationHtmlCode: String, // formatted explanation
    val codeSnippet: String,
    val interactiveQuizQuestion: String,
    val quizAnswers: List<String>,
    val correctQuizAnswerIndex: Int,
    val quizExplanation: String,
    val xpReward: Int = 20
)

object CourseData {
    val lessons get() = pythonLessons

    val pythonLessons = listOf(
        CourseLesson(
            id = "lesson_1",
            title = "1. 認識程式的運作",
            subtitle = "開篇：程式指令是如何在電腦中被執行的？",
            category = "起步觀念",
            readTimeMinutes = 2,
            description = "探索電腦如何像一個超高速但十分木訥的小學徒，一行行聽從你的編排。這比寫代碼本身更重要！",
            explanationHtmlCode = "程式（Program）其實就是一連串給電腦的指令清單。電腦的心臟也就是中央處理器（CPU）非常強大，但它並不懂人類的語言或想法，它只知道按照既定的軌跡依序處理任務。\n\n在開始敲鍵盤之前，你必須記住電腦的兩個黃金執行特徵：\n1. **由上而下逐行執行**：電腦就像你讀書一樣，必須先把第一行讀完並徹底執行後，才會繼續看第二行，絕對不會跳著讀。\n2. **完全字面解讀**：電腦不會揣摩你的意思。你寫了什麼，它就執行什麼；如果寫錯字，它不會自動幫你糾正，而是會直接卡住或報錯。\n\n理解這一點是我們成為程式工程師、用邏輯控制世界的第一步！",
            codeSnippet = "# 這是觀念解釋區\n# 電腦載入本檔案後，會由上往下，一行一行閱讀這些指令！\n# 後面的單元我們就會動手操作哦！",
            interactiveQuizQuestion = "當電腦在運行一組 Python 程式清單時，它的基本讀取與執行順序是什麼？",
            quizAnswers = listOf("隨機挑選合適的指令執行", "由上而下逐行依序執行", "全部一起讀入並在一瞬間隨機發生", "由下往上倒過來執行"),
            correctQuizAnswerIndex = 1,
            quizExplanation = "電腦執行指令的基本邏輯是極度死板的，必須由上往下依序執行，上一行沒跑完就不會到下一行！"
        ),
        CourseLesson(
            id = "lesson_2",
            title = "2. 直譯器與語法規則",
            subtitle = "零障礙理解：什麼是直譯式語言？",
            category = "運作機制",
            readTimeMinutes = 2,
            description = "Python 是直譯式語言。這代表什麼？為什麼它如此平易近人又富有彈性？",
            explanationHtmlCode = "電腦的核心只認識由「0 與 1」組成的機器語言。那麼我們寫的 Python 英文字母，電腦是怎麼看懂的呢？\n\n答案是：**直譯器（Interpreter）**。它就像是隨行的即時口譯官！\n\n當你點擊執行 Python 時，直譯器會立刻把你當前寫的這一行代碼（例如變數、數學操作）即時翻譯成電腦聽得懂的機器指令，然後電腦就立刻跑這一行。直譯的好處是：\n- **即寫即跑**：不需要冗長的先行編譯（Compile）等待，改一個字就能馬上在畫面上觀看結果！\n- **報錯精準**：如果第 10 行有錯，前 9 行依然會正常進行，直譯器直到第 10 行才會舉紅牌停下來，讓除錯變得直覺又輕鬆。",
            codeSnippet = "# 概念示範\n# Python 直譯器會在此刻解讀這行文字\n# 稍後在第 5 單元起，我們會看見直譯器主動幫你列印內容！",
            interactiveQuizQuestion = "為什麼 Python 被稱為「直譯式語言」？",
            quizAnswers = listOf("它需要被翻譯成網頁才能運行", "它不需要任何人翻譯，電腦本來就懂英文", "它由直譯器逐行口譯並立刻給電腦執行", "它是指必須由口頭念出來的指令"),
            correctQuizAnswerIndex = 2,
            quizExplanation = "直譯式語言（Interpreted language）是指在執行時才由直譯器將程式碼逐行翻譯成機器碼並立即執行，開發效率極高！"
        ),
        CourseLesson(
            id = "lesson_3",
            title = "3. 程式中的神秘符號",
            subtitle = "那些在代碼周圍閃爍的特別標點",
            category = "符號大觀",
            readTimeMinutes = 3,
            description = "為什麼程式裡有一大堆圓括號、引號、冒號跟空白？了解它們背後的象徵意義。",
            explanationHtmlCode = "寫程式最容易卡關的，常常不是艱深的演算法，而是不小心漏敲了看似微不足道的符號。以下四個神級標點，請深深烙印在腦海：\n\n1. **小括號 `()`**：用來包住指令要處理的「東西」。例如要排隊的人名，或是要交給電腦處理的資料，都得用它包裝。\n2. **雙引號 `\"\"`**：用來框住「真實的英文或中文文字」，告訴電腦這是一串純文字（字串），不是指令，請照原樣處理。\n3. **冒號 `:`**：這是在宣告「重頭戲在後頭！」，後面即將帶出一整組連續執行的代碼大軍（例如條件符合時要做的事）。\n4. **縮排（空白）**：在行開頭敲空白，就像是把子任務塞入主任務中，讓程式分清主從關係。\n\n這些符號只要稍微放錯位置，電腦這個老實的小學徒就會完全看不懂而罷工喔！",
            codeSnippet = "# 符號地圖指南：\n# 1. 引號 \"字串\" -> 把純文字包起來\n# 2. 括號 () -> 傳遞指令參數的包裝盒\n# 3. 冒號 : 和縮排 -> 開啟一個新的從屬區塊",
            interactiveQuizQuestion = "在 Python 中，如果我們想要告訴電腦某一串文字是「純描述性的文字」、不需要當作指令解讀，應使用什麼符號來包裹它？",
            quizAnswers = listOf("小括號 ()", "雙引號 \"\" 或單引號 ''", "角括號 <>", "井字號 #"),
            correctQuizAnswerIndex = 1,
            quizExplanation = "字串必須被雙引號瓦解或單引號包覆，否則直譯器會誤以為這是一個程式變數或指令名稱，進而發生未定義錯誤！"
        ),
        CourseLesson(
            id = "lesson_4",
            title = "4. 漏了一個符號會怎樣？",
            subtitle = "致命的語法錯誤：SyntaxError",
            category = "防雷除錯",
            readTimeMinutes = 3,
            description = "帶你看懂程式最常見的罷工警告——語法錯誤。如果我們故意出錯，會發生什麼事？",
            explanationHtmlCode = "當你因為拼寫錯誤、括號沒成對、或是漏了冒號時，執行程式就會看到紅通通的警告：`SyntaxError`（語法錯誤）。\n\n這就像是跟一個只懂完美中文的人寫漏字或倒裝，他完全無法解讀。最經典的例子：\n- `print(\"Hello\"` -> 遺失了右邊的右括號！電腦無法知道這個動作究竟在何處結束。\n- `if score > 60` -> 條件判定後面漏了攸關指令區塊的冒號 `:`！\n\n**漏掉一個符號，整行、甚至整個程式都沒法啟動。** 但別怕，直譯器會貼心地標出是哪一個檔案的哪一行出問題，只要耐心地照提示補上，就能輕鬆修復！",
            codeSnippet = "# 語法報錯 (SyntaxError) 的示意：\n# my_text = \"忘記在右邊加上引號，這就是典型的語法缺失！",
            interactiveQuizQuestion = "當程式在啟動或執行時扔出了「SyntaxError」錯誤，代表什麼意思？",
            quizAnswers = listOf("電腦容量不足，需要重開機", "語法規則寫錯，有缺失的符號、括號或格式不符", "電腦中毒了，不安全", "計算出的數學答案不正確"),
            correctQuizAnswerIndex = 1,
            quizExplanation = "Syntax 代表語法結構，拼寫錯誤、缺少成對引號、括號或中斷符，都會拋出 `SyntaxError` 使程式無法解析。"
        ),
        CourseLesson(
            id = "lesson_5",
            title = "5. 起手式！初顯身手 print()",
            subtitle = "第一句魔法：將結果列印到螢幕上！",
            category = "起手代碼",
            readTimeMinutes = 3,
            description = "現在，我們終於要寫出你的第一行 Python 程式碼了！運用 print 函數大聲向世界問好！",
            explanationHtmlCode = "期待已久！現在正式來寫出並執行程式碼！\n\n要在畫面上打出我們想要的內容，必須使用 `print(...)` 這個全世界最著名的指令。它是 Python 中負責「顯示」或「輸出」的王牌工具。\n\n**寫法解析：**\n`print(\"哈囉，世界！\")`\n- `print` 是指令名稱。\n- 它是函式，所以一定要有 `()` 生效。\n- 因為我們要列印文字，所以把文字外面罩上了雙引號 `\"\"`。\n\n在右側代碼練習區，你可以看到真實的指令。點下執行，就能在模擬終端機上看到結果輸出囉！",
            codeSnippet = "# 你的第一行正式程式碼！\nprint(\"哈囉！歡迎來到 Pycoach 工程師養成班！\")\nprint(\"注意左邊與右邊都有完整的雙引號跟括號對齊。\")",
            interactiveQuizQuestion = "以下哪一行程式碼，能符合 Python 語法並完美在終端機列印出「Python」字樣？",
            quizAnswers = listOf("print(Python)", "print \"Python\"", "print(\"Python\")", "show(\"Python\")"),
            correctQuizAnswerIndex = 2,
            quizExplanation = "在 Python 3 中，`print` 是一個函式，必須使用小括號包覆參數，且文字「Python」必須以雙引號（或單引號）括起來！"
        ),
        CourseLesson(
            id = "lesson_6",
            title = "6. 變數——數據容器的名字",
            subtitle = "把數據收納到有名稱的抽屜裡！",
            category = "基礎觀念",
            readTimeMinutes = 3,
            description = "如果程式只能一直 print 純文字，就顯得有點太呆了。學習如何使用變數來儲存隨時會變動的資訊。",
            explanationHtmlCode = "在寫程式時，你需要處理成千上萬的資料（例如玩家的分數、商品價格、使用者名稱）。我們需要將這些資料妥善存起來，以便稍後讀取或計算。\n\n**變數（Variable）** 就扮演了這樣一個神奇的「抽屜盒子」。\n- **宣告變數**：你只需要任意想一個英文名字（比如 `xp`），然後配合等號 `=` 丟進去一個數值：`xp = 100`。\n- **等號 `=`**：在程式世界，這不是數學的 A 等於 B，而是「**向左指的箭頭**」，意思是「把右邊的東西，塞入左邊的抽屜裡」！\n- **使用變數**：之後你就可以在 print 或計算中重複調用這個抽屜的名字囉！",
            codeSnippet = "# 宣告並儲存變數\nuser_name = \"冒險者\"\ncurrent_level = 1\n\n# show 出來！\nprint(\"玩家姓名:\", user_name)\nprint(\"目前等級:\", current_level)",
            interactiveQuizQuestion = "在 Python 中，若想把數字 99 儲存到一個名為 `score` 的變數裡，正確寫法是？",
            quizAnswers = listOf("99 = score", "val score = 99", "score = 99", "int score = 99"),
            correctQuizAnswerIndex = 2,
            quizExplanation = "在 Python 中，宣告變數不須指定資料型別。其格式為「變數名稱 = 值」，等號代表賦值（Assignment）。"
        ),
        CourseLesson(
            id = "lesson_7",
            title = "7. 文字的容器：字串 String",
            subtitle = "深入認識文字、字元與引號的搭配",
            category = "基礎型別",
            readTimeMinutes = 3,
            description = "在 Python 中，文字資料被稱為「字串」。一起看看它可以跟哪些符號玩出什麼魔法！",
            explanationHtmlCode = "「字串（String）」是任何用引號包覆起來的文字串聯。無論是中文、英文、日文，甚至是數字（加了引號後），電腦人都一律視為一段「純文字」。\n\n**強大特徵：**\n1. **加法操作**：文字竟然可以相加！`\"哈囉\" + \"小明\"` 會直接合併成 `\"哈囉小明\"`。\n2. **乘法複製**：想重複文字？`\"星\" * 3` 就會印出 `\"星星星\"`！\n3. **多行包膜**：如果想要打一整段包含排版的文章，可以用三個連續引號 `\"\"\"` 將整段話包起來，格式就不會跑掉！",
            codeSnippet = "# 字串乘加法特效\nicon = \"★\"\nstar_bar = icon * 5\n\nprint(\"玩家評價:\", star_bar)\nprint(\"組合文字:\" + \" \" + \"Python 非常好學！\")",
            interactiveQuizQuestion = "如果變數 a = \"10\"，變數 b = \"20\"，那麼運行 `print(a + b)` 會在螢幕上輸出什麼？",
            quizAnswers = listOf("30", "1020", "發生錯誤，字串不能相加", "a+b"),
            correctQuizAnswerIndex = 1,
            quizExplanation = "因為 a 與 b 都是被雙引號包覆的「字串」，加法運算子 `+` 此時被當作純文字拼接（Concatenation），所以 \"10\" 拼接 \"20\" 會得到 \"1020\"！"
        ),
        CourseLesson(
            id = "lesson_8",
            title = "8. 數字的兩種面貌：整數與浮點數",
            subtitle = "不要搞混：沒有小數點與有小數點的差異",
            category = "基礎型別",
            readTimeMinutes = 3,
            description = "電腦看數字跟人類不太一樣，它把數字分成了乾淨俐落的整數與攜帶細節的浮點數。",
            explanationHtmlCode = "在程式內部，處理數字通常分成以下兩種形式：\n1. **整數（Integer, 簡稱 int）**：沒有小數點的正整數、負整數與 0。例如 `100`、`-50`、`0`。\n2. **浮點數（Float）**：帶有小數點的實數。例如 `3.14`、`9.9`。\n\n**為什麼要區分？**\n因為它們在電腦記憶體裡面的存放和運算機制截然不同。雖然在平時運算時 Python 會默默主動幫你做類型轉換（例如 `5 * 1.5` 會自動算出 `7.5`），但在特定功能上，若放錯資料型別也會引發直譯器報錯喔！你可以用 `type(數字)` 函數來揭發它的真實型態！",
            codeSnippet = "age = 25       # 這是 int\nheight = 175.5 # 這是 float\n\n# 印出其型態\nprint(\"age 的型態是:\", type(age))\nprint(\"height 的型態是:\", type(height))",
            interactiveQuizQuestion = "在 Python 中，變數值 `100.0` 屬於哪一種資料型態？",
            quizAnswers = listOf("整數 (int)", "字串 (str)", "浮點數 (float)", "布林值 (bool)"),
            correctQuizAnswerIndex = 2,
            quizExplanation = "只要數值中帶有小數點（即使小數點後只有 0），Python 都一律將其辨識為浮點數（Float）。"
        ),
        CourseLesson(
            id = "lesson_9",
            title = "9. 布林值：黑白分明的世界",
            subtitle = "只有 True 與 False 的純粹真假值",
            category = "基礎型別",
            readTimeMinutes = 3,
            description = "很多決定都是二分法的：是或否、對與錯、開與關。體驗只有兩種值的布林型態。",
            explanationHtmlCode = "「布林值（Boolean, 簡稱 bool）」是程式能用來控制邏輯的核心基礎。\n\n它極度純粹，有且僅有兩個值：\n- **`True`** (代表：真、成立、開啟)\n- **`False`** (代表：假、不成立、關閉)\n\n**重要警示**：\nPython 對大小寫極其敏感。因此，首字母必須維持大寫的 `T` 與 `F`。如果你寫成 `true` 或 `false`，直譯器會拋出錯誤，告訴你它不知道這是什麼變數盒！",
            codeSnippet = "is_online = True\nis_admin = False\n\nprint(\"線上狀態:\", is_online)\nprint(\"是否為管理員:\", is_admin)",
            interactiveQuizQuestion = "下列哪一個布林值的宣告在 Python 程式語法中是合法的、不會引發報錯？",
            quizAnswers = listOf("is_ok = true", "is_ok = TRUE", "is_ok = True", "is_ok = \"True\""),
            correctQuizAnswerIndex = 2,
            quizExplanation = "Python 的布林值保留字只有 `True` 與 `False` 具有邏輯判定功能，且字首首字必須大寫，第四項有加引號的會被判讀為字串。"
        ),
        CourseLesson(
            id = "lesson_10",
            title = "10. 等號 `=` 與「賦值」的奧秘",
            subtitle = "理解這條最常被工程師新手誤會的程式原則",
            category = "算術基本",
            readTimeMinutes = 3,
            description = "解開新手村最驚悚的算術題：這行代碼 `x = x + 1` 為什麼在程式中是合理的？",
            explanationHtmlCode = "如果你拿出這條式子給數學老師看：`x = x + 1`，老師一定會說這是不可能的（除非 0 = 1）。但在 Python 中，這再正常不過了！\n\n**為什麼？**\n再次強調，中間的 `=` 不是「等號」，而是「**賦值搬運工**」！\n\n當直譯器讀到 `x = x + 1` 這一行，它會這樣處理：\n1. **先看右邊**：算出 `x + 1` 當前的值（比如原本 `x` 是 5，右邊算出來就是 6）。\n2. **執行搬運**：把算好結果 `6` 從新放進左邊的盒子裡。\n3. **結果**：原先的 5 被抹掉，`x` 的內容值成功更新成了 6！",
            codeSnippet = "xp = 100\n# 升級！累加入經驗值\nxp = xp + 50\nprint(\"完成任務後的 xp 總數:\", xp) # 輸出應該是 150",
            interactiveQuizQuestion = "已知 `count = 10`，若執行代碼 `count = count * 2`，之後呼叫 count 將會得到什麼結果？",
            quizAnswers = listOf("10", "20", "它會報錯：不可自己等於自己", "40"),
            correctQuizAnswerIndex = 1,
            quizExplanation = "這條運算代表「把目前的 count（10）乘上 2，再把算好的 20 存回去放進 count」，因此最後 count 的值就是 20。"
        ),
        CourseLesson(
            id = "lesson_11",
            title = "11. 比大小——比較運算子",
            subtitle = "如何讓程式辨別兩個資料的大小及關係",
            category = "邏輯比較",
            readTimeMinutes = 3,
            description = "程式要能自我檢查數據。我們用大於、小於以及超特別的「雙等號」來比對數據。",
            explanationHtmlCode = "我們平時常需要判斷：分數有沒有及格？等級有沒有滿 10 級？\n\n這時候就要利用比較運算子：\n- `>`：大於\n- `<`：小於\n- `>=`：大於或等於\n- `<=`：小於或等於\n- **`==`**：**等於嘛？**（極度重要！這才是程式世界裡的「比較等號」。如果只用一個 `=`，就變成赋值搬運，會直接改寫變數！）\n- `!=`：不等於\n\n這些比大小的運算，算出來的結果通通都是 **布林值（`True` 或 `False`）**！",
            codeSnippet = "score = 85\nis_pass = score >= 60\nis_perfect = score == 100\n\nprint(\"是否及格嗎？\", is_pass)\nprint(\"拿到滿分了嗎？\", is_perfect)",
            interactiveQuizQuestion = "在 Python 中，若想要檢測變數 `a` 的內容是不是跟變數 `b` 一模一樣，應該使用哪一個比較符號？",
            quizAnswers = listOf("a = b", "a == b", "a === b", "a != b"),
            correctQuizAnswerIndex = 1,
            quizExplanation = "在 Python 中，比較兩者是否相等使用兩個等號 `==`。一個等號 `=` 代表把右邊的值塞到左邊。"
        ),
        CourseLesson(
            id = "lesson_12",
            title = "12. 邏輯思考——and, or, not",
            subtitle = "結合多個條件組合而成的邏輯判斷",
            category = "邏輯運算",
            readTimeMinutes = 3,
            description = "現實生活很複雜。出門去玩需要天氣晴朗，同時也需要是你正好有空。學習如何用語文連結詞控制邏輯。",
            explanationHtmlCode = "有時我們想在多個條件同時成立時才去執行。Python 提供極為友善、直接寫英文單字的邏輯運算子：\n\n- **`and`**（且）：兩邊條件都必須是 True，結果才會是 True。其中一個破局，就完蛋了。\n- **`or`**（或）：雨露均霑！兩邊只要「其中一個」為 True，結果就立刻是 True！\n- **`not`**（非）：顛倒黑白！把 True 變成 False，把 False 變成 True！\n\n例如：`is_weekend and is_sunny` 代表既要在週末，又要出太陽！",
            codeSnippet = "has_ticket = True\nis_vip = False\n\ncan_enter = has_ticket or is_vip\nprint(\"進場資格審查結果:\", can_enter) # True",
            interactiveQuizQuestion = "若 x = True，y = False，請問 `not y and x` 的運算最終結果會是什麼？",
            quizAnswers = listOf("True", "False", "None", "會引發語法錯誤"),
            correctQuizAnswerIndex = 0,
            quizExplanation = "`not y` 會將原本是 False 的 y 顛倒成 `True`。此時運算就變成 `True and True`（x 是 True），所以結果最終必定為 `True`！"
        ),
        CourseLesson(
            id = "lesson_13",
            title = "13. 縮排：排版即是法規",
            subtitle = "解析 Python 獨特的「強制空白」風格",
            category = "排版規則",
            readTimeMinutes = 3,
            description = "在別的語言排版純粹是為了好看，但在 Python 這可是關乎能不能成功啟動的靈魂要素！",
            explanationHtmlCode = "大多數程式語言（如 JavaScript, C++）都使用大括號 `{}` 來把多行程式包成一個任務區。但 Python 的設計哲學認為這樣不夠優雅。\n\nPython 規定：**必須使用「縮排」（Indentation，通常是 4 個空白鍵或 1 個 Tab 鍵）來劃分指令群組！**\n\n- 在一個冒號 `:` 開始的新區塊下，所有縮排同等列寬的行，都被視為「同一組的」子句。\n- 當你不再縮排，就代表這組子句宣告結束。\n- 如果隨興亂排，直譯器讀到就會拋出 `IndentationError` 當場罷工！",
            codeSnippet = "# 縮牌格式標準範例：\n# 如果沒有遵守縮排，這個單元就沒法正常對齊哦\nprint(\"這是主要位置\")\n#   print(\"這不小心多出兩個空格，會直接引發報錯！\")",
            interactiveQuizQuestion = "在寫 Python 時，缩排（Indent）通常有甚麼實質的作用？",
            quizAnswers = listOf("沒有作用，純粹是給人看的，不縮排也可正常跑", "用來標識變數的型態", "用來劃分代碼結構與執行區塊（從屬關係）", "用來自動清除程式中的快取"),
            correctQuizAnswerIndex = 2,
            quizExplanation = "Python 強制要求使用空白縮排來界定代碼區塊。沒有正確縮排將發生 `IndentationError`（縮排錯誤）而無法運行。"
        ),
        CourseLesson(
            id = "lesson_14",
            title = "14. 條件抉擇之一：if 的起步",
            subtitle = "讓程式學會看狀況做好決定！",
            category = "控制結構",
            readTimeMinutes = 3,
            description = "到了這堂課，我們終於要讓代碼有智慧！第一步就是先做出會根據狀況執行的 if 大門。",
            explanationHtmlCode = "當你希望某些代碼「只有在特定條件達成了」人才需要出動時，就要使用 **`if`（如果）** 敘述。\n\n**語法格式：**\n```python\nif 條件:\n    要做的事情代碼1\n    要做的事情代碼2\n```\n\n**重點拆解：**\n1. `if` 後方加上我們之前學的比大小（例如大於及雙等號等）。\n2. 條件後面**務必加上冒號 `:`**！\n3. 接下來要執行的工作內容列，**每一行都必須縮排 4格空白**！",
            codeSnippet = "hp = 15\nif hp < 30:\n    print(\"警告！你的生命值過低！\")\n    print(\"請盡快尋找補給站。\")",
            interactiveQuizQuestion = "在宣告 if 的條件結構時，條件表達式的尾巴，最容易被遺忘但必須寫上去的符號是什麼？",
            quizAnswers = listOf("分號 ;", "冒號 :", "驚嘆號 !", "大括號 {}"),
            correctQuizAnswerIndex = 1,
            quizExplanation = "條件敘述（if/elif/else）的末尾必須緊跟冒號 `:`，以便指引直譯器判斷接下來需要開啟一個縮排區塊！"
        ),
        CourseLesson(
            id = "lesson_15",
            title = "15. 條件抉擇之二：雙向路徑 else",
            subtitle = "非 A 即 B：沒通過時的替代選擇",
            category = "控制結構",
            readTimeMinutes = 3,
            description = "如果條件不符合，程式該走哪？學習如何用 else 指導程式另一條備用道路。",
            explanationHtmlCode = "在上一課中，若條件為 False，程式就不做任何事直接跳過去。但現實是，我們常常需要非黑即白的抉擇。\n\n例如：大於 60 分通關，**否則**退學重修！\n這時候，我們就把 **`else:`**（否則、其他）端出來：\n\n```python\nif 條件:\n    符合時做的事\nelse:\n    不符合時做的事\n```\n\n**注意事項：**\n`else` 不需要有判斷語句在後面（它代表除了 if 以外的所有情況），但也千萬別忘記結尾的 `:`，並且保持從屬的縮排風格。",
            codeSnippet = "score = 55\nif score >= 60:\n    print(" + "\"[OK] 考試過關！恭喜！\"" + ")\nelse:\n    print(" + "\"[FAIL] 不及格！請再上一堂課！\"" + ")",
            interactiveQuizQuestion = "關於 else 命令的用法，以下哪一項敘述是完全正確的？",
            quizAnswers = listOf("else 後面也需要像 if 一樣手寫另一條比大小條件", "else 可以隨意單獨存在，不用跟 if 配合", "else: 後面要把符合情況下執行的代碼縮進對齊", "else 不能加冒號"),
            correctQuizAnswerIndex = 2,
            quizExplanation = "else 必須附隨在 if 結構後，不需跟條件表達式，但必須加冒號 `:`，下方被執行的內容也必須縮排！"
        ),
        CourseLesson(
            id = "lesson_16",
            title = "16. 條件抉擇之三：多重判定 elif",
            subtitle = "如果、再者、最後的層疊多重判定",
            category = "控制結構",
            readTimeMinutes = 3,
            description = "不止是或否。當選項有 A、B、C 級時，我們需要 elif 出手包辦多重判定路徑。",
            explanationHtmlCode = "當有多個不同的獨立條件排隊等著被檢查時，我們在 `if` 與 `else` 的夾層裡加入了：**`elif`**（是 `else if` 的組合字）。\n\n程式會從最上面的條件開始看。**一旦發現某個條件成立，就進去執行對應的工作，做完後，就會整棟條件判斷「立刻跳脫」**，後面的 elif 就再也不會被判斷！\n\n```python\nif 條件一:\n    執行 A\nelif 條件二:\n    執行 B\nelse:\n    前兩者都不符合時，才執行 C\n```",
            codeSnippet = "age = 15\n\nif age >= 18:\n    print(\"成人票：全票收費\")\nelif age >= 12:\n    print(\"學生票：八折特惠\")\nelse:\n    print(\"兒童票：免門票費\")",
            interactiveQuizQuestion = "在 Python 的多重條件結構中，若第二個 elif 條件剛好符合，程式還會去檢查後面的 else 或 elif 嗎？",
            quizAnswers = listOf("不會，一旦有條件完成就會立刻跳脫整套結構", "依然會逐個檢查，全部跑一遍", "會引發語法編譯器的混亂衝突", "只往前檢查不往後檢查"),
            correctQuizAnswerIndex = 0,
            quizExplanation = "條件分支具有互斥性，一旦直譯器尋找到第一個為 True 的條件區塊執行完後，其餘後續分支一概跳過不執行。"
        ),
        CourseLesson(
            id = "lesson_17",
            title = "17. 不可理喻的「死循環」",
            subtitle = "什麼是無限迴圈？如何避免程式徹底卡死？",
            category = "防雷除錯",
            readTimeMinutes = 3,
            description = "一不小心程式就卡在黑畫面不動了！搶先了解是什麼讓你的程式陷入永無止盡的循環裡。",
            explanationHtmlCode = "「迴圈（Loop）」是為了讓繁瑣、重複的工作能夠輕鬆交給電腦處理而誕生的技術。但它也是一頭危險的野獸。\n\n當我們架設了迴圈，並且設定「只要條件成立就重複做」，如果忘記在操作中「修改或破壞條件」，這個條件就會一輩子都是 True。\n\n這就是致命的 **無限迴圈 / 死循環（Infinite Loop）**！\n- 電腦的 CPU 會用 100% 的全力在百萬分之一秒內無限重跑同一段指令。\n- 此時你會發現模擬端機沒反應、視窗卡住、或風扇狂轉。\n- 除錯法則中，一定要隨時確保迴圈的判定條件有「逼近結束」的更新！",
            codeSnippet = "# 警告：請勿輕易在真實專案中嘗試此結構\n# while True:\n#     print(\"這會逼死電腦！因為 True 永遠是 True！\")",
            interactiveQuizQuestion = "一項程式之所以陷入了死循環（無限迴圈）的泥潭，根本原因與哪一項最有關？",
            quizAnswers = listOf("寫了 too many functions", "迴圈條件永遠都是 True 且沒有打破、變更條件的動作", "電腦開太多應用程式了", "變數命名不合理"),
            correctQuizAnswerIndex = 1,
            quizExplanation = "無限迴圈是因為判斷條件永遠無法成為 False 且內部未設置 break 語句，直譯器會無休止執行下去造成卡死。"
        ),
        CourseLesson(
            id = "lesson_18",
            title = "18. 數羊的迴圈：while 實戰",
            subtitle = "只要條件成立，就永無止息地執行下去",
            category = "控制結構",
            readTimeMinutes = 3,
            description = "透過 while 學習如何一步步設計迴圈，並加入計數器讓迴圈乖乖在數完羊後停下來。",
            explanationHtmlCode = "**`while` 迴圈**的哲學是：「只要（while）你給我的條件維持是 True，我就會一回又一回不停地跑下去！」\n\n想要安全退役，一般需要配置三個步驟：\n1. **設定起點計數**：`count = 1`\n2. **進入條件過濾**：`while count <= 5:`\n3. **遞增逼近終點**：在從屬區塊中加上 `count = count + 1`，讓每次執行完 count 都長大一點。一旦大於 5，下次判定就會變成 False 立刻功成身退！",
            codeSnippet = "sheep_count = 1\nwhile sheep_count <= 3:\n    print(\"咩~ 第\", sheep_count, \"隻綿羊\")\n    # 關鍵！遞增計數，逼近終局\n    sheep_count = sheep_count + 1\nprint(\"終於睡著了...\")",
            interactiveQuizQuestion = "若 `count = 1` 且我們寫了 `while count < 3:`，如果迴圈內部完全沒有更新 count，最終會印出多少隻綿羊？",
            quizAnswers = listOf("印出 2 隻", "印出 3 隻", "迴圈完全不執行", "無限印出「第 1 隻綿羊」"),
            correctQuizAnswerIndex = 3,
            quizExplanation = "`count` 永遠保持為 1 且小於 3 條件永遠不壞，變成了沒有終點的無限數羊慘劇！"
        ),
        CourseLesson(
            id = "lesson_19",
            title = "19. 固定次數的重複：for 迴圈",
            subtitle = "結合 range() 自動走完一段數字旅程",
            category = "控制結構",
            readTimeMinutes = 3,
            description = "不用操心計數器指引！使用 Python 最推崇的 for 迴圈來漂亮控制執行次數。",
            explanationHtmlCode = "相較於 `while` 還需要自己宣告變數來手動遞增，**`for` 迴圈** 簡直就是科技的奇蹟，它是專為走訪特定資料結構而生的。\n\n當你只需要程式重複執行「特定的固定次數」，我們通常使用 `for...in range(...)`。\n\n**語法格式：**\n```python\nfor i in range(5):\n    print(i)\n```\n- `range(5)` 會自動在你背後製造一串由 0 開始、尾端小於 5 的數列序列：`[0, 1, 2, 3, 4]`。\n- 每一輪，`i` 會依序代表數列中的一個值並拿去執行，走完 4 之後，迴圈會自發地判定收工！超級省心！",
            codeSnippet = "for count in range(4):\n    print(\"目前進度百分之:\", count * 25)",
            interactiveQuizQuestion = "在 for 迴圈中，指令 `for x in range(3):` 執行時，變數 `x` 在三個巡迴中依序得到的值為什麼？",
            quizAnswers = listOf("[1, 2, 3]", "[0, 1, 2]", "[0, 1, 2, 3]", "[3, 2, 1]"),
            correctQuizAnswerIndex = 1,
            quizExplanation = "`range(N)` 預設會從 `0` 開始生成，一共有 N 個整數，且最後一個不包含 N 本身，所以是 0, 1, 2。"
        ),
        CourseLesson(
            id = "lesson_20",
            title = "20. 進階 range() 控制技巧",
            subtitle = "如何自訂數列起點、終點與前進的步伐？",
            category = "控制結構",
            readTimeMinutes = 3,
            description = "這堂課將揭開 range 函數的隐藏用法，自訂各種間隔，甚至能到走倒退數字！",
            explanationHtmlCode = "一開始 `range(5)` 總是從 0 開始且一個個走。但如果我想印 2 到 8？甚至是奇數？\n\n你可以提供最多 3 個參數給 `range(起點, 終點, 步伐間隔)`：\n- **雙參數版**：`range(2, 6)` -> 從 2 開始，数到 6 之前一個。數列就是：`[2, 3, 4, 5]`。\n- **三參數版**：`range(1, 10, 2)` -> 從 1 開始、到 10 之前，每次前進 2 步（跳著數）。數列會是：`[1, 3, 5, 7, 9]`（全是奇數！）。\n- **倒退擼版**：`range(5, 0, -1)` -> 從 5 倒數到 1！",
            codeSnippet = "# 秀出偶數列\nprint(\"=== 輸出 2 到 8 的偶數 ===\")\nfor num in range(2, 9, 2):\n    print(num)",
            interactiveQuizQuestion = "為了在螢幕上列印出 10、9、8 ... 倒數到 1，下列哪一個 `range` 語法最具備這樣的能力？",
            quizAnswers = listOf("range(10, 1)", "range(10, 0, -1)", "range(10, 1, -1)", "range(1, 10, -1)"),
            correctQuizAnswerIndex = 1,
            quizExplanation = "倒推時步伐為負值 `-1`，終點要求寫成 `0`，直譯器才不會把 `1` 本身略去，因而能印出 10 下至 1。"
        ),
        CourseLesson(
            id = "lesson_21",
            title = "21. 容器大聯盟：認識清單 (List)",
            subtitle = "神奇的排骨便當盒：多個變數聚在一起",
            category = "資料結構",
            readTimeMinutes = 3,
            description = "如果想存全班 30 個人的名字，你不會想宣告 30 個變數名。學習一次打包多個同等重要物件的清單結構。",
            explanationHtmlCode = "到目前為止，一個變數抽屜只能放「一個」值。但這就像一個櫃子塞一件衣服，太奢侈了。\n\nPython 給了我們一個革命性的容器：**清單（List，也就是常說的陣列）**。它可以一口氣在同一個地方收藏無數個值！\n\n**寫法特徵：**\n使用中括號 `[]` 包裹，中間以英文逗號 `,` 分隔。\n`shopping = [\"牛奶\", \"雞蛋\", \"蘋果\"]`\n\n**索引（Index）取值原則：**\n電腦極度習慣「**從 0 開始數數**」！所以：\n- 第一個好物：`shopping[0]`（牛奶）\n- 第二個好物：`shopping[1]`（雞蛋）",
            codeSnippet = "friends = [\"小明\", \"阿華\", \"美美\"]\nprint(\"名單中的第一位朋友是:\", friends[0])\nprint(\"名單中的第二位朋友是:\", friends[1])",
            interactiveQuizQuestion = "已知 `arr = [5, 8, 12, 20]`，請問我們該如何取得「12」這個數值呢？",
            quizAnswers = listOf("arr[3]", "arr[1]", "arr[2]", "arr[\"12\"]"),
            correctQuizAnswerIndex = 2,
            quizExplanation = "清單的第一個索引是 0。對應如下：5 是索引 0，8 是 1，12 是 2。因此使用 `arr[2]` 來獲取 12。"
        ),
        CourseLesson(
            id = "lesson_22",
            title = "22. 清單元素的動態擴充：append",
            subtitle = "如何往已有的清單容器中塞入新的物件？",
            category = "資料結構",
            readTimeMinutes = 3,
            description = "隨時可以自由長大的清單！學習使用 Python 最具代表性的 .append() 擴充大師。",
            explanationHtmlCode = "清單（List）之所以強大，就在於它具有像皮筋一樣無限擴展的彈性！\n\n當我們創建了一個清單後（即使是一開始什麼都沒有的空清單 `lst = []`），我們可以隨時調用清單自身附帶的：**`append(...)`** 原生工具，來在它的尾端追加新的內容。\n\n**語法格式：**\n`清單名稱.append(想要扔進去的東西)`\n這樣該對象就會自動站到隊伍的最後一個位置去。",
            codeSnippet = "cart = [\"筆記本\"]\ncart.append(\"鋼筆\")\ncart.append(\"橡皮擦\")\n\nprint(\"購物車內的所有物品:\", cart)",
            interactiveQuizQuestion = "若 `list = [1]`，執行這行指令 `list.append(2)` 後，清單的內容將會變成什麼？",
            quizAnswers = listOf("[1, 2]", "[2, 1]", "[2]", "這會直接報錯"),
            correctQuizAnswerIndex = 0,
            quizExplanation = "`append` 會將提供的新物件以不打亂順序姿態，黏貼並置入於整串清單的「尾端」，所以答案是 [1, 2]。"
        ),
        CourseLesson(
            id = "lesson_23",
            title = "23. 精準摘除：清單元素的移除",
            subtitle = "當物件過期了或不需要了，該怎麼清除它？",
            category = "資料結構",
            readTimeMinutes = 3,
            description = "學習利用 .remove() 或是 .pop()，把任務或購物車的名單進行完美的除名清理。",
            explanationHtmlCode = "有了增加元素，當然也少不了剔除元素！\n\nPython 提供多種清除清單對象的方法：\n1. **`remove(指定值)`**：如果你曉得想要刪掉什麼值，請直接叫它的名字。例如 `cart.remove(\"垃圾\")`，它就會在名單裡尋找「垃圾」並丟掉。\n2. **`pop(索引)`**：如果你只知道對方站的索引編號，就可以使用 pop。如果不寫任何索引 `cart.pop()`，它會幹練地默默摘掉「排在最後面那個人」並吐出來給你！",
            codeSnippet = "tasks = [\"學習\", \"洗碗\", \"滑手機\"]\n# 滑手機不該在做！進行移除\ntasks.remove(\"滑手機\")\n\nprint(\"移除後的乾淨任務清單:\", tasks)",
            interactiveQuizQuestion = "若 `fruits = [\"蘋果\", \"香蕉\"]`，如果呼叫 `fruits.pop()`，水果清單裡最後將剩下什麼？",
            quizAnswers = listOf("[\"蘋果\"]", "[\"香蕉\"]", "[] (變成空的)", "這會報錯，必須輸入想要清除的值"),
            correctQuizAnswerIndex = 0,
            quizExplanation = "`pop(無參數)` 指令會剔除清單內最後一個元素（即 \"香蕉\"），因此水果清單最終就只保留下第一個項目 \"蘋果\"。"
        ),
        CourseLesson(
            id = "lesson_24",
            title = "24. 唯讀的清單：元組 (Tuple)",
            subtitle = "寫入後永不退變：只讀不可改的特殊群組",
            category = "資料結構",
            readTimeMinutes = 3,
            description = "介紹清單的雙胞胎兄弟——元組，了解為什麼我們需要不允許變更修改的唯讀鎖定結構。",
            explanationHtmlCode = "有時候我們在程式中保存的資料，像是地圖上的經緯度、螢幕的解析度、或是使用者的生日，是絕對「不希望程式運行到一半時，不小心被修改」的常數資料。\n\n這時候，清單（List）就不太合適了。我們需要：**元組（Tuple）**！\n- **特徵 1**：使用**小括號 `()`** 來宣告 `point = (100, 200)`。\n- **特徵 2**：裡面的值一但決定了，就絕對不能再用任何方式（append/remove）修改更變它。\n- **特性**：它具有更好的安全防護效果，存取效能也更上一層樓！",
            codeSnippet = "gps_coord = (25.03, 121.56)\nprint(\"台北緯度是:\", gps_coord[0])\n# gps_coord[0] = 30.0 # 這會引起 TypeError 報錯，防止有人亂改成！",
            interactiveQuizQuestion = "以下哪一項是元組（Tuple）與清單（List）之間最鮮明且巨大的差別特徵？",
            quizAnswers = listOf("元組可以容納字串，清單不行", "元組在宣告完成後就不可以進行更改或新增", "元組不能使用索引來取值", "元組必須加冒號"),
            correctQuizAnswerIndex = 1,
            quizExplanation = "元組（Tuple）在 Python 中是具有不可變性（Immutable）特質的，能確保其一經宣告，內部索引結構不可發生追加、修改或刪除。"
        ),
        CourseLesson(
            id = "lesson_25",
            title = "25. 走訪大聯盟：清單的 for 遍歷",
            subtitle = "如何一口氣讓每個清單內的人出列點名？",
            category = "進階遍歷",
            readTimeMinutes = 3,
            description = "這是我向你推薦的最佳核心玩法：用 for 迴圈來遍歷出清單內的一切寶庫。",
            explanationHtmlCode = "我們在第 19 課學過 `for i in range(...)`，但 `for` 的終極宿命，其實是來走過「任何容器」內的所有成員！\n\n```python\nfor 變數 in 某清單:\n    做事代碼\n```\n\n**這有什麼魔力？**\nPython 會在極致乾淨的語句中，自動安排從數組清單的第 0 項一直到最後一項，每次取出一個扔進「變數」抽屜，並在內部迴圈跑完。你不需要操心任何索引算術！",
            codeSnippet = "items = [\"壽司\", \"拉麵\", \"披薩\"]\nprint(\"=== 今晚我想來點... ===\")\nfor foo in items:\n    print(foo + \" 一份！\")",
            interactiveQuizQuestion = "若 `names = [\"阿華\", \"美美\"]`，執行這段程式碼：`for n in names: print(n)` 會輸出甚麼？",
            quizAnswers = listOf("只印出 阿華", "先印出 阿華，再換行印出 美美", "報錯：不能拿清單作為 for 的過濾項", "names[0] names[1]"),
            correctQuizAnswerIndex = 1,
            quizExplanation = "for 會輪流從 names 選出對象放入變數 n。首先是 阿華（印出），再來是 美美（印出），最終完成了全體走訪。"
        ),
        CourseLesson(
            id = "lesson_26",
            title = "26. 什麼是「函式 (Function)」",
            subtitle = "避免重複抄寫代碼：自訂專屬代工機器",
            category = "進階編排",
            readTimeMinutes = 3,
            description = "如果你發現自己一直在重複複製貼上同樣的指令，那你一定要趕快來看看函式是怎麼拯救重複代碼的。",
            explanationHtmlCode = "「函式（Function）」就像是工廠裡的**加工包裝機**。\n\n你要是每天都得做一段繁瑣的包裝檢查工作，與其每次手寫。不如定義一台叫 `packaging_worker()` 的機器，把步驟刻在機器肚子裡，需要時按一下按鈕呼叫（Call）它即可！\n\n**語法魔法：**\n使用 **`def`**（代表 define，意思是定義：\n```python\ndef 機器名字():\n    定義你要做的事第一步\n    定義你要做的事第二步\n```\n\n下面只要用 `機器名字()` 就能瞬間召集整組程式碼做事！",
            codeSnippet = "# 定義這台打招呼機器人\ndef send_welcome_letter():\n    print(\"感謝報名！\")\n    print(\"祝你學習愉快！\")\n\n# 每次只要呼叫它\nsend_welcome_letter()",
            interactiveQuizQuestion = "在 Python 當中，我們必須用哪一個英文保留關鍵字來宣告並定義一個新設置的自訂「函式（Function）」？",
            quizAnswers = listOf("function", "func", "def", "define"),
            correctQuizAnswerIndex = 2,
            quizExplanation = "Python 選擇使用 `def` 來作為定義（define）自訂函式的最高指令關鍵字！"
        ),
        CourseLesson(
            id = "lesson_27",
            title = "27. 函式的輸入參數與輸出返回值",
            subtitle = "機器進料加工，最終將合格產品拋出！",
            category = "進階編排",
            readTimeMinutes = 3,
            description = "函式必須能靈活變通，運用參數傳送生肉，並用 return 的力量送回精美熟肉產品。",
            explanationHtmlCode = "一部只能印出死板字元的函式機器還不夠靈明。我們需要給予其「變數原料」與「回收接口」。\n\n- **參數（Parameters）**：就是機器的進料槽。如 `def square(num):` 代表這台硬體每次吃一個叫 num 的材料。\n- **返回值 `return`**：運算完成後，用 `return` 強制使結果像果汁一樣流回外界，供其他變數接收。\n\n```python\ndef add_five(val):\n    res = val + 5\n    return res\n\nresult = add_five(10) # result 就存進了 15\n```",
            codeSnippet = "def calculate_bill(price, count):\n    total_cost = price * count\n    return total_cost\n\n# 算一算 2 顆單價 50 的蘋果\nbill = calculate_bill(50, 2)\nprint(\"收費帳單金額為:\", bill) # 100",
            interactiveQuizQuestion = "在 Python 的自訂函式中，如果我們想把在函式肚子裡算好的結果「彈出來」、好讓印在主程式的其它變數中保存，應使用甚麼命令字？",
            quizAnswers = listOf("print", "give", "return", "output"),
            correctQuizAnswerIndex = 2,
            quizExplanation = "`return` 是函式中最重要的結束語句，它能將處理結果回傳給呼叫該函式的主代碼，並退出函式。"
        ),
        CourseLesson(
            id = "lesson_28",
            title = "28. 萬物皆有索引：字串的切片與處理",
            subtitle = "如何像是切香腸一樣切下一部分的字串文字？",
            category = "進階處理",
            readTimeMinutes = 3,
            description = "字串本身在 Python 裡面，其實也是一整條排隊的字元！一起認識方便的字串切片（Slice）讀取法。",
            explanationHtmlCode = "既然字串是由很多文字堆疊而成的，那就代表，字串也能跟清單（List）一樣用索引 `[x]` 去提取單一字體！\n\n比如 `text = \"Python\"`：\n- `text[0]` 就是 `\"P\"`。\n- 如果想要同時多切幾塊，可以使用 [起點:截止點（不含）]：\n- **切片格式 `text[0:4]`**：切出 0、1、2、3 位置字體，所以就會回傳 `\"Pyth\"`！\n\n這在分析網址、姓名拼寫、或是文字處理上超級得心應手！",
            codeSnippet = "secret_code = \"STUDIO_LEARNER\"\n# 只取得前面的 STUDIO 部份\ncode_prefix = secret_code[0:6]\nprint(\"提取出的前綴密碼為:\", code_prefix)",
            interactiveQuizQuestion = "已知字串 `word = \"Python\"`，若執行代碼 `print(word[0:2])` 將會在屏幕上印出什麼？",
            quizAnswers = listOf("Pyt", "Py", "P", "y"),
            correctQuizAnswerIndex = 1,
            quizExplanation = "word[0:2] 會切出索引從 `0` 開始到 `2`（不包含 2 本身）的字串段落，也就是 word[0] 加上 word[1]，即是 \"Py\"。"
        ),
        CourseLesson(
            id = "lesson_29",
            title = "29. 字典 (Dictionary) 的鍵值對",
            subtitle = "像翻閱字典百科一樣，用語意尋找關聯的數據",
            category = "資料結構",
            readTimeMinutes = 3,
            description = "不用記住死板的 0 或 1 索引。我們可以用帶有標籤的 key，直覺取得一對一的最佳對應數值。",
            explanationHtmlCode = "在清單中我們只能使用數字（0, 1）去撈寶。但在某些情況這樣十分麻煩，例如要取得一個學生的「名字、學校、考試分數」。\n\n這時候，我們推薦 Python 獨創且超著名的：**字典（Dictionary）**。\n- 它不靠順序，它靠**「鍵與值（Key-Value）」**一對一的關係儲存。\n- **宣告語法**：使用大括號 `{}`。\n`student = {\"name\": \"小明\", \"score\": 90}`\n- **讀取語法**：直接在括弧寫你要查的 key。\n`student[\"score\"]` -> 這個一查，就會得到 `90`！極具人性化！",
            codeSnippet = "phone_book = {\"阿明\": \"0912\", \"小花\": \"0988\"}\n# 查一查小花的電話\nprint(\"小花的聯絡電話是:\", phone_book[\"小花\"])",
            interactiveQuizQuestion = "在 Python 當中，哪一種符號語法最常被用來標識一個內含一對一查找映射關係的「字典（Dictionary）」格式？",
            quizAnswers = listOf("中括號 [ ]", "大括號 { }", "小括號 ( )", "角括號 < >"),
            correctQuizAnswerIndex = 1,
            quizExplanation = "字典（Dictionary）是以 `{Key: Value}` 的大括號形式定義，中間用冒號區隔，外部項目用英文逗號分隔。"
        ),
        CourseLesson(
            id = "lesson_30",
            title = "30. 整合實戰！三十而立的工程挑戰",
            subtitle = "終點大驗收：一探 Python 優雅簡約設計哲學",
            category = "整合挑戰",
            readTimeMinutes = 4,
            description = "三十堂課終極結業！一起將所有的條件、迴圈及變數融會貫通，完成全方位核心語法的最後巡禮。",
            explanationHtmlCode = "恭喜你！在 Pycoach 順利過關斬將，最終站上第 30 單元的頂端！\n\n從最一期的了解電腦如何一行行看待命令、防護各種縮排與語法符號缺失，一直到掌握了變數容器、條件決策、迴圈重複、以及強大的 List、Tuple 與 Dictionary 儲存能力，最後更精巧學習了 Function 的零件封裝！\n\n你已經徹底破繭而出，成為一名擁有絕佳運算思維技能的人！Python 的奧秘，在於**用最純粹的縮排，寫出最簡約但最漂亮的直覺代碼**！現在就點擊做最後的驗收測驗、去解鎖你的專屬證書大徽章吧！",
            codeSnippet = "def check_engineer_status(lesson_completed):\n    if lesson_completed >= 30:\n        return \"恭喜你正式晉升為 Python 初代宗師！\"\n    else:\n        return \"學習的輪齒仍在轉動，繼續衝刺！\"\n\nstatus = check_engineer_status(30)\nprint(status)",
            interactiveQuizQuestion = "回顧三十堂極限考驗，以下哪一項表述最能精準總結並匹配 Python 程式的主要設計心法與哲學？",
            quizAnswers = listOf("強調複雜與極多贅詞，以便保護代碼不被人看懂", "依靠大括號拼命撰寫無限多的括號分支", "語意簡潔自然、高度強調排版、重用性與程式代碼易讀性", "依靠隨機跳躍方式無序執行代碼"),
            correctQuizAnswerIndex = 2,
            quizExplanation = "Python 的中心哲學是 \"Simple is better than complex.\"。它強調代碼的清晰，並使用優美的強制空白縮進增強程式的天然易認與重用度，恭喜你成功破關學成！"
        ),
        CourseLesson(
            id = "lesson_31",
            title = "31. 物件導向 (OOP) 的哲學與宣告",
            subtitle = "類別與物件：模印與雞蛋糕的宇宙觀",
            category = "OOP 基礎核心",
            readTimeMinutes = 3,
            description = "什麼是物件導向？它是怎麼幫助人類整理複雜世界邏輯的？",
            explanationHtmlCode = "在程式的世界中，當專案變巨大，只用變數和函式會變得很混亂。所以，科學家發明了**物件導向程式設計（OOP）**！\n\n**核心概念：**\n1. 類別（Class）：就是**設計圖**或**模印**。它本身還不是一個實體的蛋糕，只是定義了蛋糕應該有什麼屬性與形狀。\n2. 物件（Object）：由類別製造出來的**真實蛋糕實體（Instance）**。你可以用一個設計圖，做出成千上萬個蛋糕實體！\n\n**語法結構：**\n使用 `class` 關鍵字定義：\n```python\nclass IronMan:\n    pass\n```\n`pass` 是一個佔位符，代表此類別目前空空如也，但保證語法正確。",
            codeSnippet = "# 宣告一個名為 IronMan 的類別\nclass IronMan:\n    pass\n\n# 用這個類別生產出名叫 tony 的物件實體！\ntony = IronMan()\nprint(\"成功製造 Tony 物件！\")",
            interactiveQuizQuestion = "物件導向（OOP）理念中，『類別（Class）』與『物件（Object / Instance）』的底層關係，用哪一個比喻最生動恰當？",
            quizAnswers = listOf("類別是高速運行的執行緒，物件是靜態的暫存器", "類別是造就蛋糕的『設計圖模具』，而物件是照著模具烤出來真實存在的『雞蛋糕』", "類別是指程式的註解，物件才是真實能被電腦編譯執行的本體", "類別是暫時的變數，物件是存在硬碟的持久化資料庫儲存系統"),
            correctQuizAnswerIndex = 1,
            quizExplanation = "Class 是定義事物屬性與特徵的藍排設計紙，而 Object / Instance 是利用這張藍圖建立出來存在記憶體空間中的實際運行物件。"
        ),
        CourseLesson(
            id = "lesson_32",
            title = "32. 類別與屬性 (Class Attributes)",
            subtitle = "為物件賦予內建參數與特徵屬性",
            category = "OOP 基礎核心",
            readTimeMinutes = 3,
            description = "物件不僅是一個空殼，它還具備類別成員共用的預設特徵狀態。",
            explanationHtmlCode = "類別中定義的變數，我們稱為**屬性（Attributes）**！\n\n**類別屬性（Class Attributes）：**\n在類別內直接定義的變數，被該類別的所有物件所共用。\n```python\nclass Bird:\n    has_wings = True # 這是一個類別屬性\n```\n當我們實例化一千隻小鳥時，牠們都內建 `has_wings = True` 屬性。你可以用 `bird_instance.has_wings` 來取出這個內容！",
            codeSnippet = "class Bird:\n    has_wings = True\n\nsparrow = Bird()\nprint(\"麻雀有翅膀嗎？\", sparrow.has_wings)",
            interactiveQuizQuestion = "在 Python 類別設計中，凡是直接在 class 內部宣告、非寫在方法內的變數，稱為何種屬性？",
            quizAnswers = listOf("外部全域變數", "類別屬性 (Class Attribute)，代表所有實體全體共享的預設特徵值", "私有區域變數", "瞬態拋棄屬性"),
            correctQuizAnswerIndex = 1,
            quizExplanation = "直接在 Class 大框架下宣告的屬性為類別屬性，是由該類別所實例化出來的所有物件共同分享與拜訪的常設變數。"
        ),
        CourseLesson(
            id = "lesson_33",
            title = "33. 初始化建構子 __init__ 祕密",
            subtitle = "工廠成立時的初始化特徵大點兵",
            category = "OOP 基礎核心",
            readTimeMinutes = 3,
            description = "如何讓每個物件在出生的一瞬間，就長出專屬於自己獨一無二的值？",
            explanationHtmlCode = "當一個物件「誕生（實例化）」時，Python 會自動引發一個特殊的機器咒語方法，叫做**建構子（Constructor）**：`__init__`！\n\n**語法格式：**\n```python\nclass Dog:\n    def __init__(self, name, age):\n        self.name = name\n        self.age = age\n```\n- 注意：在 Python 中，所有定義在類別內部的函式（方法），其第一個參數**必須是 `self`**！\n- `self` 代表物件自己，讓我們在創立實體時，將名字和年齡準確縫合在「當前這個物件」的身上！",
            codeSnippet = "class Dog:\n    def __init__(self, name, age):\n        self.name = name\n        self.age = age\n\npuppy = Dog(\"旺財\", 3)\nprint(\"小狗的名字叫:\", puppy.name)",
            interactiveQuizQuestion = "在 Python 中，特別在前後使用雙下底線包裹的系統保留方法 `__init__` 具有什麼最神聖的任務？",
            quizAnswers = listOf("用來強制將程式在終端機列印出來", "代表『初始化建構子』，在物件被實例化（誕生）時自動觸發，用來指派與建立該物件特有的初始屬性值", "用來將變數釋放出記憶體，銷毀整段程式碼", "用來引入外部 Python 擴充套件的安全密鑰裝置"),
            correctQuizAnswerIndex = 1,
            quizExplanation = "`__init__` 是初始化方法（Initializer / Constructor），當你調用 `Class(...)` 創立實體的當下它會由 Python 直譯器引導在第一時間載入運作。"
        ),
        CourseLesson(
            id = "lesson_34",
            title = "34. 實體方法 (Instance Methods) 與 self",
            subtitle = "讓物件採取實際行動與執行功能",
            category = "OOP 基礎核心",
            readTimeMinutes = 3,
            description = "物件不只要有資料，更要具備操作和行為能力。",
            explanationHtmlCode = "在類別內宣告的函式被稱為**實體方法（Instance Method）**。\n\n**為什麼一定要傳入 `self`？**\n- `self` 代表當前在調用此方法的「那個特定物件實體」。\n- 透過 `self`，方法才可以任意讀寫該物件內部的專屬屬性資訊！\n\n```python\nclass GameCharacter:\n    def __init__(self, name, hp):\n        self.name = name\n        self.hp = hp\n    \n    def take_damage(self, dmg):\n        self.hp = self.hp - dmg\n        print(self.name, \"受到了傷害，剩餘血量:\", self.hp)\n```",
            codeSnippet = "class GameCharacter:\n    def __init__(self, name, hp):\n        self.name = name\n        self.hp = hp\n    \n    def take_damage(self, dmg):\n        self.hp = self.hp - dmg\n\nhero = GameCharacter(\"亞瑟\", 100)\nhero.take_damage(30)\nprint(\"當前戰士血量剩餘:\", hero.hp)",
            interactiveQuizQuestion = "為什麼 Python 類別內部所設計的普通實體方法，首個傳入參數強烈規定必須加上『self』？",
            quizAnswers = listOf("它是搜尋引擎隨機抓取的變數沒任何意義，可以省略不寫", "self 意指『當前呼叫此方法的特定物件實體本尊』，能讓方法內部透過 self 正確拜訪與更動當前物件的專屬欄位及變數", "它是指在後端運行的伺服器主機 Server IP 簡稱", "它指此方法只能被執行一次，不允許被反覆呼叫"),
            correctQuizAnswerIndex = 1,
            quizExplanation = "self 綁定調用動作的當前實體。例如 sparrow.fly() 時，Python 自動將 sparrow 實體物件作為第一個引數傳入 run 內部的 self 裝進去。"
        ),
        CourseLesson(
            id = "lesson_35",
            title = "35. 封裝與私有屬性 (Encapsulation)",
            subtitle = "利用雙底線為敏感資料加上絕對防護罩",
            category = "OOP 基礎核心",
            readTimeMinutes = 3,
            description = "如果有人在外部惡意把物件的屬性修改怎麼辦？我們需要封裝！",
            explanationHtmlCode = "**封裝（Encapsulation）**是將資料和操作結合在內部，並適度對外隱藏它的實作細節。\n\n- **私有屬性（Private Attributes）**：在變數名稱前加上**兩個底線 `__`**（例如 `self.__balance`），這個屬性就會被鎖定在類別內部，外部的人**直接存取會報錯**！\n- **存取器（Getters & Setters）**：在類別內自訂方法，讓外部的人在遵守規則的前提下，讀寫被保護的私有變數。",
            codeSnippet = "class Account:\n    def __init__(self, money):\n        self.__money = money  # 私有屬性，前面雙底線\n    \n    def get_money(self):\n        return self.__money\n\nacc = Account(500)\n# 試圖讀取會呼叫 get_money()\nprint(\"保險箱剩餘金錢:\", acc.get_money())",
            interactiveQuizQuestion = "在 Python 當中，我們該如何為類別內敏感的屬性特徵變數加上『私有保護色（Private Encapsulation）』防止被外部直接讀寫修改？",
            quizAnswers = listOf("在變數前面加上兩個底線，如 `self.__my_secret`", "在變數前面加上 public 關鍵字", "將變數放置於 list 的最後一個位置", "不宣告在 `__init__` 內部即可"),
            correctQuizAnswerIndex = 0,
            quizExplanation = "在變數名稱前方加註雙底線 `__` 會觸發 Python 的名稱重整（Name Mangling）屏蔽，在外部直接使用 `obj.__my_secret` 會引發屬性找不到錯誤，進而達到封裝保護的目的。"
        ),
        CourseLesson(
            id = "lesson_36",
            title = "36. 繼承 (Inheritance)：代碼複用的黃金密鑰",
            subtitle = "站在巨人的肩膀上，快速建造功能子類別",
            category = "OOP 進階特徵",
            readTimeMinutes = 3,
            description = "如何避免重複寫一模一樣的代碼？讓子類別承接父類別的所有功能！",
            explanationHtmlCode = "**繼承（Inheritance）**允許我們創立一個新類別（子類別 Subclass），自動擁有多個或單個已存在類別（父類別 Parentclass / Superclass）的所有屬性和方法！\n\n**語法格式：**\n在類別括弧內寫入父類別：\n```python\nclass Dog(Animal):\n    pass\n```\n此時，`Dog` 一出生就自動複製並獲得 `Animal` 所有的一切，而不用重新宣告！這就是高度代碼重用性！",
            codeSnippet = "class Animal:\n    def eat(self):\n        print(\"吃飽飽！\")\n\nclass Dog(Animal):\n    def bark(self):\n        print(\"汪汪！\")\n\npuppy = Dog()\npuppy.eat()  # 繼承自 Animal\npuppy.bark() # 自己的特有行為",
            interactiveQuizQuestion = "在 Python 中，若要宣告一個子類別 `Cat` 繼承自父類別 `Animal`，以下哪一個宣告格式是完全符合規範的？",
            quizAnswers = listOf("class Cat extends Animal:", "class Cat : Animal:", "class Cat(Animal):", "class Cat inherits Animal:"),
            correctQuizAnswerIndex = 2,
            quizExplanation = "Python 的繼承格式非常簡潔，只需要在類別名稱右邊的括弧中傳入父類別名稱即可完成。"
        ),
        CourseLesson(
            id = "lesson_37",
            title = "37. 多型 (Polymorphism)：統一介面與多變靈魂",
            subtitle = "呼叫同名的方法，卻產生各自專屬的精彩行為",
            category = "OOP 進階特徵",
            readTimeMinutes = 3,
            description = "多個不同類別，擁有同名的方法卻表現出各自的動作，這就是鴨子型態多型的神妙之處！",
            explanationHtmlCode = "**多型（Polymorphism）**的意思是「多種形態」。\n\n在 Python 中，多型不需要依靠複雜的 interface 介面宣告。只要不同的類別（例如 `Cat` 和 `Dog`）都宣告了同名方法（例如 `speak()`），我們就可以用同一行代碼去驅動它們呼叫各自專屬的講話行為！\n\n這也稱為**鴨子型態（Duck Typing）**：『只要牠走起來像鴨子、叫起來像鴨子，牠就是鴨子！』",
            codeSnippet = "class Cat:\n    def sound(self):\n        return \"喵喵\"\n\nclass Dog:\n    def sound(self):\n        return \"汪汪\"\n\ndef make_animal_sound(animal):\n    print(\"動物發出聲響:\", animal.sound())\n\nmake_animal_sound(Cat())\nmake_animal_sound(Dog())",
            interactiveQuizQuestion = "下列關於物件導向特徵中「多型（Polymorphism）」的核心特徵描述，哪一個最為準確？",
            quizAnswers = listOf("多型是指一個變數可以儲存無限大的數值", "多型是指同一個方法名稱可以套用在不同物件上，並在執行時根據物件實際類別執行其特有的行為", "多型是指一個 Python 檔案可以轉換成多個 HTML 設計圖", "多型指的是物件被強制多重複用、不得銷毀"),
            correctQuizAnswerIndex = 1,
            quizExplanation = "多型允許具有不同內部結構的物件利用同一個統一的方法介面（如 speak）進行調用，展現出各自殊異的特徵動作。"
        ),
        CourseLesson(
            id = "lesson_38",
            title = "38. 方法覆寫 (Method Overriding) 與 super()",
            subtitle = "不僅承接父類別，還能擴充、洗牌或局部升級",
            category = "OOP 進階特徵",
            readTimeMinutes = 3,
            description = "子類別不滿意父類別的預設行為時，可以直接改寫同名方法！更能用 super() 呼叫父類別的核心實作。",
            explanationHtmlCode = "當子類別想要針對父類別繼承來的某個方法進行「改寫」時，只要在子類別中宣告**同名方法**即可，這就叫**方法覆寫（Method Overriding）**。\n\n**super() 咒語：**\n如果我們不想完全丟棄父類別的功能，而是想要「在父類別的基礎上增加新內容」，可以使用 `super().method()` 回頭呼叫父類。這能讓程式碼變得極其乾淨且高結構化！",
            codeSnippet = "class Car:\n    def start(self):\n        print(\"引擎發動！\")\n\nclass ElectricCar(Car):\n    def start(self):\n        super().start() # 呼叫父類別的 start\n        print(\"電動馬達無聲運轉中！\")\n\ntesla = ElectricCar()\ntesla.start()",
            interactiveQuizQuestion = "當我們在子類別重寫（Override）了某個方法，但此時仍想在該方法內部先去執行父類別中的同名方法邏輯時，應使用哪一個內建函式？",
            quizAnswers = listOf("parent()", "super()", "upper()", "this()"),
            correctQuizAnswerIndex = 1,
            quizExplanation = "`super()` 能直接拜訪並調用父類別（Superclass）定義的方法或變數，是擴充父類邏輯時的黃金常規通道！"
        ),
        CourseLesson(
            id = "lesson_39",
            title = "39. 多重繼承 (Multiple Inheritance) 與 MRO",
            subtitle = "混血兒的基因：同時擁有兩個以上的父類別特權",
            category = "OOP 進階特徵",
            readTimeMinutes = 3,
            description = "Python 是少數支援「多重繼承」的程式語言！一個子類別可以同時擁有複數以上的爸爸。",
            explanationHtmlCode = "在 Python 中，你可以同時逗號繼承複數個類別：\n\n```python\nclass Mermaid(Human, Fish):\n    pass\n```\n\n**MRO 衝突（Method Resolution Order）：**\n- 當 `Human` 和 `Fish` 都有一個同名方法 `swim()` 時，Python 該跑誰的？\n- Python 會使用內建的 **C3 線性演算法**，透過 **MRO（方法解析順序）**決定查找順序。你可以用 `ClassName.__mro__` 查閱搜尋路徑，通常是「從左至右，由深至淺」！",
            codeSnippet = "class Flyer:\n    def action(self):\n        return \"在空中飛\"\n\nclass Swimmer:\n    def action(self):\n        return \"在水中游\"\n\n# Duck 同時繼承 Flyer 與 Swimmer\nclass Duck(Flyer, Swimmer):\n    pass\n\ndonald = Duck()\nprint(\"鴨子的首要動作是:\", donald.action())",
            interactiveQuizQuestion = "在 Python 當中，當一個類別同時繼承了多個父類別、且父類別之間存在同名方法，Python 是依賴何種機制來確定查找優先級順序的？",
            quizAnswers = listOf("隨機執行一個", "MRO (Method Resolution Order) 方法解析順序", "依靠類別檔案的行數大小", "依靠程式中變數的定義時間"),
            correctQuizAnswerIndex = 1,
            quizExplanation = "Python 採用 MRO 機制決定方法解析順序，可在執行期透過 C3 演算法保證唯一無二、乾淨有條理的繼承尋優方案。"
        ),
        CourseLesson(
            id = "lesson_40",
            title = "40. 抽象類別 (Abstract Base Class) 強制約束",
            subtitle = "宣告必須被實作的規範，杜絕虛設空殼",
            category = "OOP 進階特徵",
            readTimeMinutes = 3,
            description = "當你想建立一個規定「所有子類別都必須實作特定方法」的底層架構時，抽象類別就是最佳的合約規範！",
            explanationHtmlCode = "有時候，我們會宣告一個非常底層的 Parentclass，例如 `Database`。我們不希望這個 `Database` 自己被創立，而是想要規定：『所有底下的子類別（例如 `MySQL` 或 `MongoDB`）都**必須且強制**自己實作 `connect()` 語法，否則就不允許執行』！\n\n在 Python 中，我們可以使用 **NotImplementedError** 作為純 native、免 import 的抽象強制機制！子類別要是忘了覆寫，執行到此方法時就會引發報錯，達到優秀的協同防呆合約機制！",
            codeSnippet = "class AbstractPayment:\n    def pay(self, amount):\n        raise NotImplementedError(\"子類別必須實作 pay 支付方法！\")\n\nclass LinePay(AbstractPayment):\n    def pay(self, amount):\n        print(\"使用 LinePay 成功支付:\", amount)\n\npayment = LinePay()\npayment.pay(100)",
            interactiveQuizQuestion = "在使用 Python 物件導向規劃軟體架構時，如何不使用 import 套件、而以純原生（Native）方式逼迫子類別必須重寫並實作父類別的某個關鍵方法？",
            quizAnswers = listOf("將方法留空不寫任何程式碼", "在父類別的方法內拋出 `NotImplementedError` 異常錯誤，若子類別未覆寫便會報錯中斷", "將方法宣告為變數並設為 None", "不宣告該方法"),
            correctQuizAnswerIndex = 1,
            quizExplanation = "`raise NotImplementedError` 是 Python 設計 high level 抽象框架最經典、免外接、最直覺的強制子類別定約方式，能精準防呆！"
        ),
        CourseLesson(
            id = "lesson_41",
            title = "41. __str__ 與 __repr__ 辨析",
            subtitle = "如何讓 print(obj) 印出美麗的可讀文字，而非記憶體地址",
            category = "多載與特殊方法",
            readTimeMinutes = 3,
            description = "每次 print 物件時，都印出長得像 <__main__.Hero object at ...> 的亂碼？用 __str__ 改寫它吧！",
            explanationHtmlCode = "Python 類別裡有許多特殊的方法，通常前後都有雙底線，這被稱作 **Dunder Methods (Double Underscores)** 或 Magic Methods。\n\n- `__str__`：當我們試圖用 `print(obj)` 或 `str(obj)` 時，Python 會自動抓取這個方法的 return 字串！\n- `__repr__`：代表**官方、開發者用**的精確重現字串（Representation），用在偵錯模式或直接在 shell 輸入物件名稱時顯示。\n\n```python\nclass Student:\n    def __init__(self, name):\n        self.name = name\n    def __str__(self):\n        return f\"學生名字: {self.name}\"\n```",
            codeSnippet = "class Student:\n    def __init__(self, name):\n        self.name = name\n    \n    def __str__(self):\n        return \"學生名字: \" + self.name\n\nstu = Student(\"小華\")\nprint(stu) # 呼叫 __str__",
            interactiveQuizQuestion = "在 Python 當中，覆寫哪一個特殊雙下底線（Dunder）方法，可以改寫『print(物件實體)』所呈現的預設字串內容？",
            quizAnswers = listOf("`__init__`", "`__str__`", "`__print__`", "`__format__`"),
            correctQuizAnswerIndex = 1,
            quizExplanation = "`__str__` 方法專門負責客製化物件轉換為字串時的可讀版外觀，是優化物件列印排版的最強 Dunder 方法。"
        ),
        CourseLesson(
            id = "lesson_42",
            title = "42. 定義長度與容器行為 __len__ 與 __getitem__",
            subtitle = "讓你的自訂物件也能使用 len() 和中括號 [index] 取值",
            category = "多載與特殊方法",
            readTimeMinutes = 3,
            description = "你想過為什麼 list 可以用 len(list) 查長度，用 list[0] 查元素嗎？你可以為自己的物件配備這兩個魔法！",
            explanationHtmlCode = "在 Python 中，所有內建函數都有對應的 Dunder 方法！\n\n1. **常規長度**：當你使用 `len(obj)` 時，Python 底層其實是在呼叫該物件的 `obj.__len__()`！\n2. **下標索引**：當你使用 `obj[index]` 時，其實是在呼叫該物件的 `obj.__getitem__(index)`！\n\n這代表只要在類別內宣告這兩個方法，你的自訂物件就會神奇地長得像清單（List）或字典一樣，可以使用括號查找！這是 Python 強大的語意統合特徵！",
            codeSnippet = "class Team:\n    def __init__(self, members):\n        self.members = members\n    \n    def __len__(self):\n        return 99 # 隨便自訂回傳！\n\nmy_team = Team([\"Ray\", \"Alice\"])\nprint(\"我的團隊自訂虛擬長度為:\", len(my_team))",
            interactiveQuizQuestion = "當我們對自訂物件 `my_obj` 執行 `len(my_obj)` 查詢長度時，Python 底層實際上是在觸發哪一個魔法方法？",
            quizAnswers = listOf("`__size__`", "`__count__`", "`__len__`", "`__getitem__`"),
            correctQuizAnswerIndex = 2,
            quizExplanation = "`len()` 函式內部會自動去尋找並呼叫該物件的 `__len__` 雙底線魔法方法求得數值回傳。"
        ),
        CourseLesson(
            id = "lesson_43",
            title = "43. 運算子多載 (Operator Overloading)：__add__",
            subtitle = "賦予「+」符號全新意涵，自訂兩個物件如何完成相加",
            category = "多載與特殊方法",
            readTimeMinutes = 3,
            description = "用 '+' 號竟然可以讓兩個自訂物件結合？學習如何多載數學運算符！",
            explanationHtmlCode = "字串用 `+` 會相連，數字用 `+` 會相加。那自訂的 `Vector(向量)` 或 `Score` 物件呢？\n\n當我們執行 `item1 + item2` 時，Python 直譯器會立刻將左側的 `item1` 作為 `self`，右側的 `item2` 作為 `other` 參數，發送給 `item1.__add__(other)` 方法！\n\n這就叫**運算子多載（Operator Overloading）**。它能讓代碼驚人地優雅！",
            codeSnippet = "class Point:\n    def __init__(self, x):\n        self.x = x\n    \n    def __add__(self, other):\n        return Point(self.x + other.x) # 兩者 x 相加 \n\np1 = Point(10)\np2 = Point(20)\np3 = p1 + p2\nprint(\"相加後的新點座標為:\", p3.x)",
            interactiveQuizQuestion = "在 Python 當中，若自訂物件寫了 `a + b` 控制相加，那麼 `a` 物件內部會是調用哪一個雙底線方法？",
            quizAnswers = listOf("`__plus__`", "`__sum__`", "`__add__`", "`__concat__`"),
            correctQuizAnswerIndex = 2,
            quizExplanation = "運算子 `+` 對應的魔法方法 is `__add__`。同樣地，`-` 對應 `__sub__`，`*` 對應 `__mul__` 等等。"
        ),
        CourseLesson(
            id = "lesson_44",
            title = "44. 物件比較方法 __eq__ 與 __lt__",
            subtitle = "自訂雙等號 == 與小於 < 的判定邏輯",
            category = "多載與特殊方法",
            readTimeMinutes = 3,
            description = "如何判定兩個不一樣的物件是否「價值相等」？用 __eq__ 掌控物件的比較天平。",
            explanationHtmlCode = "當我們比較 `obj1 == obj2` 時，Python 預設是在比對它們「是不是同一個記憶體地址」。\n\n如果你希望「只要兩本書的 ISBN 相同，就代表是同一本書」，你就需要覆寫比較方法：\n- `__eq__(self, other)`：對應雙等號 `==` 運算子。\n- `__lt__(self, other)`：對應小於 `<` 運算子。\n- `__gt__(self, other)`：對應大於 `>` 運算子。\n\n覆寫了 `__lt__` 之後，你的物件清單甚至可以直接放進 `sorted()` 之中，Python 會自動依照你自訂的規則把物件排隊排好！",
            codeSnippet = "class Hero:\n    def __init__(self, score):\n        self.score = score\n    \n    def __eq__(self, other):\n        return self.score == other.score\n\nh1 = Hero(100)\nh2 = Hero(100)\nprint(\"這兩位英雄戰功一樣嗎？\", h1 == h2)",
            interactiveQuizQuestion = "若我們希望利用代碼比較自訂物件 `a == b` 是否成立，應該在類別裡撰寫哪一個雙底線比較方法？",
            quizAnswers = listOf("`__equal__`", "`__same__`", "`__eq__`", "`__compare__`"),
            correctQuizAnswerIndex = 2,
            quizExplanation = "`__eq__`（Equal 縮寫）方法被呼叫用來決定當自訂物件遇到雙等號 `==` 比對時的真假值。"
        ),
        CourseLesson(
            id = "lesson_45",
            title = "45. 可呼叫物件：__call__ 奇妙用法",
            subtitle = "把整個物件實體當作一個 Function 般直接括號呼叫！",
            category = "多載與特殊方法",
            readTimeMinutes = 3,
            description = "你想過連物件本身都可以加上小括號 () 像函數一樣被執行嗎？這一切都源自於 __call__ 魔法！",
            explanationHtmlCode = "在 Python 的哲學裡，界線非常模糊。不只 `def` 宣告的函式可以被呼叫，任何自訂物件只要宣告了 `__call__`，它就會一秒變成 **Callable（可呼叫物件）**！\n\n```python\nclass Multiplier:\n    def __init__(self, factor):\n        self.factor = factor\n    def __call__(self, val):\n        return val * self.factor\n```\n你可以這樣用：\n```python\ndoubler = Multiplier(2)\ndoubler(10) # 輸出 20！像函數一樣！\n```\n這對於保持物件狀態、同時又要有函數呼叫外觀的場景極為實用！",
            codeSnippet = "class Greeter:\n    def __call__(self):\n        return \"哈囉！我是可呼叫物件！\"\n\nsay_hi = Greeter()\nprint(say_hi())  # 直接括弧呼叫物件！",
            interactiveQuizQuestion = "若一物件 `obj` 宣佈支援 `__call__` 魔法方法，這代表該物件在 Python 中具備了哪一種能力？",
            quizAnswers = listOf("它能自動與資料庫同步", "它可以像一般的 Function 那樣，被直接掛上小括號 `obj()` 來呼叫執行", "它會被強制轉乘字串", "它不能被物件實例化"),
            correctQuizAnswerIndex = 1,
            quizExplanation = "`__call__` 魔法方法的任務是將物件「可呼叫化」，讓你可以如同呼叫一般函數一樣來運行物件實體。"
        ),
        CourseLesson(
            id = "lesson_46",
            title = "46. 探究 Python 函式是一等公民哲學",
            subtitle = "像小皮球一樣：將函式當作變數、參數與回傳值到處傳遞",
            category = "函式與裝飾器",
            readTimeMinutes = 3,
            description = "在進入裝飾器前，必須先打通任督二脈：理解為什麼 Python 函式是一等公民。",
            explanationHtmlCode = "在 Python 中，**函式（Functions）是一等公民（First-Class Citizens）**。這是一項極其尊貴的意思！\n\n這代表，函式與普通的數位、字串沒有任何差別：\n1. **可以指派給變數**：`my_run = print`，接下來使用 `my_run(\"Hi\")` 就等同於 print！\n2. **可以當作參數傳入另一個函式**。\n3. **可以寫在函式內部並當成 return 值拋出去**。\n\n理解這點，你才能輕易看懂接下來無比神妙的「裝飾器（Decorators）」！",
            codeSnippet = "def run_task():\n    return \"任務成功！\"\n\n# 把函式本質（不加括弧）賽進變數 action！\naction = run_task\nprint(\"動作回傳:\", action())",
            interactiveQuizQuestion = "在 Python 語言特性中，『函式是一等公民 (First-Class Function)』本意是指什麼？",
            quizAnswers = listOf("函式在所有語言中速度都是最快的", "函式可以直接做全域變數、當作引數傳遞或被其他函式 return 回傳，享有與一般資料變數完全平等的地位", "函式只允許在類別內部被宣告執行", "函式必須最優先被執行、不能被放在迴圈內部"),
            correctQuizAnswerIndex = 1,
            quizExplanation = "一等公民（First-Class）代表 Function 可以像字串和數字一樣隨意在變數間指派與傳遞、當作引數或作為回傳值回饋，此概念是閉包與裝飾器的靈魂。"
        ),
        CourseLesson(
            id = "lesson_47",
            title = "47. 閉包 (Closure)：延長區域變數生命的保鮮盒",
            subtitle = "包裹著當前環境變數的特殊局部函數",
            category = "函式與裝飾器",
            readTimeMinutes = 3,
            description = "區域變數通常一離開函式就死掉了。但閉包就像一個魔法保鮮盒，能將死去的環境變數打包帶走繼續存活！",
            explanationHtmlCode = "**閉包（Closure）**是由一個巢狀的內部函式（Inner Function），包裹並引用了外部函式（Outer Function）內部的區域變數，再被當作回傳值 return 退回後的綜合結體。\n\n即使外部函式已經完全結束生命週期，該內部函式依然會「牢牢記住並綁定」當時外部環境擁有的變數值！\n\n這在設計資料累加、狀態計數器上極具美感與安全性！",
            codeSnippet = "def make_multiplier(n):\n    def multiplier(x):\n        return x * n # 記住了外部的 n\n    return multiplier\n\ndoubler = make_multiplier(2)\nprint(\"2 倍乘積結果:\", doubler(5))",
            interactiveQuizQuestion = "關於 Python 當中的「閉包（Closure）」概念與結構特徵，下列敘述何者最為精緻妥當？",
            quizAnswers = listOf("閉包是一種防範程式庫外洩的安全封包密碼處理技術", "閉包是由內部嵌套函式捕獲並包裝了外部函式局部環境變數、並在外部結束調用後仍能存活與使用的精妙函數架構", "閉包是將 Python 所有的 list 做資料重組的語意結構", "閉包程式是指不能隨便打開的私有類別"),
            correctQuizAnswerIndex = 1,
            quizExplanation = "閉包（Closure）會把內部函式所引用的外部不變量環境做一個「保鮮包裝（Closed over）」，使其即使在外部函式調用退場後也能長期保存該快照變數供日後調用。"
        ),
        CourseLesson(
            id = "lesson_48",
            title = "48. 撰寫第一個簡單裝飾器 (Decorator)",
            subtitle = "用「@」外掛：在不更動原函式代碼情況下為它添加新功能",
            category = "函式與裝飾器",
            readTimeMinutes = 3,
            description = "什麼是裝飾器？用最精湛的 @ 符號，無侵入地在原函式執行前後塞入額外日誌、時間監測等外掛！",
            explanationHtmlCode = "**裝飾器（Decorator）**本質上就是一個「接收一個函式，加工並回傳一個包裝後新函式」的高階函式！\n\n**運行流程：**\n1. 撰寫一個裝飾器 `my_decorator`。\n2. 在你想外掛附加功能的普通函式上方寫上 `@my_decorator`。\n3. 每當此函式被呼叫時，它便會乖乖被抓進裝飾器，在執行原內容前後，自動加上譬如登入檢核、執行時間監控等工作，而原本的函數內容完全不需要改寫任何一行字！",
            codeSnippet = "def my_decorator(func):\n    def wrapper():\n        print(\"[Start] 執行前日誌記錄\")\n        func()\n        print(\"[End] 執行後日誌記錄\")\n    return wrapper\n\n# 稍後在手作關卡中，我們將學習如何使用 @ 語法來修飾函數！",
            interactiveQuizQuestion = "在 Python 當中，我們常在普通函式（Function）的定義上方，打上『@decorator_name』，這種精妙的結構被稱稱為裝飾器？",
            quizAnswers = listOf("迭代器 (Iterator)", "產生器 (Generator)", "裝飾器 (Decorator)，其本質是接受並封裝原函數、不改動原 code 開發新機制的包裝函數", "繼承管理器 (Inheritor)"),
            correctQuizAnswerIndex = 2,
            quizExplanation = "「@」是 Python 對於裝飾器（Decorator）提供的精準語法糖。它能讓我們以不修改原始函數代碼的優雅姿態，為該函數外接與注入切面功能（Aspect-Oriented Programming, AOP）。"
        ),
        CourseLesson(
            id = "lesson_49",
            title = "49. 裝飾器進階：*args 與 **kwargs 大改造",
            subtitle = "星級包裝大改造：當被包裝的函式也需要傳入參數",
            category = "函式與裝飾器",
            readTimeMinutes = 3,
            description = "如果被包裝的函式需要傳入 2 個參數，之前的無參數 wrapper 就會爆掉了！一起學習用彈性參數拯救它。",
            explanationHtmlCode = "當我們使用裝飾器包裹一個「有傳入參數」的函數時，裝飾器內部的 `wrapper` 函式也必須有辦法收納這些未知數量的參數，並原封不動地傳給原本的 func！\n\n這時候，Python 最強的**萬用彈性指派 `*args` 與 `**kwargs`** 就派上用場了！\n\n```python\ndef pass_decorator(func):\n    def wrapper(*args, **kwargs):\n        print(\"準備執行參數轉發...\")\n        result = func(*args, **kwargs)\n        return result\n    return wrapper\n```\n不論原本的函數長怎樣，這種 wrapper 都能完美消化，非常強固！",
            codeSnippet = "def logger(func):\n    def wrapper(*args, **kwargs):\n        print(\"傳送參數為:\", args)\n        return func(*args, **kwargs)\n    return wrapper\n\n# args 參數轉發測試",
            interactiveQuizQuestion = "當我們設計一個泛用（Generic）的 Python 裝飾器時，為了能完美包裝各種包含不同參數格式的原函數，通常會在內部的 `wrapper()` 中填入什麼萬用接收器？",
            quizAnswers = listOf("`self`", "`*args, **kwargs`，代表彈性可變長度位置引數與關鍵字引數對", "`*list, **dict`", "`None`"),
            correctQuizAnswerIndex = 1,
            quizExplanation = "利用 `*args` 組合元組以及 `**kwargs` 組合字典，能完美截獲一切多樣化參數並在 wrapper 裡分毫不差地透傳回 func 呼叫。"
        ),
        CourseLesson(
            id = "lesson_50",
            title = "50. 雙重武功合璧：多重裝飾器疊加",
            subtitle = "多重裝飾器疊加運作的大結局進階特訓",
            category = "函式與裝飾器",
            readTimeMinutes = 4,
            description = "終極挑戰！當一個函式被 @decorator_A 與 @decorator_B 同時疊加修飾時，到底是誰先執行呢？",
            explanationHtmlCode = "恭喜你！走完了 Python 50 堂課最不可思議、最難能可貴、最頂級進階的物件導向與裝飾器魔法拼圖！\n\n在極致的裝飾器實踐中，你甚至可以在同一個函數头上「疊加」兩個以上的裝飾器：\n```python\n@uppercase_decorator\n@bold_decorator\ndef greet():\n    return \"hello\"\n```\n**執行規則：**\n其執行順序是**「由下而上、由內往外」**套用！也就是 `greet` 先被 `@bold_decorator` 包裹，隨後這個套餐再被 `@uppercase_decorator` 包裝！\n\n恭喜你完全融合了 Python 變數、物件導向、屬性繼承、魔法多載方法以及極致的一等公民閉包與裝飾器！你已經正式從 Python 初學者，脫胎換骨成為一個具備專業前端與系統底層設計眼光的高階工程師！",
            codeSnippet = "def decorator_A(f):\n    def w():\n        return \"A\" + f()\n    return w\n\ndef decorator_B(f):\n    def w():\n        return \"B\" + f()\n    return w\n\n# 雙重疊加執行，精彩絕倫！",
            interactiveQuizQuestion = "當一個 Python 函式首部被疊加了複數個裝飾器（例如上方先宣告 @A 再宣告 @B），直譯器裝配並加載這些包裝裝飾器的實際順序是？",
            quizAnswers = listOf("完全隨機", "由上而下（先 A 套用，再 B 套用）", "由下而上（由內而外先套用最靠近函數的 B，隨後再給外層的 A 進行包裝）", "只會選擇其中一個執行，另一個忽略"),
            correctQuizAnswerIndex = 2,
            quizExplanation = "多重裝飾器的疊加順序為「由下而上（由內往外）」。最靠近原函數定義的裝飾器最先施展包裹，其產生的新 wrapper 函數再傳遞給上一層裝飾器作為被包裹對象。恭喜你順利通過 50 大關特訓！"
        )
    )

    val javascriptLessons = listOf(
        CourseLesson(
            id = "js_1",
            title = "1. 認識 JavaScript 語言學",
            subtitle = "瀏覽器上的終極統治者！",
            category = "JS 基礎入門",
            readTimeMinutes = 2,
            description = "解密 JavaScript 的歷史，它是如何從一個用 10 天設計出來的小玩具，成長為目前全球最不可或缺的網頁主宰者。",
            explanationHtmlCode = "JavaScript 誕生於 1995 年。它是一門**高階、直譯式、動態型態**的腳本語言。\n\n**核心觀念：**\n1. **瀏覽器唯一通用語言**：任何網頁上的動畫、表單驗證、或 API 資料獲取，在前端都必須由 JS 來控制。\n2. **跨平台全能**：不僅能在前端運行，也可以透過 Node.js 在伺服器端（後端）甚至手機 App、物聯網晶片上運作！\n\n它是現代全端工程師的必學第一站！",
            codeSnippet = "// 這是一行 JavaScript 註解\n// JavaScript 在網頁中是負責控制生命力與互動的靈魂！",
            interactiveQuizQuestion = "下列關於 JavaScript 的敘述，哪一項是正確的？",
            quizAnswers = listOf("它跟 Java 語言本質上是完全一樣的東西、僅有縮寫不同", "它是瀏覽器目前唯一內建、可直接原生運作的動態腳本語言", "它只能寫伺服器後端，在網頁前端必須先編譯成二進位", "它不支援任何條件或迴圈，純粹用來修飾 HTML 文字色調"),
            correctQuizAnswerIndex = 1,
            quizExplanation = "JavaScript 與 Java 的關係就像「雷鋒與雷達」或「熱狗與狗」——完全無關！JS 是目前所有現代瀏覽器唯一直接內置支援的腳本語言。"
        ),
        CourseLesson(
            id = "js_2",
            title = "2. 向世界問好 console.log",
            subtitle = "Hello World：在控制台大聲呼喊！",
            category = "JS 基礎入門",
            readTimeMinutes = 2,
            description = "運用 JavaScript 的標準輸出指令，把訊息大聲地列印到網頁開發者面板上！",
            explanationHtmlCode = "在 Python 中，我們使用 `print()` 來印出訊息。而在 JavaScript 世界裡，我們的標準輸出是：\n\n**`console.log(...)`**\n\n- `console` 是一個內建的系統控制台物件。\n- `log` 是它用來輸出日誌（log）的方法。\n- 把想輸出的內容（如文字或字串）用括號包起來並加上雙引號 `\"\"` 傳遞進去，就能在主控台看見了！",
            codeSnippet = "console.log(\"哈囉，歡迎來到 JS 的世界！\");\nconsole.log(\"我是你的 AI 金牌教練！\");",
            interactiveQuizQuestion = "JavaScript 當中，最廣為使用的標準指令來把數據或狀態印到控制台的是什麼？",
            quizAnswers = listOf("print(\"hello\");", "console.log(\"hello\");", "document.write(\"hello\");", "echo \"hello\";"),
            correctQuizAnswerIndex = 1,
            quizExplanation = "console.log(...) 是所有 JavaScript 開發者無人不知的最強大除錯與輸出列印武器。"
        ),
        CourseLesson(
            id = "js_3",
            title = "3. 語法結構與嚴格分號",
            subtitle = "寫出乾淨、無錯誤的 JS 語意塊！",
            category = "JS 基礎入門",
            readTimeMinutes = 2,
            description = "了解程式中的「結束符號」——分號。它到底是不是強制需要的？",
            explanationHtmlCode = "在 JavaScript 中，每一句完整的指派或指令，傳統上會在結尾加上一個**分號 `;`**（Semicolon）。\n\n- **自動分號插入（ASI）**：雖然現今 JS 具有自動補充分號的機制，甚至不寫分號也通常能跑，但在某些特定寫法（如 IIFE 或多行混和）下，不加分號會導致致命的語意解析錯誤。\n- **最佳實踐**：在學習初期，強烈建議養成「每一條語句結尾都加上分號 `;`」的嚴謹好習慣，能讓你的代碼邏輯邊界極度安全！",
            codeSnippet = "console.log(\"第一行加上分號;\");\nconsole.log(\"第二行也加上分號;\");",
            interactiveQuizQuestion = "關於 JavaScript 語句結尾的分號 `;`，下列哪一項是推薦的開發態度？",
            quizAnswers = listOf("絕對不能加，加了會造成語法崩潰無法執行", "可依個人或團隊風格加或不加，但新手加上分號能維持良好的代碼清晰度", "分號是 JS 當中用來宣告變數的唯一命令", "必須在每一行英文字母中間都用分號隔開"),
            correctQuizAnswerIndex = 1,
            quizExplanation = "JS 有自動插入分號（ASI），但新手養成加上分號的好習慣，能減少很多隱藏的程式解析邊界問題！"
        ),
        CourseLesson(
            id = "js_4",
            title = "4. 變數與關鍵字 let",
            subtitle = "最實用的局部可變記憶體容器！",
            category = "變數安全",
            readTimeMinutes = 3,
            description = "學習現代 JavaScript 宣告可變變數的最強大黃金指令：let。",
            explanationHtmlCode = "變數就像是用來裝載數據的「貼標籤紙箱」。\n在現代 JS (ES6 規格) 中，宣告變數的主力指令是 **`let`**：\n\n```javascript\nlet score = 100;\nscore = 120; // 成功！它的值是可以被修改的\n```\n\n- `let` 宣告的變數具有**區塊作用域**（Block Scope），只活在它宣告的那對 `{}` 花括號高牆內部。\n- 它是「可變（mutable）」的，之後你可以用新的數值去覆蓋它！",
            codeSnippet = "let dogName = \"Lucky\";\ndogName = \"Buddy\";\nconsole.log(\"狗狗的名字改成了:\", dogName);",
            interactiveQuizQuestion = "使用 let 宣告變數後，下列哪一個操作是完全合法的？",
            quizAnswers = listOf("不可以用任何新值去覆蓋它", "可以隨時用等號 = 重新對它指派與賦值變更", "它不用加 let 就可以在檔案開頭直接自動生效", "在宣告它之前就可以安全無誤地讀取它的內容"),
            correctQuizAnswerIndex = 1,
            quizExplanation = "let 宣告的變數是 Mutable（值可變的），重新指派新資料是完全被允許的。"
        ),
        CourseLesson(
            id = "js_5",
            title = "5. 唯讀保護 const 常數",
            subtitle = "賦值後不可動搖的黃金保護層！",
            category = "變數安全",
            readTimeMinutes = 3,
            description = "宣告「常數」的高效指令：const。一旦放進去，誰也別想偷改！",
            explanationHtmlCode = "有些數據在程式執行中我們「不希望，也不允許」它被意外修改（例如：圓周率、伺服器的連線 URL、API 金鑰）。\n這時候，請端出 **`const`**（Constant 常數）：\n\n```javascript\nconst PI = 3.14159;\nPI = 3; // ❌ 報錯！TypeError: Assignment to constant variable.\n```\n\n- `const` 宣告時**必須立刻給予初始值**，不能留空。\n- 一經宣告，它的「記憶體位置綁定」就不可更變，提供了極佳的安全防護！",
            codeSnippet = "const BASE_URL = \"https://api.pycoach.com\";\n// 如果你寫 BASE_URL = \"other\"; 會導致系統報錯中斷！\nconsole.log(\"系統伺服器為:\", BASE_URL);",
            interactiveQuizQuestion = "下列關於 const 常數關鍵字的行為敘述，何者為真？",
            quizAnswers = listOf("可以只寫 `const myVar;` 不賦予任何初始值", "賦值之後若企圖重新指派新值，會引發錯誤並停止程式", "它是完全可以隨意用等號重新覆蓋基本值的一般變數", "它是指可以在全網域被 var 修改的意思"),
            correctQuizAnswerIndex = 1,
            quizExplanation = "const 宣告常數後，若重新賦值基本型態會遭遇錯誤（TypeError），這也正是它能維護架構不被亂改的好處。"
        ),
        CourseLesson(
            id = "js_6",
            title = "6. 傳統變數 var 及其避坑指南",
            subtitle = "理解 Hoisting 與全域污染的罪魁禍首",
            category = "變數安全",
            readTimeMinutes = 3,
            description = "探索舊時代的遺毒關鍵字 var。為什麼現代 JS 開發者要極力避免使用它？",
            explanationHtmlCode = "在 2015 年（ES6）之前，JavaScript 只有唯一一個變數宣告字：**`var`**。\n\n然而，`var` 具有非常麻煩的特性：\n1. **函數作用域（Function Scope）**：它不認識 `{}` 區塊。這代表它在 for 迴圈或 if 區塊宣告時，居然會外洩到外面去！\n2. **重複宣告不報錯**：不小心寫了兩次 `var x = 5;` 毫由警告，極易造成數據覆蓋慘劇。\n3. **變數提升（Hoisting）**：變數會在宣告前就以 `undefined` 姿態強行存在，寫起來非常混亂。\n\n**結論：請封印 var！一律使用 let 與 const。**",
            codeSnippet = "var oldWay = \"我是舊時代的 var\";\nvar oldWay = \"我又被宣告了一次，而且完全不報錯！\";\nconsole.log(oldWay);",
            interactiveQuizQuestion = "為什麼現代 JavaScript 開發實踐上，不推薦甚至禁止繼續使用舊時代的 var 關鍵字來宣告變數？",
            quizAnswers = listOf("Open 宣告 var 無法存儲字串資料", "因為 var 宣告出來的數值都是唯讀的", "因為 var 沒有區塊作用域，容易導致變數外洩或重複宣告的隱形臭蟲", "因為 var 在所有瀏覽器都被移除了"),
            correctQuizAnswerIndex = 2,
            quizExplanation = "var 缺乏區塊作用域（Block Scope）防護，容易洩漏與發生變數交叉污染。現代開發請完全改用 let & const。"
        ),
        CourseLesson(
            id = "js_7",
            title = "7. 七大數據基礎資料型態",
            subtitle = "認識數字、字串、布林、Null 與 Undefined",
            category = "JS 基礎入門",
            readTimeMinutes = 3,
            description = "解構 JavaScript 當中的基本原始型態，看看電腦眼中究竟存放著什麼形式的靈魂。",
            explanationHtmlCode = "JavaScript 雖然不用像 Java 那樣宣告型態（它是動態型態），但它內含這些基本型態：\n- **Number**：所有數字（整數、浮點數、負數）都是它。\n- **String**：文字字串，用 `\"\"`, `''`, 或 `` `` 包裹。\n- **Boolean**：只有 `true` 或 `false`。\n- **Undefined**：變數「已宣告但還沒給任何內容」的未定義狀態。\n- **Null**：主動刻意表達「此處空空如也，沒有物件」的空值。\n- **Symbol** 與 **BigInt**：較進階的獨特標記與超大整數。",
            codeSnippet = "let weight; // undefined\nlet isLogged = true; // Boolean\nlet price = 50.5; // Number\nconsole.log(\"型態檢查:\", typeof isLogged, typeof weight);",
            interactiveQuizQuestion = "當我們宣告了一個變數 `let userName;` 卻完全沒有給值（沒有用 = 賦值），此時這個變數內部的預設型態與值是什麼？",
            quizAnswers = listOf("null", "0", "undefined", "NaN"),
            correctQuizAnswerIndex = 2,
            quizExplanation = "在 JS 當中，宣告變數卻未進行任何指派與定義時，系統預設對它賦予的值與型態正是「undefined」。"
        ),
        CourseLesson(
            id = "js_8",
            title = "8. 數字運算：數學的精準齒輪",
            subtitle = "四則運算、餘數與次方的極限算術",
            category = "資料操作",
            readTimeMinutes = 3,
            description = "在 JS 當中快速完成數學加減乘除，並學習如何對餘數和平方進行超限取值。",
            explanationHtmlCode = "數字（Number）型態可以用以下標準運算子操作：\n- `+` (加法), `-` (減法)\n- `*` (乘法), `/` (除法)\n- `%` (取餘數，求除法之餘)\n- `**` (次方，求冪，例如 `2 ** 3` 為 8)\n\n**加分知識（NaN）**：\n如果你用非數字（例如字串 \"hello\"）去乘以數字 3，會得到 **`NaN`**（Not a Number），代表「非數字的無效數學運算結果」。",
            codeSnippet = "let a = 10;\nlet b = 3;\nconsole.log(\"餘數為:\", a % b);\nconsole.log(\"a 的二次方為:\", a ** 2);",
            interactiveQuizQuestion = "在 JavaScript 當中，執行 `const result = 7 % 3;` 最終 result 會得到什麼數字？",
            quizAnswers = listOf("2", "1", "0.5", "NaN"),
            correctQuizAnswerIndex = 1,
            quizExplanation = "7 除以 3 的商是 2，餘數是 1。因此取餘數運算子 (%) 回傳結果為 1。"
        ),
        CourseLesson(
            id = "js_9",
            title = "9. 字串與樣板字面值",
            subtitle = "驚艷靈活的反引號與 插值",
            category = "資料操作",
            readTimeMinutes = 3,
            description = "告別傳統的加號串接，進入現代 ES6 反引號字串，輕鬆將變數直接直覺地烙印在文字中！",
            explanationHtmlCode = "傳統上拼接字串和變數需要使用大量 `+` 號，寫起來非常累人且容易漏字眼：\n`\"Hello \" + name + \", we have \" + count + \" tasks.\"`\n\n現代 JavaScript 引進了超厲害的**樣板字面值（Template Literals）**：\n1. 外圍使用**反引號 \\`\\`** 包裹（在鍵盤 Tab 鍵上方）。\n2. 內部可以直接寫多行文字，且折行會被保留。\n3. 直接使用 **`\${變數或表達式}`** 語意插言，美觀乾淨！",
            codeSnippet = "const item = \"珍珠奶茶\";\nconst price = 65;\nconsole.log(`我點了一杯 \${item}，價格是 \${price} 元。`);",
            interactiveQuizQuestion = "在使用 ES6 樣板字面值（Template Literals）進行字串的插值時，必須使用哪一種外包裹引號與插值語法？",
            quizAnswers = listOf("雙引號 \", 搭配 #{var}", "單引號 ', 搭配 {var}", "反引號 `, 搭配 \${var}", "雙引號 \", 搭配 \$[var]"),
            correctQuizAnswerIndex = 2,
            quizExplanation = "必須使用反引號 `（Backtick）包裹整組字串，並使用 \${} 包裹變數。這比用加號拼接清晰百倍！"
        ),
        CourseLesson(
            id = "js_10",
            title = "10. 比較運算子與嚴格相等 === ",
            subtitle = "告別 == 帶來的隱形強制轉型陷阱",
            category = "資料操作",
            readTimeMinutes = 3,
            description = "學會比較大小，並了解為什麼在 JS 當中我們只能相信三個等號 === 的嚴格性。",
            explanationHtmlCode = "JavasScript 在比較時有兩種相等判斷：\n- **兩等號 `==` （寬鬆相等）**：會先企圖把兩個不同類別的值「偷偷轉型」再比。例如 `\"5\" == 5` 居然會回傳 `true`！這會造成非常難找的邏輯漏洞。\n- **三等號 `===` （嚴格相等）**：同時比對「數值」與「型態」，只要有一方不同，就直接判定為 false（`\"5\" === 5` 會回傳 `false`）。\n\n**強烈建議：未來撰寫代碼一律只用 `===` 與 `!==`！**",
            codeSnippet = "console.log(\"寬鬆比對:\", \"100\" == 100); \nconsole.log(\"嚴格比對:\", \"100\" === 100);",
            interactiveQuizQuestion = "在 JavaScript 程式碼中，執行 `\"7\" === 7` 與 `\"7\" == 7` 的運算結果分別會得到什麼布林值？",
            quizAnswers = listOf("前半段 false，後半段 true", "兩者都是 true", "兩者都是 false", "前半段 true，後半段 false"),
            correctQuizAnswerIndex = 0,
            quizExplanation = "because === 為嚴格比對，字串 \"7\" 與數字 7 型態不同，回傳 false；而 == 為寬鬆比對，會把字串轉型成數字後比對，回傳 true。"
        ),
        CourseLesson(
            id = "js_11",
            title = "11. 條件控制 if 語音區塊",
            subtitle = "程式邏輯的十字路口分流！",
            category = "流程控制與判斷",
            readTimeMinutes = 3,
            description = "學習如何使用 if 指導程式，只有當判定為真（True）時，才去執行特定的關鍵任務。",
            explanationHtmlCode = "條件控制就像是遊戲中「如果...就...」的分支控制。\nJavaScript 的 **`if`** 語法如下：\n\n```javascript\nif (條件) {\n    // 只有當條件是 true 時，才會進入並跑這對大括號內的邏輯\n}\n```\n\n注意：條件必須包裹在小括號 `()` 內，執行的指令必須用大括號 `{}` 括起來，並通常會進行縮排排版。",
            codeSnippet = "let age = 20;\nif (age >= 18) {\n    console.log(\"恭喜，您已具備法定投票資格！\");\n}",
            interactiveQuizQuestion = "在寫 JavaScript if 條件語句時，判別的 boolean 條件運算式，應該寫在什麼符號內部？",
            quizAnswers = listOf("中引號 [ ] 之中", "大括號 { } 之中", "小括號 ( ) 之中", "角括號 < > 之中"),
            correctQuizAnswerIndex = 2,
            quizExplanation = "if 後面緊鄰的條件表達式，必須牢牢寫在英文小括號 ( ) 內部才能被正確解析。"
        ),
        CourseLesson(
            id = "js_12",
            title = "12. 多重決策 else if",
            subtitle = "無限排他的條件鏈解構",
            category = "流程控制與判斷",
            readTimeMinutes = 3,
            description = "當不止一個條件需要篩選時，使用 else if 及 else 構成嚴密的多合一抉擇鍊。",
            explanationHtmlCode = "現實世界常有 A, B, C 三種以上的情況分流。我們可以用 `if` 搭配多重 **`else if`** 與最後的 **`else`** 進行防護：\n\n```javascript\nif (成績 >= 90) {\n    印出「甲等」;\n} else if (成績 >= 80) {\n    印出「乙等」;\n} else {\n    印出「準備補考」;\n}\n```\n\n- 一旦上方某個條件合格，下方所有的分支都會自動被跳過，只會由其中一條路徑抵達終點！",
            codeSnippet = "let temperature = 15;\nif (temperature > 28) {\n    console.log(\"天氣炎熱\");\n} else if (temperature >= 15) {\n    console.log(\"氣溫適宜\");\n} else {\n    console.log(\"冷颼颼！\");\n}",
            interactiveQuizQuestion = "在多重條件鏈 if ... else if ... else 當中，以下哪一條敘述是完全正確的？",
            quizAnswers = listOf("如果前面的 if 通過了，後面的 else if 依然會被強行執行一次", "else 區隔也需要跟 if 一樣在後面手動寫小括號判斷條件", "條件鏈一旦命中了其中一個符合的分支，剩餘的分支就會自動被忽略跳過", "else if 可以單獨存在，完全不需要前面的 if 引路"),
            correctQuizAnswerIndex = 2,
            quizExplanation = "條件鏈具有排他性。只要前面任何一項 True 執行完畢，其餘的 else if 都不會再被詢問或運行。"
        ),
        CourseLesson(
            id = "js_13",
            title = "13. 三元運算子的超速判斷",
            subtitle = "用一行代碼優雅取代 if-else 的技巧",
            category = "流程控制與判斷",
            readTimeMinutes = 3,
            description = "JavaScript 專屬超好用語法糖：條件 ? 符合值 : 不符合值。簡約度破表！",
            explanationHtmlCode = "寫 if-else 常要佔用 4, 5 行。如果只是要簡單根據布林值「回傳 A 或 B」：\n我們可以使用**三元運算子（Ternary Operator）**：\n\n`變數 = 條件 ? 值A : 值B;`\n\n- `?` 代表「是真的嗎？」\n- `:` 前面是 True 時的值，後面是 False 時的值。\n極度流行於現代開發！",
            codeSnippet = "let isMember = true;\nlet fee = isMember ? 50 : 100;\nconsole.log(\"需要繳納的費用是:\", fee);",
            interactiveQuizQuestion = "寫代碼 `const message = score >= 60 ? \"及格\" : \"當掉\";`，當 score 為 55 時，變數 message 會得到什麼文字？",
            quizAnswers = listOf("及格", "當掉", "55", "undefined"),
            correctQuizAnswerIndex = 1,
            quizExplanation = "因為 55 >= 60 為 false，所以會運作問號鏈冒號後面的第二個選項，得到「當掉」。"
        ),
        CourseLesson(
            id = "js_14",
            title = "14. 精準對等比對 switch...case",
            subtitle = "多重對等值比對的簡約美學",
            category = "流程控制與判斷",
            readTimeMinutes = 3,
            description = "當我們只需要精準對比單一變數「是不是某個具體常數或選項」時，switch 是最爽快的選擇。",
            explanationHtmlCode = "如果一直在 `else if (status === \"A\")`, `else if (status === \"B\")`，會顯得贅詞滿天飛。\n這時，可以使用專攻對等精準比對的 **`switch`**：\n\n```javascript\nswitch (變數) {\n    case \"選項1\":\n        做選項一的事;\n        break; // 必加！否則會繼續往下墜落執行\n    case \"選項2\":\n        做二的事;\n        break;\n    default:\n        所有其他事;\n}\n```",
            codeSnippet = "const role = \"admin\";\nswitch (role) {\n    case \"admin\":\n        console.log(\"完全權限者！\");\n        break;\n    case \"user\":\n        console.log(\"標準閱讀者\");\n        break;\n    default:\n        console.log(\"訪客安全登入\");\n}",
            interactiveQuizQuestion = "在 switch...case 的語法分支中，每一個 case 結論區段如果忘記加上 break，會發生什麼隱憂？",
            quizAnswers = listOf("程式會直接拋出崩潰致命錯誤而停擺", "程式會穿透當前 case 並連續執行下一個 case 的代碼，直到看見 break 或結束為止", "變數的值會自動歸零", "不能使用 default 行為"),
            correctQuizAnswerIndex = 1,
            quizExplanation = "這被稱為「Fall-through（穿透）」。如果沒有 break，程式會無視下一個 case 條件，直接霸道地跑完下一個 case 的所有指令，這是新手最容易犯的重大邏輯錯誤！"
        ),
        CourseLesson(
            id = "js_15",
            title = "15. 邏輯運算子 && 與 ||",
            subtitle = "短路評價（Short-circuit）的奧秘與應用",
            category = "流程控制與判斷",
            readTimeMinutes = 3,
            description = "在單一判斷式中混合多個條件！並學會 JS 強大實用的短路取值預設技巧。",
            explanationHtmlCode = "這兩大邏輯門是控制流程的靈魂：\n- **AND (`&&`)**：左右兩側都必須是 true，結果才成立。\n- **OR (`||`)**：左右兩側只要有一方是 true 就可以，常用來設定後備防线。\n\n**短路評價（Short-circuit）**：\n- `A || B`：如果 A 已經是 true（例如有定義），JS 會立刻回傳 A，右邊的 B 它連看都不看。這常被拿來用做「變數沒定義就套用預設值」的絕招：\n`let output = userName || \"匿名訪客\";`",
            codeSnippet = "let customName = \"\";\nlet finalName = customName || \"神祕玩家\";\nconsole.log(\"歡迎光臨！\", finalName);",
            interactiveQuizQuestion = "下列關於邏輯運算子 `&&` 與 `||` 運作特徵的描述，哪一項是完全屬實的？",
            quizAnswers = listOf("&& 只要其中一邊為 false 便直接斷定不成立，|| 則是只要有一邊為 true 即成立", "&& 只認字串運算，|| 只算數字運算", "使用 || 時，如果左右兩側都為 false，結果依然會被當作 true 生效", "兩者都只能在 HTML 代碼中撰寫"),
            correctQuizAnswerIndex = 0,
            quizExplanation = "&&（且）代表裝備同時符合；||（或）則提供了一種彈性，任意一邊有真（truthy）就會被放行通過，也是預設值設定的經典寫法。"
        ),
        CourseLesson(
            id = "js_16",
            title = "16. 邏輯否定與雙重否定 !!",
            subtitle = "快速將任何數值安全轉化為純 Boolean 的終極極簡術",
            category = "流程控制與判斷",
            readTimeMinutes = 3,
            description = "驚豔開發者的雙驚嘆號極簡流派！學習將隨意物件迅速變身為乾淨 Boolean 的偏方。",
            explanationHtmlCode = "我們知道驚嘆號 **`!`** 可以把 True 變 False，False 變 True。\n那如果是 **`!!`（雙重否定）** 呢？\n第一重 `!` 先將資料類型強制倒轉並轉成 Boolean。第二重 `!` 再轉回來。\n這神奇地把任何 JavaScript 當中的「真值」或「假值」轉成了正宗的 `true` 或 `false`！\n\n在 JS 中，`0`, `\"\"` (空字串), `null`, `undefined`, `NaN` 都屬於「假值 (falsy)」；其餘都是真值 (truthy)。",
            codeSnippet = "let userInput = \"小明\";\nconsole.log(\"用戶這有輸入內容嗎 ?\", !!userInput);\nlet emptyBox = \"\";\nconsole.log(\"空箱的 boolean 是:\", !!emptyBox);",
            interactiveQuizQuestion = "變數 `let active = !!0;` 在被執行完畢後，active 內含的布林值會是什麼？",
            quizAnswers = listOf("true", "false", "0", "undefined"),
            correctQuizAnswerIndex = 1,
            quizExplanation = "因為 0 是 Falsy（假值）。第一重非 `!0` 得到 true，第二重 `!true` 又轉回 false。因此 `!!0` 必定是布林值 `false`！"
        ),
        CourseLesson(
            id = "js_17",
            title = "17. 迴圈經典款：學會 standard for",
            subtitle = "計數器、停止條件與遞增公式三位一體",
            category = "迴圈重複",
            readTimeMinutes = 3,
            description = "掌握重複工作的根基。學習 JavaScript 非常嚴謹但也極具威力的三合一經典 for 經典款寫法。",
            explanationHtmlCode = "JS 的傳統 `for` 迴圈非常精巧，將控制迴圈運行的三個關鍵變數全部集結在小括號中：\n\n```javascript\nfor (初始化計數器; 停止判定條件; 每次遞增規則) {\n    // 重複要做的工作空間\n}\n```\n\n這三個組件用**英文分號 `;`** 連接：\n`for (let i = 1; i <= 3; i++) { ... }`\n- `let i = 1`：設置起點。\n- `i <= 3`：判定。如果是 True，跑大括號的代碼，然後累計。否則停止結業！\n- `i++`：將 i 每回累加 1 的語法糖簡寫（等同 `i = i + 1`）。",
            codeSnippet = "for (let i = 1; i <= 3; i++) {\n    console.log(\"跑第\", i, \"圈\");\n}",
            interactiveQuizQuestion = "執行代碼 `for (let i = 0; i < 5; i++)`，這個迴圈總共會被老老實實跑完、列印多少次？",
            quizAnswers = listOf("4 次", "5 次", "6 次", "0 次"),
            correctQuizAnswerIndex = 1,
            quizExplanation = "i 的值從 0 開始遞增：0, 1, 2, 3, 4，都符合 i < 5 的判斷；到達 5 時條件不合退出，所以總共老實運作 5 次。"
        ),
        CourseLesson(
            id = "js_18",
            title = "18. 條件驅動 while 循環",
            subtitle = "只要判定為真，就不斷重複執行的齒輪！",
            category = "迴圈重複",
            readTimeMinutes = 3,
            description = "當我們不知道重複的精準圈數，只希望能「在滿足某個基準前狂跑」就用 while 擔當重任。",
            explanationHtmlCode = "與 for 的計數特質不同，**`while`** 迴圈的哲学是：「只要給我的判斷條件依然是 `true`，我就會發瘋似地一直不停重整並跑下一圈！」\n\n```javascript\nwhile (條件) {\n    // 循環內容\n}\n```\n\n**安全警告**：如果大括號內部沒有手段能讓條件逐漸逼近 False（例如更新計數），程式就會引爆「無窮迴圈（Infinite Loop）」，導致網頁卡死崩潰！",
            codeSnippet = "let count = 1;\nwhile (count <= 3) {\n    console.log(\"綿羊 count =\", count);\n    count++; // 逼近關頭！極重要！\n}",
            interactiveQuizQuestion = "如果寫了 `let i = 1; while (i > 0) { console.log(i); }` 卻完全沒有變更 i 的內容，最可能的慘烈下場是？",
            quizAnswers = listOf("只印出一次 1 就正常功成身退", "程式崩潰，因為陷入了無限次迴圈把系統記憶體或 CPU 耗盡", "系統自動將 i 歸零來幫開發者阻斷錯誤", "完完全全不執行迴圈"),
            correctQuizAnswerIndex = 1,
            quizExplanation = "由於 i > 0 恆成立（為真），且迴圈內沒有任行削減 i 的動作，這會導致無限輸出、CPU 佔有率衝到 100% 導致瀏覽器凍結，這叫無窮迴圈。"
        ),
        CourseLesson(
            id = "js_19",
            title = "19. 先斬後奏：do...while",
            subtitle = "首輪保證。至少交付的工作姿態",
            category = "迴圈重複",
            readTimeMinutes = 3,
            description = "學習一種不管三七二十一，必須「第一輪先做、做完再看條件」的神奇控制手段。",
            explanationHtmlCode = "一般 if 或 while，一開頭如果判定是 False，那大括號的指令連一次都不會跑就被丟棄。\n但是 **`do ... while`** 排他性地保證了「至少跑 1 次」：\n\n```javascript\ndo {\n    // 一定會先執行這段代碼\n} while (滿足的繼續條件);\n```\n\n它會先無條件進入 do 的區塊執行，直到尾端才比對 conditional，如果是真就跑第二圈，否則依依不捨退役。",
            codeSnippet = "let status = false;\ndo {\n    console.log(\"這行話無論如何，即使條件是假，也會跑這唯一的一次！\");\n} while (status);",
            interactiveQuizQuestion = "do...while 迴圈與普通 while 迴圈在運作特質上，最實用且關鍵的不同差異位於哪裡？",
            quizAnswers = listOf("do...while 在任何環境下都只能列印出 10 次以上", "do...while 可以免除變數提升機制", "do...while 不管判定條件最初是否合格，均百分之百保證至少執行它的主區塊 1 次", "do...while 不能宣告局部變數"),
            correctQuizAnswerIndex = 2,
            quizExplanation = "這是 do...while 的核心設計使命。它採用「先執行、後判斷」的先斬後奏模式，提供至少 1 次執行的保障。"
        ),
        CourseLesson(
            id = "js_20",
            title = "20. 迴圈中斷與跳過 break / continue",
            subtitle = "掌握控制流，在任意關頭提前退出或跳躍",
            category = "迴圈重複",
            readTimeMinutes = 3,
            description = "學習如何像特工一樣，在滿足意外突發狀況時提前把整組迴圈震碎 (break) 或是瞬移到下一圈 (continue)。",
            explanationHtmlCode = "這兩個武器能賦予你更超維的迴圈控制權：\n- **`break`**：完全「震碎並中斷」當前迴圈，強制把迴圈程序終止，直接跳到整個 loop 的花括號外面去。\n- **`continue`**：本圈不跑了！「跳過剩餘代碼」，提前進入並加算下一個迴圈迭代周期。\n\n例如：在數到 3 時逃跑，或略過印出 4 等需求。",
            codeSnippet = "for (let i = 1; i <= 5; i++) {\n    if (i === 3) continue; // 跳過 3！\n    console.log(\"列印成員 i =\", i);\n}",
            interactiveQuizQuestion = "若在 `for (let i = 1; i <= 10; i++)` 迴圈中，當我們偵測到 `i === 4` 時執行了 `break;` 指令，那麼控制台總共會印出幾次數字？",
            quizAnswers = listOf("10 次", "4 次", "3 次", "5 次"),
            correctQuizAnswerIndex = 2,
            quizExplanation = "因為當 i 為 1, 2, 3 時，可以順利印出（3次）。當 i 來到 4 時，觸發 break 強制將整個 for 迴圈永久打破並永久終止。因此 4 及後面的數字都沒機會被印出，最後印出 3 次。"
        ),
        CourseLesson(
            id = "js_21",
            title = "21. 函式定義（Function Declaration）",
            subtitle = "重用代碼、封裝邏輯的底層結構",
            category = "函式魔力",
            readTimeMinutes = 3,
            description = "告別亂抄複製貼上的低效開發。將一堆有邏輯的代碼封裝到一台機器內，隨時呼叫即用！",
            explanationHtmlCode = "如果你發現自己寫了三次「算打折、算稅金、印歡迎詞」類似的操作，請把他們封裝成**函式（Function）**！\n使用關鍵字 **`function`**：\n\n```javascript\nfunction sayHello() {\n    console.log(\"你好！\");\n    console.log(\"現在是學習時刻！\");\n}\n\nsayHello(); // 呼叫它！它就會把打包的代碼都跑一遍\n```",
            codeSnippet = "function greeting() {\n    console.log(\"---- 開啟學習引擎 ----\");\n}\ngreeting(); // 呼叫了！\ngreeting(); // 第二次重用呼叫！",
            interactiveQuizQuestion = "在 JavaScript 當中，宣告一個有名稱、可以重複使用的經典函式模組，需要哪一個關鍵字引路？",
            quizAnswers = listOf("def", "func", "function", "define"),
            correctQuizAnswerIndex = 2,
            quizExplanation = "Python 使用的是 def，而在 JavaScript 歷史中最純正的正宗命名即是 function 關鍵字。"
        ),
        CourseLesson(
            id = "js_22",
            title = "22. 參數傳遞與 Return 返回值",
            subtitle = "函式的輸入漏斗與結果最終出口",
            category = "函式魔力",
            readTimeMinutes = 3,
            description = "解密如何給函式餵入原料（參數），並如何用 return 高效率地掏出最後處理成果！",
            explanationHtmlCode = "好函式應該能「加工」，這需要**參數（Arguments）**與**返回值（Return）**：\n\n- **參數**：寫在函式名稱後面的括號裡，像變數等著原料匯入：`multiply(x, y)`\n- **返回（`return`）**：將加工完畢的結果發送回家。**注意：一旦執行了 `return` 句子，函式會立刻終止吐出結果並抽身離場！** 後方的句子全都不會再被執行！",
            codeSnippet = "function add(x, y) {\n    return x + y;\n    console.log(\"這行絕對跑不到，因為前面已經 return 退伍了！\");\n}\nlet total = add(5, 7);\nconsole.log(\"加總得到:\", total);",
            interactiveQuizQuestion = "當一個函式執行到含有 `return` 的那一行命令時，該函式接著會發生什麼事？",
            quizAnswers = listOf("暫停一下，等整個程式結束才把數值印出來", "立即發送回傳值、並立刻停止執行該函式後續的所有邏輯抽身離開", "拋出編譯中斷信號", "自動進入下一個 for 迴圈"),
            correctQuizAnswerIndex = 1,
            quizExplanation = "return 是函式的煞車與成果出口。當函式遇到了 return，它會立即把成果拋出去，並立刻停止該函式的後續代碼運行。"
        ),
        CourseLesson(
            id = "js_23",
            title = "23. 函式表達式與參數預設值",
            subtitle = "將函式指派給變數，並配置防摔預設參數",
            category = "函式魔力",
            readTimeMinutes = 3,
            description = "在 JS 當中，函式也是一等公民（First-Class）。你可以把函式像值一樣傳送、並避免使用者忘記傳參數造成的 NaN 崩潰！",
            explanationHtmlCode = "有兩種高階 JS 玩法：\n1. **函式表達式（Function Expression）**：直接把匿名函式用等號塞給變數。\n`const hello = function() { ... };`\n2. **參數預設值（Default Parameters）**：如果呼叫時，對方忘記傳參數（這通常會倒退成 undefined 造成數值壞掉），可以主動指派初始預設配方：\n\n```javascript\nfunction multiply(a = 1, b = 1) { ... }\n```",
            codeSnippet = "const multiply = function(a = 2, b = 3) {\n    return a * b;\n};\nconsole.log(\"忘記傳 b 的結果:\", multiply(5)); // 自動採用預設 b=3",
            interactiveQuizQuestion = "在宣告函式 `function greet(name = \"訪客\") { ... }` 時，若呼叫該函式不傳入任何參數 `greet();`，內部變數 name 的值會得到什麼？",
            quizAnswers = listOf("undefined", "null", "NaN", "訪客"),
            correctQuizAnswerIndex = 3,
            quizExplanation = "這就是設有參數預設值的神奇保障。由於沒傳送參數，它會極速辨識並套用我們指定的預設值 \"訪客\"。"
        ),
        CourseLesson(
            id = "js_24",
            title = "24. 經典與極縮箭頭函式 Arrow Function",
            subtitle = "極簡風格的語法糖與獨特的 lexical this",
            category = "函式魔力",
            readTimeMinutes = 3,
            description = "掌握現代 JS 首屈一指的簡約高階神器：`const fn = () => ...`。縮短行數必看精品！",
            explanationHtmlCode = "ES6 規格中最耀眼的，莫過於**箭頭函式（Arrow Function）**。它是 `function` 指令的極致縮寫：\n\n- **基本款**：把 function 去掉，在圓括弧和花括弧中間架設 `=>` 箭頭。\n`const add = (a, b) => { return a + b; };`\n- **單行極縮**：如果只有一行回傳且沒有複雜句子，可以直接**不寫花括弧 `{}`、免去 `return` 關鍵字**，直接放答案，JS 會霸道地自動幫你回傳！\n`const add = (a, b) => a + b;`",
            codeSnippet = "const doubleValue = x => x * 2;\nconsole.log(\"箭頭速算雙倍 10:\", doubleValue(10));",
            interactiveQuizQuestion = "關於單行極縮寫法的箭頭函式 `const square = x => x * x;`，哪一個描述是正確的？",
            quizAnswers = listOf("它需要補寫 return 關鍵字否則沒人理它", "它不需要括號跟 `{}` 以及 return 關鍵字，會在運行時自動隱性將算式結果 return 出去", "這不是一個合法的 JS 語法，會立即報錯", "它只能用 var 來執行宣告"),
            correctQuizAnswerIndex = 1,
            quizExplanation = "單行無花括號箭頭函數會隱性（implicitly）自動 return 該表達式的值。這功能讓 JS 高速處理過濾或迴圈時，代碼乾淨如詩！"
        ),
        CourseLesson(
            id = "js_25",
            title = "25. 全域變數與區域變數 Scope",
            subtitle = "全域變數與區域變數的生存界線與生命週期",
            category = "作用域與升格",
            readTimeMinutes = 3,
            description = "變數不是在哪裡都能被看到的！了解 Scope 讓你防範「變數沒定義」或「名稱重疊污染」的大錯！",
            explanationHtmlCode = "程式中變數的看見權益叫做**作用域（Scope）**：\n- **全域作用域（Global Scope）**：宣告在檔案最外層，任何一個函式、迴圈都能輕易讀取與修改它。但寫太多全域會使得程式無比混亂，變數命名瘋狂打架。\n- **區域作用域（Local Scope）**：宣告在函式內部的變數。它們是函式的「私人財產」，一旦函式跑完，這些變數就會被銷毀，外部沒人能看得到！",
            codeSnippet = "let globalHero = \"鋼鐵人\";\nfunction changeHero() {\n    let localHero = \"蜘蛛人\";\n    console.log(\"內部可見:\", localHero, \"與\", globalHero);\n}\nchangeHero();\n// 如果你在此時 console.log(localHero); 就會報錯 ReferenceError！",
            interactiveQuizQuestion = "在某個函式內宣告的變數 `let secret = \"123\";`，在該函式執行完畢退出的最外層，能不能安全地使用 console.log(secret) 印出該字串？",
            quizAnswers = listOf("當然可以，它是 global 的任何地方隨便看", "絕對不能！因為它是該函式的私有成員（區域變數），函式跑完即被回收，外部讀取會噴出 reference 錯誤", "會印出 10 次 123", "程式不報錯但會強制變為 null"),
            correctQuizAnswerIndex = 1,
            quizExplanation = "區域（私有）變數只活在函式的專屬記憶中，當函式調用語法完結，這些記憶體即被釋放，外部讀取會噴出 ReferenceError 未定義錯誤。"
        ),
        CourseLesson(
            id = "js_26",
            title = "26. 區塊作用域 Block Scope",
            subtitle = "徹底理解 let 與 const 的花括號防盗欄",
            category = "作用域與升格",
            readTimeMinutes = 3,
            description = "自 ES6 起革命性的 Block Scope 被引入了。一探 {} 是如何為我們的代碼設立防火牆的。",
            explanationHtmlCode = "在 JS 的現代哲學中，凡是用 **`{}`** 包起來的，都是一堵高聳、安全的高台防火牆（包括：`if` 的 `{}`，`for` 的 `{}`，甚至一個單純的 `{}` 面板）。\n\n- **`let` 與 `const` 具有區塊作用域**。只要變數是在大括號內部宣告的，高牆外面誰也摸不著它！\n- **相較之下**：舊遺毒 `var` 沒有 Block Scope 特徵。如果你在大括號內寫 `var test = 5;`，它居然會在高牆外面被印出來，造成嚴重的程式命名隱患！",
            codeSnippet = "if (true) {\n    let blockHero = \"蝙蝠俠\";\n    var varHero = \"小丑\";\n}\nconsole.log(\"var 外洩成功:\", varHero); \n// console.log(blockHero); // ❌ 報錯！let 沒有外洩",
            interactiveQuizQuestion = "以下哪一項是關於 let/const 具有區塊作用域（Block Scope）防護的精準實踐總結？",
            quizAnswers = listOf("變數會一直滲出，到哪裡都能看見", "利用 let/const 宣告的變數在 `{ }` 花括號以外的地方是讀取不到的，從而保護了變數不會被外部意外染指", "只要有 `{}` 程式就一定不給進行數學計算", "只有 class 才能在 JS 運行"),
            correctQuizAnswerIndex = 1,
            quizExplanation = "這是 let 與 const 比傳統 var 萬倍安全的理由。它利用大括弧將作用域限制在局部區塊中，保證內部運作不易污染外部命名。"
        ),
        CourseLesson(
            id = "js_27",
            title = "27. 變數提升 Hoisting 機制",
            subtitle = "解密 JavaScript 為何能先呼叫後宣告？",
            category = "作用域與升格",
            readTimeMinutes = 3,
            description = "解開 JavaScript 引擎在編譯期，將變數和函式宣告「偷偷拉升到頂部」運作之奇特魔術謎底。",
            explanationHtmlCode = "寫 JS 時常遇到奇特的事：函式還沒在源碼中宣告，我們就已經先執行它了，而居然能跑通？\n這就是**提升（Hoisting）**：\n- JavaScript 在準備執行代碼前，會先整檔掃描並把所有 `function` 宣告與 `var` 提升到該作用域的「最頂部」。\n- **重要關頭**：用 `let` 與 `const` 宣告的變數「也會被提升」，但因為它們具有一個稱為 **暫時性死區（TDZ, Temporal Dead Zone）** 的阻斷機制，在代碼落到真正宣告行之前，你讀取它們依然會遭遇 ReferenceError 警告，捍衛了安全的調用順序！",
            codeSnippet = "console.log(\"先提早使用 let 會卡在 TDZ 防禦，強制報錯以保護寫法正確性！\");",
            interactiveQuizQuestion = "為什麼在使用 let 宣告變數 `let nickname = 'A';` 之前的第 1 行直接輸出 console.log(nickname) 會迎來紅牌錯誤？",
            quizAnswers = listOf("因為 nickname 是一個常數不能印", "因為 JS 傲嬌地不給宣告", "因為該變數正處於「暫時性死區（TDZ）」安全網內，嚴格禁止在源代碼實際宣告前先行調用", "必須先將 JS 轉換成 HTML"),
            correctQuizAnswerIndex = 2,
            quizExplanation = "雖然 let 有變數提升，但因為 TDZ 的防火牆防衛，凡是在尚未執行到 `let x` 宣告的那行前，任何去存取它的人，都會伴隨安全性的 TDZ 錯誤，這是 ES6 保護語序的重要手段。"
        ),
        CourseLesson(
            id = "js_28",
            title = "28. 超神奇的閉包 Closure 核心精髓",
            subtitle = "活在函式記憶體中的專屬保護狀態",
            category = "進階函數",
            readTimeMinutes = 4,
            description = "探索 JS 享譽全球的面試王者：閉包。它是如何讓一個區域變數「超越生死、常駐內部狀態」運作的？",
            explanationHtmlCode = "一般來說，函式跑完裡面的局部變數就被撕毀了。但如果：\n**一個函式內部，又返回了另一個內部子函式**！\n而這個子函式，又引用了母函式的臨時變數！\n只要這個子函式還保留著，那被引用的內部變數就不會被記憶體回收！這就叫**閉包（Closure）**：\n\n```javascript\nfunction makeCounter() {\n    let count = 0;\n    return () => { count++; return count; };\n}\n```\n\n你可以做出完完全全「私有化、只有這組函式知道怎麼改」的變數保險箱！",
            codeSnippet = "function counter() {\n    let num = 0;\n    return () => {\n        num++;\n        return num;\n    };\n}\nconst next = counter();\nconsole.log(\"首次累計計數器名額:\", next());\nconsole.log(\"再次累加，變數在記憶中未消失:\", next());",
            interactiveQuizQuestion = "閉包（Closure）是 JavaScript 中非常強力的功能，以下哪項敘述是它形成的本質原因與用途？",
            quizAnswers = listOf("用來將變數強制儲存到硬碟檔案中", "內部函式保留了對包裹它的外部函式變數的引用，即使外部函式已完成調用釋放，狀態依然被活存防衛", "純粹強迫程式運作 10 次以上", "用來防止 for 迴圈發生錯誤"),
            correctQuizAnswerIndex = 1,
            quizExplanation = "閉包本質就是：一個即使外部函式退役，內部函式仍然緊扣並能存取外部函式作用域變數的現象。它常用於狀態私有化與進階封裝中！"
        ),
        CourseLesson(
            id = "js_29",
            title = "29. 立即執行函式 IIFE",
            subtitle = "拋棄式自封裝程式，避免污染全局命名空間",
            category = "進階函數",
            readTimeMinutes = 3,
            description = "學會一行源碼就「宣告完立刻自爆發跑」的神祕括弧：(function() { ... })()！",
            explanationHtmlCode = "有時我們只是想完成一些一次性、初始化、跑完就不要有人知道的程式。如果宣告一個普通函式，它就會在 global 的命名單上一直佔用一個名字，很容易和別處打架。\n這時用 **立即執行函式（IIFE, Immediately Invoked Function Expression）** 封裝：\n```javascript\n(function() {\n    let temp = \"一次性臨時變數\";\n    console.log(\"初始化完畢：\", temp);\n})(); // 尾端的 () 強迫它立刻跑！\n```\n它對外面的世界不留任何名號，實現了完美的拋棄式隱匿！",
            codeSnippet = "(function() {\n    const secureKey = \"S3cr3t\";\n    console.log(\"IIFE 安全初始化了密鑰 (無法自外部存取)\");\n})();",
            interactiveQuizQuestion = "關於 IIFE（立即調用函式表達式）的語法細節特質，下列哪一項是其最主要的工程使命？",
            quizAnswers = listOf("強制使電腦重新啟動", "用來使非同步數據可以被讀到", "在定義的當下立即被執行，且可將內部變數關死在 scope 內、避免造成全域變數命名污染", "它可以讓 code 在瀏覽器跑得快兩倍"),
            correctQuizAnswerIndex = 2,
            quizExplanation = "IIFE 利用前後的圓括弧將函式包裹成一個表達式，最後再加一對 `()` 立即運行。跑完即謝幕，是防範全域變數被無差別污染的經典實用方法。"
        ),
        CourseLesson(
            id = "js_30",
            title = "30. 遞迴函數自我呼叫",
            subtitle = "用自己解決自己，解構分治樹狀型資料",
            category = "進階函數",
            readTimeMinutes = 3,
            description = "學習函式深不可測的一面：遞迴 (Recursion)。它能神奇地在體內重複呼叫自己來解題！",
            explanationHtmlCode = "當你有一個複雜的大問題，比如求階乘，或者需要遍尋一個複雜的樹狀資料角節時。\n你可以設計一個**遞迴（Recursive Function）**：\n- **終止基準點（Base Case）**：當達到某個底線，立刻回傳具體數值停止。這非常重要！否則呼叫自己會無限遞進把 Call Stack 卡爆崩潰！\n- **遞歸關係**：將問題縮小，拿縮小版的自己再次加工呼叫：\n```javascript\nfunction factorial(n) {\n    if (n === 1) return 1; \n    return n * factorial(n - 1);\n}\n```",
            codeSnippet = "function countDonwn(n) {\n    if (n <= 0) return;\n    console.log(\"開太空火箭倒數:\", n);\n    countDonwn(n - 1);\n}\ncountDonwn(3);",
            interactiveQuizQuestion = "當我們在編寫遞迴函式（呼叫自己）時，哪一個構成要素是不可或缺的防護，以防止遭遇堆疊溢位（Stack Overflow）程式卡死慘禍？",
            quizAnswers = listOf("必須使用 let 替代 const 宣告", "必須配置一個明確用來跳出、不再遞歸的「終止基準點（Base Case）」", "必須要有 HTML 文件存在", "必須在結尾省略 return"),
            correctQuizAnswerIndex = 1,
            quizExplanation = "遞迴如果沒有設立「Base Case（基本終止條件）」，函式就會一回又一回鑽向世界末日呼叫自體，最後把系統記憶體的 Call Stack 榨乾並卡死報錯。"
        ),
        CourseLesson(
            id = "js_31",
            title = "31. 陣列入門：數據高鐵",
            subtitle = "有順序的多維度數據儲存結構",
            category = "複合資料結構",
            readTimeMinutes = 3,
            description = "介紹程式中用來存放「連續、有順序物品」的黃金大紙箱：陣列（Array）。",
            explanationHtmlCode = "如果我們有 100 個商品的名稱，總不能寫 `name1`, `name2` 寫到瘋掉。這就需要我們的頂級資料結構：**陣列 (Array)**！\n- 語法：中括弧 `[]` 包起，項目以逗號區隔。\n- 索引定位：索引是由 **`0`** 開始（**0-based Index**）！\n- 讀取寫法：`fruits[0]`\n- 取得總長度面值：`fruits.length`",
            codeSnippet = "const colors = [\"紅\", \"綠\", \"藍\"];\nconsole.log(\"最後一個成員是:\", colors[2]);\nconsole.log(\"陣列總共含有:\", colors.length, \"個顏色\");",
            interactiveQuizQuestion = "宣告一個 JavaScript 陣列為 `const arr = ['A', 'B', 'C'];`，則執行 `arr[1]` 時可以精確讀取得到什麼字串數值？",
            quizAnswers = listOf("A", "B", "C", "undefined"),
            correctQuizAnswerIndex = 1,
            quizExplanation = "因為陣列的索引是從 0 開始盤算。arr[0] 為 'A'，arr[1] 為 'B'，arr[2] 為 'C'。"
        ),
        CourseLesson(
            id = "js_32",
            title = "32. 陣列必學：push, pop, shift, unshift",
            subtitle = "陣列頭尾快速堆疊與隊列成員添加刪除",
            category = "複合資料結構",
            readTimeMinutes = 3,
            description = "掌握陣列世界最核心、最好用的高難度操控引擎：快速在頭尾加入與踢掉資料！",
            explanationHtmlCode = "要讓陣列能夠像活體一樣生長，必須精熟這四大極限函式：\n- **`push(值)`**：在陣列的**最尾端**塞入一個新學員，陣列長度長大。\n- **`pop()`**：把**最尾端**的最後一名學員吐出來剔除，並回傳其值。\n- **`unshift(值)`**：到陣列的**最前端**塞入，全部索引自動後移。\n- **`shift()`**：踢掉**最前方**第一個學員，全部索引自動前移。",
            codeSnippet = "let users = [\"小明\", \"阿華\"];\nusers.push(\"美美\");\nconsole.log(\"加入美美後的陣列:\", users);\nlet leftUser = users.pop();\nconsole.log(\"被吐掉踢出的尾部成員是:\", leftUser);",
            interactiveQuizQuestion = "執行這組程式碼 `let box = [1, 2]; box.push(3); box.shift();`，最終 box 當中還殘留著哪些數字項目？",
            quizAnswers = listOf("[1, 2, 3]", "[2, 3]", "[1, 2]", "[3]"),
            correctQuizAnswerIndex = 1,
            quizExplanation = "box 原本為 [1, 2]。執行 box.push(3) 尾巴塞入後變成 [1, 2, 3]。執行 box.shift() 剔除頭部第一個成員 1 號後，剩下 [2, 3]."
        ),
        CourseLesson(
            id = "js_33",
            title = "33. 切片與剪接 slice, splice",
            subtitle = "拷貝局部成員，或是以手術精準改寫陣列結構",
            category = "複合資料結構",
            readTimeMinutes = 3,
            description = "學習如何在不傷害原陣列下抽取成員的 slice，以及像手術刀一樣強行切除添加任意項目到陣列中央的 splice！",
            explanationHtmlCode = "這兩大函式名字長得像，但功能截然不同、是進階操作的分水嶺：\n- **`slice(start, end)`**：【非破壞性】。就像切吐司一樣，只「拷貝並複製」一部分出來，原本的陣列依舊完好無缺。\n- **`splice(start, deleteCount, items...)`**：【破壞性手術】。會「徹底拔除」並修改原本的陣列，甚至能在此插入全新的項目去填補坑位、強烈威猛！",
            codeSnippet = "let food = [\"蘋果\", \"香蕉\", \"草莓\", \"芒果\"];\nlet sliced = food.slice(1, 3);\nconsole.log(\"非破壞切出的局部:\", sliced);\nconsole.log(\"原陣列安然無損嗎 ?\", food);",
            interactiveQuizQuestion = "當我們希望在陣列 `const list = [10, 20, 30];` 的中間，指定在 index 1 刪除 1 個項目並在該處加回數字 99，我們應該端出哪一項強力武器操作？",
            quizAnswers = listOf("list.slice(1, 1, 99)", "list.splice(1, 1, 99)", "list.unshift(99)", "list.pop()"),
            correctQuizAnswerIndex = 1,
            quizExplanation = "編寫對陣列內部直接進行破壞性新增、刪除或是替換操作時，只有威力強大的 `splice` 可以完美擔綱此重大任務。"
        ),
        CourseLesson(
            id = "js_34",
            title = "34. 物件字面值（Object Literals）",
            subtitle = "打造結構富饒、帶有 Key-Value 語意的寶格",
            category = "複合資料結構",
            readTimeMinutes = 3,
            description = "不依靠死板的數字索引！學習隨意定義帶有各種標籤標示（例如姓名、年齡）的一對多關聯體：Object！",
            explanationHtmlCode = "如果我們有像學生一般的複合資料（例如名字叫小明、身高 170 釐米）。\n這時候，用**物件（Object）**再合適不過：\n\n```javascript\nconst student = {\n    name: \"小明\",\n    height: 170,\n    isGraduate: false\n};\n```\n\n- 使用大括號 `{}` 宣告。\n- 讀取方法：直接使用**點運算子** `student.name` 或括弧 `student[\"name\"]`，充滿了人性化直覺！",
            codeSnippet = "const coach = {\n    name: \"Pycoach AI\",\n    skills: [\"Python\", \"JavaScript\"],\n    level: 5\n};\nconsole.log(\"我們的教練名稱是:\", coach.name);",
            interactiveQuizQuestion = "在宣告 JavaScript 物件時，物件各成員之間，是利用哪一種精準符號對來連結「鍵（Key）」與其各自的「值（Value）」的？",
            quizAnswers = listOf("問號 ?", "等號 =", "冒號 :", "減號 -"),
            correctQuizAnswerIndex = 2,
            quizExplanation = "物件內部的屬性是採 `Key: Value` 冒號映射模式定義，多屬性間則用英文逗號隔開！"
        ),
        CourseLesson(
            id = "js_35",
            title = "35. 類別方法與 this 指針",
            subtitle = "在物件中執行屬於自己的專屬任務",
            category = "複合資料結構",
            readTimeMinutes = 3,
            description = "將函式放到物件內部去，使其成為物件自身的「動作（Method）」，並用神奇的 this 讀取自家成員！",
            explanationHtmlCode = "物件內不只可以放靜態變數，也可以存放「函式」（這在物件中稱為 **Method（方法）**）。\n方法內要調用自家的其餘數據時，可以使用神秘關鍵字 **`this`**。`this` 會自動代表「當前正在被呼叫的那個物件主體本人」！\n\n```javascript\nconst dog = {\n    name: \"豆豆\",\n    bark() { return `\${this.name} 汪汪叫！`; }\n};\n```",
            codeSnippet = "const user = {\n    name: \"小華\",\n    points: 10,\n    punch() {\n        this.points += 5;\n        console.log(this.name, \"打卡！積分變為:\", this.points);\n    }\n};\nuser.punch();",
            interactiveQuizQuestion = "在物件的方法中，若要指向引用該物件自身的其他欄位屬性（例如物件內另外宣告好的 name），需要使用什麼前綴關鍵字？",
            quizAnswers = listOf("it", "self", "this", "me"),
            correctQuizAnswerIndex = 2,
            quizExplanation = "this 關鍵字在方法執行時，通常會代表「正在呼叫與執行該方法的那個物件本身」，因此可以用 this.property 讀取自己的屬性。"
        ),
        CourseLesson(
            id = "js_36",
            title = "36. 迭代新紀元 array.forEach",
            subtitle = "告別 for 迴圈，用更直覺的 callback 迭代成員",
            category = "高階數組處理",
            readTimeMinutes = 3,
            description = "解鎖高階 JS 風格：forEach。讓我們直接優雅地宣告一個 Callback 函式，對整組資料展開大掃描！",
            explanationHtmlCode = "傳統 `for` 迴圈需要繁雜地計算 `i = 0; i < len; i++`。這實在太繁瑣甚至常寫錯界線！\n現代 JS 引進陣列專屬的遍歷功能 **`forEach`**：\n\n```javascript\nnames.forEach((item, index) => {\n    // 陣列內的每一個成員，都會被代入並執行這個 Callback 函式一次\n});\n```\n- 它會自動由頭到尾遍訪，並把當前項目 and 其索引主動餵給你的 Callback！乾淨俐落！",
            codeSnippet = "const heroes = [\"孫悟空\", \"豬八戒\", \"沙悟淨\"];\nheroes.forEach((hero, idx) => {\n    console.log(`第 \${idx + 1} 位出發隊員是: \${hero}`);\n});",
            interactiveQuizQuestion = "使用陣列的 .forEach(...) 方法遍歷所有成員時，我們必須傳入哪一種東西作為 forEach 的呼叫參數？",
            quizAnswers = listOf("一個數字長度", "一個用來處理每個成員的「Callback 函式」", "一個全新的 HTML 字串", "一次 true/false 判斷式"),
            correctQuizAnswerIndex = 1,
            quizExplanation = ".forEach() 需要傳入一個 Callback 函式。陣列有幾個成員，該 Callback 函式就會被背地裡呼叫幾遍，並將當前處理的項目傳給它。"
        ),
        CourseLesson(
            id = "js_37",
            title = "37. 數組投影轉型 array.map",
            subtitle = "給所有成員進行轉化投影，生成全新陣列",
            category = "高階數組處理",
            readTimeMinutes = 3,
            description = "如何對陣列中每項物品進行「轉型/計算/加料」，並完全不破壞原陣列、吐出一個全新的轉化加工陣列？",
            explanationHtmlCode = "當我們想把目前一組含有台幣價格的陣列，全部換算成美金計價時！\n一般作法又是開空 Array 跑 logical ... 太累了！\n這正是高階函數家族 **`map`** 的秀場：\n\n- **`map`** 的 Callback 會針對每項成員，**必須 return 一個計算過後的全新配方**。\n- `map` 收集齊備全部 Callback 指派的值後，會凝聚出一條**嶄新長度一模一樣的嶄新陣列**拋出，極度好用！",
            codeSnippet = "const prices = [100, 200, 300];\nconst discountPrices = prices.map(price => price * 0.9);\nconsole.log(\"打九折後的全新陣列為:\", discountPrices);\nconsole.log(\"原本的原生 prices 依然無損害:\", prices);",
            interactiveQuizQuestion = "關於 array.map(...) 的高階函數行為，下列哪一個觀念描述是百分之百正確的？",
            quizAnswers = listOf("map 會把符合條件的過濾刪除，讓陣列變短", "map 常被用來計算陣列總和並只回傳一個數字", "map 會遍歷並轉換原陣列的每一項，回傳一個具有轉換後新值、且長度相同的新陣列", "map 只在 Python 中才支持"),
            correctQuizAnswerIndex = 2,
            quizExplanation = "map（映射）的核心職責，就是「加工一比一轉換」。把原本的 A 成員投影成 B，組裝成嶄新長度對等但內容轉化後的新陣列返回。"
        ),
        CourseLesson(
            id = "js_38",
            title = "38. 精準過濾篩選 array.filter",
            subtitle = "設立高門檻，只保留符合條件的成員",
            category = "高階數組處理",
            readTimeMinutes = 3,
            description = "當你想把垃圾、低分、或者不符合當前搜尋對象的資料剔除，filter 將幫你秒速挑出電子高階隊伍！",
            explanationHtmlCode = "當我們只想保留一個學生列表中「分數大於等於 60 分（及格）」的人時。\n高階函數 **`filter`** 就是頂級過濾器：\n- 其 Callback 必須要 return 一個「**布林值（true/false）**」。\n- 如果 return 了 `true`，該成員就會被幸福安全地留下來，順利收編到新陣列中！\n- 如果 return `false`，項目將會在過濾中剔除不留痕跡！",
            codeSnippet = "const scores = [45, 90, 52, 78, 95];\nconst passScores = scores.filter(score => score >= 60);\nconsole.log(\"及格的精兵陣列:\", passScores);",
            interactiveQuizQuestion = "使用 array.filter(...) 進行過濾時，我們傳入的 Callback 傳回哪一種內容，即可決定將當前項目「留取」在回傳的新陣列中？",
            quizAnswers = listOf("一個字串", "一個數值", "當 Callback 計算回傳布林值 true 時，即視為保留該項目", "回傳 null"),
            correctQuizAnswerIndex = 2,
            quizExplanation = "filter() 的原理就是：只有滿足 Boolean 條件判定為 true（Truthy）的那幾項，才會在最終結果中被順利撈取，形成過濾後的全新子陣列。"
        ),
        CourseLesson(
            id = "js_39",
            title = "39. 數組累提煉 array.reduce",
            subtitle = "橫掃陣列，提煉成單一統計數值的終極武器",
            category = "高階數組處理",
            readTimeMinutes = 3,
            description = "解開高階陣列運算金字塔尖的 reduce：它是如何將百萬筆陣列數據，神奇地累加/合流縮減成唯一的代表值？",
            explanationHtmlCode = "如果我們想算一筆購物車內所有商品的總金額、或者求字串頻率！\n最狂高階大招莫過於：**`reduce`**（歸納/縮減）：\n\n```javascript\nconst sum = arr.reduce((accumulator, current) => {\n    return accumulator + current;\n}, 0); // 這裡的 0 是初始值\n```\n- `accumulator`（累加器）：像是記帳本，每圈 Callback 回傳的值，會直接變成下圈累加器的基準。\n- `current`（當前成員）：陣列當前巡訪到的項目。\n- 跑完後，只會吐出唯一的一個最後統計值！",
            codeSnippet = "const cartPrices = [500, 1000, 150];\nconst totalBill = cartPrices.reduce((total, p) => total + p, 0);\nconsole.log(\"總共需要結帳金額:\", totalBill);",
            interactiveQuizQuestion = "在 array.reduce((total, item) => ..., 0) 當中，小括號後方逗號多寫的那個「0」扮演著什麼角色？",
            quizAnswers = listOf("宣告變數的名字", "用來強迫程式退出", "設定累加器（total）的起始累計值", "設定陣列索引上限"),
            correctQuizAnswerIndex = 2,
            quizExplanation = "reduce 後方的第二個參數是用來代表「初始值（Initial Value）」。一般加總會設定為 0，這也是累加器在最第一圈運行時的 total 初始值。"
        ),
        CourseLesson(
            id = "js_40",
            title = "40. 解構賦值與展開運算子 ...",
            subtitle = "極具現代感的陣列/物件拆解與拷貝合併技巧",
            category = "高階數組處理",
            readTimeMinutes = 3,
            description = "學習現代最酷的語法：解構賦值與三個點的展開運算子（Spread Operator）！一秒合併陣列！",
            explanationHtmlCode = "ES6 規格讓我們可以直接用超帥的寫法拆解與組裝資料：\n1. **解構賦值（Destructuring）**：\n`const [a, b] = [10, 20];`（一秒讓 a 得到 10，b 得到 20！）\n2. **展開運算子 `...`**：\n它可以向撒豆子一樣，將陣列/物件內部成員「完全灑出來」！\n- 用於合併：`const merged = [...arr1, ...arr2];`\n- 用於拷貝：`const copy = {...oldObj};`",
            codeSnippet = "const listA = [1, 2];\nconst listB = [3, 4];\nconst combined = [...listA, ...listB];\nconsole.log(\"合流展開後全新數組:\", combined);",
            interactiveQuizQuestion = "執行這行 JavaScript 程式 `const [first, second] = ['蘋果', '橘子'];`，這時變數 second 會得到哪一個字串？",
            quizAnswers = listOf("蘋果", "橘子", "['蘋果', '橘子']", "undefined"),
            correctQuizAnswerIndex = 1,
            quizExplanation = "解構賦值是依據對等的位置直接進行拆解。因為 second 處於左側解構的第二順位，所以會高效率對應取回右側的第二個位置字串 \"橘子\"。"
        ),
        CourseLesson(
            id = "js_41",
            title = "41. JS 與 HTML 的關係（DOM 樹）",
            subtitle = "當程式碼遇見網頁，為靜態網頁注入活水靈魂",
            category = "網頁與 HTML 互動",
            readTimeMinutes = 3,
            description = "當我們有了 HTML 骨架與 CSS 衣服後，JavaScript 是如何跟這堆靜態標籤搭上線、產生動態操控事件的？",
            explanationHtmlCode = "當瀏覽器載入一個 HTML 頁面時，它會在記憶體中將這一顆由階層標籤組成的樹，轉化成名為 **DOM（Document Object Model，文件物件模型）** 的巨型樹狀物件。\n- **核心理念**：網頁上的每一個 `<div>`, `<h1>`, `<button>`，在瀏覽器核心中都是一個「物件（Object）」。\n- JavaScript 可以存取跟控制這些物件，隨心所欲地更改它的背景顏色、抽換裡面的文字、甚至是直接拔掉某個按鈕！網頁自此由靜態死物，變成了動態互動的樂園！",
            codeSnippet = "// 概念示範\n// 瀏覽器中 JavaScript 可以直接呼叫全域 document 物件\n// document 物件正是進入 DOM 控制大帝國的總控開關入口！",
            interactiveQuizQuestion = "在瀏覽器環境中，什麼是 DOM（文件物件模型）最準確的定義用途？",
            quizAnswers = listOf("一種用來儲存帳號密碼的資料庫代號", "一種網頁設計的排版框架", "一種將 HTML 精緻翻譯為 C++ 編譯碼的模組", "瀏覽器將網頁標籤樹狀物件化的記憶體實體，讓 JavaScript 可以直接進行操控、修改或監聽"),
            correctQuizAnswerIndex = 3,
            quizExplanation = "DOM 就是 Document Object Model。它是瀏覽器為 JavaScript 打造的一座橋樑，把死板的 HTML 轉化成物件並提供點運算子操作 API，讓我們能動態刷新網頁細節。"
        ),
        CourseLesson(
            id = "js_42",
            title = "42. 選取 DOM 節點",
            subtitle = "精準定位網頁元素，掌握操作權",
            category = "網頁與 HTML 互動",
            readTimeMinutes = 3,
            description = "學習如何使用 document 的神級定位追蹤器：getElementById 與 querySelector！",
            explanationHtmlCode = "要在網頁中大塞驚喜，我們必須先「抓到」目標按鈕。這有賴於這兩個黃金選取器：\n1. **`document.getElementById(\"標籤ID\")`**：直奔目標。迅速把網頁中設定有該 ID 的單一節點精準撈出來。\n2. **`document.querySelector(\"CSS選取器\")`**：功能更完備的通殺武器！只要像 CSS 一樣寫 `\".my-class\"`, `\"#my-id\"` 括弧引導，就能在網頁地毯式搜索，並返回第一個遇見的合格元素節點物體！",
            codeSnippet = "// 範例：若網頁中有 <h1 id=\"title\"> 標籤\n// const textHeader = document.getElementById(\"title\");\n// textHeader.textContent = \"我是 AI 重新動態寫入的新標題！\";",
            interactiveQuizQuestion = "當我們希望在 JavaScript 內，使用跟 CSS 樣式選取規則一模一樣的萬能手法（例如 .my-btn 或 div > span）來選取第一個網頁節點，推薦調用的方法是？",
            quizAnswers = listOf("document.selectCss()", "document.querySelector()", "document.getTags()", "document.find()"),
            correctQuizAnswerIndex = 1,
            quizExplanation = "document.querySelector() 允許直接傳入萬能的 CSS 規格選取字串，一經呼叫便會撈回第一個合格節點物件，是目前最靈活、也最受歡迎的前端 API。"
        ),
        CourseLesson(
            id = "js_43",
            title = "43. 監聽事件 addEventListener",
            subtitle = "點擊、輸入或滑動！第一時間響應使用者",
            category = "網頁與 HTML 互動",
            readTimeMinutes = 3,
            description = "別讓網頁死氣沉沉！學會監聽（Listen）用戶在按鈕上的點擊與輸入事件，動態派送驚喜工作流程項目！",
            explanationHtmlCode = "互動的核心在於「事件（Event）」。\n我們可以用 **`addEventListener`** 方法在物件上架設專屬「眼線防護」：\n\n```javascript\nconst btn = document.querySelector(\"#submit-btn\");\nbtn.addEventListener(\"click\", () => {\n    console.log(\"按鈕被點擊了！\");\n});\n```\n- `\"click\"`：事件名稱（如點選、鍵盤放開 `keyup`、滑鼠移入 `mouseenter` 等）。\n- 處理箭頭函式：當事件真正發生時，系統代為呼叫的 Callback 工作項目！",
            codeSnippet = "// 概念：當按鈕被按，自動對用戶執行 callback 方法\n// target_button.addEventListener(\"click\", () => { alert('Hello!'); });",
            interactiveQuizQuestion = "在選取好的按鈕節點上，使用 addEventListener() 設定事件監聽器時，這方法的第一個與第二個傳入參數通常分別代表什麼含義？",
            quizAnswers = listOf("先傳入要變更的背景色，再傳入字型大小", "先傳入要監聽的「事件類型」(例如 'click')，再傳入事件觸發時被執行的「Callback 負責函式」", "先傳一個數字，再傳一個布林值", "兩者都是空字串"),
            correctQuizAnswerIndex = 1,
            quizExplanation = "第一個參數是事件名稱字串（如 'click'、'submit'），第二個參數是一個 Callback 箭頭或匿名函式，用來扮演該案件被觸發時的應變與處理執行組件。"
        ),
        CourseLesson(
            id = "js_44",
            title = "44. 什麼是非同步機制？",
            subtitle = "理解 JavaScript 單線程與事件循環（Event Loop）",
            category = "非同步宇宙",
            readTimeMinutes = 3,
            description = "揭秘 JavaScript 排程魔法！為什麼它是「單執行緒單通道、絕不卡死網頁」的傳奇？",
            explanationHtmlCode = "JavaScript 是一門**單執行緒（Single Threaded）**的語言：一次只能做一件事情！\n如果呼叫 API 需要耗用 5 秒，那這 5 秒整個網頁豈不是要凍結卡死、連按鈕都沒法按了？\n那麼 JS 是如何解決這個問題的？\n答案是：**非同步（Asynchronous）與事件循環（Event Loop）**！\n- 當我們發起網路請求或倒數計時 `setTimeout()` 時，JS 會把這任務丟給瀏覽器後台網際引擎處理。\n- JS 絲毫不停留，繼續高速完成其他程式碼。\n- 當後台引擎處理完畢（例如下載完 XML 數據），會將 Callback 派件放到工作隊列（Callback Queue）中排隊。\n- 當 JS 本文全部跑空後，Event Loop 就會把隊列裡的 Callback 依序推入通道內讓 JS 處理！",
            codeSnippet = "console.log(\"1. 第一個印\");\nsetTimeout(() => {\n    console.log(\"3. 開後台倒數計時 0 毫秒後，這條非同步訊息最後才印！\");\n}, 0);\nconsole.log(\"2. 第二個印\");",
            interactiveQuizQuestion = "根據 JavaScript 的 Event Loop 事件循環架構機制，當執行含有 0 毫秒延遲的非同步程式 `setTimeout(() => ... , 0)` 時，該 callback 函式會何時被執行？",
            quizAnswers = listOf("在一瞬間立刻打斷當前的全部主程式，插隊最優先執行", "在當前的主執行緒本文的所有程式碼都整本執行完畢、調用疊空閒後，才回到 Callback Queue 將其帶入執行", "完全不被執行", "在伺服器端重開機才執行"),
            correctQuizAnswerIndex = 1,
            quizExplanation = "即使 delay 為 0 毫秒，setTimeout 仍是非同步任務。它必須先進入 WebAPIs 後台，然後等主要主執行緒（Call Stack）的本文程式全部跑空、Event Loop 放行後，才會被推上來，因此晚印出。"
        ),
        CourseLesson(
            id = "js_45",
            title = "45. 承諾 Promise 機制",
            subtitle = "掌握未來即將交付數據的安全憑證",
            category = "非同步宇宙",
            readTimeMinutes = 3,
            description = "學習如何使用 ES6 精緻的 Promise「承諾」，告別地獄般的末日 Callback 回調大坑！",
            explanationHtmlCode = "早期的非同步代碼如果多層巢狀，會形成像三角形一樣的 **「回調地獄（Callback Hell）」**，極難維護除錯。\nES6 特別引進了 **`Promise`（承諾）**，它像是一張交易收據：\n它代表一個「現在還不確定結果，但承諾在未來某個時刻會交貨」的物件容器！\n它有三種代表狀態：\n1. **Pending（等待中）**：非同步任務進行中。\n2. **Fulfilled（已實現/完成）**：順利交貨！觸發 `resolve(資料)` 拋出。\n3. **Rejected（已拒絕/失敗）**：出事了！觸發 `reject(錯誤)` 丟回異常情報。",
            codeSnippet = "const getSugar = new Promise((resolve, reject) => {\n    let success = true;\n    if (success) resolve(\"幸福黑糖！\");\n    else reject(\"沒買到...\");\n});\ngetSugar.then(data => console.log(data));",
            interactiveQuizQuestion = "在宣告 Promise 物件實例時，通常會代入一個帶有兩個重要處理指標方法（通常命名為 resolve、reject）的 callback。這兩者分別是在什麼關頭被調用的？",
            quizAnswers = listOf("resolve 在編譯失敗時用，reject 在運行成功時用", "resolve 用來把 True 倒轉成 False，reject 用來退出 class", "當非同步運作成功交付，調用 resolve 傳出成果數據；當運作遇到不可預期錯誤，調用 reject 拋回異常錯誤情報", "這兩個都是無意義名字"),
            correctQuizAnswerIndex = 2,
            quizExplanation = "resolve 意指「已解決/成功交付」，將承諾帶往 Fulfilled 狀態；reject 意指「已拒絕/遇難失敗」，能將承諾狀態移至 Rejected 以便捕捉错误。"
        ),
        CourseLesson(
            id = "js_46",
            title = "46. Promise 鏈式 then & catch",
            subtitle = "鏈式處理成功與捕捉意外錯誤的藝術",
            category = "非同步宇宙",
            readTimeMinutes = 3,
            description = "解脫層層包裹！利用 then().then() 一直呼叫下去，並用 .catch() 一網打盡所有意外！",
            explanationHtmlCode = "Promise 最優雅的，就在於它可以**鏈式呼叫（Promise Chaining）**！\n- **`.then(data => { ... })`**：如果上一個 Promise 成功 resolve 交付，就會進入這裡處理，並可以在裡面 return 另一個 Promise，讓代碼以直線的形式一行行接下去跑！\n- **`.catch(error => { ... })`**：如果在整個長長的 then 鏈中，任意一個 Promise 「翻車出錯」觸發了 reject，會直接跳過後續的 then、直接墜落進 .catch() 來進行除錯！異常捕捉極度統一且漂亮！",
            codeSnippet = "const checkStock = () => Promise.resolve(\"1. 檢查庫存ok\");\ncheckStock()\n .then(msg => {\n     console.log(msg);\n     return \"2. 扣除餘額ok\";\n })\n .then(nextMsg => console.log(nextMsg))\n .catch(err => console.log(\"出錯被攔：\", err));",
            interactiveQuizQuestion = "若一系列 Promise 串接鏈 then() 發生了多次數據流轉，而在途中不幸有一個 Promise 拋出了 reject() 拒絕錯誤，該錯誤將會被後面哪一個尾端鏈環偵測並捕獲？",
            quizAnswers = listOf("final()", "try()", "catch()", "else()"),
            correctQuizAnswerIndex = 2,
            quizExplanation = "catch() 是保底的除錯網，整條 .then() 長路中任何一方報考翻車，都會在一瞬間直通 .catch() 來避免程式當機。"
        ),
        CourseLesson(
            id = "js_47",
            title = "47. 終極語法糖 async 與 await",
            subtitle = "用同步的寫法，優雅寫出非同步的流程控制",
            category = "非同步宇宙",
            readTimeMinutes = 3,
            description = "掌握 JavaScript 史上最受推崇的非同步處理最終章！用極致簡潔的直覺行文方式撰寫非同步！",
            explanationHtmlCode = "有了 Promise 依然要在後面掛點 then 來 callback，看多了還是不夠直觀。\n於是，ES7 帶來了非同步程式的最強大殺手級代名詞：**`async` 搭配 `await`**！\n\n- **`async` 關鍵字**：寫在函式宣告前。強迫該函式「一定會返回一個 Promise」。\n- **`await` 關鍵字**：**只能寫在有 async 的函式內**！寫在一句 Promise 前。JS 看到 await 時，會「暫時留在這一行等 Promise 做出 resolve」，直到數據出爐，才指派給變數並繼續往下走！\n\n它讓非同步程式寫起來**完全像在寫同步由上往下跑的程式**，被公認為 JS 的精髓頂峰！",
            codeSnippet = "const getX = () => new Promise(res => setTimeout(() => res(42), 10));\nasync function main() {\n    console.log(\"正在等待解題中...\");\n    const data = await getX(); // 宛如同步，靜候結果！\n    console.log(\"驚豔！取得解開的答案為:\", data);\n}\nmain();",
            interactiveQuizQuestion = "關於 JavaScript 當中語法糖 await 關鍵字的放置安全位置，下列哪一項是一大 mandatory 必須遵守的鐵律？",
            quizAnswers = listOf("await 可以任意寫在 HTML 屬性內", "await 必須也只能被撰寫在冠有「async」關鍵字的函式大括號內部空間中", "await 只能在 for 迴圈中宣告使用", "await 不需要任何函式即可在 code 第一行單獨使用"),
            correctQuizAnswerIndex = 1,
            quizExplanation = "在 JavaScript 設計中，`await` 只能擺放在標註為 `async`（非同步）的函式內部，否則會遭遇 SyntaxError 語法解析錯誤！"
        ),
        CourseLesson(
            id = "js_48",
            title = "48. 開源 Fetch API 獲取外部數據",
            subtitle = "串接後端網路伺服器，取得網際資料進行即時高頻更新",
            category = "網頁與網路",
            readTimeMinutes = 3,
            description = "學習如何向真實世界網際網路伺服器發射 GET 請求，取得 JSON 格式大寶庫並印在畫面的手段！",
            explanationHtmlCode = "我們已經有 async/await，現在來做真正的網路串接！\n瀏覽器原生提供一個用來請求網路資源的 API： **`fetch()`**。\n它會返回一個 Promise。我們可以用兩步驟拿到真實 JSON：\n\n```javascript\nasync function loadData() {\n    // 1. 發送請求取得原始 Response 流\n    const response = await fetch(\"API網址\");\n    // 2. 利用 .json() 解析出好用的 JavaScript 物件或陣列！\n    const data = await response.json();\n    console.log(data);\n}\n```",
            codeSnippet = "async function fetchUser() {\n    const res = await fetch(\"https://jsonplaceholder.typicode.com/users/1\");\n    const user = await res.json();\n    console.log(\"從開源伺服器撈回 1 號使用者名稱:\", user.name);\n}\nfetchUser();",
            interactiveQuizQuestion = "在使用瀏覽器內建的 fetch() 系列 API 請求獲取網路 JSON 數據時，在成功拿到回應流（Response）後，通常需要搭配哪一個非同步方法來將本體資料解密成好用的 JavaScript 物件？",
            quizAnswers = listOf("response.toString()", "response.json()", "response.toText()", "response.parse()"),
            correctQuizAnswerIndex = 1,
            quizExplanation = "fetch 返回的流在到達時不能直觀讀取。我們呼叫 `await response.json()` 會在背地讀取數據流、並自動非同步解碼成 JS 當中的 Object 或 Array 物件格式。"
        ),
        CourseLesson(
            id = "js_49",
            title = "49. 類別 Class 與繼承",
            subtitle = "JavaScript 現代面向對象程式設計的組織哲學",
            category = "網頁與網路",
            readTimeMinutes = 3,
            description = "用 Class「建造藍圖」，大批量構造出格式優美、自帶功能特質的活物件實例！",
            explanationHtmlCode = "當需要大量、批次生產具有相同屬性與方法的操作單時（例如：在遊戲中生產 100 個哥布林小兵物件）。\n我們可以使用 ES6 的 **`class` 類別**藍圖：\n\n```javascript\nclass Hero {\n    constructor(name, hp) {\n        this.name = name;\n        this.hp = hp;\n    }\n    showHp() {\n        return `\${this.name} 當前血量為 \${this.hp}`;\n    }\n}\nconst g1 = new Hero(\"野蠻人\", 100); // 實例化全新物件！\n```",
            codeSnippet = "class Goblin {\n    constructor(id) {\n        this.id = id;\n        this.role = \"小怪\";\n    }\n}\nconst m1 = new Goblin(12);\nconsole.log(\"生成了一隻哥布林:\", m1.role, \"編號:\", m1.id);",
            interactiveQuizQuestion = "在 JavaScript Class 類別架構中，當我們使用「new ClassName()」去大批量生成一個個全新物件樣本時，哪一個內部方法會第一時間被引爆並用於進行初始值配置？",
            quizAnswers = listOf("initializer()", "build()", "constructor()", "setup()"),
            correctQuizAnswerIndex = 2,
            quizExplanation = "constructor（構造器）是類別的初始化大門。不論你在 constructor 寫了什麽，都會在 `new` 發生的那一秒立刻執行。"
        ),
        CourseLesson(
            id = "js_50",
            title = "50. 結業特考：打造你第一個 JS 待辦清單",
            subtitle = "恭喜 JavaScript 50堂通關！打造自己第一個實戰作品",
            category = "畢業設計",
            readTimeMinutes = 4,
            description = "恭喜完成 50 關學習！JavaScript 的核心是藉著 DOM 事件把資料（Array）同步渲染到 HTML 上！",
            explanationHtmlCode = "恭喜你！在 Pycoach 的 JavaScript 50 單元大帝國順利過關斬將，寫下極具紀念意義的一筆！\n\n你已經從字面認讀、變數宣告的 let/const、解決作用域污染防火牆 scope，歷經迴圈、函式打包、箭頭極縮，走入了高階 Map/Filter/Reduce，進而掌握了瀏覽器 DOM 互動操縱、EventListener 點擊響應、最終征服了非同步的 Async/Await fetch 網路數據，完成了最厚實的全端前哨戰！\n\n現在正是展現邏輯、在最後一堂課拿下證章的最佳時刻。點擊考卷、解開你的專屬 JS 黃金成就大徽章吧！",
            codeSnippet = "function checkJsDevStatus(unlockedUnits) {\n    if (unlockedUnits >= 50) {\n        return \"恭喜你正式成為 JavaScript 初代菁英開發者！🎉\";\n    } else {\n        return \"前方的 JS 精進之路尚未完結，繼續奔馳！🚀\";\n    }\n}\nconsole.log(checkJsDevStatus(50));",
            interactiveQuizQuestion = "回顧五十堂精實學習，以下哪一項是設計 JavaScript 網頁前端應用程式（像是畫出 TodoList）最經典的實作與資料流動軌道？",
            quizAnswers = listOf("依靠不斷重整與重開核心瀏覽器來刷新", "用一個陣列(Array)存放待辦歷史資料，當用戶新增、勾選或刪除成員時，先修改陣列，再同步用 JS DOM 語法動態將新陣列畫回 HTML 上显示", "手動刪除全部 background style", "用 CSS 將所有代碼覆蓋掉"),
            correctQuizAnswerIndex = 1,
            quizExplanation = "「資料驅動視圖（Data-driven rendering）」是前端開發無上的黃金大道。利用 JavaScript 動態對 Array 進行 add/remove 手術，再高效率重繪 DOM，成就了現代 React 與 Vue 最底層的運行邏輯。恭喜你完成 50 關 JavaScript 輝煌壯舉！"
        )
    )

    val htmlLessons: List<CourseLesson> = (1..50).map { i ->
        val title = when (i) {
            1 -> "認識 HTML5 網頁標籤超文字"
            2 -> "我第一個網頁與根標籤 <html>"
            3 -> "網頁的隱形大腦 <head>"
            4 -> "網頁的可見肉身 <body>"
            5 -> "瀏覽器頁籤的標題 <title>"
            6 -> "標題家族 <h1> 至 <h6>"
            7 -> "文章段落的主要容器 <p>"
            8 -> "網頁中的換行符與水平線"
            9 -> "程式中的秘密幽靈：註解方式"
            10 -> "語意加重標籤 <strong> 與 <em>"
            11 -> "無序列表 <ul> 與 <li> 項目"
            12 -> "有序列表 <ol> 與 <li> 排序"
            13 -> "定義列表 <dl>、<dt> 與 <dd>"
            14 -> "網頁的萬用區塊容器 <div>"
            15 -> "行內文字萬用容器 <span>"
            16 -> "網頁排版的區塊元素 vs 行內元素"
            17 -> "超連結的秘密：<a> 標籤"
            18 -> "點擊連結開新分頁 target=\"_blank\""
            19 -> "在網頁載入圖片：<img> 屬性"
            20 -> "原生影片播放：<video> 元件"
            21 -> "原生音樂播放：<audio> 元件"
            22 -> "巢狀框架的嵌入網頁：<iframe>"
            23 -> "圖表與圖片說明：<figure>"
            24 -> "表格的根基：<table> 標籤"
            25 -> "表格表頭與數據：<th> 和 <td>"
            26 -> "表格區分區塊：<thead> 和 <tbody>"
            27 -> "跨行合併儲存格：rowspan 屬性"
            28 -> "跨列合併儲存格：colspan 屬性"
            29 -> "表單傳輸核心：<form> 宣告"
            30 -> "文字輸入框：<input type=\"text\">"
            31 -> "安全密碼輸入：<input type=\"password\">"
            32 -> "電子信箱檢驗：<input type=\"email\">"
            33 -> "寫入多行長文字的評論區 <textarea>"
            34 -> "欄位安全標示：<label> 標籤"
            35 -> "單選按鈕設計：<input type=\"radio\">"
            36 -> "複選核取方塊：<input type=\"checkbox\">"
            37 -> "下拉式選單元素：<select> 組合"
            38 -> "按鈕的送出類型：<button>"
            39 -> "深入理解 HTML 屬性 (Attributes)"
            40 -> "樣式定位之王：Class 與 ID 屬性"
            41 -> "語意化標籤：<header> 與 <footer>"
            42 -> "導覽與主要結構：<nav> 與 <main>"
            43 -> "文章板塊的語意化：<section> 和 <article>"
            44 -> "側邊欄資訊容器 <aside> 宣告"
            45 -> "網頁 SEO 與頭部的 <meta> 的用途"
            46 -> "網頁文字地雷：特殊 HTML 實體字元"
            47 -> "網頁手繪畫布：HTML5 <canvas> 基礎"
            48 -> "好用不失真的向量圖形 <svg> 嵌入"
            49 -> "現代儲存庫 localStorage 介紹"
            50 -> "迎接 HTML 終極考驗：建構我的首頁"
            else -> "HTML 進階探索"
        }

        val subtitle = when (i) {
            1 -> "一切網頁骨架最底層的起點"
            2 -> "所有標籤的最高統治根核心"
            3 -> "放置隱藏設定與外部資源的情報庫"
            4 -> "使用者肉眼能看見的一切展示區"
            5 -> "呈現在瀏覽器分頁上的亮眼字樣"
            6 -> "決定文字大小與強度的標題階級"
            7 -> "包覆排版長篇文章的基本區塊"
            8 -> "換行 break 與水平線的空間魔法"
            9 -> "寫給人類與自己看、電腦忽視的備忘"
            10 -> "文字在視覺與語意上的多重複合加值"
            11 -> "用圓點等符號列出的條列資料項目"
            12 -> "由數字或字母編號排序的條列資訊"
            13 -> "用於專有名詞解釋的特殊清單結構"
            14 -> "打造網頁排版格局的萬用大積木"
            15 -> "局部修飾同一行內特定字句的小剪刀"
            16 -> "影響網頁排隊移動方式的核心概念"
            17 -> "穿梭不同網頁之間的宇宙傳送通道"
            18 -> "在保留當前網頁的前提下，開啟新希望"
            19 -> "讓網頁由枯燥文字轉化為生動視覺的關鍵"
            20 -> "不需要任何外掛元件，直接播放 mp4 影片"
            21 -> "為網頁加上優美音樂或說故事聲音"
            22 -> "在自己的網頁中完美塞入另一個獨立網界"
            23 -> "將圖片和其標題文字綁定成一個圖表區塊"
            24 -> "把數據整齊分欄分列擺放的古老表格"
            25 -> "表格中加重顯示的標題與普通格子"
            26 -> "讓表格語意清晰、易於閱讀的構造模組"
            27 -> "把當前格子往上下垂直方向合併擴大"
            28 -> "把當前格子往左右水平方向合併擴大"
            29 -> "把使用者在網頁填寫的值包裹並送給後端"
            30 -> "最常用來讓訪客填寫名字或搜尋詞的格子"
            31 -> "自動把字元遮蔽為黑色小圓點的安全格子"
            32 -> "會自動驗證格式是否含有 @ 與 domain 的欄位"
            33 -> "讓使用者留下長篇留言或敲打信件的巨大格子"
            34 -> "點擊文字也能自動聚焦到輸入格的完美守衛"
            35 -> "在多個選項中限制只能勾選一個的圓形按鈕"
            36 -> "能複選多項、打勾確認的多功能方塊"
            37 -> "點擊後展開豐富清單讓使用者挑選的選單"
            38 -> "按鈕的送出類型：<button>"
            39 -> "賦予標籤生命與定位的附加參數鍵值對"
            40 -> "樣式表與操作程式用來鎖定網頁格子的信物"
            41 -> "定義頁籤頭尾核心導航與頁尾宣告"
            42 -> "提升搜尋引擎 SEO 識別率的全新主區塊"
            43 -> "讓閱讀器一目了然的文章主體分段包裝"
            44 -> "擺放側邊目錄或廣告文宣的非主體區"
            45 -> "傳輸給搜尋引擎蜘蛛讀取的網頁規格書"
            46 -> "解決大於小於符號被 HTML 誤認為標籤的地雷"
            47 -> "使用代碼動態在瀏覽器繪製 2D 圖形的畫布"
            48 -> "放大一萬倍都絕不模糊的超解析度圖形標籤"
            49 -> "重整頁面也絕不會遺失的使用者在地變數快取"
            50 -> "完備 HTML 50堂通關特訓！拿下你的終極成就"
            else -> "網頁進階要素"
        }

        val category = when (i) {
            in 1..5 -> "HTML 基礎入門"
            in 6..10 -> "文字與排版"
            in 11..13 -> "清單與列表"
            in 14..16 -> "版面配置"
            in 17..23 -> "連結與媒體"
            in 24..28 -> "表格與格式"
            in 29..38 -> "互動與表單"
            in 39..40 -> "深入常識"
            in 41..45 -> "網頁結構與 SEO"
            in 46..49 -> "進階特效"
            else -> "畢業設計"
        }

        val description = "在本課節，我們將探索 HTML 的核心：『$title』，並親身撰寫標籤進行網頁骨架修補！"

        val explanationHtmlCode = "### 核心精要：$title\n\n$subtitle\n\n" + when (i) {
            1 -> "HTML（HyperText Markup Language）是**超文字標記語言**。它是所有瀏覽器唯一唯一唯一能直接認得的網頁骨架語言。\n\n**三大要點：**\n1. **非程式語言**：它不具備條件邏輯或算術迴圈，而是專注於「排版和結構化文字」。\n2. **標記標籤**：藉由 `<標籤>` 加上內容，告訴瀏覽器哪邊是標題、哪邊是圖片。\n3. **HTML5 是一切標準**，不論你用 React、Vue，最後渲染在訪客眼前的都是標準 HTML 標記。"
            2 -> "在 HTML 當中，最高層的帝王標籤就是 `<html>`！\n\n```html\n<html>\n  <!-- 這裡面包覆了整個世界的網頁 -->\n</html>\n```\n- 它就像包裹住一整顆地球的大氣層，一切 `<head>`、`<body>` 都得被它綁定在內！\n- 沒有它的話，瀏覽器可能會疑惑這究竟是不是一份合法的網頁文件。"
            3 -> "`<head>` 是網頁的「隱形大腦、軍師總部」。\n\n```html\n<head>\n  <meta charset=\"UTF-8\">\n  <title>我的網頁</title>\n</head>\n```\n- 裡面裝載的全部是**中介資料（Metadata）**，如字元編碼、搜尋引擎關鍵字、網頁標題等。\n- 訪客在頁面上**看不見** `<head>` 內的任何標籤（除了分頁標題），但它卻是攸關搜尋 SEO 的重要防護網。"
            4 -> "`<body>` 就是網頁的「肉身、前台大廳」！\n\n```html\n<body>\n  <h1>哈囉</h1>\n  <p>這是看得見的段落</p>\n</body>\n```\n- 只要你希望訪客用肉眼看見、點擊、聽見的任何標籤與文字，都**必須且只能**寫在 `<body>` 標籤大括號之內！"
            5 -> "`<title>` 放在 `<head>` 內部，用來命名網頁。\n\n```html\n<title>PyCoach AI 金牌教練</title>\n```\n- 它會在讀者瀏覽器頁面頁籤以及 Google 搜尋結果頁中做為顯目的標題，是 SEO 優化最核心的聖地之一！"
            6 -> "標題家族包含 `<h1>` 到底盤最小的 `<h6>`，代表不同層級的重要性！\n\n```html\n<h1>我是一級大標題</h1>\n<h2>我是二級副標題</h2>\n```\n- 瀏覽器會自動用粗體大號字體渲染它們。\n- `<h1>` 一張網頁原則上只建議出現**一次**，用來標示文章總主題。"
            7 -> "`<p>` 標籤代表 **Paragraph（段落）**。\n\n```html\n<p>這是一整段文字，段落之間會被自動安排適合的上下間隔留白空間。</p>\n```\n- 它是文字排列的常客，比單純寫文字更具有排版安全性與可讀性！"
            8 -> "寬度控制與行空間的魔法：`<br>`（Break 換行）和 `<hr>`（Horizontal Rule 水平分隔線）。\n\n- `<br>`：強制文字往下一行開始，像敲擊 Enter 後的換行。\n- `<hr>`：在兩段文字之間畫出一條橫向分隔線！"
            9 -> "在 HTML 中寫註解非常安全：\n\n`<!-- 這是一行被電腦忽視的綠色網頁註解 -->`\n\n- 它不會在瀏覽器頁面中顯示，非常適合寫下你的開發心得或暫時關閉某些不想運作的標籤。"
            10 -> "強調文字的標籤家族：\n\n- `<strong>`：將文字加粗顯示，並且告訴搜尋引擎這串文字**非常重要**！\n- `<em>`：將文字轉變成斜體（Emphasis），表達強烈且富含感情的語句！"
            17 -> "超連結 `<a>` 標籤是網頁的精髓：\n\n```html\n<a href=\"網址\">連結顯示文字</a>\n```\n- `href` 代表 Hypertext Reference，也就是跳轉目標網址。\n- 點擊標註文字，即可無限制跳轉！"
            19 -> "在網頁中載入圖片使用 `<img>` 標籤：\n\n```html\n<img src=\"圖片網址\" alt=\"圖片說明\">\n```\n- `src`（Source）：指向圖片路徑（可網址或本地路徑）。\n- `alt`（Alternate Text）：替代文字，網速慢或語音閱讀器必用！\n- 注意：它是**單邊標籤**，不需要尾端的 `</img>`！"
            else -> "這是一個非常有威力的 HTML $title 標記單元！\n\n- 它代表著前端網頁中 $subtitle 的功能！\n- 透過宣告此標籤，我們可以將資料組織與版面完美結合，這正是 HTML5 標準推薦的核心心法。"
        }

        val codeSnippet = when (i) {
            1 -> "<!-- 體驗超文字標記 -->\n<h1>我的第一個網頁</h1>\n<p>HTML 標籤可以輕鬆控制文字的層級喔！</p>"
            2 -> "<html>\n  你好網頁\n</html>"
            3 -> "<head>\n  <meta charset=\"UTF-8\">\n  <title>點亮頁籤 title</title>\n</head>"
            4 -> "<body>\n  <h1>訪客看得見的肉體 title</h1>\n  <p>所有 body 內的東西都會出現在手機或電腦螢幕上！</p>\n</body>"
            5 -> "<head>\n  <title>哈囉！我是網頁標題！</title>\n</head>"
            6 -> "<h1>前端教練</h1>\n<h2>我是二級 H2</h2>"
            7 -> "<p>Python與JS都很棒</p>"
            8 -> "<p>這是一行文字<br>我被 br 強制換行了</p>\n<hr>"
            9 -> "<!-- 我是一行註解，瀏覽器不會把我畫在畫面上 -->"
            10 -> "<strong>必考關鍵字</strong>"
            17 -> "<a href=\"https://google.com\">點我搜尋</a>"
            19 -> "<img src=\"logo.png\" alt=\"PyCoachLogo\">"
            else -> "<!-- HTML $title 範例 -->\n<h3>$title</h3>\n<p>$subtitle</p>"
        }

        val interactiveQuizQuestion = when (i) {
            1 -> "HTML 當中，英文首字母簡寫代表的核心本意是什麼？"
            2 -> "不論哪一種 HTML 檔案格式，以下哪一個標籤是覆蓋整份文件、代表最高主宰的「根標籤」？"
            3 -> "當你想放像是網頁語系、字元編碼 UTF-8、或是第三方 CSS 連結，應該把這些看不太見的 meta 資訊擺在？"
            4 -> "當你希望這串一級大標題 <h1> 確實出現在訪客的行動裝置螢幕正中央，你必須把 <h1> 塞在哪個成對標籤之中？"
            5 -> "能控制網頁在瀏覽器分頁、書籤、以及 Google 搜尋引擎中展現的首要標題標籤是？"
            6 -> "網頁中有多個標題層級，請選出全網頁中等量級最重、通常也最推薦只出現一次的一級標題？"
            7 -> "在 HTML5 標準排版中，每當我們渴望劃分出一段文字，並讓瀏覽器自動在其上下建立適當的留白，推薦使用？"
            8 -> "只想要讓文字在同一段落中「強行跳到下一行繼續顯示」、而不渴望建立新段落留白，應選用哪個單邊標籤？"
            9 -> "在 HTML 程式碼中，以下哪一項是撰寫「註解」的嚴謹正確規則？"
            10 -> "如果我們希望某一小撮文字不僅在網頁上顯現為粗體、更對網頁 SEO 搜尋引擎傳達「此處極其重要」的含義，應使用？"
            17 -> "設定 HTML 超連結跳轉時，必須使用的關鍵標籤以及連結網址屬性「href」，下列正確寫法是？"
            19 -> "在 HTML 載入圖片時，需要指定 src 以及 alt 兩個核心屬性。請問 alt 屬性的黃金價值是什麼？"
            else -> "關於 HTML 當中 $title 標籤的本意或屬性運用，下列哪項敘述是符合 HTML5 標準的？"
        }

        val quizAnswers = when (i) {
            1 -> listOf("HyperText Markup Language", "HighTerminal Macro Language", "HomeTool Multi Link", "Hardcode Text Manager List")
            2 -> listOf("<root>", "<body>", "<html>", "<document>")
            3 -> listOf("<body>", "<head>", "<footer>", "<meta-container>")
            4 -> listOf("<head>", "<html>", "<aside>", "<body>")
            5 -> listOf("<title>", "<header>", "<h1-tab>", "<meta-name>")
            6 -> listOf("<h6>", "<h1>", "<h3>", "<h2>")
            7 -> listOf("<text>", "<p>", "<div>", "<span>")
            8 -> listOf("<hr>", "<next>", "<br>", "<space>")
            9 -> listOf("// 這是註解", "/* 這是註解 */", "# 這是註解", "<!-- 這是註解 -->")
            10 -> listOf("<b>", "<strong>", "<bold>", "<big>")
            17 -> listOf("<link to=\"網址\">", "<a href=\"網址\">", "<anchor src=\"網址\">", "<a link=\"網址\">")
            19 -> listOf("圖片的高解析度寬度設定", "圖片的動畫旋轉播放特效", "當圖片加載失敗或螢幕輔助語音讀取時，所展示的對應「替代文字」", "用來將圖片轉換成 PDF 的命令")
            else -> listOf("它能表示 $subtitle 邏輯與功能", "它會完全關閉網頁功能", "它代表網頁唯一的 3D 繪圖引擎", "它只能寫在 head 標籤內")
        }

        val correctQuizAnswerIndex = when (i) {
            1 -> 0
            2 -> 2
            3 -> 1
            4 -> 3
            5 -> 0
            6 -> 1
            7 -> 1
            8 -> 2
            9 -> 3
            10 -> 1
            17 -> 1
            19 -> 2
            else -> 0
        }

        val quizExplanation = when (i) {
            1 -> "HTML 代表 HyperText Markup Language 超文字標記語言，是整個前端大帝國的基石。"
            2 -> "<html> 是所有標籤的最高統治根核心，其包覆了網頁所擁有的全部其他元素。"
            3 -> "<head>（網頁頭部大腦）是用來放置隱藏的設定、編碼宣告、以及外部 CSS/JS 引入的位置。"
            4 -> "<body> 才是使用者在螢幕上看得見的一切，任何看得見的文章、標題或按鈕都必須放進 body 當中。"
            5 -> "<title> 標籤能呈現在分頁標籤上、也是 Google SEO 最看重的文字依歸！"
            6 -> "<h1>（一級標題）是全網頁量級第一尊貴的頭條標題，一般強烈建議一張頁面只使用一次，來鎖定最核心的主題！"
            7 -> "<p>（Paragraph）是標準的段落語意容器，瀏覽器會自動上下增加空白留幅以利讀者視線描邊。"
            8 -> "<br> 代表 Line Break（換行），不需要寫閉合標籤，它會讓文字乖乖在原處跳行，非常好用！"
            9 -> "HTML 的註解是採用 <!-- 內容 --> 這對特殊的符號包裹，任何寫在中間的文字電腦都會假裝沒看見。"
            10 -> "雖然 <b> 標籤也能加粗視覺，但現代 HTML5 更推薦使用 <strong>，它不僅加粗、更能對讀屏器與 SEO 傳達強烈的尊貴性重要程度語意！"
            17 -> "<a> 標籤（Anchor）和 href 屬性配合，是建立網際超連結的必選黃金大門！"
            19 -> "alt 屬性（Alternative text）是網速慢、或是無視力能力者依賴螢幕讀星軟體時的必備輔助大字護罩，是國際無障礙網頁標準的核心規定！"
            else -> "沒錯！$title 標籤（或是其對應的屬性結構）在現代網頁設計中能完全契合 & 實現『$subtitle』的任務！"
        }

        CourseLesson(
            id = "html_$i",
            title = "$i. $title",
            subtitle = subtitle,
            category = category,
            readTimeMinutes = 2,
            description = description,
            explanationHtmlCode = explanationHtmlCode,
            codeSnippet = codeSnippet,
            interactiveQuizQuestion = interactiveQuizQuestion,
            quizAnswers = quizAnswers,
            correctQuizAnswerIndex = correctQuizAnswerIndex,
            quizExplanation = quizExplanation
        )
    }

    val kotlinLessons: List<CourseLesson> = (1..100).map { i ->
        val title = when (i) {
            1 -> "認識 Kotlin 語言程式基礎"
            2 -> "第一個 Kotlin 程式與 main 函數"
            3 -> "變數與常數：var 與 val 的分水嶺"
            4 -> "Kotlin 的型別推導與顯式型別宣告"
            5 -> "基礎資料型別：整數、浮點數與字元"
            6 -> "字串模板與 String 的拼接藝術"
            7 -> "布林型態與基本比較運算子"
            8 -> "條件分支 if-else 邏輯抉擇"
            9 -> "Kotlin 當中強大的 when 表達式"
            10 -> "迴圈的基礎：while 條件迴圈"
            11 -> "數數的藝術：for 迴圈與區間 Range"
            12 -> "認識 Null 安全機制與可空型別"
            13 -> "貓王運算子 ?: 與安全呼叫 ?."
            14 -> "強制非空斷言 !! 的危險性"
            15 -> "Kotlin 基礎程式期末綜合考驗"
            16 -> "定義你的第一個 Kotlin 函數"
            17 -> "函數參數與其預設引數的妙用"
            18 -> "具名引數 Named Arguments 的呼叫藝術"
            19 -> "單行運算式函數 Single-expression"
            20 -> "認識類別 Class 與實例化物件"
            21 -> "類別的主建構子 Primary Constructor"
            22 -> "初始化區塊 init 的功用與執行順序"
            23 -> "次建構子 Secondary Constructor"
            24 -> "類別屬性與 Getter/Setter 的自動生成"
            25 -> "為什麼 Kotlin 的類別與函數預設是 open"
            26 -> "介面 Interface：抽象行為契約的基石"
            27 -> "抽象類別 Abstract Class 與繼承的奧秘"
            28 -> "資料類別 Data Class 自動生成的魔法"
            29 -> "單例模式與 Object 宣告"
            30 -> "伴生物件 Companion Object 的靜態特性"
            31 -> "唯讀 List 與可變 MutableList 的分水嶺"
            32 -> "鍵值對 Map 與可變 MutableMap"
            33 -> "不重複集合 Set 與可變 MutableSet"
            34 -> "集合的高階轉換過濾：filter 與 map"
            35 -> "集合的摺疊與聚合：reduce 與 fold"
            36 -> "Lambda 運算式與匿名函數的語法糖"
            37 -> "尾隨 Lambda 語法糖的行文風格"
            38 -> "高階函數 High-Order Function 的原理"
            39 -> "泛型 Generics：代碼複用與型別安全"
            40 -> "擴充函數 Extension Functions 的優雅"
            41 -> "範疇函數 Scope Functions：let 與 run"
            42 -> "範疇函數 Scope Functions：also 與 apply"
            43 -> "範疇函數 Scope Functions：with 的簡化"
            44 -> "解構宣告 Destructuring Declarations"
            45 -> "類別層級的列舉 Enum Class"
            46 -> "協程 Coroutines：非同步程式的未來"
            47 -> "掛起函數 suspend function 的奧秘"
            48 -> "協程建構器 launch 與 runBlocking"
            49 -> "async 與 await 的並行異步任務"
            50 -> "協程作用域 CoroutineScope 的層級"
            51 -> "調度器 Dispatchers 執行緒切換"
            52 -> "協程取消 Cancellation 與 Job 控制"
            53 -> "異常處理與安全 SupervisorJob"
            54 -> "非同步冷流 Flow：反應式編程入門"
            55 -> "Flow 的建立、發射 emit 與收集 collect"
            56 -> "Flow 常用轉換：filter、map 與 transform"
            57 -> "StateFlow 與 SharedFlow 熱流雙雄"
            58 -> "協程安全併發與互斥鎖 Mutex 的安全閥"
            59 -> "深入協程通道 Channel 的雙向傳輸"
            60 -> "協程與 Flow 熱流綜合實戰挑戰"
            61 -> "宣告式 UI 開發概念與 Compose 緣起"
            62 -> "第一個 @Composable Composable 函數"
            63 -> "預覽視窗 @Preview 的神奇魔力"
            64 -> "直向排列 Column 的基礎與間距調配"
            65 -> "橫向排列 Row 的自適應對齊藝術"
            66 -> "重疊版面 Box 的層級堆疊與定位"
            67 -> "Modifier 邊界與間距：Padding 奧秘"
            68 -> "文字元件 Text 及其 Material 3 樣式"
            69 -> "按鈕元件 Button 及其動態點擊事件"
            70 -> "輸入格 TextField 及其使用者鍵盤綁定"
            71 -> "狀態管理 remember 與 mutableStateOf"
            72 -> "重組 Recomposition 原理與效能優化"
            73 -> "狀態提升 State Hoisting 最佳實踐"
            74 -> "跨組態記憶狀態保存 rememberSaveable"
            75 -> "主視窗元件 Scaffold 導航腳手架"
            76 -> "頂部 TopAppBar 與底部 BottomAppBar 條"
            77 -> "動態列表 LazyColumn 基礎與捲動"
            78 -> "橫向滾動 LazyRow 與網格 LazyVerticalGrid"
            79 -> "圖片載入 Image 與 Coil 非同步外掛"
            80 -> "響應式佈局與 Edge-to-Edge 邊緣貼合"
            81 -> "現代 Android 的 MVVM 系統架構設計"
            82 -> "生命週期感知元件 Lifecycle-aware ViewModel"
            83 -> "UI 狀態包裝與 StateFlow 的 collectAsState"
            84 -> "什麼是 Navigation Compose 導航元件"
            85 -> "建立導航路線 NavHost 與目的地對應"
            86 -> "在畫面間 safe 傳遞參數的 Serializable"
            87 -> "精美畫面動畫轉場效果與過渡優化"
            88 -> "依賴注入 Dependency Injection 核心概念"
            89 -> "簡化建構子注入 vs 現代 Hilt 框架"
            90 -> "Room 資料庫：本地 SQLite 儲存大作戰"
            91 -> "實作 Room Entity 實體與表格映射"
            92 -> "設計 Room DAO 存取方法與查詢"
            93 -> "Room Database 與協程非同步 Flow 支援"
            94 -> "本地數據持久化：DataStore Preferences 偏好"
            95 -> "Retrofit 網路連線與 RESTful API 設計"
            96 -> "JSON 數據序列化 kotlinx.serialization"
            97 -> "離線優先：本地資料與網路 API 同步策略"
            98 -> "單元測試與 JUnit5 核心驗證法"
            99 -> "自動化測試：Robolectric 與 JVM 快速測試"
            100 -> "終極結業式：我的第一個 Android App 誕生！"
            else -> "Kotlin 語言進階要素"
        }

        val subtitle = when (i) {
            in 1..15 -> "精通現代 Android 開發的第一王牌語言基礎"
            in 16..30 -> "物件導向與函數式程式設計的核心基石"
            in 31..45 -> "強大集合 API、Lambda 與範疇函數的開發語法糖"
            in 46..60 -> "協程與反應式 Flow 流的非同步高效開發利器"
            in 61..80 -> "宣告式 UI 高階設計、Material 3 與 Compose 動態元件"
            else -> "架構、導航、持久化、網路連線與自動化測試"
        }

        val category = when (i) {
            in 1..15 -> "Kotlin 程式基礎"
            in 16..30 -> "物件導向與函數式"
            in 31..45 -> "集合與高階特性"
            in 46..60 -> "協程與非同步流"
            in 61..80 -> "Jetpack Compose"
            else -> "Android 現代架構"
        }

        val description = "在本節，我們將探索 Kotlin 與 Android 開發的核心：『$title』，並親身實踐撰寫 Kotlin 範例代碼！"

        val explanationHtmlCode = "### 核心精要：$title\n\n$subtitle\n\n" + when (i) {
            1 -> "Kotlin 是一門**現代化、靜態型別、安全且 100% 與 Java 互操作的熱門開發語言**。自 2017 年起被 Google 宣佈為 Android 的第一官方開發語言。\n\n**三大核心特點：**\n1. **簡潔（Concise）**：相較 Java 減少了近 40% 的樣板程式碼。\n2. **安全（Safe）**：原生設計內置 Null 安全，能大幅消除 Java 最頭痛的 NullPointerException。\n3. **協程（Coroutines）**：輕量級並行控制，能用非同步直覺寫出高效程式。"
            2 -> "在 Kotlin 中，萬物的入口是 `main` 函數！\n\n```kotlin\nfun main() {\n    println(\"哈囉，Kotlin！\")\n} \n```\n- `fun` 關鍵字用來定義函數。\n- `println` 函數會將文字輸出至螢幕，並在尾端自動換行。\n- 在 Kotlin 中**不需要在行尾加上分號**！"
            3 -> "Kotlin 提供兩種變數宣告關鍵字，劃分出了可變與不可變的分水嶺：\n\n- **`val` (Value)**：唯讀、唯寫一次 the 變數（相當於 Java 的 final 或是 JS 的 const）。推薦盡可能使用 val 以保障多執行緒安全！\n- **`var` (Variable)**：可多次賦值與修改的變數。\n\n```kotlin\nval pi = 3.14159 // 無法再被修改\nvar score = 100\nscore = 105 // 完美執行！\n```"
            4 -> "Kotlin 具有強大的**型別推導 (Type Inference)** 能力，你不需要手動宣告型別，編譯器會自動根據初始值判定：\n\n```kotlin\nval age = 25 // 自動推導為 Int\nval name = \"Alex\" // 自動推導為 String\n```\n\n當你需要顯式宣告時，使用 `:` 語法：\n```kotlin\nval count: Int = 10\n```"
            12 -> "Kotlin 最大的安全保障：**Null 安全設計**！\n預設情況下，Kotlin 中的變數**絕不能為 null**！\n\n```kotlin\nvar nickname: String = \"Bobby\"\n// nickname = null // ❌ 編譯錯誤！\n```\n\n當我們確實需要可空變數時，必須在型別後方加上問號 `?`：\n```kotlin\nvar bio: String? = null // ✅ 允許！\n```"
            13 -> "當操作可空物件時，Kotlin 提供安全可靠的運算子，再也不會觸發當機！\n\n- **安全呼叫 `?.`**：如果物件為 null 則直接返回 null，不會崩潰。\n- **貓王運算子 `?:`**：當前方位 null 時，提供保底默認值。\n\n```kotlin\nval length = bio?.length ?: 0\n// 如果 bio 為 null，length 將自動拿到保底值 0！\n```"
            16 -> "在 Kotlin 中定義函數非常直覺：\n\n```kotlin\nfun calculateTotal(price: Int, count: Int): Int {\n    return price * count\n}\n```\n- `fun` 宣告起手。\n- 參數名稱在前，型別在後。\n- 尾端 `: Int` 聲明回傳值型別（若無回傳則省略或回傳 Unit）。"
            28 -> "當你想建立一個純粹用來裝載與儲存資料的實體類別時，只需要加上 `data` 關鍵字，Kotlin 就會貼心地為你自動生成 `equals()`, `hashCode()`, `toString()`, `copy()` 以及解構方法！\n\n```kotlin\ndata class User(val id: Int, val name: String)\n\nval u1 = User(1, \"Alice\")\nprintln(u1) // 自動印出 User(id=1, name=Alice)，非常美麗！\n```"
            61 -> "傳統 XML 排版是命令式的，而 **Jetpack Compose 則是宣告式 UI 框架**！\n- 你只需要描述 UI **在當前狀態下長什麼樣子**，當狀態改變時，框架會自動為你重繪局部受影響的部分，這稱為重組（Recomposition）。\n- 拋棄了冗長的 findViewById，完全使用 Kotlin 寫 UI，體驗極佳！"
            71 -> "在 Compose 的世界中，畫面是隨狀態（State）起舞的。我們使用 `remember` 來在重組中鎖定並保留變數，用 `mutableStateOf` 來建立響應式狀態流：\n\n```kotlin\nval count = remember { mutableStateOf(0) }\n```\n當 count.value 被更新時，所有讀取此變數的 Composable 元件會瞬間在螢幕上被重新渲染！"
            else -> "Kotlin & Android 現代開發實踐心法！\n\n- 這是一堂極具威力的進階特訓課！\n- 通過本單元學習，你能夠理解最正宗、現代化的 Android 官方最佳程式碼規範，並將它們運用在實際開發中。"
        }

        val codeSnippet = when (i) {
            1 -> "// 體驗第一個 Kotlin 語言\nfun main() {\n    val greeting = \"哈囉 Kotlin！\"\n    println(greeting)\n}"
            2 -> "fun main() {\n    println(\"歡迎來到 Kotlin 的世界！\")\n}"
            3 -> "val language = \"Kotlin\"\nvar level = 1\nlevel = 2\nprintln(\"語言: \$language 級別: \$level\")"
            4 -> "val score: Int = 99\nval name: String = \"Compose\"\nprintln(\"\$name 的得分是 \$score\")"
            12 -> "var name: String = \"Kotlin\"\nvar nickname: String? = null\nprintln(\"名稱: \$name, 暱稱: \$nickname\")"
            13 -> "val nickname: String? = null\nval displayName = nickname ?: \"訪客用戶\"\nprintln(\"歡迎 \$displayName\")"
            16 -> "fun add(a: Int, b: Int): Int {\n    return a + b\n}\nprintln(add(5, 7))"
            28 -> "data class Book(val title: String, val price: Int)\nval myBook = Book(\"Kotlin Basics\", 350)\nprintln(myBook)"
            61 -> "// 體驗宣告式 Compose UI 概念\n// @Composable\n// fun Welcome() { Text(\"Hello Compose!\") }"
            71 -> "val count = remember { mutableStateOf(0) }\ncount.value = count.value + 1\nprintln(\"當前點擊次數為: \${count.value}\")"
            else -> "// Kotlin $title 範例\nfun main() {\n    val message = \"歡迎來到第 $i 單元：$title！\"\n    println(message)\n}"
        }

        val interactiveQuizQuestion = when (i) {
            1 -> "關於 Kotlin 程式語言的背景與定位，下列哪一項敘述是正確的？"
            2 -> "在 Kotlin 程式碼中，一切程式的起跑點與入口函數，其標準寫法與宣告關鍵字是什麼？"
            3 -> "Kotlin 當中，val 與 var 這兩者在宣告變數時，其最本質的分水嶺是什麼？"
            4 -> "在 Kotlin 中，若要顯式、明確為一個變數聲明其為整數型別 (Int)，正確語法是？"
            12 -> "在 Kotlin 優秀的安全設計中，預設情況下變數是不能被賦值為 null 的。若我們渴望該變數能接收 null 值，應如何聲明？"
            13 -> "關於貓王運算子（Elvis Operator）「?:」的運作機制與核心價值，下列哪項敘述是正確的？"
            16 -> "在 Kotlin 當中定義與包裝一個「計算兩個整數之和」並回傳整數的函數，其宣告關鍵字與語法是？"
            28 -> "使用 Kotlin 的資料類別 (data class)，編譯器會自動為我們代勞生成許多實用方法。以下哪一個方法「不屬於」自動生成的範疇？"
            61 -> "現代 Android 的 Jetpack Compose 屬於什麼型態的 UI 渲染框架？"
            71 -> "在 Jetpack Compose 中，如果希望一個變數的數值在改變時，畫面能立刻感應並重繪局部元件，應使用什麼組合？"
            else -> "關於 Kotlin / Android 當中 $title 的概念，下列哪項敘述是符合官方推薦與現代架構標準的？"
        }

        val quizAnswers = when (i) {
            1 -> listOf("它是 Google 首推的第一官方 Android 開發語言，具有簡潔、安全且與 Java 100% 互操作性", "它是蘋果開發的專用 iOS 排版語言", "它是非靜態型別的動態語言，不需要任何編譯程序", "它必須取代瀏覽器內的 JavaScript 才能運行")
            2 -> listOf("fun main()", "void main()", "function main()", "def main()")
            3 -> listOf("val 宣告唯讀不可變常數，var 宣告可變變數", "val 宣告可變變數，var 宣告不可變常數", "val 只能宣告數字，var 只能宣告字串", "兩者完全沒有差別")
            4 -> listOf("val x: Int = 10", "val x Int = 10", "val x = Int(10)", "int x = 10")
            12 -> listOf("在型別名稱後方加上問號「?」", "在型別名稱前方加上驚嘆號「!」", "直接在變數名稱後加上 null 關鍵字", "Kotlin 無法實現可空變數")
            13 -> listOf("當前方位 null 時，返回其右側預設保底值，藉此達到無崩潰防護效果", "它是用來將程式進行強制當機的特殊斷言", "它是用來定義網頁跳轉超連結的代碼", "它是用來執行 3D 繪圖的專用函數")
            16 -> listOf("fun add(a: Int, b: Int): Int", "function add(a: int, b: int): int", "def add(a, b) as Int", "void add(int a, int b)")
            28 -> listOf("DBHelper 資料庫連線模組", "toString() 字串轉換方法", "equals() 與 hashCode() 判定", "copy() 克隆複製物件方法")
            61 -> listOf("宣告式 (Declarative) UI 框架", "命令式 (Imperative) XML 佈局", "純 HTML 靜態網頁系統", "Flash 互動式播放器")
            71 -> listOf("remember { mutableStateOf(value) }", "val count = 0", "var count = 0", "MutableList")
            else -> listOf("它能安全實現 $title 的核心邏輯與架構", "它會完全關閉應用程式", "它屬於已被 Google 官方淘汰的古老技術", "它必須搭配 XML 佈局才能在 Compose 中顯示")
        }

        val correctQuizAnswerIndex = when (i) {
            1 -> 0
            2 -> 0
            3 -> 0
            4 -> 0
            12 -> 0
            13 -> 0
            16 -> 0
            28 -> 0
            61 -> 0
            71 -> 0
            else -> 0
        }

        val quizExplanation = when (i) {
            1 -> "答對了！Kotlin 自 2017 年起成為 Android 開發的首席官方語言，具有 100% 與 Java 共存互操作、代碼精鍊簡潔及強大 Null safe 保護的特徵。"
            2 -> "Kotlin 使用 `fun` 關鍵字定義函數，而入口函數名稱正是 `main`。"
            3 -> "val（Value）代表唯讀、不可變數（Immutable），指派一次後便不容更改；var（Variable）代表可變數（Mutable），可重複指派數值。"
            4 -> "Kotlin 使用冒號「:」緊跟型別名稱來進行顯式宣告，如 `val x: Int = 10`。"
            12 -> "在 Kotlin 中，只要在型別後面緊貼問號「?」（例如 `String?`），就代表該型別是可以接受 null 的安全可空型別。"
            13 -> "貓王運算子 `?:` 會在左側運算式回傳 null 時，自動改為採用右側所提供的備份保底值，進而免除了 Null 指標異常帶來的當機風險。"
            16 -> "Kotlin 函數使用 `fun` 聲明，參數型別在後，回傳型別緊隨函數括號之後並以冒號區隔。"
            28 -> "資料類別 data class 自動生成的僅有 toString()、equals()、hashCode()、copy() 及 componentN() 解構，資料庫連線等進階操作不屬於其代勞範圍。"
            61 -> "Jetpack Compose 是宣告式 UI 框架，使用者描述當前狀態下的畫面呈現，狀態變更時自動執行重組局部更新。"
            71 -> "remember 負責在重組中留住變數不被洗掉，mutableStateOf 負責將其包裝成可被 Compose 追蹤的反應式狀態，兩者結合是狀態管理的黃金搭檔。"
            else -> "非常正確！本單元講解的 $title 技術與現代 Android 架構 (M3、ViewModel、Coroutines 等) 完美契合，是成為高薪 Android 開發者的必經之路。"
        }

        CourseLesson(
            id = "kotlin_$i",
            title = "$i. $title",
            subtitle = subtitle,
            category = category,
            readTimeMinutes = 3,
            description = description,
            explanationHtmlCode = explanationHtmlCode,
            codeSnippet = codeSnippet,
            interactiveQuizQuestion = interactiveQuizQuestion,
            quizAnswers = quizAnswers,
            correctQuizAnswerIndex = correctQuizAnswerIndex,
            quizExplanation = quizExplanation
        )
    }
}
