package de.gwik.similarity

abstract class GenericQuery(dataUrl: String) {

  def queryNN(queryVector: Seq[Double], nearestNeighborCount: Int): Seq[QueryResult]

}