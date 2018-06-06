import qlearning.actorSystem.LegalTranstitions
import org.scalatest._

object tests extends FlatSpec {

  val transtions_generated = LegalTranstitions.generateTransitions(3)
  val transitions = Map(
    0 -> Seq(1, 3),
    1 -> Seq(0, 2, 4),
    2 -> Seq(1, 5),
    3 -> Seq(0, 4, 6),
    4 -> Seq(3, 1, 5, 7),
    5 -> Seq(2, 4, 8),
    6 -> Seq(3, 7),
    7 -> Seq(6, 4, 8),
    8 -> Seq(5, 7)
  )

  assert(transtions_generated.forall{case (k, v) => transitions.getOrElse(k, Seq.empty).toSet == v.toSet} === true,
  "The generated transitions are wrong !")
}