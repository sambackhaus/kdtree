package de.gwik.similarity

import java.util.UUID

import com.thesamet.spatial.{DimensionalOrdering, KDTreeMap}
import de.gwik.similarity.LshTest.samples

import scala.util.Random

object KdmapTest extends AbsTest {

  def createRandomVector(dim : Int) : Seq[Double] = {
    for (j <- 1 to dim) yield Random.nextDouble()
  }

  override def main(args: Array[String]): Unit = {
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

    val allStart = System.currentTimeMillis()

    val deltaTs = rndTestSeq.map(i => {
      start = System.currentTimeMillis()
      print(s"$start: looking for neighbours...")
      val neighboursNodes = treeMap.findNearest(i, neighbours)
      val deltaT = System.currentTimeMillis()-start
      println(s"done (took: ${deltaT}ms)")
      deltaT
    })
    println(s"average: ${deltaTs.sum.toDouble/samples.toDouble}ms")

    print("fin!")
  }
}
