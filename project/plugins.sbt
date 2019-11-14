addSbtPlugin("com.geirsson"      % "sbt-ci-release" % "1.4.31")
addSbtPlugin("com.geirsson"      % "sbt-scalafmt"   % "1.5.1")
addSbtPlugin("de.heikoseeberger" % "sbt-header"     % "5.3.0") // see https://github.com/sbt/sbt-header

libraryDependencies += "org.slf4j" % "slf4j-nop" % "1.7.29" // Needed by sbt-git
