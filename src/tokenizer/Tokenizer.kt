package Tokenizer

class Tokenizer(private val code: String) {
    private var keywords: HashMap<String, String> = hashMapOf(
        "log" to "log",
        "if" to "if",
        "true" to "true",
        "false" to "false",
        "var" to "variable",
        "for" to "for",
        "while" to "while",
        "else" to "else",
        "elif" to "elif",
        "function" to "function")
    private var tokenList = arrayListOf<Token>()
    private var index = 1
    private var currentCharacter = code[index]
    private var tokenType = ""
    private var tokenValue = code[index].toString()
    private var isPreviousVar = false

    fun tokenize(): List<Token>{
        while (index < code.length) {
            currentCharacter = code[index]
            tokenValue = code[index].toString()
            tokenType = ""
            when(currentCharacter){
                '(' -> tokenType = "left_parenthesis"
                ')' -> tokenType = "right_parenthesis"
                '{' -> tokenType = "left_curly_brace"
                '}' -> tokenType = "right_curly_brace"
                '+' -> tokenType = "add"
                '-' -> tokenType = "subtract"
                '/' -> tokenType = "divide"
                '*' -> tokenType = "multiply"
                '%' -> tokenType = "modulo"
                '>' -> tokenType = "greater_than"
                '<' -> tokenType = "less_than"
                '=' -> tokenType = "assign"
                '$' -> tokenType = "global_variable"
                ';' -> tokenType = "semi_colon"
                '"' -> getString()
                '#' -> {
                    handleComment()
                    continue
                }
                '|' -> {
                    tokenType = "or"
                    index++
                }
                '&' -> {
                    tokenType = "and"
                    index++
                }
                else -> {
                    // Whitespace has no meaning in Arion. Therefore, we skip over it.
                    if(currentCharacter.isWhitespace()){
                        index++
                        continue
                    }

                    if(currentCharacter.isDigit()){
                        getNumber()
                    }

                    if(currentCharacter.isLetter()){
                        getWord()

                        if(keywords.containsKey(tokenValue)){
                            tokenType = keywords[tokenValue].toString()
                        }

                        if(isPreviousVar){
                            tokenType = "variable"
                        }

                        isPreviousVar = tokenValue == "var"
                    }

                    if (tokenType == "" || tokenValue == "") {
                        println("Tokenizer error with this character: $currentCharacter")
                    }
                }
            }

            tokenList.add(Token(tokenType, tokenValue))
            index++
        }

        return tokenList
    }

    private fun getNumber(){
        var value = ""
        while (index < code.length && currentCharacter.isDigit()) {
            value += currentCharacter
            index++

            if (index < code.length) {
                currentCharacter = code[index]
            }

        }

        tokenType = "number"
        tokenValue = value
        index -= 1
    }

    private fun getWord(){
        var value = ""
        while (index < code.length && currentCharacter.isLetter()) {
            value += currentCharacter
            index++

            if (index < code.length) {
                currentCharacter = code[index]
            }

        }

        tokenType = "variableName"
        tokenValue = value
        index -= 1
    }

    private fun getString(){
        // Skip the first quote
        index++

        currentCharacter = code[index]
        var value = ""
        while (index < code.length && currentCharacter !=  '"'){
            value += currentCharacter
            index++

            if (index < code.length) {
                currentCharacter = code[index]
            }

        }

        tokenType = "word"
        tokenValue = value
    }

    private fun handleComment(){
        currentCharacter = code[++index]
        while(currentCharacter != '#'){
            currentCharacter = code[++index]
        }
        index++
    }
}