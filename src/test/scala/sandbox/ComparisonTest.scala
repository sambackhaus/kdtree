package sandbox

import de.gwik.similarity.{DataConfig, DataGenerator, KdtreeQuery}
import org.scalatest.{FunSpec, Matchers}


class ComparisonTest extends FunSpec with Matchers with DataConfig {

  DataGenerator.generate()

  describe("inclusion test") {
    it("TODO: put tests") {

      val kdtreeQuery = new KdtreeQuery(dataFolder + dataName)
      val queryVector = DataGenerator.createRandomVector()

      val result = kdtreeQuery.queryNN(queryVector, neighbours)

      result.length shouldBe neighbours
    }

  }
}


