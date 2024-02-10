import lang.lexer.Lexer
import lang.parser.Parser
import lang.test.expect
import kotlin.test.Test


internal class LuaExpressionTest {

	@Test
	fun TestSingleAssignmentStatement() {
		val tree = Parser(Lexer("""
			a = 1
		""".trimIndent())).rootNode
		expect(tree).statementCountToBe(1)
	}

	@Test
	fun TestMultiAssignmentStatement() {
		val tree = Parser(Lexer("""
			a,b,c = 1,2,3
		""".trimIndent())).rootNode
		expect(tree).statementCountToBe(1)
	}

	@Test
	fun TestAssignmentExpression() {
		val tree = Parser(Lexer("""
			a,b,c,d = 1+1,2-2,3*3,4/4
		""".trimIndent())).rootNode
		expect(tree).statementCountToBe(1)
	}



}