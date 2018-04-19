package de.gwik.similarity

class QueryResult(queryVector: Seq[Double] = Seq(), resultVector: Seq[Double] = Seq()) {

  def distanceSq: Double = {
    val length = queryVector.length
    var i = 0
    var res = 0.0
    while (i < length) {
      res += queryVector(i) * resultVector(i)
      i += 1
    }
    res
  }
}