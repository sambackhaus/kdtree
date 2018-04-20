package de.gwik.similarity

import breeze.numerics.sqrt

class QueryResult(
                   queryVector: Seq[Double] = Seq(),
                   resultVector: Option[Seq[Double]] = None,
                   distance: Option[Double] = None,
                   label: Option[String] = None
                 ) {

  def getDistance: Double = {
    distance.getOrElse(sqrt(this.getDistanceSq))
  }

  def getLabel: String = {
    label.getOrElse("None")
  }

  def getDistanceSq: Double = {
    if (resultVector.isDefined) {
      val rV = resultVector.get
      val length = queryVector.length
      var i = 0
      var res = 0.0
      while (i < length) {
        res += queryVector(i) * rV(i)
        i += 1
      }
      res
    }
    else
      {
        Double.NaN
      }

  }
}