package de.gwik.similarity

abstract class GenericQuery(dataUrl: String) {

  var avgQueryTimeInMS: Double = 0.0
  var totalInvocations: Int = 0

  def queryNN(queryVector: Seq[Double], nearestNeighborCount: Int): Seq[QueryResult]

  def profileQueryNN(queryVector: Seq[Double], nearestNeighborCount: Int): Seq[QueryResult] = {
    val start = System.currentTimeMillis()
    //print(s"$start: looking for neighbours...")

    val result = this.queryNN(queryVector, nearestNeighborCount)

    val deltaT = System.currentTimeMillis() - start
    //println(s"done (took: ${deltaT}ms)")
    avgQueryTimeInMS = (avgQueryTimeInMS * totalInvocations + deltaT) / (totalInvocations + 1)
    totalInvocations += 1
    result
  }

  def getCurrentProfile():String = {
    val name = this.getClass.toString
    s"$name, avg query time: $avgQueryTimeInMS ms, invocations: $totalInvocations"
  }

}
