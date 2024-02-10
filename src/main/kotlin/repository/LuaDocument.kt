package repository

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import lang.lexer.Lexer
import lang.lexer.Token
import lang.lexer.TokenType
import lang.parser.Parser

/* example
function a()
	local b = 1 + 1
	if b >= 2 then
		print("hello world")
	end
end
 */

/* example
function a()
	local b = 1 + 1
	-- if b >= 2 then
		print("hello world")
	end
end
 */

class LuaDocument {

	val keywords = setOf(
		TokenType.And, TokenType.Break, TokenType.Do, TokenType.Else,
		TokenType.Elseif, TokenType.End, TokenType.False, TokenType.For,
		TokenType.Function, TokenType.Goto, TokenType.If, TokenType.In,
		TokenType.Local, TokenType.Nil, TokenType.Not, TokenType.Or,
		TokenType.Repeat, TokenType.Return, TokenType.Then, TokenType.True,
		TokenType.Until, TokenType.While, TokenType.VariableArgs,
	)

	val names = setOf(
		TokenType.Name,
		TokenType.Label,
	)

	val comments = setOf(
		TokenType.Comment,
	)

	val literals = setOf(
		TokenType.NumberLiteral,
	)

	val operators = setOf(
		TokenType.Assignment, TokenType.BitWiseLeftShift,TokenType.Addition, TokenType.IntegerDivision,
		TokenType.Modulo, TokenType.Multiply, TokenType.Division, TokenType.Order,
		TokenType.Length, TokenType.BitWiseAnd, TokenType.BitWiseOr, TokenType.Negate,
		TokenType.Xor, TokenType.Concatenate, TokenType.StatementEnd, TokenType.VariableArguments,
		TokenType.ListSeparator, TokenType.DotAccessor, TokenType.ColonAccessor,
	)

	val comparators = setOf(
		TokenType.Equality, TokenType.LessThanEquals, TokenType.LessThan,
		TokenType.GreaterThan, TokenType.GreaterThanEquals, TokenType.NotEqual,
	)

	val brackets = setOf(
		TokenType.OpenNormal, TokenType.ClosedNormal,
		TokenType.OpenSquare, TokenType.ClosedSquare,
		TokenType.OpenCurly,  TokenType.ClosedCurly,
	)

	val errors = setOf(
		TokenType.InvalidName,
		TokenType.InvalidNumber,
		TokenType.InvalidString,
		TokenType.Invalid,
		TokenType.InvalidLabel,
	)


	private var _text = MutableStateFlow("")

	val rawText: Flow<String> = _text

	val tokens = _text.map { source ->
		Lexer(source).tokens
	}

	val chunk = _text.map { source ->
		Parser(Lexer(source = source)).rootNode
	}

	val annotatedText: Flow<AnnotatedString> = _text.map { source ->

		val lexer = Lexer(source)

		AnnotatedString.Builder(source).apply {
			lexer.tokens.forEach { token ->

				when (token.type) {
					in keywords -> {
						this.addStyle(SpanStyle(color = Color.Blue), token.start, token.end+1)
					}
					in literals -> {
						this.addStyle(SpanStyle(color = Color.Magenta), token.start, token.end+1)
					}
					in errors -> {
						this.addStyle(SpanStyle(background = Color.Red, color = Color.White), token.start, token.end+1)
					}
					in operators -> {
						when (token.type) {
							TokenType.StatementEnd -> this.addStyle(SpanStyle(background = Color.DarkGray, color = Color.White), token.start, token.end+1)
							else -> this.addStyle(SpanStyle(background = Color.Red, color = Color.Yellow), token.start, token.end+1)
						}
					}
					in comparators -> {
						this.addStyle(SpanStyle(background = Color.Blue, color = Color.White), token.start, token.end+1)
					}
					in names -> {
						this.addStyle(SpanStyle(background = Color.Black, color = Color.Yellow), token.start, token.end+1)
					}
					in comments -> {
						this.addStyle(SpanStyle(background = Color.Gray, color = Color.White), token.start, token.end+1)
					}
					in brackets -> {
						this.addStyle(SpanStyle(background = Color.White, color = Color.Cyan), token.start, token.end+1)
					}
					TokenType.StringLiteral -> {
						this.addStyle(SpanStyle(background = Color.Green, color = Color.White), token.start, token.end+1)
					}
					else -> {
						print("got here")
					}
				}
			}
			val a = lexer
			println(a)
		}.toAnnotatedString()

	}

	fun updateText(text: String) {
		_text.value = text
	}
}