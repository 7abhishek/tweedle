name := """tweedle"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  "org.apache.kafka" % "kafka_2.10" % "0.10.1.1",
  "org.apache.kafka" % "kafka-clients" % "0.10.1.1",
  "org.apache.storm" % "storm-kafka" % "1.0.2",
  "org.apache.storm" % "storm-core" % "1.0.2",
  "org.apache.zookeeper" % "zookeeper" % "3.4.0",
  "com.twitter" % "hbc-core" % "2.2.0",
  "com.twitter" % "hbc-twitter4j" % "2.2.0",
  "org.mongodb.morphia" % "morphia" % "1.3.0",
  "org.mongodb" % "mongo-java-driver" % "3.4.0",
  "edu.stanford.nlp" % "stanford-corenlp" % "3.6.0"


)
