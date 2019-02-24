# TrigSimple
[![Build Status](https://travis-ci.org/CBSkarmory/TrigSimple.png)](https://travis-ci.org/CBSkarmory/TrigSimple)
[![codecov](https://codecov.io/gh/CBSkarmory/TrigSimple/branch/master/graph/badge.svg)](https://codecov.io/gh/CBSkarmory/TrigSimple)
[![License: AGPL v3](https://img.shields.io/badge/License-AGPL%20v3-blue.svg)](https://www.gnu.org/licenses/agpl-3.0)

Simplifies trigonometric expressions automatically with state space search AI

Shows work from start to simplified expression

## Build Instructions
Use `sbt assembly` to generate a standalone jar file

Use `sbt run` to run with [sbt](https://www.scala-sbt.org/)

Use `sbt test` to run tests

## Example Usage
### Simplifier
```
[TrigSimple v0.2-beta]
Type 'exit' to exit or an expression to simplify. Ctrl-C to cancel
----------------
> tan ^ 2 / (tan^2 + 1)
----------------
Parsed: ((tan^2) / ((tan^2) + 1))
768 nodes explored
12 steps
----------------
((tan^2) / ((tan^2) + 1))
((tan^2) / (((sin / cos)^2) + 1))
((tan^2) / (((sin^2) / (cos^2)) + 1))
((tan^2) / (((sin^2) / (cos^2)) + ((cos^2) / (cos^2))))
((tan^2) / (((sin^2) + (cos^2)) / (cos^2)))
(((tan^2) * (cos^2)) / ((sin^2) + (cos^2)))
(((tan^2) * (cos^2)) / 1)
((tan^2) * (cos^2))
((cos^2) * (tan^2))
((cos^2) * ((sin / cos)^2))
((cos^2) * ((sin^2) / (cos^2)))
(sin^2)
----------------
> tan * sin + cos
----------------
Parsed: ((tan * sin) + cos)
23 nodes explored
9 steps
----------------
((tan * sin) + cos)
((sin * tan) + cos)
((sin * (sin / cos)) + cos)
(((sin * sin) / cos) + cos)
(((sin^2) / cos) + cos)
(((sin^2) + (cos * cos)) / cos)
(((sin^2) + (cos^2)) / cos)
(1 / cos)
sec
----------------
> exit
```
### Proof Solver
```
[TrigSimple v0.3-beta]
Type 'exit' to exit or an expression to simplify. Ctrl-C to cancel
[Identity proof solver]
Enter starting expression:
> sin * tan + cos
--------
Parsed: ((sin * tan) + cos), enter target expr or 'retry' to re-enter starting expr:
> 1 / cos
Parsed: (1 / cos)
21 nodes explored
7 steps
--------
((sin * tan) + cos)
((sin * (sin / cos)) + cos)
(((sin * sin) / cos) + cos)
(((sin^2) / cos) + cos)
(((sin^2) + (cos * cos)) / cos)
(((sin^2) + (cos^2)) / cos)
(1 / cos)
```