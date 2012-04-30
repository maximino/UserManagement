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

    val refNodesWeShouldHave = List("USER_ROOT", "ROLE_ROOT")
    val refNodesInGraph = RefNode.getAllRefNodes map {
      _.name
    }

    if (!refNodesWeShouldHave.sameElements(refNodesInGraph)){
      createBasicNodes(refNodesWeShouldHave, refNodesInGraph)
    }
  }

  private def createBasicNodes(refNodesWeShouldHave: List[String], refNodesInGraph: List[String]){
    //Making the assumption that if one of the nodes isn't there, none are.
    refNodesWeShouldHave.foreach(
      RefNode(null.asInstanceOf[Long], _).save
    )

    createBasicRoleNodes()
    createAdmin()
  }

  def createBasicRoleNodes(){
    Role(null.asInstanceOf[Long], "SUPERVISOR").save
    Role(null.asInstanceOf[Long], "ADMIN").save
  }

  private def createAdmin(){
    val u = User(null.asInstanceOf[Long], "admin", "admin@bigfish.com", "admin").save
    User.makeUserASupervisor(u)
    User.makeUserAnAdmin(u)
  }
}