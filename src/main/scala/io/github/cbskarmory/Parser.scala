package io.github.cbskarmory

object Parser {

    private def lookahead(l: List[Token]): Token = l match {
        case h :: _ => h
        case _ => throw new ParseError("No lookahead")
    }

    private def matchToken(toks: List[Token], tok: Token): List[Token] = toks match {
        case Nil => throw new IllegalArgumentException(
            s"Failed to parse ${tok.getClass.getName} from empty token list"
        )
        case `tok` :: t => t
        case h :: _ => throw new IllegalArgumentException(
            s"Expected ${tok.getClass.getName} from input; got ${h.getClass.getName}"
        )
    }

    def parseExpr(toks: List[Token]): (List[Token], Expr) = {
        parseAdditiveExpr(toks)
    }

    private def parseAdditiveExpr(toks: List[Token]): (List[Token], Expr) = {
        val (toks2, ex) = parseMultiplicativeExpr(toks)
        lookahead(toks2) match {
            case TokPlus() =>
                val toks3 = matchToken(toks2, TokPlus())
                val (toks4, ex2) = parseAdditiveExpr(toks3)
                (toks4, Add(ex, ex2))
            case TokMinus() =>
                val toks3 = matchToken(toks2, TokMinus())
                val (toks4, ex2) = parseAdditiveExpr(toks3)
                (toks4, Sub(ex, ex2))
            case _ => (toks2, ex)
        }
    }

    private def parseMultiplicativeExpr(toks: List[Token]): (List[Token], Expr) = {
        throw new NotImplementedError()
    }

    private def parsePowerExpr(toks: List[Token]): (List[Token], Expr) = {
        throw new NotImplementedError()
    }

    private def parsePrimaryExpr(toks: List[Token]): (List[Token], Expr) = {
        throw new NotImplementedError()
    }
}

class ParseError(msg: String) extends Error {
    val this.msg = msg
    override def toString: String = {
        msg + "\n" + super.toString
    }
}
