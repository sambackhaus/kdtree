package de.gwik.similarity

import edu.cmu.lti.oaqa.similarity.{QueryService, ReplyEntry}
import org.apache.thrift.TException
import org.apache.thrift.protocol.TBinaryProtocol
import org.apache.thrift.transport.TSocket

import scala.util.Random

object NmslibTest extends AbsTest {

  class NmslibClient {

    def query(queryObj : Seq[Double], k : Int) = {
      try {
        val transport = new TSocket("localhost", 10000)
        transport.open()

        val protocol = new  TBinaryProtocol(transport)
        val client = new QueryService.Client(protocol)

        val t1 = System.nanoTime()

        println(s"Running a $k-NN search")

        import scala.collection.JavaConversions._
        val result = client.knnQuery(k, queryObj.mkString("   "), true, true)
        val res : List[ReplyEntry] = result.toList

        val t2 = System.nanoTime()

        println("Finished in %g ms".format((t2 - t1)/1e6))

        res.foreach(e => println("id=%d dist=%g externId=%s".format(e.getId(), e.getDist(), e.getExternId())))

        transport.close(); // Close transport/socket !
      } catch {
        case (te : TException ) =>
          println("Apache Thrift exception: " + te)
          te.printStackTrace()
      }
    }
  }

  def createRandomVector(dimensions : Int) : Seq[Double] = for (j <- 1 to dimensions) yield Random.nextDouble()

  override def main(args: Array[String]): Unit = {
    var start = System.currentTimeMillis()
    print(s"$start: creating $samples random test sequences...")
    val rndTestSeq = for (i <- numPoints*dimensions +1 to numPoints*dimensions + samples) yield createRandomVector(dimensions)
    println(s"done (took: ${System.currentTimeMillis()-start}ms)")

    val client = new NmslibClient()

    val allStart = System.currentTimeMillis()
    val deltaTs = rndTestSeq.map(data => {
      start = System.currentTimeMillis()
      print(s"$start: looking for neighbours...")
      val items2 = client.query(data, neighbours)
      val deltaT = System.currentTimeMillis()-start
      println(s"done (took: ${deltaT}ms)")
      deltaT
    })
    println(s"average: ${deltaTs.sum.toDouble/samples.toDouble}ms")

    println(s"Found $samples * $neighbours in ${System.currentTimeMillis()-allStart}ms")

    print("fin!")
  }

}
