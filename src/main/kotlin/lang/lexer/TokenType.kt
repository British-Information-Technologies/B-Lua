package lang.lexer

enum class TokenType {
 	Name, NumberLiteral, StringLiteral, Comment, Label,

 	// keywords
	And, Break, Do, Else,
	Elseif, End, False, For,
	Function, Goto, If, In,
	Local, Nil, Not, Or,
	Repeat, Return, Then, True,
	Until, While, VariableArgs,

	// Operators
	Assignment, StatementEnd, VariableArguments,
	ListSeparator, DotAccessor, ColonAccessor,

	// Expessions
	BitWiseLeftShift, BitWiseRightShift, Addition, IntegerDivision,
	Modulo, Multiply, Division, Order,
	Length, BitWiseAnd, BitWiseOr, Negate,
	Xor, Concatenate,

	//	comparators
	Equality, LessThanEquals, LessThan,
	GreaterThan, GreaterThanEquals, NotEqual,

	// brackets
	OpenNormal, ClosedNormal,
	OpenSquare, ClosedSquare,
	OpenCurly, ClosedCurly,

	// whitespace
	Space,
	Tab,
	Newline,
	EndOfSource,

	// errors
	InvalidName, InvalidNumber, InvalidString, Invalid, InvalidLabel;


	companion object {
		val expressions = setOf(
			BitWiseLeftShift, BitWiseRightShift, Addition, IntegerDivision,
			Modulo, Multiply, Division, Order,
			Length, BitWiseAnd, BitWiseOr, Negate,
			Xor, Concatenate, Name,  NumberLiteral, StringLiteral, Nil
		)
	}
}