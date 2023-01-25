import Expression.*
import Statement.*
import tokenizer.Token


class Parser(private val tokenList: List<Token>) {
    private var currentIndex = 0
    private var currentToken = tokenList[0]

    fun parseTokens(): List<Statement?> {
        val statementList = mutableListOf<Statement?>()
        var ast: Statement

        while (currentIndex < tokenList.size) {
            ast =  parseDeclaration()
            statementList.add(ast)
        }

        return statementList
    }

    private fun parseDeclaration(): Statement {
        return if (compareNextToken("variable")){
            scanToken()
            parseVariableDeclaration()
        } else {
            parseStatement()
        }
    }

    private fun parseVariableDeclaration(): Statement {
        validateSyntax("variable")
        val variableName = currentToken
        scanToken()
        var variableValue: Expression = parseAssignment()

        return VariableInitializer(variableName, variableValue)
    }

    private fun parseStatement(): Statement {
        return if (compareNextToken("if")){
            scanToken()
            parseIfStatement()
        }else if (compareNextToken("log")){
            scanToken()
            parseLogStatement()
        }else{
            parseExpressionStatement()
        }

    }

    private fun parseIfStatement(): Statement {
        validateSyntax("left_parenthesis")
        val booleanExpression = parseAssignment()
        validateSyntax("right_parenthesis")

        validateSyntax("left_curly_brace")
        val ifStatementBody: Statement? = parseStatement()
        validateSyntax("right_curly_brace")

        return If(booleanExpression, ifStatementBody)
    }

    private fun parseLogStatement(): Statement {
        validateSyntax("left_parenthesis")
        val valueToLog = parseAssignment()
        validateSyntax("right_parenthesis")
        return Log(valueToLog)
    }

    private fun parseExpressionStatement(): Statement {
        val expression = parseAssignment()
        return ExpressionStatement(expression)
    }

    private fun parseAssignment(): Expression {
        val ast = parseComparison()

        while(true){
            if (compareNextToken("assign")) {
                scanToken()
                val variableValue = parseAssignment()
                if (ast is VariableDeclaration) {
                    val variableName = ast.variableName
                    return Assign(variableName, variableValue)
                }
            }else{
                return ast
            }
        }
    }

    private fun parseComparison(): Expression {
        var ast = parseTerm()
        while(true){
            if(compareNextToken("greater_than", "less_than", "greater_than_or_equal_to", "less_than_or_equal_to", "not_equals", "equals")){
                scanToken()
                val operator = currentToken
                val rightAst = parseTerm()
                ast = Binary(ast, operator, rightAst)
            }else{
                return ast
            }
        }
    }

    private fun parseTerm(): Expression {
        var ast = parseFactor()
        while(true){
            if(compareNextToken("add", "subtract")){
                scanToken()
                val operator = currentToken
                val rightAst = parseFactor()
                ast = Binary(ast, operator, rightAst)
            }else{
                return ast
            }
        }
    }

    private fun parseFactor(): Expression {
        var ast = parseUnary()
        while(true){
            if(compareNextToken( "multiply", "divide")){
                scanToken()
                val operator = currentToken
                val rightAst = parseUnary()
                ast = Binary(ast, operator, rightAst)
            }else{
                return ast
            }
        }
    }

    private fun parseUnary(): Expression {
        var ast = parseLiteral()
        while(true){
            return if(compareNextToken("not", "minus_minus")){
                scanToken()
                val operator = currentToken
                val rightExpression = parseUnary()
                Unary(operator, rightExpression)
            }else{
                ast
            }
        }
    }

    private fun parseLiteral(): Expression {
        while(true){
            if (compareNextToken("true", "false", "number", "word")){
                scanToken()
                return Literal(currentToken.tokenValue)
            }else{
                return parseParenthesis()
            }
        }
    }

    private fun parseParenthesis(): Expression {
        while(true){
            return if (compareNextToken("left_parenthesis")) {
                scanToken()
                val ast = parseAssignment()
                validateSyntax("right_parenthesis")
                Grouping(ast)
            }else if(compareNextToken("variableName")){
                scanToken()
                VariableDeclaration(currentToken)
            }else{
                throw Exception("Syntax error: " + currentToken.tokenValue)
            }
        }
    }

    private fun validateSyntax(tokenTypeInput: String) {
        if (currentIndex < tokenList.size && tokenList[currentIndex].tokenType == tokenTypeInput){
            scanToken()
            return
        }

        throw Exception("Syntax validation error with type: $tokenTypeInput and peek: " + currentToken.tokenValue)
    }

    private fun compareNextToken(vararg tokenTypesInput: String): Boolean {
        for (tokenType in tokenTypesInput) {
            if (currentIndex < tokenList.size && tokenList[currentIndex].tokenType.equals(tokenType)) {
                return true
            }
        }
        return false
    }

    private fun scanToken() {
        if (currentIndex < tokenList.size){
            currentIndex++
        }

        currentToken = tokenList[currentIndex - 1]
    }
}
