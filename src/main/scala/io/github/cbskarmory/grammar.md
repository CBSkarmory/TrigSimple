
- Expr -> AdditiveExpr
- AdditiveExpr -> MultiplicativeExpr AdditiveOperator **AdditiveExpr** | MultiplicativeExpr
  - AdditiveOperator -> `+` | `-`
- MultiplicativeExpr -> PowerExpr MultiplicativeOperator **MultiplicativeExpr** | PowerExpr
  - MultiplicativeOperator -> `*` | `/`
- PowerExpr -> PrimaryExpr `^` `TokInt` | PrimaryExpr
- PrimaryExpr -> `TokInt` | `TokSin` | `TokCos` | ... | `TokLParen` Expr `TokRParen`
