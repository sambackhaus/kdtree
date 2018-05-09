package de.gwik.similarity

import breeze.numerics.sqrt

import scala.collection.mutable
import scala.io.Source

class ReferenceQuery(dataUrl: String) extends GenericQuery(dataUrl) {

  var testSequences: Seq[Seq[Double]] = _
  var dim_val: Int = _

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

  override def dim: Double = dim_val

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
    res.map(i => new QueryResult(
      i.asInstanceOf[Scored].userVector,
      Option(i.asInstanceOf[Scored].articleVector),
      Option(sqrt(i.asInstanceOf[Scored].score)),
      None)
    )
  }

  override def tearUp(): Unit = {
    val src: Iterator[String] = Source.fromFile(dataUrl).getLines
    testSequences = src.map(l => l.split("   ").map(c => c.toDouble).toSeq).toSeq
    dim_val = testSequences.head.length
  }

  override def tearDown(): Unit = {
  }

}
