package de.gwik.kdtree

import com.thesamet.spatial.{DimensionalOrdering, KDTree}

import scala.util.Random

object KdtreeTest extends App {

  def createRandomVector(start: Int, dim : Int) : Seq[Double] = {
    for (j <- start to start+dim-1) yield Random.nextDouble()
  }

  val numPoints = 10000000
  val dimensions = 100

  var start = System.currentTimeMillis()
  print(s"${start}: creating $numPoints test sequences with $dimensions dimensions...")
  val testSequences : Seq[Seq[Double]] = for (i <- 1 to numPoints*dimensions by dimensions) yield createRandomVector(i, dimensions)
  println(s"done (took: ${System.currentTimeMillis()-start}ms)")

  start = System.currentTimeMillis()
  print(s"${System.currentTimeMillis()}: creating kdtree from test points...")
  val tree = KDTree.fromSeq(testSequences)(DimensionalOrdering.dimensionalOrderingForSeq[Seq[Double], Double](dimensions))
  println(s"done (took: ${System.currentTimeMillis()-start}ms)")

  val neighbours = 150
  val rndTestSeq = createRandomVector(1, dimensions)

  start = System.currentTimeMillis()
  print(s"${System.currentTimeMillis()}: looking for nearest $neighbours neighbours of a random test vector...")
  val neighbourSeqs = tree.findNearest(rndTestSeq, neighbours)
  println(s"done (took: ${System.currentTimeMillis()-start}ms)")

  println(s"tree size: ${tree.size}")
  print("fin!")

}
