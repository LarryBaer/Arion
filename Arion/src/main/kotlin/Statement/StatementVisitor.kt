package Statement

interface StatementVisitor<T> {
    fun visitLogStatement(log: Log)
    fun visitExpressionStatement(expressionStatement: ExpressionStatement)
    fun visitVariableInitializationStatement(variableInitializer: VariableInitializer)
    fun visitIfStatement(ifStatement: If)
}