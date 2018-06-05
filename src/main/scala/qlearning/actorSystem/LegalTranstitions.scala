package qlearning.actorSystem

object LegalTranstitions {

  /*
  generation of a matrix of possible states and transitions
   */
  def generateTransitions(n: Int): Map[Int, Seq[Int]] = {
    val transitions = scala.collection.mutable.Map[Int, Seq[Int]]()
    for (i <- 0 until n*n) {
      if (i > 0 && i < n - 1) transitions(i) = Seq(i - 1, i + 1, i + n)
      else if ((i > n - 1 ) && (i < n*n - 1) && (i % n == n - 1)) transitions(i) = Seq(i - n, i - 1, i + n)
      else if ((i > n*(n - 1)) && (i < n*n - 1)) transitions(i) = Seq(i - 1, i - n, i + 1)
      else if ((i > 0) && (i < n*(n - 1)) && (i % n == 0)) transitions(i) = Seq(i - n, i + 1, i + n)
      else if (i == 0) transitions(i) = Seq(1, n)
      else if (i == n - 1) transitions(i) = Seq(i - 1, 2*i + 1)
      else if (i == n*n - 1) transitions(i) = Seq(i - n, i - 1)
      else if (i == n*(n - 1)) transitions(i) = Seq(i - n, i + 1)
      else transitions(i) = Seq(i - 1, i - n, i + 1, i + n)
    }
    collection.immutable.Map[Int, Seq[Int]]() ++ transitions
  }
}
