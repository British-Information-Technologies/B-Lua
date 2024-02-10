package lang.parser

import lang.lexer.TokenType
import lang.parser.ast.*


internal fun Parser.parseExpression(): ASTExpression {
	return parseTerm()
}

private fun Parser.parseTerm(): ASTExpression {
	val start = cursorPosition
	var left = parseFactor()

	while (matchCurrent(TokenType.Addition, TokenType.Negate)) {
		val tokenType = currentToken.type

		advance()

		val right = parseTerm()
		val type = when (tokenType) {
			TokenType.Addition -> ASTBinaryOperationType.Addition
			TokenType.Negate -> ASTBinaryOperationType.Subtraction
			else -> ASTBinaryOperationType.Invalid
		}
		left = ASTBinaryOperation(
			left = left,
			type = type,
			right = right,
			start = start,
			end = cursorPosition,
			parser = this,
		)
	}
	return left
}

private fun Parser.parseFactor(): ASTExpression {
	val start = cursorPosition
	var left = parseUnary()

	while (matchCurrent(TokenType.Multiply, TokenType.Division, TokenType.IntegerDivision)) {
		val tokenType = currentToken.type

		advance()

		val right = parseFactor()
		val type = when (tokenType) {
			TokenType.Multiply -> ASTBinaryOperationType.Multiplication
			TokenType.Division -> ASTBinaryOperationType.Division
			TokenType.IntegerDivision -> ASTBinaryOperationType.IntegerDivision
			else -> ASTBinaryOperationType.Invalid
		}
		left = ASTBinaryOperation(
			left = left,
			type = type,
			right = right,
			start = start,
			end = cursorPosition,
			parser = this,
		)
	}
	return left
}

private fun Parser.parseUnary(): ASTExpression {
	val start = cursorPosition

	val type = when (currentToken.type) {
		TokenType.Negate -> ASTUnaryOperationType.Negate
		TokenType.Xor -> ASTUnaryOperationType.Not
		TokenType.Length -> ASTUnaryOperationType.Length
		else -> ASTUnaryOperationType.Invalid
	}

	if (type == ASTUnaryOperationType.Invalid) {
		return parseValue()
	}

	advance()

	val expression = parseValue()

	val node = ASTUnaryOperation(
		type = type,
		expression = expression,
		start = start,
		end = cursorPosition,
		parser = this
	)

	return node
}

private fun Parser.parseValue(): ASTExpression {
	val start = cursorPosition

	val node = when (currentToken.type) {
		TokenType.Name -> {
			ASTVariable(
				name = currentToken.text,
				start = start,
				end = cursorPosition,
				parser = this
			)
		}
		TokenType.NumberLiteral -> {
			ASTNumberLiteral(
				value = currentToken.text,
				start = start,
				end = cursorPosition,
				parser = this
			)
		}
		TokenType.StringLiteral -> {
			ASTStringLiteral(
				value = currentToken.text.slice(1..<currentToken.text.lastIndex),
				start = start,
				end = cursorPosition,
				parser = this
			)
		}
		TokenType.True -> {
			ASTBooleanLiteral(
				value = true,
				start = start,
				end = cursorPosition,
				parser = this
			)
		}
		TokenType.False -> {
			ASTBooleanLiteral(
				value = true,
				start = start,
				end = cursorPosition,
				parser = this
			)
		}
		TokenType.Nil -> {
			ASTNilLiteral(
				start = start,
				end = cursorPosition,
				parser = this
			)
		}
		TokenType.OpenNormal -> {
			advance()
			val expression = parseExpression()
			if (currentToken.type == TokenType.ClosedNormal) {
				expression
			} else {
				ASTInvalidExpression(
					start = start,
					end = cursorPosition,
					parser = this
				)
			}
		}
		else -> {
			ASTInvalidValue(
				start = start,
				end = cursorPosition,
				parser = this
			)
		}
	}
	advance()
	return node
}
