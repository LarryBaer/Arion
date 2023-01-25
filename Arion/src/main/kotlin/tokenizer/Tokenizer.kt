//TODO: put the if statements that are in the when else into separate functions if possible

package tokenizer

class Tokenizer(code: String) {
    val code = code

    fun tokenize(): List<Token>{
        var tokenList = arrayListOf<Token>()
        var i = 1
        var isPreviousVar = 0
        while (i < code.length) {
            var currentCharacter = code[i]
            var tokenType = ""
            var tokenValue = code[i].toString()

            when(currentCharacter){
                '(' -> tokenType = "left_parenthesis"
                ')' -> tokenType = "right_parenthesis"
                '{' -> tokenType = "left_curly_brace"
                '}' -> tokenType = "right_curly_brace"
                '+' -> tokenType = "add"
                '-' -> tokenType = "subtract"
                '/' -> tokenType = "divide"
                '*' -> tokenType = "multiply"
                '>' -> tokenType = "greater_than"
                '<' -> tokenType = "less_than"
                '=' -> tokenType = "assign"
                '$' -> tokenType = "global_variable"
                else -> {
                    // Whitespace has to meaning in Arion. Therefore, we skip over it.
                    if(currentCharacter.isWhitespace()){
                        i++
                        continue
                    }

                    if(currentCharacter.isLetter()){
                        var value = ""
                        var c = currentCharacter
                        while (i < code.length && c.isLetter()) {
                            value += c
                            i++

                            if (i < code.length) {
                                c = code[i]
                            }

                        }

                        tokenType = "variableName"
                        tokenValue = value
                        i -= 1

                        // Keywords
                        //TODO: Create hashmap to handle keywords
                        if(isPreviousVar == 1){
                            tokenType = "variable"
                            isPreviousVar = 0
                        }

                        if(tokenValue == "log"){
                            tokenType = "log"
                        }

                        if(tokenValue == "if"){
                            tokenType = "if"
                        }

                        if(tokenValue == "true"){
                            tokenType = "true"
                        }

                        if(tokenValue == "var"){
                            tokenType = "variable"
                            isPreviousVar = 1
                        }

                    }

                    if(currentCharacter == '"'){
                        i++

                        // this will fail if it reaches the end of the code
                        currentCharacter = code[i]
                        var value = ""
                        var c = currentCharacter
                        while (i < code.length && c.isLetter()) {
                            value += c
                            i++

                            if (i < code.length) {
                                c = code[i]
                            }

                        }

                        tokenType = "word"
                        tokenValue = value
                    }

                    if(currentCharacter.isDigit()){
                        var value = ""
                        var c = currentCharacter
                        while (i < code.length && c.isDigit()) {
                            value += c
                            i++

                            if (i < code.length) {
                                c = code[i]
                            }

                        }

                        tokenType = "number"
                        tokenValue = value
                        i -= 1
                    }

                    if (tokenType == "" || tokenValue == "") {
                        println("Tokenizer error with this character: $currentCharacter")
                    }
                }
            }



            var token = Token(tokenType, tokenValue)
            tokenList.add(token)
            i++
        }

        return tokenList
    }
}