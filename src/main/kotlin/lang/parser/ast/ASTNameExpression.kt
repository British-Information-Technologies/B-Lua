package lang.parser.ast

import lang.parser.Parser


class ASTNameExpression(
	val name: String,
	start:Int,
	end:Int,
	parser: Parser,
): ASTExpression(
	start = start,
	end = end,
	parser = parser
)