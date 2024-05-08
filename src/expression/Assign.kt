package Expression

import Tokenizer.Token

class Assign(val variableName: Token, val variableValue: Expression): Expression() {
    override fun <T> accept(visitor: ExpressionVisitor<T>): T = visitor.visitAssignExpression(this)
}