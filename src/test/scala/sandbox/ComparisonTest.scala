package sandbox

import de.gwik.similarity.{DataConfig, _}
import org.scalatest.{FunSpec, Matchers}

import scala.collection.mutable
import scala.concurrent.duration._
import scala.io.Source
import scala.util.Random
import java.nio.charset.CodingErrorAction
import scala.io.Codec




class ComparisonTest extends FunSpec with Matchers with DataConfig {

  DataGenerator.generate()

  describe("inclusion test") {
    it("profiling test") {
      implicit val codec = Codec("UTF-8")
      codec.onMalformedInput(CodingErrorAction.REPLACE)
      codec.onUnmappableCharacter(CodingErrorAction.REPLACE)

      //val dn = "test_data_tesla.ssv"
      //val qn = "query_data_tesla.ssv"

      val dn = "test_data.tsv"
      val qn = "query_data.tsv"
      val queries: Seq[GenericQuery] = Seq(
        new KdtreeQuery(dataFolder + dn),
        new LshQuery(dataFolder + dn),
        new NmslibQuery(dataFolder + dn),
        new ReferenceQuery(dataFolder + dn)
      )


      val src: Iterator[String] = Source.fromFile(dataFolder + qn).getLines
      val queryVectors = src.map(l => l.split("   ").map(c => c.toDouble  ).toSeq).toSeq

      val resultProfiles: Seq[mutable.HashMap[String, Double]] = queries.map(q => {
        println("preparing: " + q.getClass + ".................................")
        System.gc()
        q.tearUp()
        println("ready: " + q.getClass + ".....................................")

        val deadline: Deadline = deadlineSeconds.seconds.fromNow
        while (deadline.hasTimeLeft) {
          q.profileQueryNN(queryVectors(Random.nextInt(queryVectors.size)), neighbours)
        }
        val profile = q.getProfile
        println(profile.toString())
        print("-----------------------------------\n\n\n")
        q.tearDown()
        profile
      })
      print("-----------------------------------\nResult:\n" + configuration + "\n\n")
      resultProfiles.foreach(println)
      1 shouldBe 1
    }

  }
}


