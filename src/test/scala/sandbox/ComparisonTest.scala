package sandbox

import de.gwik.similarity.{DataConfig, DataGenerator, KdtreeQuery, ReferenceQuery}
import org.scalatest.{FunSpec, Matchers}


class ComparisonTest extends FunSpec with Matchers with DataConfig {

  DataGenerator.generate()

  describe("inclusion test") {
    it("TODO: put tests") {
      val queryVector = DataGenerator.createRandomVector()
      val kdtreeQuery = new KdtreeQuery(dataFolder + dataName)
      val referenceQuery = new ReferenceQuery(dataFolder + dataName)

      val result1 = kdtreeQuery.profileQueryNN(queryVector, neighbours)
      val result2 = referenceQuery.profileQueryNN(queryVector, neighbours)

      println(kdtreeQuery.getCurrentProfile())
      println(referenceQuery.getCurrentProfile())

      result1.length shouldBe neighbours
    }

  }
}


