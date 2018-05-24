import akka.actor.{Actor, ActorSystem, Props}
import qlearning.actorSystem.SagPlayer

import scalafx.application.{JFXApp, Platform}
import scalafx.application.JFXApp.PrimaryStage
import scalafx.beans.property.DoubleProperty.sfxDoubleProperty2jfx
import scalafx.scene.canvas.Canvas
import scalafx.scene.control.Button
import scalafx.scene.paint.Stop.sfxStop2jfx
import scalafx.scene.paint.{Color, CycleMethod, LinearGradient, Stop}
import scalafx.scene.shape.Rectangle
import scalafx.scene.{Group, Scene}
import qlearning.actorSystem.Utils._

import scala.collection.mutable

object App extends JFXApp {


  val canvas = new Canvas(CANVAS_HEIGHT, CANVAS_WIDTH)

  val rect = new Rectangle {
    height = SCENE_HEIGHT
    width = SCENE_WIDTH
    fill = new LinearGradient(0, 0, 1, 1, true, CycleMethod.Reflect, List(Stop(0, Color.Green), Stop(1, Color.Blue)))
  }


  implicit val system = ActorSystem("sample-system")

  // Create Main actor
  val player = system.actorOf(Props(new SagPlayer))
  var path = mutable.ListBuffer[Int]()


  class UIActor extends Actor {
    // Request ticker to send TickCount
    player ! Learn
    /**
      * UIActor message processing
      * @return
      */
    def receive = {
      /**
        * The important part here is the 'Platform.runLater' which ensures
        * we are updating the UI from the UI Thread !
        */
      case Path(calculatedPath) => {
        path = calculatedPath
        println("Kurwa mac to jest hit")
        howManyItersToLearn = path.length
      }

      case Resp(msg) => {
        println(msg)
        player ! Play
      }

      case _ => println("I received ")
    }
  }

  val uiactor = system.actorOf(Props(new UIActor))


  val buttonSim = new Button("Simulate")
  buttonSim.layoutX = 20
  buttonSim.layoutY = 50
  buttonSim.onAction = (_) => {
    print("Simulation step: " + (howManyItersToLearn - path.length) + "\n")
    drawSomeone(Color.Blue, path.head%4, path.head/4)
    path = path.drop(1)
    if(path.length == 0)
      System.exit(0)
    drawSomeone(Color.White, path.head%4, path.head/4)

  }

  val rootPane = new Group
  rootPane.children = List(rect, canvas, buttonSim)
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
  drawSomeone(Color.Red, 2,0)
  drawSomeone(Color.Red, 1,1)
  drawSomeone(Color.Red, 0,3)
  drawSomeone(Color.Yellow, 3,3)

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

  private def reset(color: Color) {
    gc.fill = color
    gc.fillRect(0, 0, canvas.width.get, canvas.height.get)
  }

}