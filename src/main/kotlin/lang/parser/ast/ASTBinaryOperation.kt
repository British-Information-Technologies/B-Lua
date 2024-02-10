package lang.parser.ast

import lang.parser.Parser

class ASTBinaryOperation(
	val type: ASTBinaryOperationType,
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