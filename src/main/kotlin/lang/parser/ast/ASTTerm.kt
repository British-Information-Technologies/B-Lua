package lang.parser.ast

import lang.parser.Parser

class ASTTerm(
	val type: Boolean,
	val left: ASTExpression,
	val right: ASTExpression,
	start: Int,
	end: Int,
	parser: Parser,
): ASTExpression(
	start = start,
	end = end,
	parser = parser,
)