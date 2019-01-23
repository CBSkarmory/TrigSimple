package io.github.cbskarmory

sealed abstract class Token
case class TokSin() extends Token
case class TokCsc() extends Token
case class TokCos() extends Token
case class TokSec() extends Token
case class TokTan() extends Token
case class TokCot() extends Token
case class TokPlus() extends Token
case class TokMinus() extends Token
case class TokTimes() extends Token
case class TokDiv() extends Token
case class TokPow() extends Token
case class TokLParen() extends Token
case class TokRParen() extends Token
case class TokInt(x: Int) extends Token
case class EOF() extends Token