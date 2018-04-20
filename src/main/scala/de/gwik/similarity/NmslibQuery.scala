package de.gwik.similarity

import java.util.UUID

import edu.cmu.lti.oaqa.similarity.{QueryService, ReplyEntry}
import org.apache.thrift.TException
import org.apache.thrift.protocol.TBinaryProtocol
import org.apache.thrift.transport.TSocket
import scala.io.Source
import scala.collection.JavaConversions._

class NmslibQuery(dataUrl: String) extends GenericQuery(dataUrl) with DataConfig {

  val src: Iterator[String] = Source.fromFile(dataUrl).getLines
  val testSequences: Seq[Seq[Double]] = src.map(l => l.split("   ").map(c => c.toDouble  ).toSeq).toSeq
  val dim: Int = testSequences.head.length

  val transport = new TSocket("localhost", 10000)
  transport.open()
  val protocol = new  TBinaryProtocol(transport)
  val client = new QueryService.Client(protocol)

  override def queryNN(queryVector: Seq[Double], nearestNeighborCount: Int): Seq[QueryResult] = {
    val queryString = queryVector.mkString("   ")

    try {
      val result = client.knnQuery(nearestNeighborCount, queryString, true, true).toList
      result.map(re => new QueryResult(queryVector,None, Option(re.getDist()), Option(re.getId().toString)))
    } catch {
      case (te : TException ) =>
        println("Apache Thrift exception: " + te)
        te.printStackTrace()
        println("needs: \n ./query_server -i /home/chambroc/code/sandbox/out/test_data.tsv -s l2 -m sw-graph -c NN=10,efConstruction=200 -p 10000")
        Seq()
    }
  }

  def stopServer(): Unit = {
    transport.close()
  }
}
