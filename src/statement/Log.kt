package Statement

import Expression.Expression

class Log(val expression: Expression) : Statement() {
    override fun <T> accept(visitor: StatementVisitor<T>) = visitor.visitLogStatement(this)
}
