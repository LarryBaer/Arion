import Expression.*
import Statement.*
import Tokenizer.Token

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
        }else if(compareNextToken("function")){
            scanToken()
            return parseFunctionDeclaration()
        } else {
            parseStatement()
        }
    }

    private fun parseFunctionDeclaration(): FunctionDeclaration {
        val name: Token = tokenList[currentIndex]
        scanToken()
        validateSyntax("left_parenthesis")
        validateSyntax("right_parenthesis")
        validateSyntax("left_curly_brace")
        val body: List<Statement>? = parseSection()
        return FunctionDeclaration(name, body)
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
        }else if(compareNextToken("left_curly_brace")){
            scanToken()
            return Section(parseSection())
        }else if(compareNextToken("while")){
            scanToken()
            return parseWhileStatement()
        }else if(compareNextToken("for")){
            scanToken()
            return parseForStatement()
        } else{
            parseExpressionStatement()
        }

    }

    private fun parseForStatement(): Statement{
        validateSyntax("left_parenthesis")

        val expression1: Statement = if (compareNextToken("variable")) {
            scanToken()
            parseVariableDeclaration()
        } else {
            // Get variable outside of loop scope
            parseExpressionStatement()
        }

        validateSyntax("semi_colon")
        var expression2: Expression = parseAssignment()
        validateSyntax("semi_colon")
        var expression3: Expression = parseAssignment()
        validateSyntax("right_parenthesis")
        var loopBody: Statement = parseStatement()

        loopBody = Section(listOf(expression1,
            While(expression2,
                Section(listOf(loopBody,
                    ExpressionStatement(expression3))))))

        return loopBody
    }

    private fun parseWhileStatement(): Statement {
        validateSyntax("left_parenthesis")
        val booleanExpression = parseAssignment()
        validateSyntax("right_parenthesis")
        val whileStatementBody: Statement = parseStatement()
        return While(booleanExpression, whileStatementBody)
    }

    private fun parseIfStatement(): Statement {
        validateSyntax("left_parenthesis")
        val booleanExpression = parseAssignment()
        validateSyntax("right_parenthesis")

        val ifStatementBody: Statement? = parseStatement()
        var elifStatementBody: Statement? = null
        var elseStatementBody: Statement? = null

        if(compareNextToken("elif")){
            scanToken()
            elifStatementBody = parseIfStatement()
        }

        if(compareNextToken("else")){
            scanToken()
            elseStatementBody = parseStatement()
        }

        return If(booleanExpression, ifStatementBody, elifStatementBody, elseStatementBody)
    }

    private fun parseLogStatement(): Statement {
        validateSyntax("left_parenthesis")
        val valueToLog = parseAssignment()
        validateSyntax("right_parenthesis")
        return Log(valueToLog)
    }

    private fun parseSection(): List<Statement>? {
        val sectionBody = arrayListOf<Statement>()

        // Get every line of code in a section
        while (currentIndex < tokenList.size && tokenList[currentIndex].tokenType != "right_curly_brace") {
            sectionBody.add(parseDeclaration())
        }

        validateSyntax("right_curly_brace")
        return sectionBody
    }

    private fun parseExpressionStatement(): Statement {
        val expressionStatement = parseAssignment()
        return ExpressionStatement(expressionStatement)
    }

    private fun parseAssignment(): Expression {
        val ast = parseOr()

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

    private fun parseOr(): Expression {
        var ast = parseAnd()

        while(true){
            if(compareNextToken("or")){
                scanToken()
                val logicalOr: Token = tokenList[currentIndex - 1]
                val rightAst = parseAnd()
                ast = Logical(ast, logicalOr, rightAst)
            }else{
                return ast
            }
        }
    }

    private fun parseAnd(): Expression {
        var ast = parseComparison()

        while(true){
            if(compareNextToken("and")){
                scanToken()
                val logicalAnd: Token = tokenList[currentIndex - 1]
                val rightAst = parseComparison()
                ast = Logical(ast, logicalAnd, rightAst)
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
            if(compareNextToken( "multiply", "divide", "modulo")){
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
        while(true){
            return if(compareNextToken("not", "minus_minus")){
                scanToken()
                val operator = currentToken
                val rightExpression = parseUnary()
                Unary(operator, rightExpression)
            }else{
                parseFunctionCall()
            }
        }
    }

    private fun parseFunctionCall(): Expression {
        var ast: Expression = parseLiteral()
        while (true) {
            if (compareNextToken("left_parenthesis")) {
                scanToken()
                validateSyntax("right_parenthesis")
                if(ast is VariableDeclaration){
                    ast = FunctionCall(ast.variableName.tokenValue)
                }
            } else {
                break
            }
        }
        return ast
    }

    private fun parseLiteral(): Expression {
        while(true){
            return if (compareNextToken("true", "false", "number", "word")){
                scanToken()
                Literal(currentToken.tokenValue)
            }else{
                parseParenthesis()
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
            if (currentIndex < tokenList.size && tokenList[currentIndex].tokenType == tokenType) {
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
