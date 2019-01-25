package io.github.cbskarmory

import java.lang.reflect.Constructor

import scala.collection.mutable

class Simplifier(core: Expr,
                 transforms: Vector[Expr=> Option[Expr]] = Rules.transforms,
                 targets: scala.collection.immutable.Set[Expr] = Rules.targets) {

    val seen = new mutable.HashSet[Expr]()
    val toCheck = new mutable.Queue[Expr]()
    val this.transforms =  transforms

    def explore(): Option[Expr] = {
        toCheck.enqueue(core)
        while (toCheck.nonEmpty) {
            val curr = toCheck.dequeue()
            if (!seen.contains(curr)) {
                seen.add(curr)
                if (targets.contains(curr)) {
                    return Some(curr)
                }
                getAdj(curr).foreach(v => toCheck.enqueue(v))
            }
            // go to next iteration
        }
        None // not found
    }

    private def getTr(ex: Expr): Set[Expr] = ex match {
        case Add(e1, e2) => trAndCross(classOf[Add].getConstructor(classOf[Expr], classOf[Expr]), e1, e2)
        case Mult(e1, e2) => trAndCross(classOf[Mult].getConstructor(classOf[Expr], classOf[Expr]), e1, e2)
        case Sub(e1, e2) => trAndCross(classOf[Sub].getConstructor(classOf[Expr], classOf[Expr]), e1, e2)
        case Div(e1, e2) => trAndCross(classOf[Div].getConstructor(classOf[Expr], classOf[Expr]), e1, e2)
        case Pow(e1, e2) => trAndCross(classOf[Pow].getDeclaredConstructor(classOf[Expr], classOf[Expr]), e1, e2)

        case ex : Expr =>
            transforms.flatMap(_(ex)).toSet + ex
    }

    private def trAndCross(constructor: Constructor[_ <: Expr], e1: Expr, e2: Expr): Set[Expr] = {
        val e1Tr = getTr(e1)
        val e2Tr = getTr(e2)
        (for {x <- e1Tr; y <- e2Tr} yield constructor.newInstance(x, y)).map(
            ex => transforms.flatMap(_(ex)).toSet + ex
        ).reduce((a,b) => a | b)
    }

    private def getAdj(ex: Expr): Set[Expr] = {
        val trEx: Set[Expr] = ex match {
            case Add(e1, e2) => trAndCross(classOf[Add].getConstructor(classOf[Expr], classOf[Expr]), e1, e2)
            case Mult(e1, e2) => trAndCross(classOf[Mult].getConstructor(classOf[Expr], classOf[Expr]), e1, e2)
            case Sub(e1, e2) => trAndCross(classOf[Sub].getConstructor(classOf[Expr], classOf[Expr]), e1, e2)
            case Div(e1, e2) => trAndCross(classOf[Div].getConstructor(classOf[Expr], classOf[Expr]), e1, e2)
            case Pow(e1, e2) => trAndCross(classOf[Pow].getDeclaredConstructor(classOf[Expr], classOf[Expr]), e1, e2)
            case _ => Set.empty
        }
        val exVariants: Set[Expr] = trEx + ex
        val ans = exVariants.map(e => { // TODO debug this
            transforms.map(
                _ (ex) match {
                    case None => None
                    case Some(v) => if (seen.contains(v)) None else Some(v)
                }
            ).filter(_.isDefined)
        })
        ans.reduce((a, b) => a | b)

    }

}
