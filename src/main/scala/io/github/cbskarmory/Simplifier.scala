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
                genAdj(curr).foreach(v => toCheck.enqueue(v))
            }
            // go to next iteration
        }
        None // not found
    }

    private def genAdj(ex: Expr): Set[Expr] = {
        (genDirectTransforms(ex) | genRecursiveTransforms(ex)) -- seen

    }

    private def genDirectTransforms(ex: Expr): Set[Expr] = {
        transforms.flatMap(_(ex)).toSet
    }

    private def genRecursiveTransforms(ex: Expr): Set[Expr] = {
        Set.empty
    }



}
