import tokenizer.Token

class State {
    private val variables: MutableMap<String, String> = mutableMapOf()
    fun declareVariable(name: String, value: String) {
        variables[name] = value
    }

    fun getVariableValue(name: Token): String? {
        return variables[name.tokenValue]
    }

    fun setVariableValue(name: Token, value: String) {
        variables[name.tokenValue] = value
    }
}