package lang.parser.ast

import lang.parser.Parser

open class ASTInvalidNode(
	start: Int,
	end: Int,
	parser: Parser,
): ASTNode(
	start = start,
	end = end,
	parser = parser,
)