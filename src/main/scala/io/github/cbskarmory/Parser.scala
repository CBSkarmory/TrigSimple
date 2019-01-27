package io.github.cbskarmory

object Parser {

    private def lookahead(l: List[Token]): Token = l match {
        case h :: _ => h
        case _ => throw new ParseError("No lookahead")
    }

    def parseExpr(toks: List[Token]): Expr = {
        val (toks2, ex) = parseExprHelp(toks)
        matchToken(toks2, EOF())
        ex
    }

    private def matchToken(toks: List[Token], tok: Token): List[Token] = toks match {
        case Nil => throw new IllegalArgumentException(
            s"Failed to parse ${tok.toString} from empty token list"
        )
        case `tok` :: t => t
        case h :: _ => throw new ParseError(
            s"Malformed input -- parse error: " +
                    s"Expected ${tok.toString} from input; got ${h.toString}"
        )
    }

    private def parseExprHelp(toks: List[Token]): (List[Token], Expr) = {
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
        val (toks2, ex) = parsePowerExpr(toks)
        lookahead(toks2) match {
            case TokTimes() =>
                val toks3 = matchToken(toks2, TokTimes())
                val (toks4, ex2) = parseMultiplicativeExpr(toks3)
                (toks4, Mult(ex, ex2))
            case TokDiv() =>
                val toks3 = matchToken(toks2, TokDiv())
                val (toks4, ex2) = parseMultiplicativeExpr(toks3)
                (toks4, Div(ex, ex2))
            case _ => (toks2, ex)
        }
    }

    private def parsePowerExpr(toks: List[Token]): (List[Token], Expr) = {
        val (toks2, base) = parsePrimaryExpr(toks)
        lookahead(toks2) match {
            case TokPow() =>
                val toks3 = matchToken(toks2, TokPow())
                val (toks4, exponent) = parsePowerExpr(toks3)
                (toks4, Pow(base, exponent))
            case _ => (toks2, base)
        }
    }

    private def parsePrimaryExpr(toks: List[Token]): (List[Token], Expr) = {
        lookahead(toks) match {
            case TokInt(s) => (matchToken(toks, TokInt(s)), IntExpr(s.toInt))
            case TokSin() => (matchToken(toks, TokSin()), Sin())
            case TokCsc() => (matchToken(toks, TokCsc()), Csc())
            case TokCos() => (matchToken(toks, TokCos()), Cos())
            case TokSec() => (matchToken(toks, TokSec()), Sec())
            case TokTan() => (matchToken(toks, TokTan()), Tan())
            case TokCot() => (matchToken(toks, TokCot()), Cot())
            case EOF() => throw new ParseError("Encountered EOF token early; is the token list empty?")
            case _ =>
                // handles end of expr automatically
                val toks2 = matchToken(toks, TokLParen())
                val (toks3, ex) = parseExprHelp(toks2)
                val toks4 = matchToken(toks3, TokRParen())
                (toks4, ex)
        }
    }
}

class InterpreterError(msg: String) extends IllegalArgumentException(msg) {
    val this.msg = msg

    override def toString: String = msg
}

class ParseError(msg: String) extends InterpreterError(msg)
