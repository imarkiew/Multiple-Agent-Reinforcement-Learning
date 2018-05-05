package qlearning.sag

object LegalTranstitions {

  val transitions3 =  Map(
    0 -> Seq(1,3),
    1 -> Seq(0,2,4),
    2 -> Seq(1,5),
    3 -> Seq(0,4,6),
    4 -> Seq(1,3,5,7),
    5 -> Seq(2,4,8),
    6 -> Seq(3,7),
    7 -> Seq(4,6,8),
    8 -> Seq(5,7)
  )

  val transitions4 = Map(
    0 -> Seq(1,4),
    1 -> Seq(0,2,5),
    2 -> Seq(1,3,6),
    3 -> Seq(2,7),
    4 -> Seq(0,5,8),
    5 -> Seq(1,4,6,9),
    6 -> Seq(2,5,7,10),
    7 -> Seq(3,6,11),
    8 -> Seq(4,9,12),
    9 -> Seq(5,8,10,13),
    10 -> Seq(6,9,11,14),
    11 -> Seq(7,10,15),
    12 -> Seq(8,13),
    13 -> Seq(9,12,14),
    14 -> Seq(10,13,15),
    15 -> Seq(11,14)
  )

}
