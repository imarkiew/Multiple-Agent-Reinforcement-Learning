package qlearning.sag

import java.util.Scanner
import qlearning.common._

class SagPlayer {

  val table = new QTable(SagBoard(), None, epsilon = 0.05)
  private val learner = new QLearner[Int]()
  private val scanner = new Scanner(System.in)

  def learnAndPlay(): Unit = {
    learnHowToPlay()
    playTheGame()
  }

  def learnHowToPlay(): Unit = {
    print("Learning...")
    learner.learn(table, 200000)
    println("...I just learned how to play.\n")
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
