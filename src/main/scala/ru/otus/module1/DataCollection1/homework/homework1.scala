package ru.otus.module1.DataCollection1

import scala.util.Random

// Вариант решения:
// class BallsExperiment {
//   val collection: List[Int] = List[Int](1, 1, 1, 0, 0, 0)
//   val iterator: Iterator[Int] = Random.shuffle(collection).iterator

//   def isAnyWhiteInTwoPicks(): Boolean = {
//     iterator.next() == 1 || iterator.next() == 1
//   }
// }

class BallsExperiment {
  val collection: List[Int] = List[Int](1, 1, 1, 0, 0, 0)

  def getRandomIndex(): Int =
    Random.nextInt(collection.size)

  def isAnyWhiteInTwoPicks(): Boolean = {
    val firstIndx = getRandomIndex()
    var secondIndx = getRandomIndex()

    while (firstIndx == secondIndx)
      secondIndx = getRandomIndex()

    collection(firstIndx) == 1 || collection(secondIndx) == 1
  }
}

object BallsTest {
  def main(args: Array[String]): Unit = {
    val countOfExperiments = 10000

    val listOfExperiments: List[BallsExperiment] =
      List.fill(countOfExperiments)(BallsExperiment())

    val countOfPositiveExperiments: Float =
      listOfExperiments.map(_.isAnyWhiteInTwoPicks()).count(_ == true)


    // Если просят хотя бы один белый после двух попыток достать:
    // Б+Б: (3/6) * (2/5)  \
    // Ч+Б: (3/6) * (3/5)  | = 0.2 + 0.3 + 0.3 = 0.8 \
    // Б+Ч: (3/6) * (3/5)  /                         | 0.8 / (0.2 + 0.8) = 0.8
    // Ч+Ч: (3/6) * (2/5)                      = 0.2 /
    //
    // Ответ должен быть 0.8

    println(
      "Теория: 0.8, расчёты " + countOfPositiveExperiments / countOfExperiments
    )

  }
}
