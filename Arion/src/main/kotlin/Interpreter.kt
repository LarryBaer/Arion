import Expression.*
import Statement.*

class Interpreter :  ExpressionVisitor<String>, StatementVisitor<Void> {
    private val state = State()

    fun interpretStatements(statements: List<Statement?>) {
        for(i in 0 until statements.size){
            if(statements[i] != null){
                executeStatement(statements[i])
            }
        }
    }

    private fun executeStatement(statement: Statement?) {
        statement?.accept(this)
    }

    private fun evaluateExpression(expression: Expression): String {
        return expression.accept(this)
    }

    // ----------| Expressions |----------
    override fun visitAssignExpression(assign: Assign): String {
        val variableValue = evaluateExpression(assign.variableValue)
        state.setVariableValue(assign.variableName, variableValue)
        return variableValue
    }

    override fun visitGroupingExpression(grouping: Grouping): String {
        return evaluateExpression(grouping.operand)
    }

    override fun visitUnaryExpression(unary: Unary): String {
        val operand = evaluateExpression(unary.operand)

        // TODO: Possibly create a compareToken() method similar to the one in Parser.kt
        if(unary.operator.tokenType == "not"){
            return (!operand.toBoolean()).toString()
        }

        throw Exception("visitUnaryExpression error")
    }

    override fun visitLiteralExpression(literal: Literal): String {
        return literal.operand
    }

    override fun visitBinaryExpression(binary: Binary): String {
        val leftOperand = evaluateExpression(binary.leftOperand)
        val rightOperand = evaluateExpression(binary.rightOperand)
        when (binary.operator.tokenType) {
            "add" -> return (leftOperand.toInt() + rightOperand.toInt()).toString()
            "subtract" -> return (leftOperand.toInt() - rightOperand.toInt()).toString()
            "multiply" -> return (leftOperand.toInt() * rightOperand.toInt()).toString()
            "divide" -> return (leftOperand.toInt() / rightOperand.toInt()).toString()
            "greater_than" -> return (leftOperand > rightOperand).toString()
            "less_than" -> return (leftOperand < rightOperand).toString()
            "greater_than_or_equal_to" -> return (leftOperand >= rightOperand).toString()
            "less_than_or_equal_to" -> return (leftOperand <= rightOperand).toString()
            "equal" -> return (leftOperand == rightOperand).toString()
            "not_equal" -> return (leftOperand != rightOperand).toString()
        }

        throw Exception("Visit binary expression error")
    }

    override fun visitVariableDeclarationExpression(variable: VariableDeclaration): String {
        return state.getVariableValue(variable.variableName).toString()
    }

    // ----------| Statements |----------
    override fun visitExpressionStatement(expressionStatement: ExpressionStatement) {
        evaluateExpression(expressionStatement.expression)
    }

    override fun visitLogStatement(log: Log) {
        val valueToLog = evaluateExpression(log.expression)
        println(valueToLog)
    }

    override fun visitIfStatement(ifStatement: If) {
        if ((evaluateExpression(ifStatement.booleanExpression)).toBoolean()) {
            executeStatement(ifStatement.ifStatementBody)
        }
    }

    override fun visitVariableInitializationStatement(variable: VariableInitializer) {
        var variableValue = evaluateExpression(variable.variableValue)
        state.declareVariable(variable.variableName.tokenValue, variableValue)
    }
}