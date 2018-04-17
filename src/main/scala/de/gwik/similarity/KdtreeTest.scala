package de.gwik.similarity

import com.thesamet.spatial.{DimensionalOrdering, KDTree}
import org.apache.spark.sql.SparkSession

import scala.util.Random

object KdtreeTest {

  def createRandomVector(start: Int, dim : Int) : Seq[Double] = {
    for (j <- start until start+dim) yield Random.nextDouble()
  }

  def main(args: Array[String]): Unit = {
    val sparkSession = SparkSession.builder.appName("KdtreeTest").getOrCreate()
    val sc = sparkSession.sparkContext

    val numPoints = 400000
    val dimensions = 70
    var start = System.currentTimeMillis()
    print(s"$start: creating $numPoints test sequences with $dimensions dimensions...")
    val testSequences : Seq[Seq[Double]] = for (i <- 1 to numPoints*dimensions by dimensions) yield createRandomVector(i, dimensions)
    println(s"done (took: ${System.currentTimeMillis()-start}ms)")

    print(s"$start: creating kdtree from test sequences...")
    val tree = KDTree.fromSeq(testSequences)(DimensionalOrdering.dimensionalOrderingForSeq[Seq[Double], Double](dimensions))
    println(s"done (took: ${System.currentTimeMillis()-start}ms)")

    start = System.currentTimeMillis()
    print(s"$start: broadcasting tree...")
    val bcTree = sc.broadcast(tree)
    println(s"done (took: ${System.currentTimeMillis()-start}ms)")

    val samples = 2000
    start = System.currentTimeMillis()
    print(s"$start: creating $samples random test sequences...")
    val rndTestSeq = for (i <- numPoints*dimensions +1 to numPoints*dimensions + samples) yield createRandomVector(i, dimensions)
    println(s"done (took: ${System.currentTimeMillis()-start}ms)")

    start = System.currentTimeMillis()
    print(s"$start: looking for neighbours...")
    val deltaTs = sc.parallelize(rndTestSeq).map(i => {

      val neighbours = 150

      var start = System.currentTimeMillis()
      val aTree = bcTree.value
      print(s"$start: getting bcTree.value took: ${System.currentTimeMillis()-start}ms)")
      
      start = System.currentTimeMillis()
      val neighboursNodes = aTree.findNearest(i, neighbours)
      val deltaT = System.currentTimeMillis()-start
      println(s"done (took: ${deltaT}ms)")
      deltaT
    }).collect()
    println(s"average: ${deltaTs.sum.toDouble/samples.toDouble}ms")

    print("fin!")
  }
}
