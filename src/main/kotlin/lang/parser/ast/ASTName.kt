package lang.parser.ast

import kotlinx.serialization.Serializable
import lang.parser.Parser

class ASTName(
	val name: String,
	start: Int,
	end: Int,
	parser: Parser,
): ASTNode(
	start = start,
	end = end,
	parser = parser
) {
}