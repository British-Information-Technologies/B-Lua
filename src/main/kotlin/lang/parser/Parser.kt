package lang.parser

import lang.lexer.Lexer
import lang.lexer.Token
import lang.lexer.TokenType
import lang.parser.ast.*
import java.beans.Expression


/**
 * The Lua Parser
 *
 * follows the standard Lua grammar from the lua doc:
 * ```
 * 	chunk ::= block
 *
 * 	block ::= {stat} [retstat]
 *
 * 	stat ::=  ‘;’ |
 * 		 varlist ‘=’ explist |
 * 		 functioncall |
 * 		 label |
 * 		 break |
 * 		 goto Name |
 * 		 do block end |
 * 		 while exp do block end |
 * 		 repeat block until exp |
 * 		 if exp then block {elseif exp then block} [else block] end |
 * 		 for Name ‘=’ exp ‘,’ exp [‘,’ exp] do block end |
 * 		 for namelist in explist do block end |
 * 		 function funcname funcbody |
 * 		 local function Name funcbody |
 * 		 local attnamelist [‘=’ explist]
 *
 * 	attnamelist ::=  Name attrib {‘,’ Name attrib}
 *
 * 	attrib ::= [‘<’ Name ‘>’]
 *
 * 	retstat ::= return [explist] [‘;’]
 *
 * 	label ::= ‘::’ Name ‘::’
 *
 * 	funcname ::= Name {‘.’ Name} [‘:’ Name]
 *
 * 	varlist ::= var {‘,’ var}
 *
 * 	var ::=  Name | prefixexp ‘[’ exp ‘]’ | prefixexp ‘.’ Name
 *
 * 	namelist ::= Name {‘,’ Name}
 *
 * 	explist ::= exp {‘,’ exp}
 *
 * 	exp ::=  nil | false | true | Numeral | LiteralString | ‘...’ | functiondef |
 * 		 prefixexp | tableconstructor | exp binop exp | unop exp
 *
 * 	prefixexp ::= var | functioncall | ‘(’ exp ‘)’
 *
 * 	functioncall ::=  prefixexp args | prefixexp ‘:’ Name args
 *
 * 	args ::=  ‘(’ [explist] ‘)’ | tableconstructor | LiteralString
 *
 * 	functiondef ::= function funcbody
 *
 * 	funcbody ::= ‘(’ [parlist] ‘)’ block end
 *
 * 	parlist ::= namelist [‘,’ ‘...’] | ‘...’
 *
 * 	tableconstructor ::= ‘{’ [fieldlist] ‘}’
 *
 * 	fieldlist ::= field {fieldsep field} [fieldsep]
 *
 * 	field ::= ‘[’ exp ‘]’ ‘=’ exp | Name ‘=’ exp | exp
 *
 * 	fieldsep ::= ‘,’ | ‘;’
 *
 * 	binop ::=  ‘+’ | ‘-’ | ‘*’ | ‘/’ | ‘//’ | ‘^’ | ‘%’ |
 * 		 ‘&’ | ‘~’ | ‘|’ | ‘>>’ | ‘<<’ | ‘..’ |
 * 		 ‘<’ | ‘<=’ | ‘>’ | ‘>=’ | ‘==’ | ‘~=’ |
 * 		 and | or
 *
 * 	unop ::= ‘-’ | not | ‘#’ | ‘~’
 * ```
 *
 * @author michael-bailey
*/
class Parser(
	private val _tokens: List<Token>,
	private val counter: IParserCounter = ParserCounter(_tokens),

): IParserCounter by counter {

	constructor(
		lexer: Lexer
	): this(
		_tokens = lexer.tokens.filter {
			!listOf(TokenType.Space,TokenType.Tab).contains(it.type)
		}
	) {  }
	
	/**
	 * Starts calculating the AST
	 *
	 * @author michael-bailey
	 */
	val rootNode by lazy<ASTChunk?> {
		if (_tokens.isEmpty()) return@lazy null

		val statements = mutableListOf<ASTStatement>()

		while (!matchCurrent(TokenType.EndOfSource)) {
			statements += parseStatement()
		}

		ASTChunk(
			statements = statements,
			end = cursorPosition,
			parser = this
		)
	}

	/**
	 * attempts to parse a statement
	 */
	private fun parseStatement(): ASTStatement {
		val start = cursorPosition

		val node = when (currentToken.type) {
			TokenType.Name -> parseAssignment()
			else -> return ASTInvalidStatement(
				start = start,
				end = cursorPosition,
				parser = this
			)
		}

		return node
	}


	/**
	 * parses an assignment statement
	 *
	 * from the lua docs:
	 * ```
	 * varlist ‘=’ explist
	 * varlist ::= var {‘,’ var}
	 * explist ::= exp {‘,’ exp}
	 * ```
	 *
	 */
	private fun parseAssignment(): ASTStatement {
		val start = cursorPosition
		val names = parseNameList()

		if (isTokensEnd || currentToken.type != TokenType.Assignment) {
			advance()
			return ASTInvalidStatement(
				start = start,
				end = cursorPosition,
				parser = this
			)
		}

		val assignmentToken = currentToken
		advance()

		val expressions = parseExpressionList()

		return ASTAssignment(
			variables = names,
			expressions = expressions,
			start = start,
			end = cursorPosition,
			parser = this
		)
	}

	/**
	 * parses a name list
	 */
	private fun parseNameList(): List<ASTName> {
		val start = cursorPosition
		val list = mutableListOf<ASTName>()

		list += ASTName(
			name = currentToken.text,
			start = start,
			end = cursorPosition,
			parser = this
		)
		advance()

		while (currentToken.type == TokenType.ListSeparator) {
			advance()
			if (currentToken.type == TokenType.Name) {
				list += ASTName(
					name = currentToken.text,
					start = start,
					end = cursorPosition,
					parser = this
				)
				advance()
			}
		}
		return list
	}

	private fun parseExpressionList(): List<ASTExpression> {
		val list = mutableListOf<ASTExpression>()

		list += parseExpression()

		while (matchCurrent(TokenType.ListSeparator)) {
			advance()
			if (matchCurrent(TokenType.expressions)) {
				list += parseExpression()
			}
		}
		return list
	}
}