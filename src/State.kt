import Statement.FunctionDeclaration
import Tokenizer.Token

class State {
    private val variables: MutableMap<String, String> = mutableMapOf()
    private val functions_: MutableMap<String, FunctionDeclaration> = mutableMapOf()
    private var sectionState: State? = null

    constructor(sectionState: State?) {
        this.sectionState = sectionState
    }

    fun declareFunction(function_: FunctionDeclaration) {
        functions_[function_.name.tokenValue] = function_
    }

    fun getFunction(functionName: String): FunctionDeclaration? {
        if (functions_[functionName] == null) {
            throw Error("Function does not exist")
        }
        return functions_[functionName]
    }

    fun declareVariable(name: String, value: String) {
        variables[name] = value
    }

    fun getVariableValue(name: Token): String? {
        if (variables[name.tokenValue] == null) {
            val tempSectionState = sectionState
            if (tempSectionState != null) {
                return tempSectionState.getVariableValue(name)
            } else {
                throw Error("Variable does not exist")
            }
        }

        return variables[name.tokenValue]
    }

    fun setVariableValue(name: Token, value: String) {
        if (variables[name.tokenValue] == null) {
            val tempSectionState = sectionState
            if (tempSectionState != null) {
                tempSectionState.setVariableValue(name, value)
                sectionState = tempSectionState
                return
            }
        }

        variables[name.tokenValue] = value
    }

    fun printAllVariables() {
        for (variable in variables) {
            println(variable.toString())
        }
    }
}
