package Expression

import tokenizer.Token

class Binary(val leftOperand: Expression, val operator: Token, val rightOperand: Expression): Expression() {
    override fun <T> accept(visitor: ExpressionVisitor<T>): T = visitor.visitBinaryExpression(this)
}