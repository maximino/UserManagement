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

    val refNodeStringWeShouldHave = List("USERS_REFERENCE", "ROLES_REFERENCE")
    val refNodesInGraph = RefNode.getAllRefNodes
    val refNodesInGraphStrings = refNodesInGraph map {refNode: RefNode => refNode.name}


    refNodeStringWeShouldHave map { refNodeString =>
      if (! refNodesInGraphStrings.contains(refNodeString)){
        RefNode(null.asInstanceOf[Long], refNodeString).save
      }
    }
  }
}