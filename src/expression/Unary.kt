package Expression

import Tokenizer.Token

class Unary(val operator: Token, val operand: Expression): Expression() {
    override fun <T> accept(visitor: ExpressionVisitor<T>): T = visitor.visitUnaryExpression(this)
}