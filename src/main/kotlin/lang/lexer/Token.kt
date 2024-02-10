package lang.lexer

/**
 * Reperesents a lexed token
 *
 * @property type [TokenType] The type of this token.
 * @property text [String] The source text that this token was derived from.
 * @property start [Int] The source index of the first character of this token.
 * @property end [Int] The source index of the end of this character.
 *
 * @property lexer [Lexer] The lexer used to create this token, used for extracting extra data.
 *
 * @author michael-bailey
 * @since 0.1
 */
data class Token(
	val type: TokenType,
	val text: String,
	val start: Int,
	val end: Int,
	private val lexer: Lexer,
) {
	fun getSourceString(): String = lexer.source.slice(start..end)


}