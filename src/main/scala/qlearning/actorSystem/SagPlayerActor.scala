package qlearning.actorSystem

import java.util.Scanner

import akka.actor.Actor
import qlearning.actorSystem.Utils._
import qlearning.common._
import qlearning.sag.{SagBoard, SagPlayer}

import scala.collection.mutable.ListBuffer


class SagPlayer extends Actor {

  import context._

  val table = new QTable(SagBoard(), None, epsilon = 0.05)
  private val learner = new QLearner[Int]()
  private val scanner = new Scanner(System.in)


  def receive = {
    case Learn => {
      learnAndPlay()
      println("...I just learned how to play.\n")
      sender ! Resp("Thanks")
    }

    case Play => {
      println("I am given Play Message")
      var state = SagBoard()
      var positions = ListBuffer[Int](0)
      while (!state.isWon() && state.hasTransitions) {
        println("--------")
        println(state.toString)
        state = state.makeTransition(this.table.getBestMove(state)._1)
        positions += state.playerPosition
      }
      sender ! Path(positions)
    }
    case _ => println("What the hell did I receive?")
  }

  def learnAndPlay(): Unit = {
    learnHowToPlay()
  }

  def learnHowToPlay(): Unit = {
    print("Learning...")
    learner.learn(table, 200)
  }

  def playOneRound(state: SagBoard): SagBoard = {
    var newstate = state
    var pos = scanner.next()
    if (!newstate.isWon() && newstate.hasTransitions) {
      println("--------")
      println(newstate.toString)
      newstate = newstate.makeTransition(table.getBestMove(newstate)._1)
      Thread.sleep(1000)
    }
    newstate
  }

  private def playTheGame(): SagBoard = {
    var state: SagBoard = SagBoard()
    var pos = scanner.next()
    while (!state.isWon() && state.hasTransitions) {
      println("--------")
      println(state.toString)
      state = state.makeTransition(table.getBestMove(state)._1)
      Thread.sleep(1000)
      }
    state
  }

}
