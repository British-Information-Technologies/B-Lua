package lang.parser.ast

import lang.parser.Parser

class ASTBooleanLiteral(
	val value: Boolean,
	start: Int,
	end: Int,
	parser: Parser,
): ASTExpression(
	start = start,
	end = end,
	parser = parser
)
