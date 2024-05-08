package Expression

class FunctionCall(val functionName: String): Expression() {
    override fun <T> accept(visitor: ExpressionVisitor<T>): T = visitor.visitCallExpression(this)
}