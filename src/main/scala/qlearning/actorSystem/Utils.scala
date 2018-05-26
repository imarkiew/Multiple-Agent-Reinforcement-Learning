package qlearning.actorSystem

import scala.collection.mutable.ListBuffer

object Utils {

  val SCENE_HEIGHT = 600
  val SCENE_WIDTH = 600
  val CANVAS_HEIGHT = 500
  val CANVAS_WIDTH = 500
  val UNIT = 100
  val AGENT_RADIUS = 40
  var howManyItersToLearn = 0
  val state5 = "..T....T..T.......T.TT..P"
  val PRIZE_POSITION_5 = 24
  val PRIZE_POSITION = 24
  val TRAP_POSITION = Seq(2,7,10,18,20,21,22)
  val AGENT_POSITION = Seq(0,4,12)

  case class Learn()
  case class Position(pos: Int, who: Int)
  case class MakeMove()
  case class Moves(oldPos: Int, moves: List[(Int, Float)])
  case class DoThisMove(move: (Int,Float))
  case class FinishedLearning()
  case class CircleType(color: String)
}
