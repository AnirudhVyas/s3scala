import Dependencies._

resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
resolvers += Resolver.mavenCentral
credentials += Credentials(Path.userHome / ".sbt" / ".credentials")

lazy val root = (project in file("."))
  .settings(
    organization := "me.free",
    name := "s3scala",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.13.3",
    libraryDependencies ++= coreDependencies,
    libraryDependencies ++= testDependencies,
    credentials += Credentials(Path.userHome / ".sbt" / ".credentials")
  )

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding",
  "UTF-8",
  "-target:jvm-1.8",
  "-language:implicitConversions",
  "-language:higherKinds",
  "-language:postfixOps",
  "-feature",
  "-Xfatal-warnings"
)
