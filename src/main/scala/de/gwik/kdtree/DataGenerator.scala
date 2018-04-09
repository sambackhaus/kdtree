package de.gwik.kdtree

import java.io.{File, PrintWriter}

import scala.util.Random

object DataGenerator extends App with DataConfig {

  val outFolder = new File("out/")
  val outFile = new File(outFolder, "test_data.tsv")
  if(outFile.exists()) outFile.delete() else outFolder.mkdirs()
  val writer = new PrintWriter(outFile)

  var start = System.currentTimeMillis()
  print(s"$start: creating $numPoints in fs with $dimensions dimensions...")
  (1 to numPoints).map(i => writer.write((for (j <- 1 to dimensions) yield Random.nextDouble()).mkString("\t")))
  println(s"done (took: ${System.currentTimeMillis()-start}ms)")

  print("fin!")
}
