package lang.parser

import lang.lexer.Token
import lang.lexer.TokenType


internal fun Parser.bypassStatement() {
	while (matchCurrent(TokenType.StatementEnd, TokenType.EndOfSource)) {
		advance()
	}
}
