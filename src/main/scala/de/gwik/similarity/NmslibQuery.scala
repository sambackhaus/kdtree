package de.gwik.similarity

import edu.cmu.lti.oaqa.similarity.QueryService
import org.apache.thrift.protocol.TBinaryProtocol
import org.apache.thrift.transport.TSocket

import scala.io.Source
import scala.collection.JavaConversions._
import scala.sys.process._

class NmslibQuery(dataUrl: String) extends GenericQuery(dataUrl) with DataConfig {

  var dim: Int = _

  var nmsLibServerProcess: Process = _
  var transport: Option[TSocket] = None
  var client: QueryService.Client = _

  override def queryNN(queryVector: Seq[Double], nearestNeighborCount: Int): Seq[QueryResult] = {
    val queryString = queryVector.mkString("   ")

    val result = client.knnQuery(nearestNeighborCount, queryString, true, false).toList
    result.map(re => new QueryResult(queryVector,None, Option(re.getDist()), Option(re.getId().toString)))
  }

  override def tearUp(): Unit = {
    val src: Iterator[String] = Source.fromFile(dataUrl).getLines
    val testSequences: Seq[Seq[Double]] = src.map(l => l.split("   ").map(c => c.toDouble  ).toSeq).toSeq
    dim = testSequences.head.length

    val pathToQueryServer = "../nmslib/query_server/cpp_client_server/query_server"
    val shellExecute = s"$pathToQueryServer -i ./target/test_data.tsv -s l2 -m sw-graph -c NN=10 efConstruction=200 -p 10000"
    println(shellExecute)
    nmsLibServerProcess = shellExecute run

    var isConnected = false
    transport = None
    print("Connection to localhost:10000")
    while(!isConnected) {
      try {
        transport = Some(new TSocket("localhost", 10000))
        transport.get.open()
        isConnected = true
      } catch {
        case e : Exception => {
          Thread.sleep(1000)
          print(".")
        }}}
    val protocol = new TBinaryProtocol(transport.get)
    client = new QueryService.Client(protocol)
    println("done!")
  }

  override def tearDown(): Unit = {
    if(transport.isDefined) transport.get.close()
    nmsLibServerProcess.destroy()
  }
}
