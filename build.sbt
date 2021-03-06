inThisBuild(Seq(
  name := "jnrtest",
  organization := "org.lolhens",
  version := "0.0.0",

  scalaVersion := "2.12.2",

  externalResolvers := Seq("artifactory" at "http://lolhens.no-ip.org/artifactory/maven-public/"),

  dependencyUpdatesExclusions := moduleFilter(organization = "org.scala-lang"),

  scalacOptions ++= Seq("-Xmax-classfile-name", "254"),

  mainClass in Compile := None
))

name := (name in ThisBuild).value

lazy val settings = Seq(
  addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full),
  addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.3")
)

lazy val root = project.in(file("."))
  .settings(publishArtifact := false)
  .aggregate(jnrtest)

lazy val macros = project.in(file("macros"))
  .settings(
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-reflect" % "2.12.2",
      "org.typelevel" %% "cats" % "0.9.0",
      "org.scodec" %% "scodec-bits" % "1.1.4"
    )
  )
  .settings(settings: _*)

lazy val jnrtest = project.in(file("jnrtest"))
  .settings(
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-reflect" % "2.12.2",
      "org.typelevel" %% "cats" % "0.9.0",
      "org.scodec" %% "scodec-bits" % "1.1.4",
      "com.github.jnr" % "jnr-ffi" % "2.1.5"
    )
  )
  .settings(settings: _*)
  .dependsOn(macros)
