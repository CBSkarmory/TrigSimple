lazy val commonSettings = Seq(
    name := "TrigSimple",
    version := "v0.3-beta",
    scalaVersion := "2.12.8",
    libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.5",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test",
    libraryDependencies ++= {
        if (coverageEnabled.value)
            Seq("org.scoverage" %% "scalac-scoverage-runtime" % "1.3.1")
        else
            Nil
    },
    coverageEnabled.in(Test, test) := true
)

lazy val Simplifier = (project in file("."))
        .settings(
            commonSettings,
        )

lazy val ProofSolver = (project in file("."))
        .settings(
            commonSettings,
        )