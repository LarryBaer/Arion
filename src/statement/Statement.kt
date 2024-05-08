package Statement


abstract class Statement {
    abstract fun <T> accept(visitor: StatementVisitor<T>)
}