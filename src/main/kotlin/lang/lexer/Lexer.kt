package lang.lexer

/**
 * The lexer, or tokeniser for the language.
 *
 * DOESN'T SUPPORT SOME STRING DEFINITIONS
 *
 * @author michael-bailey
 * @since 0.1
 */
class Lexer(
	val source: String,
) {

	private var _tokens = listOf<Token>()

	private var currentStart: Int = 0
	private var currentEnd: Int = 0

	private val currentString: String get() = if (isSourceEnd) {
		source.slice(currentStart..source.lastIndex)
	} else {
		source.slice(currentStart..currentEnd)
	}

	private val currentChar: Char get() = source[currentEnd]
	private val nextChar: Char? get() = kotlin.runCatching { source[currentEnd+1] }.getOrNull()

	private val isSourceEnd: Boolean get() = currentEnd > source.lastIndex

	/**
	 * generates tokens from the source, when called.
	 */
	val tokens by lazy {
		if (source.isEmpty()) return@lazy listOf<Token>()
		while (!isSourceEnd) {
			when(currentChar) {
				// all operators that start with a '='
				'=' -> equalsOperators()
				// all operators that start with a '<' or '>'
				'>' , '<' -> angleOperators()
				'0','1','2','3','4','5','6','7','8','9' -> number()
				' ', '\t' -> whitespace()
				'\n' -> newLine()
				'+', '*', '/', '%', '^', '&', '-', '~', ',' -> standardOperator()
				'(', ')', '{', '}', '[', ']' -> bracket()
				'"', '\'' -> string()
				';' -> semicolon()
				':' -> colon()
				'.' -> dots()
				else -> nameAndKeyword()
			}
		}
		addToken(TokenType.EndOfSource)
		_tokens
	}

	private fun equalsOperators() {
		when (nextChar) {
			'=' -> {
				advance()
				addToken(TokenType.Equality)
			}
			else -> {
				addToken(TokenType.Assignment)
			}
		}
	}

	private fun angleOperators() {
		val type = when (currentChar) {
			'>' -> {
				when (nextChar) {
					'=' -> {
						advance()
						TokenType.GreaterThanEquals
					}
					'>' -> {
						advance()
						TokenType.BitWiseRightShift
					}
					else -> TokenType.GreaterThan
				}
			}
			'<' -> {
				when (nextChar) {
					'=' -> {
						advance()
						TokenType.LessThanEquals
					}
					'<' -> {
						advance()
						TokenType.BitWiseLeftShift
					}
					else -> TokenType.LessThan
				}
			}
			else -> {
				null
			}
		}

		if (type != null) {
			addToken(type)
		} else {
			addToken(TokenType.Invalid)
		}
	}

	private fun number() {
		while (nextChar != null && numberCharacters.matches(nextChar.toString())) {
			advance()
		}
		addToken(TokenType.NumberLiteral)
	}

	private fun nameAndKeyword() {
		while (nextChar != null && nameCharacters.matches(nextChar.toString())) {
			advance()
		}
		val type = getKeywordType()
		if (type != null) {
			addToken(type)
		} else {
			if (isValidName()) {
				addToken(TokenType.Name)
			} else {
				addToken(TokenType.InvalidName)
			}
		}
	}

	private fun getKeywordType(): TokenType? {
		return when (currentString) {
			"and" -> TokenType.And
			"break" -> TokenType.Break
			"do" -> TokenType.Do
			"else" -> TokenType.Else
			"elseif" -> TokenType.Elseif
			"end" -> TokenType.End
			"false" -> TokenType.False
			"for" -> TokenType.For
			"function" -> TokenType.Function
			"goto" -> TokenType.Goto
			"if" -> TokenType.If
			"in" -> TokenType.In
			"local" -> TokenType.Local
			"nil" -> TokenType.Nil
			"not" -> TokenType.Not
			"or" -> TokenType.Or
			"repeat" -> TokenType.Repeat
			"return" -> TokenType.Return
			"then" -> TokenType.Then
			"true" -> TokenType.True
			"until" -> TokenType.Until
			"while" -> TokenType.While
			else -> null
		}
	}

	private fun isValidName(): Boolean = nameValidator.matches(currentString)

	private fun whitespace() {

		val type = when(currentChar) {
			' ' -> TokenType.Space
			'\t' -> TokenType.Tab
			// not ever reached, if you do, you are special.
			else -> TokenType.Space
		}
		addToken(type)
	}

	private fun newLine() {
		addToken(TokenType.StatementEnd)
	}

	private fun standardOperator() {
		when (currentChar) {
			'+' -> addToken(TokenType.Addition)
			'*' -> addToken(TokenType.Multiply)
			'/' -> {
				if (nextChar == '/') {
					advance()
					addToken(TokenType.IntegerDivision)
				} else {
					addToken(TokenType.Division)
				}
			}
			'%' -> {
				addToken(TokenType.Modulo)
			}
			'^' -> addToken(TokenType.Order)
			'#' -> addToken(TokenType.Length)
			'&' -> addToken(TokenType.BitWiseAnd)
			'|' -> addToken(TokenType.BitWiseOr)
			'-' -> {
				when (nextChar) {
					'-' -> {
						advance()
						while (nextChar != '\n') {
							advance()
						}
						addToken(TokenType.Comment)
					}
					else -> {
						addToken(TokenType.Negate)
					}
				}
			}
			'~' -> {
				when (nextChar) {
					'=' -> {
						advance()
						addToken(TokenType.NotEqual)
					}
					else -> {
						addToken(TokenType.Xor)
					}
				}
			}
			',' -> comma()
			else -> {addToken(TokenType.Invalid)}
		}
	}

	private fun bracket() {
		when (currentChar) {
			'(' -> addToken(TokenType.OpenNormal)
			')' -> addToken(TokenType.ClosedNormal)
			'[' -> addToken(TokenType.OpenSquare)
			']' -> addToken(TokenType.ClosedSquare)
			'{' -> addToken(TokenType.OpenCurly)
			'}' -> addToken(TokenType.ClosedCurly)
		}
	}

	private fun string() {
		val startChar = currentChar

		while (!isSourceEnd) {

			if (nextChar == startChar) {
				advance()
				addToken(TokenType.StringLiteral)
				return
			}

			if (currentChar == '\\') {
				advance()
				advance()
			} else if (nextChar == '\n') {
				addToken(TokenType.Invalid)
				advance()
				return
			} else if (nextChar == null) {
				addToken(TokenType.Invalid)
				return
			} else {
				advance()
			}
		}
		addToken(TokenType.Invalid)
	}

	private fun semicolon() {
		addToken(TokenType.StatementEnd)
	}

	private fun colon() {
		if (nextChar == ':') {
			advance()
			advance()
			while (nextChar != null && nameCharacters.matches(nextChar.toString())) {
				advance()
			}
			if (nextChar != ':') {
				addToken(TokenType.InvalidLabel)
			}
			advance()
			if (nextChar != ':') {
				addToken(TokenType.InvalidLabel)
			}
			advance()
			addToken(TokenType.Label)
		} else {
			addToken(TokenType.ColonAccessor)
		}

	}

	private fun dots() {
		if (nextChar != '.') {
			addToken(TokenType.DotAccessor)
		}
		advance()
		if (nextChar != '.') {
			addToken(TokenType.Concatenate)
		}
		advance()
		addToken(TokenType.VariableArgs)
	}

	private fun comma() {
		addToken(TokenType.ListSeparator)
	}

	/**
	 * Appends a token and increments the counters to begin a new token scan
	 *
	 * @author michael-bailey
	 */
	private fun addToken(type: TokenType) {
		val token = Token(
			type = type,
			text = currentString,
			start = currentStart,
			end = currentEnd,
			lexer = this@Lexer
		)

		_tokens += listOf(token)
		currentEnd += 1
		currentStart = currentEnd
	}

	private fun advance() {
		currentEnd += 1
	}

	companion object {
		val nameCharacters = Regex("[a-zA-Z_]")
		val numberCharacters = Regex("[-0-9.]")

		val nameValidator = Regex("[a-zA-Z_][a-zA-Z0-9_]*")
		val numberValidator = Regex("-?\\d+(\\.\\d+)?([eE][+-]?\\d+)?")
	}
}