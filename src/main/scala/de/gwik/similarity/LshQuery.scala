package de.gwik.similarity

import java.util.UUID

import breeze.linalg.DenseVector
import io.krom.lsh.Lsh

import scala.io.Source


class LshQuery(dataUrl: String) extends GenericQuery(dataUrl) with DataConfig {

  var lsh: Lsh = _
  var dim_val: Int = _

  override def queryNN(queryVector: Seq[Double], nearestNeighborCount: Int): Seq[QueryResult] = {
    val ret: Seq[(String, Double)] = lsh.query(new DenseVector(queryVector.toArray), maxItems = nearestNeighborCount)
    ret.map(i => new QueryResult(queryVector, None, Option(i._2), Option(i._1)))
  }

  override def dim: Double = dim_val

  override def tearUp(): Unit = {
    val src: Iterator[String] = Source.fromFile(dataUrl).getLines
    val testSequences: Seq[Seq[Double]] = src.map(l => l.split("   ").map(c => c.toDouble).toSeq).toSeq
    dim_val = testSequences.head.length

    lsh = Lsh(numBits, dim_val, numTables)
    testSequences.foreach(i => {
      try {
        val payload = new DenseVector(i.toArray)
        lsh.store(payload, UUID.randomUUID().toString)
      }
      catch {
        case e: Exception => println(i.length)
      }
    })
  }

  override def tearDown(): Unit = {
  }
}

