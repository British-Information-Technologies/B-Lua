package lang.parser.ast

data class ASTFunctionStatement(
	val name: String,
	val params: List<String>,
	val body: List<ASTStatement>,
)