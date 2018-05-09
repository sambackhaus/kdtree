package de.gwik.similarity

import breeze.linalg.{ DenseVector, sum }
import breeze.numerics.{ abs, pow, sqrt }

/**
 * Calculation of the exponentially weighted standard deviation of input variables.
 * @param inHalfLifeCount Half life count time of exponential weighting of the values.
 */
class Ewstd(inHalfLifeCount: Double) extends Filter {

  val maFilterAvg = new Ewma(inHalfLifeCount)
  val maFilterSquare = new Ewma(inHalfLifeCount)

  /**
   * Reset to initial conditions (empty vectors in MA filters)
   */
  def reset(): Unit = {
    maFilterAvg.reset()
    maFilterSquare.reset()
  }

  /**
   * Filter step
   * @param y Input vector of variables to calculate the standard deviation of. Return values will be in the same order.
   */
  def step(y: DenseVector[Double]): Unit = {
    if (!sum(y).isNaN) {
      maFilterAvg.step(y)
      maFilterSquare.step(pow(y, 2))
    }
  }

  /**
   * Caclulates the standard deviation of all values.
   * @return Vector of standard deviations of variables of the order of the input in step.
   */
  def std: DenseVector[Double] = {
    sqrt(abs(maFilterSquare.values - pow(maFilterAvg.values, 2)))
  }

  /**
   * Same as std() but for the power value. This will save speed (no need for sqrt calculation) if you are only interested in the power.
   * @return Vector of standard deviations squared of variables of the order of the input in step.
   */
  def stdP2(): DenseVector[Double] = {
    abs(maFilterSquare.values - pow(maFilterAvg.values, 2))
  }

  /**
   *
   * Calculates the standard deviation of a single value. Use if not the whole vector is needed for speed improovement.
   * @param inIDX The index refers to the element which is used in the input to the step method.
   * @return Standard deviation of selected value.
   */
  def std(inIDX: Int): Double = {
    sqrt(abs(maFilterSquare.values(inIDX) - pow(maFilterAvg.values(inIDX), 2)))
  }

  /**
   * Same as std(inIDX : Int) but for the power value. This will save speed (no need for sqrt calculation) if you are only interested in the power.
   * @param inIDX The index refers to the element which is used in the input to the step method.
   * @return Standard deviation squared of selected value.
   */
  def stdP2(inIDX: Int): Double = {
    abs(maFilterSquare.values(inIDX) - pow(maFilterAvg.values(inIDX), 2))
  }

  def stdP2(inIDX: Seq[Int]): Double = stdP2(inIDX.headOption.getOrElse(-1))

  /**
   * Calculation of the averages of the input variable as return vector.
   * @return Vector of averages. The index refers to the element which is used in the input to the step method.
   */
  def avg: DenseVector[Double] = {
    maFilterAvg.values
  }

}