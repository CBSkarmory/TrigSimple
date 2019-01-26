package io.github.cbskarmory

import java.lang.reflect.Constructor

import scala.collection.mutable



class Simplifier(core: Expr,
                 transforms: Vector[Expr => Option[Expr]] = Rules.transforms,
                 targets: scala.collection.immutable.Set[Expr] = Rules.targets) {

    val DEFAULT_EXPLORATION_LIMIT: Int = 5e4.asInstanceOf[Int]
    val DEFAULT_MAX_DEPTH: Int = 16

    val seen = new mutable.HashSet[Expr]()
    private def cmp(tup: (Int, Expr)): Int = {-tup._1}
    private val toCheck : mutable.PriorityQueue[(Int, Expr)]= mutable.PriorityQueue.empty(Ordering.by(cmp))
    val this.transforms = transforms

    def getSimplified: Option[Expr] = this.ans
    def getWork: Option[Vector[Expr]] = this.path

    var checks = 0
    var skips = 0
    var maxDepth: Int = core.depth

    private def explore(maxChecks: Int = Int.MaxValue): (Option[Expr], Option[Vector[Expr]]) = {
        val parent : mutable.HashMap[Expr,Expr]= mutable.HashMap()
        val level : mutable.HashMap[Expr, Int] = mutable.HashMap(core -> 0)
        toCheck.enqueue((core.depth, core))
        while (toCheck.nonEmpty) {
            val curr = toCheck.dequeue()._2
            checks += 1
            if (checks >= maxChecks) {
                println(s"Timeout at $checks nodes explored, probably unknown")
                return (None, None)
            }
            //if (!seen.contains(curr)) {
                seen.add(curr)
                maxDepth = if (maxDepth >= curr.depth) maxDepth else curr.depth

                if (targets.contains(curr) || curr.isInstanceOf[IntExpr]) {
                    var trace = Vector(curr)
                    var ptr = curr
                    while (parent contains ptr) {
                        ptr = parent(ptr)
                         trace :+= ptr
                    }
                    return (Some(curr), Some(trace.reverse))
                }
                val nextLevel = level(curr) + 1
                genAdj(curr).foreach(v => {
                    if (v.depth >= DEFAULT_MAX_DEPTH || (level.keySet contains v)) {
                        skips += 1
                    } else {
                        toCheck.enqueue((v.depth * 4 + nextLevel * 1, v))
                        parent(v) = curr
                        level(v) = nextLevel
                    }
                })
            //}
            // go to next iteration

        }
        (None, None)// not found
    }
    private val (ans, path) = explore(maxChecks = DEFAULT_EXPLORATION_LIMIT)

    private def genAdj(ex: Expr): Set[Expr] = {
        genDirectTransforms(ex) | genRecTransforms(ex)
    }

    private def genDirectTransforms(ex: Expr): Set[Expr] = {
        transforms.flatMap(_ (ex)).toSet
    }

    private def genRecTransforms(ex: Expr): Set[Expr] = {
        val recTransforms: Set[Expr] = ex match {
            case Add(e1, e2) => genRecTrHelp(classOf[Add].getConstructor(classOf[Expr], classOf[Expr]), e1, e2)
            case Mult(e1, e2) => genRecTrHelp(classOf[Mult].getConstructor(classOf[Expr], classOf[Expr]), e1, e2)
            case Sub(e1, e2) => genRecTrHelp(classOf[Sub].getConstructor(classOf[Expr], classOf[Expr]), e1, e2)
            case Div(e1, e2) => genRecTrHelp(classOf[Div].getConstructor(classOf[Expr], classOf[Expr]), e1, e2)
            case Pow(e1, e2) => genRecTrHelp(classOf[Pow].getDeclaredConstructor(classOf[Expr], classOf[Expr]), e1, e2)
            case _ => Set.empty
        }
        recTransforms | genDirectTransforms(ex)
    }

    private def genRecTrHelp(constructor: Constructor[_ <: Expr], ex1: Expr, ex2: Expr): Set[Expr] = {
        val (subTr1, subTr2) = (genRecTransforms(ex1), genRecTransforms(ex2))
        val withLeftChanged: Set[Expr] = for {tr1 <- subTr1} yield constructor.newInstance(tr1, ex2)
        val withRightChanged: Set[Expr] = for {tr2 <- subTr2} yield constructor.newInstance(ex1, tr2)
        withLeftChanged | withRightChanged
    }
}
