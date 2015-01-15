ReleaseSettings.defaults

organization := "edu.washington.cs.knowitall"

name := "openregex-scala"

crossScalaVersions := Seq("2.11.5", "2.10.4")

scalaVersion <<= crossScalaVersions { (vs: Seq[String]) => vs.head }

libraryDependencies ++= Seq("edu.washington.cs.knowitall" % "openregex" % "1.1.1",
    "com.google.code.findbugs" % "jsr305" % "2.0.1",
    "junit" % "junit" % "4.11" % "test",
    "org.specs2" %% "specs2-core" % "2.4.15" % "test",
    "org.specs2" %% "specs2-scalacheck" % "2.4.15" % "test",
    "org.specs2" %% "specs2-junit" % "2.4.15" % "test")

licenses := Seq("LGPL" -> url("http://www.gnu.org/licenses/lgpl.html"))

homepage := Some(url("http://github.com/knowitall/openregex-scala"))

resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

publishMavenStyle := true

publishTo <<= version { (v: String) =>
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

pomExtra := (
  <scm>
    <url>https://github.com/knowitall/openregex-scala</url>
    <connection>scm:git://github.com/knowitall/openregex-scala.git</connection>
    <developerConnection>scm:git:git@github.com:knowitall/openregex-scala.git</developerConnection>
    <tag>HEAD</tag>
  </scm>
  <developers>
   <developer>
      <name>Michael Schmitz</name>
    </developer>
  </developers>)
