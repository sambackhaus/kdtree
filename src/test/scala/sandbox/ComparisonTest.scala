package sandbox

import de.gwik.similarity.{DataConfig, _}
import org.scalatest.{FunSpec, Matchers}

import scala.concurrent.duration._

class ComparisonTest extends FunSpec with Matchers with DataConfig {

  DataGenerator.generate()

  describe("inclusion test") {
    it("profiling test") {

      val queries: Seq[GenericQuery] = Seq(
        new KdtreeQuery(dataFolder + dataName),
        new LshQuery(dataFolder + dataName),
        new NmslibQuery(dataFolder + dataName),
        new ReferenceQuery(dataFolder + dataName)
      )

      val resultProfiles: Seq[String] = queries.map(q => {

        println("preparing: " + q.getClass + ".................................")
        System.gc()
        q.tearUp()
        println("ready: " + q.getClass + ".....................................")

        val deadline: Deadline = deadlineSeconds.seconds.fromNow
        while (deadline.hasTimeLeft) {
          q.profileQueryNN(DataGenerator.createRandomVector(), neighbours)
        }
        val profile = q.getCurrentProfile()
        println(profile)
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


