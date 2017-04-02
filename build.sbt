name := """tweedle"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.10.3"

routesGenerator := InjectedRoutesGenerator

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  filters,
  "org.apache.kafka" % "kafka_2.10" % "0.10.1.1",
  "org.apache.kafka" % "kafka-clients" % "0.10.1.1",
  "org.apache.kafka" % "kafka-streams" % "0.10.1.1",
  "org.apache.storm" % "storm-kafka" % "1.0.3",
  "org.apache.storm" % "storm-core" % "1.0.3",
  "org.apache.storm" % "storm" % "1.0.3",
  "org.apache.storm" % "storm-kafka-client" % "1.0.3",
  "org.apache.zookeeper" % "zookeeper" % "3.4.9",
  "com.twitter" % "hbc-core" % "2.2.0",
  "com.twitter" % "hbc-twitter4j" % "2.2.0",
  "org.mongodb.morphia" % "morphia" % "1.3.2",
  "org.mongodb" % "mongo-java-driver" % "3.4.0",
  "edu.stanford.nlp" % "stanford-corenlp" % "3.7.0",
  "edu.stanford.nlp" % "stanford-corenlp" % "3.7.0" classifier "models",
  "edu.stanford.nlp" % "stanford-pos-tagger" % "3.0.2",
  "edu.stanford.nlp" % "stanford-parser" % "3.7.0",
  "org.apache.curator" % "curator-client" % "3.3.0"
)
resolvers += "clojars repo" at "http://clojars.org/repo/"
