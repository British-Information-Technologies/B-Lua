package lang.parser.ast

import kotlinx.serialization.Serializable
import lang.parser.Parser

class ASTAssignment(
	private val variables: List<ASTName>,
	private val expressions: List<ASTExpression>,
	start: Int,
	end: Int,
	parser: Parser,
): ASTStatement(
	start = start,
	end = end,
	parser = parser
)