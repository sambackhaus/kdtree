package de.gwik.similarity

trait DataConfig {

  val numPoints = 1000000
  val dimensionsForGenerator = 90
  val samples = 100
  val neighbours = 150
  val numBits = 16
  val numTables = 5
  val dataName = "test_data.tsv"
  val dataFolder = "target/"
  val deadlineSeconds = 120

  def configuration(): String = {
    s"numPoints: $numPoints\ndimensionsForGenerator: $dimensionsForGenerator\nsamples: $samples\nneighbours: $neighbours\nnumBits: $numBits\nnumTables: $numTables\ndataName: $dataName\ndataFolder: $dataName\ndeadlineSeconds: $deadlineSeconds"
  }
}


