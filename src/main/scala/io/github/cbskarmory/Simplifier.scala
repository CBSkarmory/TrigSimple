package io.github.cbskarmory

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

    private def getAdj(ex: Expr): Vector[Expr] = {
        transforms.flatMap(
            _(ex) match {
                case None => None
                case Some(v) => if (seen.contains(v)) None else Some(v)
            }
        )
    }
}
