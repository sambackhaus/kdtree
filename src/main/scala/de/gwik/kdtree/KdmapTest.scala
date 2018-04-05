package de.gwik.kdtree

import java.util.UUID

import com.thesamet.spatial.{DimensionalOrdering, KDTreeMap}

import scala.util.Random

object KdmapTest {

  def createRandomVector(dim : Int) : Seq[Double] = {
    for (j <- 1 to dim) yield Random.nextDouble()
  }

  def main(args: Array[String]): Unit = {
    val numPoints = 4000000
    val dimensions = 2
    val samples = 100
    val neighbours = 150

    var start = System.currentTimeMillis()
    print(s"$start: creating $numPoints test sequences with $dimensions dimensions...")
    val testSequences : IndexedSeq[(Seq[Double], String)] = for (i <- 1 to numPoints*dimensions by dimensions) yield (createRandomVector(dimensions) -> UUID.randomUUID().toString)
    println(s"done (took: ${System.currentTimeMillis()-start}ms)")

    print(s"$start: creating kdtree from test sequences...")
    val treeMap = KDTreeMap(testSequences :_*)(DimensionalOrdering.dimensionalOrderingForSeq[Seq[Double], Double](dimensions))
    println(s"done (took: ${System.currentTimeMillis()-start}ms)")

    start = System.currentTimeMillis()
    print(s"$start: creating $samples random test sequences...")
    val rndTestSeq = for (i <- numPoints*dimensions +1 to numPoints*dimensions + samples) yield createRandomVector(dimensions)
    println(s"done (took: ${System.currentTimeMillis()-start}ms)")

    start = System.currentTimeMillis()
    print(s"$start: looking for neighbours...")
    rndTestSeq.map(i => {
      var start = System.currentTimeMillis()
      val neighboursNodes = treeMap.findNearest(i, neighbours)
      println(s"$start: looking for nearest $neighbours neighbours took: ${System.currentTimeMillis()-start}ms)")

    })
    println(s"done (took: ${System.currentTimeMillis()-start}ms)")

    print("fin!")
  }
}
