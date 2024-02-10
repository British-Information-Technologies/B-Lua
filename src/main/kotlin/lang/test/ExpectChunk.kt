package lang.test

import lang.parser.ast.ASTChunk

class ExpectChunk(private val chunk: ASTChunk?) {
	fun statementCountToBe(count: Int) {
		assert(chunk?.statements?.count() == count)
	}

	fun statementCountToBeGreaterThan(count: Int) {
		assert(chunk?.statements?.count() == count)
	}

	fun statementCountToBeGreaterThanOrEqual(count: Int) {
		assert(chunk?.statements?.count() == count)
	}

	fun ToBeNull() {
		assert (chunk == null)
	}

	fun ToNotBeNull() {
		assert (chunk != null)
	}

	fun firstStatement(): ExpectStatement {
		return ExpectStatement(chunk?.statements?.get(0))
	}
}
