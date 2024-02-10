package lang.parser.ast

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import lang.parser.Parser

/**
 * A single Node in the AST tree
 *
 * @property start [Int] The start index of the first token being parsed
 * @property end [Int] The end index of the first token being parsed
 * @property parser [Parser] The parser instance that created this node
 *
 * @author michael-bailey
 */
@Serializable
sealed class ASTNode(
	var start: Int,
	var end: Int,
	@Transient
	private val parser: Parser? = null,
) {
}
