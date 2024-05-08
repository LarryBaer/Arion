package Expression

import Tokenizer.Token

class VariableDeclaration(val variableName: Token): Expression(){
    override fun <T> accept(visitor: ExpressionVisitor<T>): T = visitor.visitVariableDeclarationExpression(this)
}