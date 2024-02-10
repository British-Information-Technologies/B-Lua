package lang.parser.ast

import lang.parser.Parser

class ASTInvalidStatement(
	start: Int,
	end: Int,
	parser: Parser
): ASTStatement(
	start = start,
	end = end,
	parser = parser,
) {
}