package Expression

class Grouping(val operand: Expression): Expression() {
    override fun <T> accept(visitor: ExpressionVisitor<T>): T = visitor.visitGroupingExpression(this)
}