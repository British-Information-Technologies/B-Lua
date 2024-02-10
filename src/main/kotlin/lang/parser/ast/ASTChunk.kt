package lang.parser.ast

import lang.parser.Parser


class ASTChunk(
	val statements: List<ASTStatement>,
	end: Int,
	parser: Parser,
): ASTNode(
	start = 0,
	end = end,
	parser = parser,
)