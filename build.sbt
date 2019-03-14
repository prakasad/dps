import sbt.Path

name := """dps"""

version := "1.0-SNAPSHOT"

//lazy val other = file("stanford-corenlp-full-2018-10-05").getAbsoluteFile.toURI
//.dependsOn(other)

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.7"



libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs
)

//routesGenerator := StaticRoutesGenerator

scalaVersion := "2.11.11"

//libraryDependencies += guice
libraryDependencies += "org.mockito" % "mockito-core" % "2.10.0" % "test"

libraryDependencies ++= Seq(
"com.couchbase.client" % "couchbase-client" % "1.4.9",
  "com.google.inject" % "guice" % "3+",
"com.couchbase.client" % "java-client" % "2.5.6",
  "com.googlecode.json-simple"  % "json-simple" % "1.1.1",
  "javax.json" % "javax.json-api" % "1.1.3"
)

//libraryDependencies += "edu.standford.nlp" % "standford-corenlp" % "3.9.2" from "file://stanford-corenlp-full-2018-10-05/stanford-corenlp-3.9.2.jar"
 //"edu.standford.nlp" % "standford-corenlp" % "3.9.2"