package Expression

class Literal(val operand: String): Expression() {
    override fun <T> accept(visitor: ExpressionVisitor<T>): T = visitor.visitLiteralExpression(this)
}