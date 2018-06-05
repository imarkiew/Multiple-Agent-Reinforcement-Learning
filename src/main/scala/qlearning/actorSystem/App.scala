import java.security.KeyStore.TrustedCertificateEntry

import akka.actor.{Actor, ActorSystem, Props}
import akka.routing._
import qlearning.actorSystem.SagPlayerActor
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

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Random

object App extends JFXApp {


  val canvas = new Canvas(CANVAS_HEIGHT, CANVAS_WIDTH)

  val rect = new Rectangle {
    height = SCENE_HEIGHT
    width = SCENE_WIDTH
    fill = new LinearGradient(0, 0, 1, 1, true, CycleMethod.Reflect, List(Stop(0, Color.Green), Stop(1, Color.Blue)))
  }

  class UIActor extends Actor {
    // Request ticker to send TickCount
    var tempCounter = 0

    val router = {
      val routees = scala.collection.immutable.Vector.empty
          AGENT_POSITION.foreach(e => {
            val agent = context.actorOf(Props(new SagPlayerActor(e)))
            context.watch(agent)
            ActorRefRoutee(agent)
            agent ! Learn
            routees :+ agent
      })
    }

    val AGENT_POSITION_COPY = collection.mutable.ListBuffer() ++= AGENT_POSITION

    def receive = {

      case FinishedLearning => {
        sender ! MakeMove
      }

      case Moves(oldPos, moves) => {
        if(oldPos == PRIZE_POSITION)
          context.stop(sender())
        val bestActions = moves.filter(_._2 == moves.map(_._2).max).filter(e => {
          val i = AGENT_POSITION_COPY.count(pos => pos != e._1)
          (e._1 == PRIZE_POSITION) || (if (i == AGENT_POSITION_COPY.length) true else false)
        })

        if(bestActions.length != 0) {
          println("Simulation step")
          val moveToDo = bestActions(Random.nextInt(bestActions.length))
          AGENT_POSITION_COPY(AGENT_POSITION_COPY.indexOf(oldPos)) = moveToDo._1
          Platform.runLater(
            drawTwoCircles(oldPos%boardSize, oldPos/boardSize, moveToDo._1 % boardSize, moveToDo._1/boardSize)
          )
          context.system.scheduler.scheduleOnce(2.second, sender, DoThisMove(moveToDo))
          //sender ! DoThisMove(moveToDo)
        }
        else {
          println("\n\n\n" + "Error no possibe move!!!" + "\n\n\n")
          context.system.scheduler.scheduleOnce(2.second, sender, MakeMove)
        }
      }

      case _ => println("I received something that I cannot interpret")
    }
  }

  implicit val system = ActorSystem("sample-system")

  val uiactor = system.actorOf(Props(new UIActor))

  val rootPane = new Group
  rootPane.children = List(rect, canvas)
  stage = new PrimaryStage {
    title = "Systemy Agentowe - Symulacja"
    scene = new Scene(SCENE_HEIGHT, SCENE_WIDTH) {
      root = rootPane

    }
  }
  stage.setOnCloseRequest(e => {
    system.terminate()
    Platform.exit()
  })



  canvas.translateX = (SCENE_WIDTH - CANVAS_WIDTH) / 2
  canvas.translateY = (SCENE_HEIGHT - CANVAS_HEIGHT) / 2
  val gc = canvas.graphicsContext2D
  reset(Color.Blue)

  drawLines(Color.Black)
  drawElements(CircleType("Agent"))
  drawElements(CircleType("Trap"))
  drawElements(CircleType("Prize"))

  //drawSomeone(Color.Yellow, 4,4)

  /** Update the shape using current `start` and `end` points. */
  def drawElements(circleType: CircleType) = circleType match{
    case CircleType("Trap") => TRAP_POSITION.foreach(elem => {
      drawSomeone(Color.Red, elem%boardSize, elem/boardSize)
    })
    case CircleType("Agent") => AGENT_POSITION.foreach(elem => {
      drawSomeone(Color.White, elem%boardSize, elem/boardSize)
    })
    case CircleType("Prize") => drawSomeone(Color.Yellow, PRIZE_POSITION%boardSize, PRIZE_POSITION/boardSize)
    case _ => println("I dont recognize this type of circle")
  }

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
    if(TRAP_POSITION.contains(i1%boardSize + i2*boardSize))
      gc.fill = Color.Red
    else
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