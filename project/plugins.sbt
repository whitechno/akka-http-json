addSbtPlugin("com.geirsson" % "sbt-ci-release" % "1.4.31")
//addSbtPlugin("com.geirsson"      % "sbt-scalafmt"   % "1.5.1")
addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.3.2")

// see https://github.com/sbt/sbt-header
addSbtPlugin("de.heikoseeberger" % "sbt-header" % "5.3.0")

// For assembly:
// The burning desire to have a simple deploy procedure.
// see https://github.com/sbt/sbt-assembly
addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.10")

libraryDependencies += "org.slf4j" % "slf4j-nop" % "1.7.29" // Needed by sbt-git
