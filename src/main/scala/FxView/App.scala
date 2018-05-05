package scalafx.canvas

import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.beans.property.DoubleProperty.sfxDoubleProperty2jfx
import scalafx.scene.canvas.Canvas
import scalafx.scene.input.MouseEvent
import scalafx.scene.paint.Stop.sfxStop2jfx
import scalafx.scene.paint.{Color, CycleMethod, LinearGradient, Stop}
import scalafx.scene.shape.{Line, Rectangle, Ellipse}
import scalafx.scene.{Group, Scene}

/**
  * Example adapted from code showed in [[http://docs.oracle.com/javafx/2/canvas/jfxpub-canvas.htm]].
  */
object App extends JFXApp {

  val SCENE_HEIGHT = 600
  val SCENE_WIDTH = 600
  val CANVAS_HEIGHT = 300
  val CANVAS_WIDTH = 300
  val UNIT = 100
  val AGENT_RADIUS = 30

  val canvas = new Canvas(CANVAS_HEIGHT, CANVAS_WIDTH)

  // Draw background with gradient
  val rect = new Rectangle {
    height = SCENE_HEIGHT
    width = SCENE_WIDTH
    fill = new LinearGradient(0, 0, 1, 1, true, CycleMethod.Reflect, List(Stop(0, Color.Green), Stop(1, Color.Blue)))
  }

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

  drawSomeone(Color.White, UNIT/2 - AGENT_RADIUS/2, UNIT/2 - AGENT_RADIUS/2)
  //drawSomeone(Color.Yellow, 4*(UNIT/2 - AGENT_RADIUS/2), 4*(UNIT/2 - AGENT_RADIUS/2))
  drawSomeone(Color.Red, UNIT + (UNIT/2 - AGENT_RADIUS/2), UNIT + (UNIT/2 - AGENT_RADIUS/2))
  drawSomeone(Color.Yellow, 2*UNIT + (UNIT/2 - AGENT_RADIUS/2), 2*UNIT + (UNIT/2 - AGENT_RADIUS/2))



  // Clear away portions as the user drags the mouse



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
    gc.fillRoundRect(x, y, AGENT_RADIUS, AGENT_RADIUS, 5, 5)

  }




  /**
    * Resets the canvas to its original look by filling in a rectangle covering
    * its entire width and height. Color.Blue is used in this demo.
    *
    * @param color The color to fill
    */
  private def reset(color: Color) {
    gc.fill = color
    gc.fillRect(0, 0, canvas.width.get, canvas.height.get)
  }

}