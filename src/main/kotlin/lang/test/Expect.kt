package lang.test

import lang.parser.ast.ASTChunk
import lang.parser.ast.ASTStatement

fun expect(chunk: ASTChunk?): ExpectChunk {
	return ExpectChunk(chunk = chunk)
}

fun expect(statement: ASTStatement?): ExpectStatement {
	return ExpectStatement(statement)
}