package de.gwik.similarity

import java.util.UUID

import breeze.linalg.DenseVector
import io.krom.lsh.Lsh

import scala.util.Random

object LshTest extends AbsTest {

  def createRandomVector(dim : Int) : DenseVector[Double] = {
    //DenseVector.rand(dim)
    new DenseVector((for (j <- 0 until dimensions) yield Random.nextDouble()).toArray)
  }

  override def main(args: Array[String]): Unit = {
    val lsh = Lsh(numBits, dimensions, numTables)

    var start = System.currentTimeMillis()
    print(s"$start: creating $numPoints in lsh store with $dimensions dimensions...")
    (1 to numPoints).foreach(i => lsh.store(createRandomVector(dimensions), UUID.randomUUID().toString))
    println(s"done (took: ${System.currentTimeMillis()-start}ms)")

    start = System.currentTimeMillis()
    print(s"$start: creating $samples random test sequences...")
    val rndTestSeq = (1 to samples).map(i => createRandomVector(dimensions))
    println(s"done (took: ${System.currentTimeMillis()-start}ms)")
    val allStart = System.currentTimeMillis()

    val deltaTs = rndTestSeq.map(data => {
      start = System.currentTimeMillis()
      print(s"$start: looking for neighbours...")
      val items2 = lsh.query(data, maxItems = neighbours)
      val deltaT = System.currentTimeMillis()-start
      println(s"done (took: ${deltaT}ms)")
      deltaT
    })
    println(s"average: ${deltaTs.sum.toDouble/samples.toDouble}ms")


    println(s"Found $samples * $neighbours in ${System.currentTimeMillis()-allStart}ms")

    print("fin!")

  }

}
