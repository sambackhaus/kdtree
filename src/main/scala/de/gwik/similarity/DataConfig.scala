package de.gwik.similarity

trait DataConfig {

  val numPoints = 1000000
  val dimensions = 90
  val samples = 100
  val neighbours = 150
  val numBits = 16
  val numTables = 5
  val dataName = "test_data.tsv"
  val dataFolder = "target/"
  val deadlineSeconds = 1200

  def configuration(): String = {
    s"numPoints: $numPoints\ndimensions: $dimensions\nsamples: $samples\nneighbours: $neighbours\nnumBits: $numBits\nnumTables: $numTables\ndataName: $dataName\ndataFolder: $dataName\ndeadlineSeconds: $deadlineSeconds"
  }
}
