import Expression.*
import Statement.*

class Interpreter : ExpressionVisitor<String>, StatementVisitor<Void> {
    private var state = State(null)

    fun interpretStatements(statements: List<Statement?>) {
        for (i in statements.indices) {
            if (statements[i] != null) {
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

    fun executeSection(statementsToExecute: List<Statement>?, sectionState: State) {
        val tempState = state

        // Temporarily change the state to utilize scope
        state = sectionState

        if (statementsToExecute != null) {
            for (statement in statementsToExecute.indices) {
                if (statementsToExecute[statement] != null) {
                    executeStatement(statementsToExecute[statement])
                }
            }
        }

        state = tempState
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

        if (unary.operator.tokenType == "not") {
            return (!operand.toBoolean()).toString()
        }

        throw Exception("Unary expression error")
    }

    override fun visitLiteralExpression(literal: Literal): String {
        return literal.operand
    }

    override fun visitBinaryExpression(binary: Binary): String {
        val leftOperand = evaluateExpression(binary.leftOperand).toInt()
        val rightOperand = evaluateExpression(binary.rightOperand).toInt()
        when (binary.operator.tokenType) {
            "add" -> return (leftOperand + rightOperand).toString()
            "subtract" -> return (leftOperand - rightOperand).toString()
            "multiply" -> return (leftOperand * rightOperand).toString()
            "divide" -> return (leftOperand / rightOperand).toString()
            "modulo" -> return (leftOperand % rightOperand).toString()
            "greater_than" -> return (leftOperand > rightOperand).toString()
            "less_than" -> return (leftOperand < rightOperand).toString()
            "greater_than_or_equal_to" -> return (leftOperand >= rightOperand).toString()
            "less_than_or_equal_to" -> return (leftOperand <= rightOperand).toString()
            "equal" -> return (leftOperand == rightOperand).toString()
            "not_equal" -> return (leftOperand != rightOperand).toString()
        }

        throw Exception("Binary expression error")
    }

    override fun visitLogicalExpression(logical: Logical): String {
        val leftOperand = evaluateExpression(logical.leftOperand)
        val rightOperand = evaluateExpression(logical.rightOperand)

        if (logical.operator.tokenType === "or") {
            if (leftOperand == "true" || rightOperand == "true") {
                return "true"
            }
        } else if (logical.operator.tokenType === "and") {
            if (leftOperand == "true" && rightOperand == "true") {
                return "true"
            }
        }

        return "false"
    }

    override fun visitVariableDeclarationExpression(variable: VariableDeclaration): String {
        return state.getVariableValue(variable.variableName).toString()
    }

    override fun visitCallExpression(functionCall: FunctionCall): String {
        val functionName = functionCall.functionName
        val functionBody = state.getFunction(functionName)?.body
        executeSection(functionBody, state)
        return ""
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
        } else if (
            ifStatement.elifStatementBody is If &&
            evaluateExpression(ifStatement.elifStatementBody.booleanExpression).toBoolean()
        ) {
            executeStatement(ifStatement.elifStatementBody)
        } else if (
            ifStatement.elifStatementBody is If &&
            ifStatement.elifStatementBody.elseStatementBody != null
        ) {
            executeStatement(ifStatement.elifStatementBody.elseStatementBody)
        } else if (ifStatement.elseStatementBody != null) {
            executeStatement(ifStatement.elseStatementBody)
        }
    }

    override fun visitVariableInitializationStatement(variable: VariableInitializer) {
        val variableValue = evaluateExpression(variable.variableValue)
        state.declareVariable(variable.variableName.tokenValue, variableValue)
    }

    override fun visitWhileStatement(whileLoop: While) {
        while (evaluateExpression(whileLoop.booleanExpression) == "true") {
            executeStatement(whileLoop.whileStatementBody)
        }
    }

    override fun visitSectionStatement(section: Section) {
        if (section.sectionBody != null) {
            executeSection(section.sectionBody, State(state))
        }
    }

    override fun visitFunctionStatement(functionDeclaration: FunctionDeclaration) {
        state.declareFunction(functionDeclaration)
    }
}
