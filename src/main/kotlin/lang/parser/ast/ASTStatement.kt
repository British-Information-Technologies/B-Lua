package lang.parser.ast

import kotlinx.serialization.Serializable
import lang.parser.Parser

sealed class ASTStatement(
	start: Int,
	end: Int,
	parser: Parser,
): ASTNode(
	start = start,
	end = end,
	parser = parser
)