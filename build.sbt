name := "DockerSample"

version := "0.0.1"

organization := "com.sigmaxyz"

scalaVersion := "2.11.2"

resolvers ++= Seq("snapshots"     at "https://oss.sonatype.org/content/repositories/snapshots",
                  "staging"       at "https://oss.sonatype.org/content/repositories/staging",
                  "releases"      at "https://oss.sonatype.org/content/repositories/releases"
                 )

seq(webSettings :_*)

unmanagedResourceDirectories in Test <+= (baseDirectory) { _ / "src/main/webapp" }

scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature")

libraryDependencies ++= {
  val liftVersion = "2.6"
  Seq(
    "net.liftweb"       %% "lift-webkit"        % liftVersion        % "compile",
    "net.liftweb"       %% "lift-mapper"        % liftVersion        % "compile",
    "net.liftweb"       %% "lift-mongodb"         % liftVersion        % "compile->default" withSources(),
    "net.liftweb"       %% "lift-mongodb-record"  % liftVersion        % "compile->default" withSources(),
    "com.foursquare"    %% "rogue-field"          % "2.5.0" intransitive(),
    "com.foursquare"    %% "rogue-core"           % "2.5.0" intransitive(),
    "com.foursquare"    %% "rogue-lift"           % "2.5.0" intransitive(),
    "com.foursquare"    %% "rogue-index"          % "2.5.0" intransitive(),
    "net.liftmodules"   %% "lift-jquery-module_2.6" % "2.8",
    "net.liftmodules"   %% "fobo_2.6"           % "1.3"     % "compile",
    "org.eclipse.jetty" % "jetty-webapp"        % "8.1.7.v20120910"  % "container,test",
    "org.eclipse.jetty" % "jetty-plus"          % "8.1.7.v20120910"  % "container,test", // For Jetty Config
    "org.eclipse.jetty.orbit" % "javax.servlet" % "3.0.0.v201112011016" % "container,test" artifacts Artifact("javax.servlet", "jar", "jar"),
    "ch.qos.logback"    % "logback-classic"     % "1.0.6",
    "org.specs2"        %% "specs2"             % "2.3.12"           % "test",
    "com.h2database"    % "h2"                  % "1.3.167",
    "net.databinder" %% "dispatch-http" % "0.8.10",
    "net.databinder" %% "dispatch-mime" % "0.8.10",
    "net.databinder" %% "dispatch-json" % "0.8.10"
  )
}