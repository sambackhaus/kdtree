package de.gwik.similarity

import scala.collection.mutable
import scala.io.Source

class ReferenceQuery(dataUrl: String) extends GenericQuery(dataUrl) {

  val src: Iterator[String] = Source.fromFile(dataUrl).getLines
  val testSequences: Seq[Seq[Double]] = src.map(l => l.split("   ").map(c => c.toDouble).toSeq).toSeq
  val dim: Int = testSequences.head.length

  trait Scorable {
    def id: String

    def veryShortPrefix: String
  }

  case class Scored(userVector: Seq[Double], articleVector: Seq[Double], score: Double)

  def dotProduct(a: Array[Double], b: Array[Double]): Double = {
    val length = a.length
    var i = 0
    var dot = 0.0
    while (i < length) {
      dot += a(i) * b(i)
      i += 1
    }
    dot
  }

  def toScore(articleVector: Array[Double], userVector: Array[Double]): Scored = {
    val score = dotProduct(userVector, articleVector)
    Scored(userVector, articleVector, score)
  }

  override def queryNN(queryVector: Seq[Double], nearestNeighborCount: Int): Seq[QueryResult] = {

    val topScores: mutable.PriorityQueue[Scored] = collection.mutable.PriorityQueue()(Ordering.by[Scored, Double](-_.score))
    var highestDropout = Double.NaN
    testSequences.foreach { prod =>
      val scoredArticle = toScore(prod.toArray, queryVector.toArray)
      if (highestDropout.isNaN || scoredArticle.score > highestDropout) {
        topScores.enqueue(scoredArticle)
        if (topScores.length > nearestNeighborCount) highestDropout = topScores.dequeue().score
      }
    }
    val res: Seq[Any] = topScores.dequeueAll.reverse
    res.map(i => new QueryResult(i.asInstanceOf[Scored].userVector, i.asInstanceOf[Scored].articleVector))
  }

}
