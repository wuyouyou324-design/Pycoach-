package com.example.courses

data class HandsOnChallenge(
    val lessonId: String,
    val title: String,
    val description: String,
    val expectedExplanation: String, // what they need to achieve
    val initialCode: String,
    val validate: (code: String, output: String, variables: Map<String, String>) -> Pair<Boolean, String>
)

object HandsOnChallenges {
    val challenges = mapOf<String, HandsOnChallenge>(
        // Python Challenges
        "lesson_5" to HandsOnChallenge(
            lessonId = "lesson_5",
            title = "💡 實作對話一：第一句程式咒語",
            description = "還記得 `print()` 是什麼嗎？它就像是程式的廣播大喇叭！",
            expectedExplanation = "請在下方編輯器中，使用 `print(\"I Love Python\")` 印出該段字串。注意字母大小寫及成對的小括號和雙引號！",
            initialCode = "# 請在下方填寫：使用 print 印出 \"I Love Python\"\n",
            validate = { code, output, vars ->
                val cleanOutput = output.trim()
                if (cleanOutput.contains("I Love Python")) {
                    Pair(true, "太棒了！你親手使直譯器成功印出了第一個字串，成功舉一反三！")
                } else {
                    Pair(false, "糟糕，印出的內容似乎沒有包含 \"I Love Python\" 耶。請檢查字元拼寫與雙引號喔！")
                }
            }
        ),
        "lesson_6" to HandsOnChallenge(
            lessonId = "lesson_6",
            title = "💡 實作對話二：宣告你的專屬容器",
            description = "變數是數據的抽屜盒子。用 `=` 可以把右邊的值塞到左邊的變數盒中！",
            expectedExplanation = "請宣告一個變數盒叫 `level`，並把整數值 `99` 裝進去，然後虛擬執行時在下一行用 `print(level)` 印出它！",
            initialCode = "# 請在下方寫程式：\n# 1. 宣告變數 level 並指派值為 99\n# 2. 列印 level 的內容\n",
            validate = { code, output, vars ->
                val value = vars["level"]?.trim()
                val hasPrinted = output.trim().contains("99")
                if (value == "99" && hasPrinted) {
                    Pair(true, "完美！你不但成功在記憶體中配置了 level 抽屜，並把它列印出來了！")
                } else if (value == "99") {
                    Pair(false, "你成功將變數 level 設為了 99，但似乎還沒有在畫面上 print 輸出它喔！")
                } else {
                    Pair(false, "找不到值為 99 的 level 變數。請寫：`level = 99`！")
                }
            }
        ),
        "lesson_7" to HandsOnChallenge(
            lessonId = "lesson_7",
            title = "💡 實作對話三：神奇怪物名字融合術",
            description = "在 Python 當中，字串（引號文字）竟然可以用 `+` 符號像黏土一樣黏起來！",
            expectedExplanation = "我們已經宣告了 `word1 = \"Py\"`。請在下方再宣告一個 `word2 = \"Coach\"`，然後宣告一個 `cool_name = word1 + word2`，最後 `print(cool_name)`！",
            initialCode = "word1 = \"Py\"\n# 在下方接著寫：\n# 1. 宣告 word2 為 \"Coach\"\n# 2. 宣告 cool_name 為 word1 與 word2 的相加\n# 3. 印出 cool_name\n",
            validate = { code, output, vars ->
                val coolName = vars["cool_name"]?.trim()
                val cleanOutput = output.trim()
                if ((coolName == "PyCoach" || cleanOutput.contains("PyCoach"))) {
                    Pair(true, "成功了！字串被完美的縫合在一起，列印出了 PyCoach！這就是拼接魔法！")
                } else {
                    Pair(false, "看起來 cool_name 的內容不是 \"PyCoach\" 喔。提示：請寫 `word2 = \"Coach\"` 且 `cool_name = word1 + word2`！")
                }
            }
        ),
        "lesson_10" to HandsOnChallenge(
            lessonId = "lesson_10",
            title = "💡 實作對話四：神秘的自我更新術",
            description = "在程式中，`x = x + 1` 代表把舊的 x 加上 1 算好後，再放回 x 盒子。這正是升級累加的常客！",
            expectedExplanation = "我們已經幫你的玩家初始化了經驗值 `xp = 100`。請寫 code 將你的 `xp` 乘上 `3` 倍（即 `xp = xp * 3`），然後 `print(xp)` 看看有沒有成功變 300 點！",
            initialCode = "xp = 100\n# 請在下方寫程式：\n# 1. 將 xp 的值乘以 3 倍並更新回自身\n# 2. 列印 xp 變數的值\n",
            validate = { code, output, vars ->
                val xl = vars["xp"]?.trim()
                val has300 = output.trim().contains("300")
                if (xl == "300" && has300) {
                    Pair(true, "太厲害了！你成功掌握了自我賦值更新，`xp` 順利飆升至 300！")
                } else if (xl == "300") {
                    Pair(false, "你成功將 xp 修改成了 300，但還沒有調用 `print(xp)` 輸出結果供肉眼觀測喔！")
                } else {
                    Pair(false, "xp 的值目前為 $xl，不是 300。提示：使用算術乘號：`xp = xp * 3`！")
                }
            }
        ),
        "lesson_11" to HandsOnChallenge(
            lessonId = "lesson_11",
            title = "💡 實作對話五：財富自由審計師",
            description = "比較運算子（如 `>=`、`==`）在比對大小關係後，會返回真/假值（布林值）。",
            expectedExplanation = "假設你的存款餘額 `balance = 80`，商品扣款價格 `price = 100`。請宣告一個變數叫 `can_buy`，其值為比較式：`balance >= price`（餘額是否大於等於商品價）。最後列印 `can_buy`！",
            initialCode = "balance = 80\nprice = 100\n# 請在下方寫程式：\n# 1. 宣告 can_buy 變數儲存 balance >= price 的比較結果\n# 2. 列印 can_buy\n",
            validate = { code, output, vars ->
                val canBuy = vars["can_buy"]?.trim()
                val hasPrinted = output.trim().contains("False")
                if (canBuy == "False" && hasPrinted) {
                    Pair(true, "非常正確！比大小傳回了布林值 False，代表餘額不夠買商品。你寫出了完美的條件判定邏輯！")
                } else if (canBuy == "False") {
                    Pair(false, "can_buy 計算正確（False），但你忘記用 `print(can_buy)` 印出來給大家看了！")
                } else {
                    Pair(false, "can_buy 的值似乎不是 False。請確認你的比對規則是：`can_buy = balance >= price`！")
                }
            }
        ),
        "lesson_21" to HandsOnChallenge(
            lessonId = "lesson_21",
            title = "💡 實作對話六：冒險者背包清單",
            description = "清單 (List) 使用中括號 `[]` 來包裹一整袋的元素，成員順序由 0 開始數數！",
            expectedExplanation = "請建立一個名為 `bag` 的背包清單，裝入三個字串：`\"sword\"`、`\"shield\"`、`\"potion\"`！最後列印第一個物品 `bag[0]` 裝載！",
            initialCode = "# 請在下方寫程式：\n# 1. 宣告 bag 清單，裝有 \"sword\", \"shield\", \"potion\"\n# 2. 印出第一個元素 bag[0]\n",
            validate = { code, output, vars ->
                val bagExpr = vars["bag"]?.trim()
                val hasPrintedSword = output.trim().contains("sword")
                if (bagExpr != null && bagExpr.contains("sword") && hasPrintedSword) {
                    Pair(true, "太棒了！行李打包完成！透過 bag[0] 你成功拿出了最核心的武器 sword 指向外敵！")
                } else if (bagExpr != null && bagExpr.contains("sword")) {
                    Pair(false, "你成功宣告了背包 bag 陣列，但還沒有用 `print(bag[0])` 列印出第一個裝備 sword 喔！")
                } else {
                    Pair(false, "找不到名為 bag 的背包清單，或者裡面拼寫不正確. 格式：`bag = [\"sword\", \"shield\", \"potion\"]`！")
                }
            }
        ),
        "lesson_31" to HandsOnChallenge(
            lessonId = "lesson_31",
            title = "💡 實作對話：宣告第一個類別設計圖",
            description = "自訂類別像定義一個模具，而物件是照模具烤出來存在的蛋糕。",
            expectedExplanation = "請建立一個名為 Person 的空白類別（利用 pass 即可）。然後建立一個叫 star 的變數為 Person() 的實體，最後用 print(star) 印出它！",
            initialCode = "# 請在下方寫程式：\n# 1. 宣告一個名為 Person 的類別且內部用 pass\n# 2. 建立 star 變數並實例化 Person()\n",
            validate = { code, output, vars ->
                val hasClass = code.replace("\\s".toRegex(), "").contains("classPerson")
                val hasStar = code.replace("\\s".toRegex(), "").contains("star=Person()")
                if (hasClass && hasStar) {
                    Pair(true, "太棒了！你宣告了人生中第一個 Python 類別 Person 並成功實例化為 star 物件！")
                } else {
                    Pair(false, "找不到定義為 class Person 的類別或 star = Person() 實例化語法！")
                }
            }
        ),
        "lesson_32" to HandsOnChallenge(
            lessonId = "lesson_32",
            title = "💡 實作對話：共享的翅膀",
            description = "類別屬性是所有物件一輩子共享且看得到的初始基礎變數。",
            expectedExplanation = "請建立一個名為 Bird 的類別，在裡面宣告類別屬性 has_wings 並賦予 True。然後實例化一個 sparrow = Bird() 變數，最後 print(sparrow.has_wings)！",
            initialCode = "# 請在下方寫程式：\n# 1. 宣告 class Bird，內含 has_wings = True\n# 2. 宣告 sparrow = Bird()\n# 3. 印出 sparrow.has_wings\n",
            validate = { code, output, vars ->
                val hasClass = code.replace("\\s".toRegex(), "").contains("classBird")
                val hasWings = code.replace("\\s".toRegex(), "").contains("has_wings=True")
                val hasSparrow = code.replace("\\s".toRegex(), "").contains("sparrow=Bird()")
                if (hasClass && hasWings && hasSparrow) {
                    Pair(true, "完美！你完成了 Bird 類別設定並宣告 sparrow 物件，印出了 True！")
                } else {
                    Pair(false, "請確認你宣告了 class Bird，內含 has_wings = True 屬性，且建立了 sparrow = Bird() 喔！")
                }
            }
        ),
        "lesson_33" to HandsOnChallenge(
            lessonId = "lesson_33",
            title = "💡 實作對話：小貓工廠初始化建構子",
            description = "使用 __init__(self, name) 建構子在貓咪誕生時立刻冠上專屬名字。",
            expectedExplanation = "請建立一個 Cat 類別，在裡面實作它的初始化建構子 def __init__(self, name): 且將 self.name 設為 name。然後建立一隻 kitty = Cat(\"Kitty\")，並列印 kitty.name 的名稱！",
            initialCode = "# 請在下方寫程式：\n# 1. 實作 Cat 類別且包含 __init__ 設定 self.name\n# 2. 實例化 kitty = Cat(\"Kitty\")\n# 3. 列印 kitty.name\n",
            validate = { code, output, vars ->
                val hasInit = code.contains("def __init__")
                val hasSelfName = code.contains("self.name")
                val hasKitty = code.replace("\\s".toRegex(), "").contains("kitty=Cat(")
                if (hasInit && hasSelfName && hasKitty) {
                    Pair(true, "萬歲！有了 __init__，小貓 Kitty 一出生就被貼上了代表牠自己名牌的屬性！")
                } else {
                    Pair(false, "請確認有實作 `def __init__(self, name):` 並為 `self.name` 賦值，且正確建立 `kitty = Cat(\"Kitty\")`！")
                }
            }
        ),
        "lesson_34" to HandsOnChallenge(
            lessonId = "lesson_34",
            title = "💡 實作對話：小狗快跑實體行為",
            description = "實體方法需要 self 作為第一個參數，才能調用物件本身的狀態變數。",
            expectedExplanation = "定義一個 Dog 類別，其具有 name 屬性（在 __init__ 中），並實作一個叫 run(self) 的實體方法，裡面用 print(self.name + \" 正在奔跑！\") 列印。接著創立一隻 puppy = Dog(\"旺財\") 並調用 puppy.run()！",
            initialCode = "# 請在下方寫程式：\n# 1. 宣告 Dog 類別與其 __init__、run 方法\n# 2. 宣告 puppy = Dog(\"旺財\")\n# 3. 呼叫 puppy.run()\n",
            validate = { code, output, vars ->
                val hasRunMethod = code.contains("def run(self)")
                val hasCall = code.contains("puppy.run()")
                if (hasRunMethod && hasCall) {
                    Pair(true, "卓越！你成功定義了實體方法 run(self) 並呼叫了牠，讓小狗旺財歡快地飛奔了起來！")
                } else {
                    Pair(false, "請確認在 Dog 內寫了 `def run(self):` 函式，且宣告了 `puppy = Dog(...)` 並調用 `puppy.run()`！")
                }
            }
        ),
        "lesson_35" to HandsOnChallenge(
            lessonId = "lesson_35",
            title = "💡 實作對話：保險箱的絕對封裝隔離",
            description = "利用雙下底線限制外界非法強改變數。",
            expectedExplanation = "請宣告一個 BankAccount 類別，在其建構子 __init__ 中將其餘額屬性設為私有屬性 self.__balance = 1000。接著宣告一個 get_balance(self) 的方法，用來 return self.__balance。",
            initialCode = "# 請在下方寫程式：\n# 1. 宣告 BankAccount 類別，其擁有 __money 或 __balance 私有變數\n# 2. 實作 get_balance(self) 回傳該私有餘額\n",
            validate = { code, output, vars ->
                val hasPrivate = code.contains("self.__balance") || code.contains("self.__money")
                val hasGetter = code.contains("def get_balance")
                if (hasPrivate && hasGetter) {
                    Pair(true, "棒極了！外界無法直接透過面額存取私有變數，必須乖乖透過 get_balance()，這就是封裝的安全精髓！")
                } else {
                    Pair(false, "請檢查是否有寫入私有屬性 `self.__balance = 1000` 以及 `def get_balance(self):` 方法！")
                }
            }
        ),
        "lesson_36" to HandsOnChallenge(
            lessonId = "lesson_36",
            title = "💡 實作對話：繼承的鋼鐵基因傳遞",
            description = "讓鋼鐵戰士子類別直接擁有普通人父類別的一切天賦！",
            expectedExplanation = "宣告一個 Human 類別，並寫個 walk(self) 列印 walk。再宣告一個 IronMan 類別繼承自 Human，在 IronMan 內寫個 fly(self) 列印 fly。最後創立 tony = IronMan() 並且調用 tony.walk()！",
            initialCode = "# 請在下方寫程式：\n# 1. 宣告 Human 且定義 walk(self)\n# 2. 宣告 IronMan 繼承自 Human 且定義 fly(self)\n# 3. 實例化 tony 並調用 walk()\n",
            validate = { code, output, vars ->
                val hasInherit = code.replace("\\s".toRegex(), "").contains("classIronMan(Human)")
                val hasWalk = code.contains("tony.walk()")
                if (hasInherit && hasWalk) {
                    Pair(true, "無懈可擊！IronMan 類別括號傳入 Human 完成了繼承，即便 IronMan 沒寫 walk，也能完美拜訪父類別特權！")
                } else {
                    Pair(false, "請檢查你的類別宣告是否為 `class IronMan(Human):` 且實例化 tony 後有呼叫 `tony.walk()`！")
                }
            }
        ),
        "lesson_37" to HandsOnChallenge(
            lessonId = "lesson_37",
            title = "💡 實作對話：動態聲音多型挑戰",
            description = "同一個 play() 命令，對鋼琴和吉他產生各自的動聽旋律！",
            expectedExplanation = "宣告 Piano 與 Guitar 兩個類別，兩邊都實作一個同名方法 sound(self) 分別回傳 \"叮咚\" 與 \"不啷\"。在下方實例化 Piano 與 Guitar 並拜訪同名 sound() 方法！",
            initialCode = "# 請在下方寫程式：\n# 1. 宣告 Piano 與 Guitar\n# 2. 分別實作 sound() 並回傳專屬彈奏字串\n",
            validate = { code, output, vars ->
                val hasPiano = code.replace("\\s".toRegex(), "").contains("classPiano")
                val hasGuitar = code.replace("\\s".toRegex(), "").contains("classGuitar")
                val bothSound = code.contains("def sound")
                if (hasPiano && hasGuitar && bothSound) {
                    Pair(true, "驚嘆！兩個毫不相關的類別在呼叫同名的 sound 介面時各演奏出驚奇的樂章，體現了完美的對接多型！")
                } else {
                    Pair(false, "請確認宣告了 Piano 與 Guitar 分支，且兩者內部都設計了 `def sound(self):` 函式！")
                }
            }
        ),
        "lesson_38" to HandsOnChallenge(
            lessonId = "lesson_38",
            title = "💡 實作對話：方法覆寫與 super() 回信",
            description = "重新升級子類別，並用 super() 呼叫父類。",
            expectedExplanation = "宣告一個 Person，寫個 def greet(self)。再宣告子類別 Student(Person)，在內部覆寫 greet(self) 方法，使其先調用 super().greet()！",
            initialCode = "# 請在下方寫程式：\n# 1. 宣告 Person 類別與 greet 方法\n# 2. 宣告 Student 繼承 Person，重寫 greet 並呼叫 super().greet()\n",
            validate = { code, output, vars ->
                val hasOverriding = code.replace("\\s".toRegex(), "").contains("classStudent(Person)")
                val hasSuper = code.contains("super().greet")
                if (hasOverriding && hasSuper) {
                    Pair(true, "超級厲害！你覆寫了 greet 且運用 super() 指引父類，印出了精緻、重用的招呼語文串！")
                } else {
                    Pair(false, "提示：請確認 `Student` 繼承 `Person`，且重寫了 `greet` 方法，並在內部包含 `super().greet()` 敘述！")
                }
            }
        ),
        "lesson_39" to HandsOnChallenge(
            lessonId = "lesson_39",
            title = "💡 實作對話：雙重混血兒多重繼承",
            description = "同時調用兩個完全獨立的父類特技！",
            expectedExplanation = "宣告 Rider 類別寫下 ride 方法，再宣告 Hunter 類別寫下 hunt 方法。建立子類別 Ranger，同時繼承 Rider 與 Hunter，並在下方宣告 my_hero = Ranger()！",
            initialCode = "# 請在下方寫程式：\n# 1. 宣告 Rider 與 Hunter\n# 2. 宣告 Ranger 共同繼承二者\n",
            validate = { code, output, vars ->
                val hasMulti = code.replace("\\s".toRegex(), "").contains("classRanger(Rider,Hunter)") || code.replace("\\s".toRegex(), "").contains("classRanger(Hunter,Rider)")
                if (hasMulti) {
                    Pair(true, "混血成功！Ranger 同時擁有了 Rider（座騎）與 Hunter（狩獵）的兩大極限絕學！多重繼承全通盤！")
                } else {
                    Pair(false, "找不到符合多重繼承多個父類別的格式：`class Ranger(Rider, Hunter):` 喔！")
                }
            }
        ),
        "lesson_40" to HandsOnChallenge(
            lessonId = "lesson_40",
            title = "💡 實作對話：逼迫子類實作的合約定約",
            description = "使用 NotImplementedError 來為核心類別做出硬性限制規格防護。",
            expectedExplanation = "定義一個 Shape 類別，在裡面寫 get_area(self)，且故意 raise NotImplementedError(\"錯誤\")。接著在子類 Circle(Shape) 裡正確覆寫 get_area(self) 函數回傳 100。",
            initialCode = "# 請在下方寫程式：\n# 1. 宣告 Shape，內含 get_area 拋出 NotImplementedError\n# 2. 宣告 Circle 繼承 Shape 並重寫 get_area 回傳 100\n",
            validate = { code, output, vars ->
                val hasRaise = code.contains("NotImplementedError")
                val hasSubclass = code.replace("\\s".toRegex(), "").contains("classCircle(Shape)")
                val hasOverride = code.contains("def get_area")
                if (hasRaise && hasSubclass && hasOverride) {
                    Pair(true, "讚！這就是 Python 的最快接口合約架構，不重寫 get_area 就會在呼叫時與擲出異常，達到百分百安全定格！")
                } else {
                    Pair(false, "請確認在 Shape.get_area() 中 `raise NotImplementedError` 且 Circle 繼承 Shape 並重置了 `get_area`！")
                }
            }
        ),
        "lesson_41" to HandsOnChallenge(
            lessonId = "lesson_41",
            title = "💡 實作對話：自訂精美的列印格式",
            description = "覆寫 __str__ 讓隨手列印物件出來看的時候不再是一行亂碼地址，而是賞心悅目的字串！",
            expectedExplanation = "自訂 Book 類別，在其建構子 __init__ 擁有屬性 self.title = \"Python神書\"。接著在類別中覆寫 __str__(self) 返回 \"書名: \" + self.title。最後列印印出這個 book 物件！",
            initialCode = "# 請在下方寫程式：\n# 1. 宣告 Book，在 __init__ 設定 self.title = \"Python神書\"\n# 2. 實作 __str__(self) 回傳包含自訂書名的文字\n",
            validate = { code, output, vars ->
                val hasStrDunder = code.contains("def __str__")
                val hasReturn = code.contains("return") && (code.contains("self.title") || code.contains("Python神書"))
                if (hasStrDunder && hasReturn) {
                    Pair(true, "大功告成！印出 Book 實體不再是十六進位地址，而是親切的『書名: Python神書』！")
                } else {
                    Pair(false, "找不到 `def __str__(self):` 方法，或者方法中未正確 return 書名文字串！")
                }
            }
        ),
        "lesson_42" to HandsOnChallenge(
            lessonId = "lesson_42",
            title = "💡 實作對話：偽裝成容器的自訂長度",
            description = "讓你的物件對 len() 函數也起效能，並回傳你想給他的長度大小！",
            expectedExplanation = "宣告一個 Playlist 類別，在其 __init__ 的時候將 self.songs = [\"SongA\", \"SongB\"]。在類別中覆寫 __len__(self)，回傳 2 或是 list 的長度！",
            initialCode = "# 請在下方寫程式：\n# 1. 宣告 Playlist 與其 songs 清單屬性\n# 2. 宣告 __len__(self) 回傳長度\n",
            validate = { code, output, vars ->
                val hasLenDunder = code.contains("def __len__")
                val hasLenReturn = code.contains("len") || code.contains("return")
                if (hasLenDunder && hasLenReturn) {
                    Pair(true, "太神奇了！原本是普通自訂物件，在覆寫 __len__ 之後居然也能被 Python 內建的 len() 所直接拜訪了！")
                } else {
                    Pair(false, "請檢視是否設計了 `def __len__(self):` 且回傳了 songs 的長度！")
                }
            }
        ),
        "lesson_43" to HandsOnChallenge(
            lessonId = "lesson_43",
            title = "💡 實作對話：兩個物件相加的加法多載",
            description = "利用 __add__ 魔法方法，自訂 '+' 運算子在兩個物件相加時的內部動作。",
            expectedExplanation = "宣告 Score 類別擁有 pts 屬性。覆寫 __add__(self, other) 方法使其支援用 + 號相加！",
            initialCode = "# 請在下方寫程式：\n# 1. 宣告 Score 與其 __init__ 設定 pts 屬性\n# 2. 宣告 __add__(self, other) 方法\n",
            validate = { code, output, vars ->
                val hasAddDunder = code.contains("def __add__")
                val hasOther = code.contains("other")
                if (hasAddDunder && hasOther) {
                    Pair(true, "無可匹敵的實作！加號 '+' 被賦予了全新靈魂，讓兩個 Score 物件一碰就主動融合成 pts 相加的全新物件！")
                } else {
                    Pair(false, "請在類別中撰寫 `def __add__(self, other):` 方法，使其能回傳相加結果。")
                }
            }
        ),
        "lesson_44" to HandsOnChallenge(
            lessonId = "lesson_44",
            title = "💡 實作對話：物件價值相等性大比對",
            description = "利用 __eq__ 讓雙等號 '==' 改為只辨識內容，而不是去比對記憶體。",
            expectedExplanation = "建立 Item 類別擁有 name 字串。覆寫 __eq__(self, other) 返回 self.name == other.name 比對真假值。",
            initialCode = "# 請在下方寫程式：\n# 1. 宣告 Item 與 name 屬性\n# 2. 實作 __eq__(self, other) 比較兩者的姓名是否相當\n",
            validate = { code, output, vars ->
                val hasEqDunder = code.contains("def __eq__")
                val hasCompare = code.contains("==")
                if (hasEqDunder && hasCompare) {
                    Pair(true, "相等的邏輯通達！現在只要兩個 Item 的 name 相同，即使它們儲存在不同記憶體地址也會判定為 True！")
                } else {
                    Pair(false, "請記得宣告 `def __eq__(self, other):` 比較兩者的 name 特徵！")
                }
            }
        ),
        "lesson_45" to HandsOnChallenge(
            lessonId = "lesson_45",
            title = "💡 實作對話：物件化身 callable 特種兵",
            description = "覆寫 __call__ 技巧，讓物件也能括弧執行任務！",
            expectedExplanation = "建立一 Secret 類別。覆寫 __call__(self) 魔法方法使其 return \"密碼5566\" 的字串。隨後宣告 key = Secret()，並且 print(key())！",
            initialCode = "# 請在下方寫程式：\n# 1. 宣告 Secret 且覆寫 __call__(self)\n# 2. 宣告 key = Secret() 並調用 key()\n",
            validate = { code, output, vars ->
                val hasCallDunder = code.contains("def __call__")
                val hasInvoke = code.contains("key()") || code.contains("print(key())") || code.contains("Secret()")
                if (hasCallDunder && hasInvoke) {
                    Pair(true, "不可置信！物件居然掛上小括號 key() 直接執行了 run，這種 Callable 模型在 Python 庫設計中極為常見！")
                } else {
                    Pair(false, "請宣告 `def __call__(self):` 方法，且在外部將 key 物件以 `key()` 形態執行！")
                }
            }
        ),
        "lesson_46" to HandsOnChallenge(
            lessonId = "lesson_46",
            title = "💡 實作對話：一等公民的自由飛躍",
            description = "練習直接把函數名 assignment 給變數，再由該變數接力呼叫跑指令。",
            expectedExplanation = "定義一個 greet() 返回 \"Hello\"。接著將這個函式本身賦給一個叫做 welcome 的變數（即 welcome = greet，不加括弧），最後 print(welcome())！",
            initialCode = "# 請在下方寫程式：\n# 1. 定義 greet() 函式\n# 2. 將其賦值 welcome = greet\n# 3. 印出 welcome()\n",
            validate = { code, output, vars ->
                val hasGreet = code.contains("def greet")
                val hasWelcomeAssign = code.replace("\\s".toRegex(), "").contains("welcome=greet")
                val hasWelcomeCall = code.contains("welcome()")
                if (hasGreet && hasWelcomeAssign && hasWelcomeCall) {
                    Pair(true, "漂亮！welcome 完全接手了 greet 的武功身手！在 Python 裡，Function 本身就是一種高級一等公民資料。")
                } else {
                    Pair(false, "確認是否有寫入：`welcome = greet`（注意：右方不要加 ()），且在下一行用了 `print(welcome())`！")
                }
            }
        ),
        "lesson_47" to HandsOnChallenge(
            lessonId = "lesson_47",
            title = "💡 實作對話：狀態保鮮的專屬累加器",
            description = "利用閉包封住外部變數 x 的值，使其長久存在。",
            expectedExplanation = "請宣告一個 count_maker(start) 方法，內部定義 nested 函數 adder(val) 返回 start + val，然後 count_maker 應該返回 adder 函數。宣告 test = count_maker(10)！",
            initialCode = "# 請在下方寫程式：\n# 1. 宣告 count_maker(start) 裡面嵌套 adder(val)\n# 2. 回傳 adder 並在外部呼叫 count_maker(10)\n",
            validate = { code, output, vars ->
                val hasNested = code.contains("def count_maker") && code.contains("def adder")
                val hasReturnAdder = code.contains("return adder")
                val hasTestCall = code.contains("count_maker")
                if (hasNested && hasReturnAdder && hasTestCall) {
                    Pair(true, "無與倫比！儘管 count_maker 已經結束了，adder(5) 仍然記得當時 start 被初始設定為 10，成功累計回傳 15，這就是閉包保鮮！")
                } else {
                    Pair(false, "請確認你實作了 `count_maker`，內部嵌套定義 `adder(val)` 回傳 `start + val`，且外層有正確的回傳 `adder`！")
                }
            }
        ),
        "lesson_48" to HandsOnChallenge(
            lessonId = "lesson_48",
            title = "💡 實作對話：第一個外掛日誌裝飾器",
            description = "利用 @ 符號，不侵入地擴充列印任務。",
            expectedExplanation = "寫一個 my_decorator(func) 裝飾器，內部定義 wrapper() 先執行印出 \"[Start]\"，隨後 func()，最後 return wrapper。接著在一個普通 hello() 方法的頭部冠上 @my_decorator 修飾它！",
            initialCode = "# 請在下方寫程式：\n# 1. 宣告 my_decorator 包含 wrapper 並回傳它\n# 2. 宣告 hello() 函數並用 @my_decorator 修飾\n",
            validate = { code, output, vars ->
                val hasDecorator = code.contains("def my_decorator") && code.contains("return wrapper")
                val hasAnnotation = code.contains("@my_decorator")
                val hasHello = code.contains("def hello")
                if (hasDecorator && hasAnnotation && hasHello) {
                    Pair(true, "太狂了！透過將 @my_decorator 放到 hello 的上方，hello 在呼叫時立刻被外掛了日誌行為！")
                } else {
                    Pair(false, "請建立 `my_decorator` 包裝函數，並在 `def hello():` 的上一行標註 `@my_decorator` 修飾！")
                }
            }
        ),
        "lesson_49" to HandsOnChallenge(
            lessonId = "lesson_49",
            title = "💡 實作對話：萬用 args 引數轉發裝飾器",
            description = "在 wrapper 裡加上 *args 封包與原封不動回發，避免引數數目不同而崩潰！",
            expectedExplanation = "自訂 one_decorator(func)，在其內部 wrapper(*args) 前列印 \"呼叫中\"，隨後 return func(*args)。並在 add(x, y) 的上方擺上 @one_decorator 進行修飾！",
            initialCode = "# 請在下方寫程式：\n# 1. 宣告 one_decorator 包含 wrapper(*args) 並進行參數透傳\n# 2. 使用 @one_decorator 修飾相加 add 方法\n",
            validate = { code, output, vars ->
                val hasArgs = code.contains("*args")
                val hasDecoration = code.contains("@one_decorator")
                if (hasArgs && hasDecoration) {
                    Pair(true, "完美的引數轉發！透過 *args 的輔助，你的 wrapper 變成了萬能吞吐盒子，無論什麼參數來都能轉接！")
                } else {
                    Pair(false, "請確認有在 wrapper 參數與呼叫 original func 時套用 `*args`，並使用 `@one_decorator` 修飾 add 相加方法！")
                }
            }
        ),
        "lesson_50" to HandsOnChallenge(
            lessonId = "lesson_50",
            title = "💡 實作對話：高階裝飾器疊加整合考驗",
            description = "恭喜進入最後大關卡！疊加兩個裝飾器做出最華麗的雙重裝修！",
            expectedExplanation = "宣告兩個裝飾器 wrap_a(func) 與 wrap_b(func)。接著在 main_func() 方法上，同時疊加 `@wrap_a` 與 `@wrap_b` 這兩行註解，達到疊加包裹它的效果！",
            initialCode = "# 請在下方寫程式：\n# 1. 建立裝飾器 wrap_a 與 wrap_b\n# 2. 用雙重 @ 修飾行為疊加在 main_func 上\n",
            validate = { code, output, vars ->
                val hasWrapA = code.contains("@wrap_a")
                val hasWrapB = code.contains("@wrap_b")
                val hasMain = code.contains("def main_func")
                if (hasWrapA && hasWrapB && hasMain) {
                    Pair(true, "極致傲視凡群！你完成了 50 大關的最強考驗，實作出多重修飾器堆疊，程式碼的設計格局已被開啟到系統大師層級！")
                } else {
                    Pair(false, "請確認你在 `def main_func():` 的前頭同時打上了 `@wrap_a` 及 `@wrap_b` 的雙重裝配註解喔！")
                }
            }
        ),

        // JavaScript Challenges
        "js_2" to HandsOnChallenge(
            lessonId = "js_2",
            title = "💡 實作對話一：控制台的廣播喇叭",
            description = "常說 `console.log()` 是 JS 當中的萬能大喇叭，快來親自呼叫它！",
            expectedExplanation = "請在下方編輯器中，使用 `console.log(\"I Love JS\");` 印出該段字串。字元大小寫跟括號引號都要非常講究喔！",
            initialCode = "// 請在下方填寫：使用 console.log 印出 \"I Love JS\"\n",
            validate = { code, output, vars ->
                val cleanOutput = output.trim()
                if (cleanOutput.contains("I Love JS")) {
                    Pair(true, "太棒了！你親手使 JS 控制台成功印出了字串，踏出了舉一反三的第一步！")
                } else {
                    Pair(false, "印出的內容似乎沒有包含 \"I Love JS\" 喔。請檢查字元拼寫、雙引號或括號！")
                }
            }
        ),
        "js_4" to HandsOnChallenge(
            lessonId = "js_4",
            title = "💡 實作對話二：let 局部變數登載",
            description = "`let` 是現代 JS 宣告可變變數（mutable）的標準黃金關鍵字。",
            expectedExplanation = "請宣告一個變數盒叫 `score`，並指派數值 `100` 給它。然後在下一行用 `console.log(score);` 印出它！",
            initialCode = "// 請在下方寫程式：\n// 1. 宣告 let score 並指派值為 100\n// 2. 呼叫 console.log(score);\n",
            validate = { code, output, vars ->
                val value = vars["score"]?.trim()
                val hasPrinted = output.trim().contains("100")
                if (value == "100" && hasPrinted) {
                    Pair(true, "完美！你不但成功配置了 score 這個 let 變數，還順利印出與讀取了它！")
                } else if (value == "100") {
                    Pair(false, "你成功將變數 score 設為了 100，但似乎還沒有在 console 中輸出它喔！")
                } else {
                    Pair(false, "找不到值為 100 的 score 變數。提示：請寫 `let score = 100;` 喔！")
                }
            }
        ),
        "js_5" to HandsOnChallenge(
            lessonId = "js_5",
            title = "💡 實作對話三：const 耐溫防火牆常數",
            description = "`const` 具有賦值後不可動搖的黃金保護層。試著定義你的安全常數！",
            expectedExplanation = "請宣告一個常數叫 `PI`，並指派值為 `3.14` 給它。在下一行用 `console.log(PI);` 印出它！",
            initialCode = "// 請在下方寫程式：\n// 1. 宣告 const PI 並指派常數值為 3.14\n// 2. 呼叫 console.log(PI);\n",
            validate = { code, output, vars ->
                val value = vars["PI"]?.trim()
                val hasPrinted = output.trim().contains("3.14")
                if (value == "3.14" && hasPrinted) {
                    Pair(true, "太優秀了！常數 PI 配置完成且順利輸出。它被鎖定在記憶體中，任何人都無法偷偷修改它了！")
                } else if (value == "3.14") {
                    Pair(false, "你成功將常數 PI 設為了 3.14，但忘記在 console 輸出它囉！")
                } else {
                    Pair(false, "找不到定義為 3.14 的 PI 常數。特別提示：請寫 `const PI = 3.14;` 喔！")
                }
            }
        ),
        "js_8" to HandsOnChallenge(
            lessonId = "js_8",
            title = "💡 實作對話四：等號與自身更新乘法",
            description = "在程式世界， `=` 依然代表搬運工，將右邊算好的值覆蓋回左邊變數盒！",
            expectedExplanation = "你的帳戶餘額為 `let balance = 20;`。請在下方將你的餘額乘以 `5` 倍（即 `balance = balance * 5;`），最後列印出 `balance`！",
            initialCode = "let balance = 20;\n// 請在下方接著寫：\n// 1. 將 balance 的值乘以 5 倍並更新回自身\n// 2. 用 console.log 列印 balance 的值\n",
            validate = { code, output, vars ->
                val value = vars["balance"]?.trim()
                val hasPrinted = output.trim().contains("100")
                if (value == "100" && hasPrinted) {
                    Pair(true, "太令人興奮了！運用自我乘法賦值，你的餘額順利飆升並列印出了 100！")
                } else if (value == "100") {
                    Pair(false, "餘額 balance 已正確乘五變 100，但你忘記用 `console.log(balance);` 印出來看結果了喔！")
                } else {
                    Pair(false, "餘額目前為 $value，不是 100。特別提示：使用算術乘號：`balance = balance * 5;`！")
                }
            }
        ),
        "js_9" to HandsOnChallenge(
            lessonId = "js_9",
            title = "💡 實作對話五：樣板字面與反引號融合",
            description = "在 ES6 中，我們可以用反引號 ` 與 \${變數} 插值，實現超直覺的拼貼結合！",
            expectedExplanation = "我們已經宣告 `const hero = \"JsCoach\"`。請在下方接著宣告一個常數 `const msg`，其值為樣板插值：`` `Hello \${hero}` ``。最後呼叫 `console.log(msg);`！",
            initialCode = "const hero = \"JsCoach\";\n// 請在下方接著寫：\n// 1. 宣告 const msg 為反引號樣板字句，包含 hero 變數插值\n// 2. 用 console.log 輸出 msg 的內容\n",
            validate = { code, output, vars ->
                val value = vars["msg"]?.trim()
                val hasPrinted = output.trim().contains("Hello JsCoach")
                if (value == "Hello JsCoach" || hasPrinted) {
                    Pair(true, "炫麗的融合技！成功運用反引號，在螢幕上列印併合出了 \"Hello JsCoach\" 字樣！")
                } else {
                    Pair(false, "看起來 msg 的內容不是 \"Hello JsCoach\"。提示：請寫 `const msg = `Hello \${hero}`;` 並印出它！")
                }
            }
        ),
        "js_10" to HandsOnChallenge(
            lessonId = "js_10",
            title = "💡 實作對話六：三等號 === 的嚴格性考驗",
            description = "寬鬆相等 `==` 會偷偷幫你強制轉型，而嚴格相等 `===` 同時比對數值型態！",
            expectedExplanation = "請宣告一個變數叫 `isMatch`，將比較算式 `10 === \"10\"` 存入其中（即判斷數字 10 與字串 \"10\" 是否嚴格相等）。最後把 `isMatch` 用 `console.log(isMatch);` 列印出來！",
            initialCode = "// 請在下方寫程式：\n// 1. 宣告 let isMatch 儲存 10 === \"10\" 的比較結果\n// 2. 將 isMatch 用 console.log 印出\n",
            validate = { code, output, vars ->
                val isMatch = vars["isMatch"]?.trim()
                val hasPrinted = output.trim().contains("false")
                if (isMatch == "false" && hasPrinted) {
                    Pair(true, "完全正確！因為型態不同（Number 與 String），`===` 嚴格判定為 false，成功避開強制轉型地雷！")
                } else if (isMatch == "false") {
                    Pair(false, "isMatch 的計算是正確的 false。但你忘記用 `console.log(isMatch);` 列印它了！")
                } else {
                    Pair(false, "找不到值為 false 的 isMatch 比較。提示：必須寫成 `let isMatch = 10 === \"10\";`！")
                }
            }
        ),

        // HTML Challenges
        "html_2" to HandsOnChallenge(
            lessonId = "html_2",
            title = "💡 實作對話一：宣告你的 HTML 根元素大門",
            description = "所有的 HTML 網頁元素都必須被包裹在完整的雙標籤內！",
            expectedExplanation = "請在下方編輯器中宣告一對完整的 `<html>` 與 `</html>` 標籤，並在它們中間寫下進程文字 `你好網頁`！",
            initialCode = "<!-- 請在下方宣告 html 根標籤與內容 -->\n",
            validate = { code, output, vars ->
                val clean = code.replace("\\s".toRegex(), "")
                if (clean.contains("<html>") && clean.contains("</html>") && clean.contains("你好網頁")) {
                    Pair(true, "太棒了！你手寫出第一個 HTML 網頁根元素！標籤開閉完整，奠定完美網頁基礎！")
                } else {
                    Pair(false, "糟糕，似乎沒有宣告成對的 <html> </html> 或其中缺少了「你好網頁」文字哦。請檢查拼寫！")
                }
            }
        ),
        "html_6" to HandsOnChallenge(
            lessonId = "html_6",
            title = "💡 實作對話二：主宰頭條的 H1 大標題",
            description = "h1 是網頁上最具份量、最不可忽視的第一級標題！",
            expectedExplanation = "請在下方編輯器中宣告一對完整的 `<h1>` 與 `</h1>` 標籤，並在中間裝入文字 `前端教練` 來印出超大標題！",
            initialCode = "<!-- 在下方寫出標題代碼 -->\n",
            validate = { code, output, vars ->
                val hasH1 = vars.any { it.key.startsWith("h1_") && it.value.contains("前端教練") }
                if (hasH1) {
                    Pair(true, "完美！你成功透過 h1 大字體描繪出了最核心的網頁大字標題物件，渲染成功！")
                } else {
                    Pair(false, "找不到寫著「前端教練」的 <h1> 標籤。提示：請寫 `<h1>前端教練</h1>` 喔！")
                }
            }
        ),
        "html_7" to HandsOnChallenge(
            lessonId = "html_7",
            title = "💡 實作對話三：裝載故事的段落盒子",
            description = "段落標籤 p 會幫內容換行並預留上下邊距，最適合存放長篇文章！",
            expectedExplanation = "請在下方宣告一對 `<p>` 與 `</p>` 標籤，裡面整齊裝入文字 `Python與JS都很棒`！",
            initialCode = "<!-- 請在下方寫出段落標籤 -->\n",
            validate = { code, output, vars ->
                val hasP = vars.any { it.key.startsWith("p_") && it.value.contains("Python與JS都很棒") }
                if (hasP) {
                    Pair(true, "成功！段落渲染完畢，這正是構成資訊網頁的最基本元素！")
                } else {
                    Pair(false, "找不到寫著「Python與JS都很棒」的 <p> 標籤。提示：請寫 `<p>Python與JS都很棒</p>`")
                }
            }
        ),
        "html_10" to HandsOnChallenge(
            lessonId = "html_10",
            title = "💡 實作對話四：醒目的粗體強調重點",
            description = "用 strong 標籤可以讓部分語句變得加粗、重要，利於搜尋引擎抓取！",
            expectedExplanation = "請在下方宣告一對完整的 `<strong>` 與 `</strong>` 標籤，將文字 `必考關鍵字` 加粗強調！",
            initialCode = "<!-- 請在此處寫粗體強調標籤 -->\n",
            validate = { code, output, vars ->
                val hasStrong = vars.any { it.key.startsWith("strong_") && it.value.contains("必考關鍵字") }
                if (hasStrong) {
                    Pair(true, "太突出了！文字在視覺上被順利加粗，SEO 抓取重要度翻倍！")
                } else {
                    Pair(false, "找不到寫著「必考關鍵字」的 <strong> 標籤。提示：請寫 `<strong>必考關鍵字</strong>`！")
                }
            }
        ),
        "html_17" to HandsOnChallenge(
            lessonId = "html_17",
            title = "💡 實作對話五：穿梭宇宙的傳送門超連結",
            description = "a 標籤帶有 href 屬性，點擊它即可把讀者無縫傳送到世界的彼端！",
            expectedExplanation = "請建立一個 `<a href=\"https://google.com\">點我搜尋</a>` 超連結標籤！",
            initialCode = "<!-- 建立超連結，href 指向 https://google.com -->\n",
            validate = { code, output, vars ->
                val hasLink = vars.any { it.key.startsWith("a_") && it.value.contains("https://google.com") && (it.value.contains("點我搜尋") || it.value.contains("点我搜尋")) }
                if (hasLink) {
                    Pair(true, "太厲害了啦！這個超連結標籤能讓使用者一鍵飛向搜尋大帝國 Google！")
                } else {
                    Pair(false, "連結配置未成功。提示：確認屬性為 `href=\"https://google.com\"` 且中間顯示文字為 `點我搜尋`！")
                }
            }
        ),
        "html_19" to HandsOnChallenge(
            lessonId = "html_19",
            title = "💡 實作對話六：精彩的圖片顯示標籤",
            description = "img 是單邊標籤，它不需要 closing 閉合。只要告訴它 src 來源圖片路徑即可！",
            expectedExplanation = "請宣告一個 `<img src=\"logo.png\" alt=\"PyCoachLogo\">` 圖片標籤！",
            initialCode = "<!-- 請在此處寫圖片標籤 -->\n",
            validate = { code, output, vars ->
                val hasImg = vars.any { it.key.startsWith("img_") && it.value.contains("src=logo.png") && it.value.contains("alt=PyCoachLogo") }
                if (hasImg) {
                    Pair(true, "大獲成功！圖片標籤完整解開。alt 替代文字能在圖片失效或視障者閱讀時完美替代，是一門大愛心設計！")
                } else {
                    Pair(false, "圖片標籤配置未完全正確。提示：寫法 ` <img src=\"logo.png\" alt=\"PyCoachLogo\"> `，注意不要亂加 </img> 喔！")
                }
            }
        ),
        // Kotlin Challenges
        "kotlin_1" to HandsOnChallenge(
            lessonId = "kotlin_1",
            title = "💡 Kotlin 實作一：第一句 Kotlin 程式",
            description = "Kotlin 的輸出核心為 println() 函數！",
            expectedExplanation = "請在下方編輯器中，使用 println(\"Hello Kotlin\") 印出該段字串。",
            initialCode = "// 請在下方撰寫：使用 println 印出 \"Hello Kotlin\"\n",
            validate = { code, output, vars ->
                val cleanOutput = output.trim()
                if (cleanOutput.contains("Hello Kotlin")) {
                    Pair(true, "太棒了！你成功印出了第一個 Kotlin 字串！")
                } else {
                    Pair(false, "輸出的內容沒有包含 \"Hello Kotlin\"。請檢查拼寫與大小寫！")
                }
            }
        ),
        "kotlin_3" to HandsOnChallenge(
            lessonId = "kotlin_3",
            title = "💡 Kotlin 實作二：變數與常數的修煉",
            description = "val 宣告不可變常數，var 宣告可變變數。",
            expectedExplanation = "請宣告 `val language = \"Kotlin\"`，再宣告 `var score = 100`，並將 score 改為 `105`，最後列印 score！",
            initialCode = "// 請宣告 language 與 score 變數，並更新 score 為 105，最後 println(score)\n",
            validate = { code, output, vars ->
                val scoreVal = vars["score"]?.trim()
                val hasPrinted = output.trim().contains("105")
                if (scoreVal == "105" && hasPrinted) {
                    Pair(true, "完美！成功掌握 val 與 var 的分水嶺與變數更新！")
                } else {
                    Pair(false, "score 變數未設為 105 或尚未印出。提示：使用 var 宣告 score，並指派 105 後 println(score)！")
                }
            }
        ),
        "kotlin_12" to HandsOnChallenge(
            lessonId = "kotlin_12",
            title = "💡 Kotlin 實作三：Null 安全與可空型態",
            description = "在型態後加上問號 ? 可以宣告可空的變數！",
            expectedExplanation = "請宣告一個可空變數 `var nickname: String? = null`，並用 println(nickname) 印出它！",
            initialCode = "// 請宣告可空變數 nickname 並設為 null，接著印出它\n",
            validate = { code, output, vars ->
                val hasPrinted = output.trim().contains("null")
                if (hasPrinted) {
                    Pair(true, "正確！在 Kotlin 中，String? 代表其值可以安然承載 null！")
                } else {
                    Pair(false, "未偵測到輸出 null。提示：`var nickname: String? = null` 且 `println(nickname)`！")
                }
            }
        ),
        "kotlin_13" to HandsOnChallenge(
            lessonId = "kotlin_13",
            title = "💡 Kotlin 實作四：貓王運算子 ?: 的安全城堡",
            description = "貓王運算子 ?: 能在左方為 null 時提供保底默認值！",
            expectedExplanation = "已提供 `val nickname: String? = null`。請宣告 `val displayName = nickname ?: \"訪客\"` 並列印 displayName！",
            initialCode = "val nickname: String? = null\n// 請在下方宣告 displayName 搭配 ?: 保底值 \"訪客\" 並印出\n",
            validate = { code, output, vars ->
                val display = vars["displayName"]?.trim()
                val hasPrinted = output.trim().contains("訪客")
                if (hasPrinted || display == "訪客") {
                    Pair(true, "超讚！貓王運算子成功捕捉了 null 並安全替換為「訪客」！")
                } else {
                    Pair(false, "displayName 尚未等於「訪客」。提示：`val displayName = nickname ?: \"訪客\"`！")
                }
            }
        ),
        "kotlin_28" to HandsOnChallenge(
            lessonId = "kotlin_28",
            title = "💡 Kotlin 實作五：Data Class 資料類別精髓",
            description = "data class 自動生成 toString()、equals()、hashCode() 與 copy()！",
            expectedExplanation = "請建立一筆資料 `val myBook = Book(\"Kotlin\", 350)` 並列印 myBook！",
            initialCode = "data class Book(val title: String, val price: Int)\n// 請宣告 myBook 變數並印出\n",
            validate = { code, output, vars ->
                val cleanOutput = output.trim()
                if (cleanOutput.contains("Book") || cleanOutput.contains("Kotlin")) {
                    Pair(true, "完全正確！Data class 自動輸出了漂亮易讀的屬性文字！")
                } else {
                    Pair(false, "未檢測到 Book 的列印結果。提示：`val myBook = Book(\"Kotlin\", 350)` 並 `println(myBook)`！")
                }
            }
        )
    )
}
