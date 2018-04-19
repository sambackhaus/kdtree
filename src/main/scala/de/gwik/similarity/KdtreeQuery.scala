package de.gwik.similarity

import com.thesamet.spatial.{DimensionalOrdering, KDTree}



class KdtreeQuery(dataUrl: String) extends GenericQuery(dataUrl) {

  val testSequences =
  val tree: KDTree[Seq[Double]] = KDTree.fromSeq(testSequences)(DimensionalOrdering.dimensionalOrderingForSeq[Seq[Double], Double](dimensions))

  def query(queryVector: Seq[Double], nearestNeighborCount: Int): Seq[QueryResult] = {

    val neighboursNodes: Seq[Seq[Double]] = tree.findNearest(queryVector, nearestNeighborCount)
    neighboursNodes.map(res => new QueryResult(queryVector, res))
  }
}
