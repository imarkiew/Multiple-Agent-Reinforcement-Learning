package qlearning.actorSystem

import qlearning.common._
import qlearning.actorSystem.Utils._
import scala.util.Random

case class SagBoard(state: String = "c.T....T..T.......T.TT..P", playerToMove: Char = 'c', playerPosition: Int = 0) extends State[Int] {


  val transtitions = LegalTranstitions.generateTransitions(boardSize)

  def hasTransitions: Boolean = return !isWon()

  def makeTransition(position: Int): SagBoard = {
    var newState = state.substring(0, position) + playerToMove + state.substring(position + 1)
    newState =  newState.substring(0, playerPosition) + '.' + newState.substring(playerPosition+1)
    SagBoard(newState, 'c', position)
  }

  def isWon(): Boolean = return PRIZE_POSITION == playerPosition

  def getLegalTransitions: Seq[Int] = {
    if (isWon()) Seq()
    else transtitions(playerPosition)
  }

  def rewardForLastMove: Float = {
    if (isWon()) 2.0f
    else if(TRAP_POSITION.contains(playerPosition)) -1.0f
    else 0.0f
  }

  def selectBestAction(actionList: Seq[(Int, Float)], rnd: Random): (Int, Float) = {
    val bestValue = if (playerToMove == 'c') actionList.map(_._2).max
    val bestActions = actionList.filter(_._2 == bestValue)
    bestActions(rnd.nextInt(bestActions.length))
  }
}
