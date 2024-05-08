package Statement
import Tokenizer.Token

class FunctionDeclaration(val name: Token, val body: List<Statement>?) : Statement() {
    override fun <T> accept(visitor: StatementVisitor<T>) = visitor.visitFunctionStatement(this)
}
