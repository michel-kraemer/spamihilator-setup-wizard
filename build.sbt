seq(WebPlugin.webSettings: _*)

name := "Spamihilator Online Setup Wizard"

version := "1.0"

organization := "com.spamihilator"

scalaVersion := "2.8.1"

libraryDependencies ++= Seq(
  "net.liftweb" %% "lift-webkit" % "2.3",
  "ch.qos.logback" % "logback-classic" % "0.9.26",
  "com.mongodb.casbah" %% "casbah" % "2.0.2",
  "org.springframework.security" % "spring-security-web" % "3.0.5.RELEASE",
  "org.springframework.security" % "spring-security-config" % "3.0.5.RELEASE",
  "org.springframework" % "spring-webmvc" % "3.0.5.RELEASE",
  "org.eclipse.jetty" % "jetty-webapp" % "7.3.0.v20110203" % "jetty",
  "org.pegdown" % "pegdown" % "1.0.1",
  "org.mindrot" % "jbcrypt" % "0.3m",
  "javax.servlet" % "servlet-api" % "2.5"
)
