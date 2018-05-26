import akka.actor.{Actor, ActorSystem, Props}
import qlearning.actorSystem.{SagBoard, SagPlayer}

import scalafx.application.{JFXApp, Platform}
import scalafx.application.JFXApp.PrimaryStage
import scalafx.beans.property.DoubleProperty.sfxDoubleProperty2jfx
import scalafx.scene.canvas.Canvas
import scala.concurrent.duration._
import scalafx.scene.paint.Stop.sfxStop2jfx
import scalafx.scene.paint.{Color, CycleMethod, LinearGradient, Stop}
import scalafx.scene.shape.Rectangle
import scalafx.scene.{Group, Scene}
import qlearning.actorSystem.Utils._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.collection.mutable
import scala.util.Random

object App extends JFXApp {


  val canvas = new Canvas(CANVAS_HEIGHT, CANVAS_WIDTH)

  val rect = new Rectangle {
    height = SCENE_HEIGHT
    width = SCENE_WIDTH
    fill = new LinearGradient(0, 0, 1, 1, true, CycleMethod.Reflect, List(Stop(0, Color.Green), Stop(1, Color.Blue)))
  }


  implicit val system = ActorSystem("sample-system")

  // Create Main actor
  val playerFirst = system.actorOf(Props(new SagPlayer(0)))
  val playerSecond = system.actorOf(Props(new SagPlayer(4)))
  var path = mutable.ListBuffer[Int]()


  class UIActor extends Actor {
    // Request ticker to send TickCount
    var tempCounter = 0
    var playerFirstPos = 0
    var playerSecPos = 4

    playerFirst ! Learn
    playerSecond ! Learn


    def receive = {

      case FinishedLearning => {
        sender ! MakeMove
      }

      case Moves(oldPos, moves) => {
        val bestActions = moves.filter(_._2 == moves.map(_._2).max).filter(e => (e ._1 == 24 || (e._1 != playerSecPos && e._1 != playerFirstPos)))
        if(bestActions.length != 0) {
          println("Simulation step")
          val moveToDo = bestActions(Random.nextInt(bestActions.length))
          if(playerFirstPos == oldPos)
            playerFirstPos = moveToDo._1
          else
            playerSecPos = moveToDo._1
          Platform.runLater(
            drawTwoCircles(oldPos%5, oldPos/5, moveToDo._1 % 5, moveToDo._1/5)
          )
          system.scheduler.scheduleOnce(1.second, sender, DoThisMove(moveToDo))
          //sender ! DoThisMove(moveToDo)
        }
      }

      case _ => println("I received something that I cannot interpret")
    }
  }

  val uiactor = system.actorOf(Props(new UIActor))





  val rootPane = new Group
  rootPane.children = List(rect, canvas)
  stage = new PrimaryStage {
    title = "Systemy Agentowe - Symulacja"
    scene = new Scene(SCENE_HEIGHT, SCENE_WIDTH) {
      root = rootPane

    }
  }

  canvas.translateX = (SCENE_WIDTH - CANVAS_WIDTH) / 2
  canvas.translateY = (SCENE_HEIGHT - CANVAS_HEIGHT) / 2
  val gc = canvas.graphicsContext2D
  reset(Color.Blue)

  drawLines(Color.Black)
  drawSomeone(Color.White, 0,0)
  drawSomeone(Color.White,4,0)
  drawSomeone(Color.Red, 2,0)
  drawSomeone(Color.Red, 2,1)
  drawSomeone(Color.Red, 0,2)
  drawSomeone(Color.Red, 2,2)
  drawSomeone(Color.Red, 3,3)
  drawSomeone(Color.Red, 0,4)
  drawSomeone(Color.Red, 1,4)
  drawSomeone(Color.Yellow, 4,4)

  /** Update the shape using current `start` and `end` points. */
  def drawLines(color: Color) {
    for(i <- 0 to CANVAS_HEIGHT / UNIT) {
      gc.fill = color
      gc.setLineWidth(5)
      gc.strokeLine(i * UNIT, 0, i * UNIT, canvas.height.get)
      gc.fill = color
      gc.setLineWidth(5)
      gc.strokeLine(0, i * UNIT, canvas.width.get, i * UNIT)
    }
  }

  def drawSomeone(color: Color, x: Int, y: Int): Unit = {
    gc.fill = color
    gc.fillRoundRect(x*UNIT + (UNIT/2 - AGENT_RADIUS/2), y*UNIT + (UNIT/2 - AGENT_RADIUS/2), AGENT_RADIUS, AGENT_RADIUS, 5, 5)
  }

  def drawTwoCircles(i1: Int, i2: Int, i3: Int, i4: Int): Unit = {
    gc.fill = Color.Blue
    gc.fillRoundRect(i1*UNIT + (UNIT/2 - AGENT_RADIUS/2), i2*UNIT + (UNIT/2 - AGENT_RADIUS/2), AGENT_RADIUS, AGENT_RADIUS, 5, 5)
    gc.fill = Color.White
    gc.fillRoundRect(i3*UNIT + (UNIT/2 - AGENT_RADIUS/2), i4*UNIT + (UNIT/2 - AGENT_RADIUS/2), AGENT_RADIUS, AGENT_RADIUS, 5, 5)

  }

  private def reset(color: Color) {
    gc.fill = color
    gc.fillRect(0, 0, canvas.width.get, canvas.height.get)
  }

}