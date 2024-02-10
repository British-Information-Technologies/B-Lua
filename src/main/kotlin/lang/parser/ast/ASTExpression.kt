package lang.parser.ast

import lang.parser.Parser


abstract class ASTExpression(
	start: Int,
	end: Int,
	parser: Parser,
): ASTNode(
	start = start,
	end = end,
	parser = parser,
)