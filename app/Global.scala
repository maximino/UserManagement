import models._
import play.api._
import libs.json.Format
import utils.cypher.CypherQueries
import utils.persistance.graph

/**
 * ndidialaneme
 */

object Global extends GlobalSettings {
  override def onStart(app : Application) {

    val refNodesWeShouldHave = List("USER_ROOT", "SUPERVISOR_ROOT", "ROLE_ROOT")
    val refNodesInGraph = RefNode.getAllRefNodes map {refNode: RefNode => refNode.name}


    refNodesWeShouldHave map { refNode =>
      if (! refNodesInGraph.contains(refNode)){
        RefNode(null.asInstanceOf[Long], refNode).save
      }
    }
  }
}