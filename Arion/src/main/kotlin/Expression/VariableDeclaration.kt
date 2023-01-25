package Expression

import tokenizer.Token

class VariableDeclaration(val variableName: Token): Expression(){
    override fun <T> accept(visitor: ExpressionVisitor<T>): T = visitor.visitVariableDeclarationExpression(this)
}