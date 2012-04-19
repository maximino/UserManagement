package models

import play.api.libs.json._
import utils.persistance.graph
import utils.cypher.CypherQueries

/**
 * ndidialaneme
 */

case class RefNode(id: Long, name: String) extends Model[RefNode]{

  def save(implicit f:Format[RefNode]):RefNode = super.save(this, Relationships.REF_NODE, graph.root)

}

object RefNode{

  def getAllRefNodes(implicit f:Format[RefNode]) = {
    graph.relationTargets(CypherQueries.match1(graph.root, Relationships.REF_NODE))
  }

  //Hacky, I know.
  def userRefNode (implicit f:Format[RefNode]) = {
    val list = graph.relationTargets(CypherQueries.match1WhereName1(graph.root, Relationships.REF_NODE, "\"USERS_REFERENCE\""))
    list(0)
  }

  def roleRefNode (implicit f:Format[RefNode]) = {
    val list = graph.relationTargets(CypherQueries.match1WhereName1(graph.root, Relationships.REF_NODE, "\"ROLES_REFERENCE\""))
    list(0)
  }

  implicit object RefNodeFormat extends Format[RefNode] {
    def reads(json: JsValue): RefNode = RefNode(
      (json \ "id").asOpt[Long].getOrElse(null.asInstanceOf[Long]),
      (json \ "name").as[String]
    )

    def writes(u: RefNode): JsValue =
      JsObject(List(
       "name" -> JsString(u.name)
      ) ::: (if (u.id != null.asInstanceOf[Long]) {
        List("id" -> JsNumber(u.id))
      } else {
        Nil
      }))
  }

}