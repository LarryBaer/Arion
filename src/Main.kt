import Tokenizer.Token
import Tokenizer.Tokenizer
import java.io.File

fun main(args: Array<String>) {
    if (args.size < 1) {
        throw IllegalArgumentException("ERROR: Missing or invalid file path argument.")
        return
    }

    var file = File(args[0]).readText()

    val tokenizer = Tokenizer(file)
    var tokenList = tokenizer.tokenize()

    val parser = Parser(tokenList)
    val statementList = parser.parseTokens()

    val interpreter = Interpreter()
    interpreter.interpretStatements(statementList)
}

private fun printTokenList(tokenList: List<Token>) {
    for (token in tokenList) {
        println(token.tokenValue + " type: " + token.tokenType)
    }
}
