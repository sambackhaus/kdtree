package de.gwik.similarity

import com.thesamet.spatial.{DimensionalOrdering, KDTree}

import scala.io.Source


class KdtreeQuery(dataUrl: String) extends GenericQuery(dataUrl, "KDTree") {

  val src: Iterator[String] = Source.fromFile(dataUrl).getLines
  val testSequences: Seq[Seq[Double]] = src.map(l => l.split("   ").map(c => c.toDouble  ).toSeq).toSeq
  val dim: Int = testSequences.head.length
  val tree: KDTree[Seq[Double]] = KDTree.fromSeq(testSequences)(DimensionalOrdering.dimensionalOrderingForSeq[Seq[Double], Double](dim))

  override def queryNN(queryVector: Seq[Double], nearestNeighborCount: Int): Seq[QueryResult] = {
    val neighboursNodes: Seq[Seq[Double]] = tree.findNearest(queryVector, nearestNeighborCount)
    neighboursNodes.map(res => new QueryResult(queryVector, res))
  }
}
