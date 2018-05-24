package qlearning.actorSystem

import scala.collection.mutable.ListBuffer

object Utils {

  val SCENE_HEIGHT = 600
  val SCENE_WIDTH = 600
  val CANVAS_HEIGHT = 400
  val CANVAS_WIDTH = 400
  val UNIT = 100
  val AGENT_RADIUS = 40
  var howManyItersToLearn = 0

  case class Learn()
  case class Play()
  case class Path(positions: ListBuffer[Int])
  case class Resp(msg: String)
}
