package Statement

interface StatementVisitor<T> {
    fun visitLogStatement(log: Log)
    fun visitExpressionStatement(expressionStatement: ExpressionStatement)
    fun visitVariableInitializationStatement(variableInitializer: VariableInitializer)
    fun visitIfStatement(ifStatement: If)
    fun visitSectionStatement(section: Section)
    fun visitWhileStatement(whileStatement: While)
    fun visitFunctionStatement(function: FunctionDeclaration)
}