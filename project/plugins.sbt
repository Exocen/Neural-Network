// make jar
addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.11.2")

// sbt & scala project integration within scala-mode for emacs
addSbtPlugin("org.ensime" % "ensime-sbt-cmd" % "0.1.2")

// create eclipse definition project
addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "2.5.0")
