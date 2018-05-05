/** Given a QTable, play lots of episodes in order to train the table so that
  * correct reward values can be assigned to the next move.
  * See https://en.wikipedia.org/wiki/Q-learning and
  * https://medium.com/emergent-future/simple-reinforcement-learning-with-tensorflow-part-0-q-learning-with-tables-and-neural-networks-d195264329d0
  * https://www.cs.rochester.edu/u/kautz/Courses/242spring2014/242ai20-reinforcement-learning.pdf
  * @author Barry Becker
  */

package qlearning.common
import qlearning.common.QLearner._


object QLearner {
  val DEFAULT_LEARNING_RATE = 0.8f
  val DEFAULT_FUTURE_REWARD_DISCOUNT = 0.8f
}

case class QLearner[T](learningRate: Float = DEFAULT_LEARNING_RATE,
                       futureRewardDiscount: Float = DEFAULT_FUTURE_REWARD_DISCOUNT) {
  def learn(qtable: QTable[T], numEpisodes: Int = 10000): Unit = {
    for (i <- 0 until numEpisodes) {
      var state = qtable.initialState
      while (state.hasTransitions) {
        val action = qtable.getNextAction(state, i)
        val nextState = state.makeTransition(action._1)
        qtable.update(state, action, nextState, learningRate, futureRewardDiscount)
        state = nextState
        //print(state.toString + '\n')
        //Thread.sleep(1000)
      }
    }
  }
}