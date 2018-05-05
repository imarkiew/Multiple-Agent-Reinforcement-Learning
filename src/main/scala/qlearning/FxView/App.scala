package qlearning.FxView


import qlearning.sag.{SagBoard, SagPlayer}
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.beans.property.DoubleProperty.sfxDoubleProperty2jfx
import scalafx.event.ActionEvent
import scalafx.scene.canvas.Canvas
import scalafx.scene.control.Button
import scalafx.scene.paint.Stop.sfxStop2jfx
import scalafx.scene.paint.{Color, CycleMethod, LinearGradient, Stop}
import scalafx.scene.shape.{Rectangle}
import scalafx.scene.{Group, Scene}
import qlearning.FxView.Utils._

object App extends JFXApp {


  val canvas = new Canvas(CANVAS_HEIGHT, CANVAS_WIDTH)

  val rect = new Rectangle {
    height = SCENE_HEIGHT
    width = SCENE_WIDTH
    fill = new LinearGradient(0, 0, 1, 1, true, CycleMethod.Reflect, List(Stop(0, Color.Green), Stop(1, Color.Blue)))
  }

  val button = new Button("Learn it")
  button.layoutX = 20
  button layoutY = 20
  button.onAction = (event: ActionEvent) => {
    val gamePlayer = new SagPlayer()
    println("Make Widzew Great Again")
    gamePlayer.learnHowToPlay()
    var state = SagBoard()
    var newstate = state
    while (!state.isWon() && state.hasTransitions) {
      println("--------")
      println(state.toString)
      state = state.makeTransition(gamePlayer.table.getBestMove(state)._1)
      positions += state.playerPosition
    }
    howManyItersToLearn = positions.length
  }

  val buttonSim = new Button("Simulate")
  buttonSim.layoutX = 20
  buttonSim.layoutY = 50
  buttonSim.onAction = (_) => {
    print("Simulation step: " + (howManyItersToLearn - positions.length) + "\n")
    drawSomeone(Color.Blue, positions.head%4, positions.head/4)
    positions = positions.drop(1)
    if(positions.length == 0)
      System.exit(0)
    drawSomeone(Color.White, positions.head%4, positions.head/4)

  }

  val rootPane = new Group
  rootPane.children = List(rect, canvas, button, buttonSim)
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