package Statement

import Expression.Expression
import Tokenizer.Token

class VariableInitializer(val variableName: Token, val variableValue: Expression): Statement() {
    override fun <T> accept(visitor: StatementVisitor<T>) = visitor.visitVariableInitializationStatement(this)
}