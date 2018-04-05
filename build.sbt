name := "sandbox"

version := "0.1"

scalaVersion := "2.11.12"

resolvers += "Sonatype OSS Releases" at "http://oss.sonatype.org/content/repositories/releases/"

libraryDependencies += "com.thesamet" %% "kdtree" % "1.0.4"
libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.3.0" % "provided"

