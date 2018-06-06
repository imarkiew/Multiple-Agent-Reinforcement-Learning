package qlearning.common

import scala.util.Random


trait State[T] {

  def hasTransitions: Boolean
  def makeTransition(transition: T): State[T]
  def isWon(): Boolean
  def getLegalTransitions: Seq[T]
  def rewardForLastMove: Float
  def selectBestAction(actionList: Seq[(T, Float)], rnd: Random): (T, Float)
}
