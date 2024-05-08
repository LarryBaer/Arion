package Statement

import Expression.Expression

class While(val booleanExpression: Expression, val whileStatementBody: Statement): Statement() {
    override fun <T> accept(visitor: StatementVisitor<T>) = visitor.visitWhileStatement(this)
}