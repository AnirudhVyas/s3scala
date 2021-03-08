import sbt._

object Dependencies {
  private val AwsSdkVersion         = "2.16.13"
  private val CatsEffectVersion     = "2.3.3"
  private val CatsEffectTestVersion = "0.5.2"
  private val GatlingVersion        = "3.5.1"
  private val MonixVersion          = "3.3.0"
  private val ScalaTestVersion      = "3.1.1"

  val coreDependencies: Seq[ModuleID] = Seq(
    "software.amazon.awssdk" % "s3"          % AwsSdkVersion,
    "org.typelevel"         %% "cats-effect" % CatsEffectVersion,
    "io.monix"              %% "monix"       % MonixVersion
  )

  val testDependencies: Seq[ModuleID] = Seq(
    "com.codecommit" %% "cats-effect-testing-scalatest-scalacheck" % CatsEffectTestVersion,
    "org.scalatest"  %% "scalatest"                                % ScalaTestVersion,
    "io.gatling"      % "gatling-test-framework"                   % GatlingVersion
  ).map(_ % Test)
}
