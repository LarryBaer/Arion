package Expression

interface ExpressionVisitor<T> {
    fun visitAssignExpression(assign: Assign): T
    fun visitBinaryExpression(binary: Binary): T
    fun visitGroupingExpression(grouping: Grouping): T
    fun visitLiteralExpression(literal: Literal): T
    fun visitUnaryExpression(unary: Unary): T
    fun visitVariableDeclarationExpression(variableDeclaration: VariableDeclaration): T
    fun visitLogicalExpression(logical: Logical): T
    fun visitCallExpression(call: FunctionCall): T
}