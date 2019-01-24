package io.github.cbskarmory

object Lexer {

    private val Digit = "([\\d])".r

    def tokenize(chars: List[Char]): List[Token] = chars match {
        case Nil => List(EOF())
        case 's' :: 'i' :: 'n' :: (t: List[Char]) => TokSin() :: tokenize(t)
        case 'c' :: 's' :: 'c' :: (t: List[Char]) => TokCsc() :: tokenize(t)
        case 'c' :: 'o' :: 's' :: (t: List[Char]) => TokCos() :: tokenize(t)
        case 's' :: 'e' :: 'c' :: (t: List[Char]) => TokSec() :: tokenize(t)
        case 't' :: 'a' :: 'n' :: (t: List[Char]) => TokTan() :: tokenize(t)
        case 'c' :: 'o' :: 't' :: (t: List[Char]) => TokCot() :: tokenize(t)
        case '^' :: (t: List[Char]) => TokPow() :: tokenize(t)
        case '+' :: (t: List[Char]) => TokPlus() :: tokenize(t)
        case '-' :: (t: List[Char]) => TokMinus() :: tokenize(t)
        case '*' :: (t: List[Char]) => TokTimes() :: tokenize(t)
        case '/' :: (t: List[Char]) => TokDiv() :: tokenize(t)
        case '(' :: (t: List[Char]) => TokLParen() :: tokenize(t)
        case '[' :: (t: List[Char]) => TokLParen() :: tokenize(t)
        case ')' :: (t: List[Char]) => TokRParen() :: tokenize(t)
        case ']' :: (t: List[Char]) => TokRParen() :: tokenize(t)
        case Digit(x) :: (t: List[Char]) =>
            val tokensAfter: List[Token] = tokenize(t)
            tokensAfter match {
                case TokInt(i) :: tail =>
                    TokInt(x + i) /* string concatenation */ :: tail
                case _ => TokInt(x.toString) :: tokensAfter
            }
        case ' ' :: (t: List[Char]) => tokenize(t) // ignore spaces
        case (somethingElse: Char) :: (_: List[Char]) => throw new IllegalArgumentException(somethingElse.toString)
    }

}
