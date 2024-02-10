package lang.parser

import lang.lexer.Token
import lang.lexer.TokenType

class ParserCounter(
	override val tokens: List<Token>,
): IParserCounter {
	override var cursorPosition: Int = 0
}
