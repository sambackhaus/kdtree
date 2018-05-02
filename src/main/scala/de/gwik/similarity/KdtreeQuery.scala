package de.gwik.similarity

import com.thesamet.spatial.{DimensionalOrdering, KDTree}

import scala.io.Source


class KdtreeQuery(dataUrl: String) extends GenericQuery(dataUrl) {

  var dim: Int = _
  var tree: KDTree[Seq[Double]] = null

  override def queryNN(queryVector: Seq[Double], nearestNeighborCount: Int): Seq[QueryResult] = {
    val neighboursNodes: Seq[Seq[Double]] = tree.findNearest(queryVector, nearestNeighborCount)
    neighboursNodes.map(res => new QueryResult(queryVector, Option(res)))
  }

  override def tearUp(): Unit = {
    val src: Iterator[String] = Source.fromFile(dataUrl).getLines
    val testSequences: Seq[Seq[Double]] = src.map(l => l.split("   ").map(c => c.toDouble  ).toSeq).toSeq
    dim = testSequences.head.length
    tree = KDTree.fromSeq(testSequences)(DimensionalOrdering.dimensionalOrderingForSeq[Seq[Double], Double](dim))
  }

  override def tearDown(): Unit = {
  }
}
