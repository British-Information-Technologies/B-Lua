package lang.parser.ast

import lang.parser.Parser

class ASTNumberLiteral(
	val value: String,
	start: Int,
	end: Int,
	parser: Parser
): ASTExpression(
	start = start,
	end = end,
	parser = parser
)