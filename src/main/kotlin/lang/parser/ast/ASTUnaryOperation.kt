package lang.parser.ast

import lang.parser.Parser

class ASTUnaryOperation(
	private val type: ASTUnaryOperationType,
	private val expression: ASTExpression,
	start: Int,
	end: Int,
	parser: Parser
): ASTExpression(
	start = start,
	end = end,
	parser = parser,
) {
}