package de.gwik.similarity

import breeze.linalg.DenseVector

/**
 * Common trait for all filters
 */
trait Filter {

  /**
   * Reset the filter to initial conditions
   */
  def reset()

  /**
   * Enhance the filter by one step with time separation dx
   * @param aVal a DenseVector to enhance the filter with. Any output vector's index will reference the index of this vector
   */
  def step(aVal: DenseVector[Double])

  /**
   * Get the implementing Filter instance as a certain type
   *
   * @tparam V type of the Filter
   * @return a filter casted to the given type
   */
  def getInstance[V]: V = {
    this.asInstanceOf[V]
  }

}

/**
 * Static Helper for all Filters
 */
object Filter {

  sealed trait Type

  case object EWMA extends Type {
    override def toString: String = "EWMA"
  }

  case object EWSTD extends Type {
    override def toString: String = "EWSTD"
  }

  /**
   * Factory method to create filter instances
   *
   * @param inFilterType type of the filter to create
   * @param inHalfLifeCount half life count of the filter
   * @return the filter instance
   */
  def newInstanceOf(inFilterType: Filter.Type, inHalfLifeCount: Double): Filter = {
    inFilterType match {
      case EWMA =>
        new Ewma(inHalfLifeCount)
      case EWSTD =>
        new Ewstd(inHalfLifeCount)
      case _ =>
        throw new NotImplementedError(s"Unknown Filter type: ${inFilterType.toString}")
    }
  }

}

