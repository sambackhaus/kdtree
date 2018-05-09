package de.gwik.similarity

import scala.collection.mutable.HashMap
import breeze.linalg.DenseVector
import de.gwik.similarity.Ewma

import scala.collection.mutable

abstract class GenericQuery(dataUrl: String) {

  var avgQueryTimeInMS: Double = 0.0
  var totalInvocations: Int = 0
  var movingFilter: Ewstd = new Ewstd(20)

  def queryNN(queryVector: Seq[Double], nearestNeighborCount: Int): Seq[QueryResult]

  def profileQueryNN(queryVector: Seq[Double], nearestNeighborCount: Int): Seq[QueryResult] = {
    val start = System.currentTimeMillis()
    val result = this.queryNN(queryVector, nearestNeighborCount)
    val deltaT = System.currentTimeMillis() - start
    avgQueryTimeInMS = (avgQueryTimeInMS * totalInvocations + deltaT) / (totalInvocations + 1)
    movingFilter.step(DenseVector(deltaT))
    totalInvocations += 1
    result
  }

  def dim: Double

  def getProfile: mutable.HashMap[String, Double] = {
    val std: DenseVector[Double] = movingFilter.std
    val avg: DenseVector[Double] =  movingFilter.avg
    mutable.HashMap(
      ("dim", dim),
      ("totalAverage", avgQueryTimeInMS),
      ("average@20", avg(0)),
      ("std@20", std(0)),
      ("totalInvocations", totalInvocations))
  }

  def tearUp(): Unit = {
  }

  def tearDown(): Unit = {
  }

}
