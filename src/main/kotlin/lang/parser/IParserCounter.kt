package lang.parser

import lang.lexer.Token
import lang.lexer.TokenType

interface IParserCounter {
	val tokens: List<Token>

	var cursorPosition: Int

	val currentToken: Token get() = tokens[cursorPosition]

	val isTokensEnd: Boolean get() = currentToken.type == TokenType.EndOfSource

	/**
	 * Tells parser if the current token matches any of the provided tokens.
	 */
	fun matchCurrent(vararg tokens: TokenType): Boolean = kotlin
		.runCatching { tokens.contains(currentToken.type) }
		.getOrDefault(false)

	fun matchCurrent(tokens: Collection<TokenType>): Boolean = kotlin
		.runCatching { tokens.contains(currentToken.type) }
		.getOrDefault(false)

	fun advance() {
		cursorPosition += 1
	}
}