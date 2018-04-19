package de.gwik.similarity

import java.io.{File, PrintWriter}

import scala.util.Random

object DataGenerator extends App with DataConfig {

  def generate() {
    val outFolder = new File("target/")
    val outFile = new File(outFolder, "test_data.tsv")
    if (outFile.exists()) outFile.delete() else outFolder.mkdirs()
    val writer = new PrintWriter(outFile)
    try {
      val start = System.currentTimeMillis()
      print(s"$start: creating $numPoints in fs with $dimensions dimensions...")
      (1 to numPoints).foreach(i => {
        val numbers = for (j <- 1 to dimensions) yield Random.nextDouble()
        val str = numbers.mkString("   ") + "\n"
        writer.write(str)
      })
      println(s"done (took: ${System.currentTimeMillis() - start}ms)")
    } finally {
      writer.close()
    }

    print("fin!")
  }
}
