package sandbox

import de.gwik.similarity._
import org.scalatest.{FunSpec, Matchers}
import scala.concurrent.duration._

class ComparisonTest extends FunSpec with Matchers with DataConfig {

  DataGenerator.generate()

  describe("inclusion test") {
    it("profiling test") {

      val queries: Seq[GenericQuery] = Seq(
        new ReferenceQuery(dataFolder + dataName),
        new KdtreeQuery(dataFolder + dataName),
        new LshQuery(dataFolder + dataName),
        new NmslibQuery(dataFolder + dataName)
      )

      val resultProfiles: Seq[String] = queries.map(q => {

        println("preparing: " + q.getClass + ".................................")
        System.gc()
        q.tearUp()
        println("ready: " + q.getClass + ".....................................")

        val deadline = 5.seconds.fromNow
        while (deadline.hasTimeLeft) {
          q.profileQueryNN(DataGenerator.createRandomVector(), neighbours)
        }
        val profile = q.getCurrentProfile()
        println(profile)
        print("-----------------------------------\n\n\n")
        q.tearDown()
        profile
      })
      print("-----------------------------------\nResult:")
      resultProfiles.foreach(println)
      1 shouldBe 1
    }

  }
}


