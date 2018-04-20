lazy val buildThriftTask = taskKey[Unit]("Build thrift client classes")

buildThriftTask := {
  import sys.process._

  val protocolFileUrl = "https://raw.githubusercontent.com/nmslib/nmslib/master/query_server/protocol.thrift"
  val localProtocolFile = "target/protocol.thrift"

  Seq("wget", "-O", localProtocolFile, protocolFileUrl)!

  Seq("thrift", "-o", "src/main/", "--gen", "java", localProtocolFile)!
}

name := "sandbox"

version := "0.1"

scalaVersion := "2.11.12"

resolvers += "Sonatype OSS Releases" at "http://oss.sonatype.org/content/repositories/releases/"

libraryDependencies ++= Seq(
  "com.thesamet" %% "kdtree" % "1.0.4",
  "io.krom" % "lsh-scala_2.11" % "0.1",
  "org.apache.spark" %% "spark-sql" % "2.3.0" % "provided",
  "org.apache.thrift" % "libthrift" % "0.9.2" % "compile",
  "commons-cli" % "commons-cli" % "1.2",
  "org.slf4j" % "slf4j-api" % "1.7.12",
  "org.scalactic" %% "scalactic" % "3.0.5",
  "org.scalatest" %% "scalatest" % "3.0.5" % "test"
)

unmanagedSourceDirectories in Compile += baseDirectory.value / "src" / "main" / "gen-java"

compile in Compile := {
  buildThriftTask.value
  (compile in Compile).value
}