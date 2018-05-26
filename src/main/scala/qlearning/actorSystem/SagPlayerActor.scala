package qlearning.actorSystem


import akka.actor.Actor
import qlearning.actorSystem.Utils._
import qlearning.common._



class SagPlayer(initialState: Int) extends Actor {

  var initialStateString = state5.substring(0, initialState) + 'c' + state5.substring(initialState+1)

  val table = new QTable(SagBoard(initialStateString,'c',initialState), None, epsilon = 0.05)
  val learner = new QLearner[Int]()
  var playerPosition = initialState
  var state = SagBoard(initialStateString, 'c', playerPosition)


  def receive = {

    case Learn => {
      learnAndPlay()
      println("...I just learned how to play.\n")
      sender ! FinishedLearning
    }

    case MakeMove => {

      if(!state.isWon() && state.hasTransitions) {
        sender ! Moves(playerPosition, this.table.getPossibleActions(state))
      }
      else
        sender ! Moves(playerPosition, List[(Int, Float)]())
    }

    case DoThisMove(move) => {
      state = state.makeTransition(move._1)
      playerPosition = move._1
      if(!state.isWon() && state.hasTransitions) {
        sender ! Moves(playerPosition, this.table.getPossibleActions(state))
      }
      else
        sender ! Moves(playerPosition, List[(Int, Float)]())
    }


    case _ => println("What the hell did I receive?")
  }

  def learnAndPlay(): Unit = {
    print("Learning...")
    learner.learn(table, initialState, 2000)
  }

}
