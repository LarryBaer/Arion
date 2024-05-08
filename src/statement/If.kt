package Statement

import Expression.Expression

class If(val booleanExpression: Expression, val ifStatementBody: Statement?, val elifStatementBody: Statement?, val elseStatementBody: Statement?): Statement(){
    override fun <T> accept(visitor: StatementVisitor<T>) = visitor.visitIfStatement(this)
}