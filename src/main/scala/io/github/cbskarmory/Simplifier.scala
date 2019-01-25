package io.github.cbskarmory

import java.lang.reflect.Constructor

import scala.collection.mutable

class Simplifier(core: Expr,
                 transforms: Vector[Expr => Option[Expr]] = Rules.transforms,
                 targets: scala.collection.immutable.Set[Expr] = Rules.targets) {

    val seen = new mutable.HashSet[Expr]()
    val toCheck = new mutable.Queue[Expr]()
    val this.transforms = transforms

    def explore(): Option[Expr] = {
        toCheck.enqueue(core)
        while (toCheck.nonEmpty) {
            val curr = toCheck.dequeue()
            if (!seen.contains(curr)) {
                seen.add(curr)
                if (targets.contains(curr)) {
                    return Some(curr)
                }
                genAdj(curr).foreach(v => toCheck.enqueue(v))
            }
            // go to next iteration
        }
        None // not found
    }

    private def genAdj(ex: Expr): Set[Expr] = {
        (genDirectTransforms(ex) | genRecTransforms(ex)) -- seen

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
