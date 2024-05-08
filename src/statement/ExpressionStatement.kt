package Statement

import Expression.Expression

class ExpressionStatement(val expression: Expression) : Statement() {
    override fun <T> accept(visitor: StatementVisitor<T>) = visitor.visitExpressionStatement(this)
}