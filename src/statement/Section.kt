package Statement

class Section(val sectionBody: List<Statement>?) : Statement() {
    override fun <T> accept(visitor: StatementVisitor<T>) = visitor.visitSectionStatement(this)
}
