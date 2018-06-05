package qlearning.actorSystem

import scala.collection.mutable.ListBuffer

object Utils {

  val SCENE_HEIGHT = 600
  val SCENE_WIDTH = 600
  val CANVAS_HEIGHT = 500
  val CANVAS_WIDTH = 500
  val AGENT_RADIUS = 40
  var howManyItersToLearn = 0
  val boardSize = 6
  val PRIZE_POSITION = 35
  val TRAP_POSITION = Seq(2, 5, 6, 15, 30)
  val AGENT_POSITION = Seq(0, 4, 17)
  val UNIT = CANVAS_HEIGHT / boardSize

  case class Learn()
  case class Position(pos: Int, who: Int)
  case class MakeMove()
  case class Moves(oldPos: Int, moves: List[(Int, Float)])
  case class DoThisMove(move: (Int,Float))
  case class FinishedLearning()
  case class CircleType(color: String)
}
