name := "sandbox"

version := "0.1"

scalaVersion := "2.11.12"

resolvers += "Sonatype OSS Releases" at "http://oss.sonatype.org/content/repositories/releases/"

libraryDependencies ++= Seq(
  "com.thesamet" %% "kdtree" % "1.0.4",
  "io.krom" % "lsh-scala_2.11" % "0.1",
  "org.apache.spark" %% "spark-sql" % "2.3.0" % "provided"

)

