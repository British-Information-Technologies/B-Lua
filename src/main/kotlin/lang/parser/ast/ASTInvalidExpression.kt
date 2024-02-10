package lang.parser.ast

import lang.parser.Parser

class ASTInvalidExpression(
	start: Int,
	end: Int,
	parser: Parser,
): ASTExpression(
	start = start,
	end = end,
	parser = parser,
) {
}