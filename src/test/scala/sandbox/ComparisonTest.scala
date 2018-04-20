package sandbox

import de.gwik.similarity._
import org.scalatest.{FunSpec, Matchers}
import scala.concurrent.duration._

class ComparisonTest extends FunSpec with Matchers with DataConfig {

  DataGenerator.generate()

  describe("inclusion test") {
    it("TODO: put tests") {

      val queries: Seq[GenericQuery] = Seq(
        new KdtreeQuery(dataFolder + dataName),
        new ReferenceQuery(dataFolder + dataName),
        new LshQuery(dataFolder + dataName)
      )

      queries.map(q => {
        val deadline = 5.seconds.fromNow
        while(deadline.hasTimeLeft) {
          q.profileQueryNN(DataGenerator.createRandomVector(), neighbours)
        }
        println(q.getCurrentProfile())
        q.getCurrentProfile()
      })

      1 shouldBe 1
    }

  }
}


