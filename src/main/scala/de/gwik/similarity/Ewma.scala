package de.gwik.similarity

import breeze.linalg.DenseVector
import breeze.numerics.{ exp, log }

/**
 * Exponential weighted moving average.
 * Note, that the standard input is redundant, since both values are combined to a single weighting factor (factor).
 * The constructor is therefore overloaded and you can also specify the number of parameters you want to have in the 50% interval.
 *
 * TODO: refactor to be reactive (e.g. extends Actor)
 * @param halfLifeCount  input count until the values count only half to the moving average
 */
class Ewma(halfLifeCount: Double) extends Filter {
  private var valueOption: Option[DenseVector[Double]] = None
  private val factor: Double = exp(log(0.5) / halfLifeCount)

  /**
   * Reset the filter.
   */
  def reset(): Unit = { valueOption = None }

  /**
   * Next parameters to be included in the exponential moving average.
   * @param y Vector of parameters. Note, that the first input will replace the option and this is the first time the filter knows the length of the values to be averaged.
   */
  def step(y: DenseVector[Double]): Unit = { valueOption = Some(valueOption.getOrElse(y) * factor + (y * (1.0 - factor))) }

  /**
   * Access elements of the current averages.
   * @param i Index of the average vector with respect to the ones inputted in the step method.
   * @return average of ith value
   */
  def apply(i: Int): Double = { valueOption.getOrElse(DenseVector.zeros[Double](i + 1) * Double.NaN)(i) }

  /**
   * Same as apply(i: Int) but for the first element of a sequence.
   * @param inIDX Sequence of indices of which the first index is taken to return the value.
   * @return
   */
  def apply(inIDX: Seq[Int]): Double = apply(inIDX.headOption.getOrElse(-1))

  /**
   * Vector of averages.
   * @return Vector of averages.
   */
  def values: DenseVector[Double] = valueOption.getOrElse(DenseVector[Double]())

}