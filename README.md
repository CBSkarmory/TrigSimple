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
```
[TrigSimple v0.2-beta]
Type 'exit' to exit or an expression to simplify. Ctrl-C to cancel
> tan^2 / (tan^2 + 1)
--------
Parsed: ((tan^2) / ((tan^2) + 1))
768 nodes explored
12 steps
--------
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
```