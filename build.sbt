import sbt.Path

name := """dps"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.7"



libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs
)

routesGenerator := StaticRoutesGenerator

scalaVersion := "2.12.2"

//libraryDependencies += guice
libraryDependencies += "org.mockito" % "mockito-core" % "2.10.0" % "test"


//libraryDependencies += guice
libraryDependencies += "org.mockito" % "mockito-core" % "2.10.0" % "test"

libraryDependencies ++= Seq(
"com.couchbase.client" % "couchbase-client" % "1.4.9",
  "com.google.inject" % "guice" % "3+",
"com.couchbase.client" % "java-client" % "2.5.6"
)
 //"edu.standford.nlp" % "standford-corenlp" % "3.9.2"