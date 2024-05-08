package Expression

abstract class Expression {
    abstract fun <T> accept(visitor: ExpressionVisitor<T>): T
}
