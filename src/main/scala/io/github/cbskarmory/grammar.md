## Grammar for expressions
Spaces are ignored in between phrases (`sin + cos` == `sin+cos`)

Putting a space in the middle of a phrase (eg `t an`) will result in a parse error

- Expr -> AdditiveExpr
- AdditiveExpr -> MultiplicativeExpr AdditiveOperator **AdditiveExpr** | MultiplicativeExpr
  - AdditiveOperator -> `+` | `-`
- MultiplicativeExpr -> PowerExpr MultiplicativeOperator **MultiplicativeExpr** | PowerExpr
  - MultiplicativeOperator -> `*` | `/`
- PowerExpr -> PrimaryExpr `^` **PowerExpr** | PrimaryExpr
- PrimaryExpr -> `TokInt` | `TokSin` | `TokCos` | ... | `TokLParen` Expr `TokRParen`
