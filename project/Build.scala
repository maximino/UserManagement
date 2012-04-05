import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "UserManagement"
    val appVersion      = "1.0"
	
	val sbtIdeaRepo		= "sbt-idea-repo" at "http://mpeltonen.github.com/maven/"

    val appDependencies = Seq(
      "org.neo4j" % "neo4j" % "1.7.M01"
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
      // Add your own project settings here
	  	resolvers ++= Seq(
			sbtIdeaRepo
		)
    )

}
