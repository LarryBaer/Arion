import Statement.Statement
import tokenizer.Token
import tokenizer.Tokenizer
import java.io.File

fun main(args: Array<String>) {
    var file = getFile()

    val tokenizer = Tokenizer(file)
    var tokenList = tokenizer.tokenize()
//    for(token in tokenList){
//        println(token.tokenValue + " type: " + token.tokenType)
//    }
    val parser = Parser(tokenList)
    val statementList = parser.parseTokens()

    val interpreter = Interpreter()
    interpreter.interpretStatements(statementList)
}

// TODO: Use a file input from user (arn "filepath")
fun getFile(): String{
    return File("C:\\Users\\lbaer\\OneDrive\\Desktop\\Arion\\src\\test\\test1.txt").readText()
}