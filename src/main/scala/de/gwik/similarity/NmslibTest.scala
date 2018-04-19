package de.gwik.similarity

import edu.cmu.lti.oaqa.similarity.{QueryService, ReplyEntry}
import org.apache.thrift.TException
import org.apache.thrift.protocol.TBinaryProtocol
import org.apache.thrift.transport.TSocket

import scala.util.Random

object NmslibTest extends AbsTest {

  class NmslibClient {

    def query(queryObj : String, k : Int) = {
      try {
        val transport = new TSocket("localhost", 10000)
        transport.open()

        val protocol = new  TBinaryProtocol(transport)
        val client = new QueryService.Client(protocol)

        val t1 = System.nanoTime()

        val result = client.knnQuery(k, queryObj, true, true)
        val t2 = System.nanoTime()
        println("Finished in %g ms".format((t2 - t1)/1e6))

        import scala.collection.JavaConversions._
        val res : List[ReplyEntry] = result.toList
        //res.foreach(e => println("id=%d dist=%g externId=%s".format(e.getId(), e.getDist(), e.getExternId())))

        transport.close(); // Close transport/socket !
      } catch {
        case (te : TException ) =>
          println("Apache Thrift exception: " + te)
          te.printStackTrace()
          println("needs: \n ./query_server -i /home/chambroc/code/sandbox/out/test_data.tsv -s l2 -m sw-graph -c NN=10,efConstruction=200 -p 10000")
      }
    }
  }

  def createRandomVector(dimensions : Int) : Seq[Double] = for (j <- 1 to dimensions) yield Random.nextDouble()

  override def main(args: Array[String]): Unit = {
    var start = System.currentTimeMillis()
    println("needs: \n ./query_server -i /home/chambroc/code/sandbox/out/test_data.tsv -s l2 -m sw-graph -c NN=10,efConstruction=200 -p 10000")
    print(s"$start: creating $samples random test sequences...")
    val rndTestSeq = for (i <- numPoints*dimensions +1 to numPoints*dimensions + samples) yield createRandomVector(dimensions)
    println(s"done (took: ${System.currentTimeMillis()-start}ms)")

    val client = new NmslibClient()

    val allStart = System.currentTimeMillis()
    println(s"Running $samples $neighbours-NN searches...")
    val deltaTs = rndTestSeq.map(data => {
      start = System.nanoTime()
      client.query(data.mkString("   "), neighbours)
      val deltaT = (System.nanoTime()-start)/1e6
      deltaT
    })

    println(s"average: ${deltaTs.sum/samples.toDouble}ms")
    println(s"Found $samples * $neighbours neighbours in ${System.currentTimeMillis()-allStart}ms")

    print("fin!")
  }

}
