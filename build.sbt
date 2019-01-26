name := "trig_simplify"

version := "0.1"

scalaVersion := "2.12.8"

libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.5"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"

libraryDependencies ++= {
    if (coverageEnabled.value)
        Seq("org.scoverage" %% "scalac-scoverage-runtime" % "1.3.1")
    else
        Nil
}

coverageEnabled.in(Test, test) := true
testOptions in Test += Tests.Argument("-P")

mainClass in Compile := Some("io.github.cbskarmory.InputReader")