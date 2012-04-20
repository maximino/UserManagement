package models

import utils.cypher.CypherQueries
import utils.persistance.graph
import play.api.libs.json._

/**
 * ndidialaneme
 */

case class Role(id: Long, name: String) extends Model[Role]{

  def save[T <: Model[_]](implicit f:Format[Role]):Role = {
    super.save(this, Relationships.ROLE, RefNode.roleRefNode)
  }
}

object Role{

  def getRoleById(id: Long)(implicit f:Format[Role]) = Model.one[Role](id)

  def getAllRoles(implicit f:Format[Role]) = graph.cypherQuery(CypherQueries.match1(RefNode.roleRefNode, Relationships.ROLE))

  def getAllRolesForUser(user: User)(implicit f:Format[Role]): List[Role]= {
    graph.cypherQuery(CypherQueries.match1(user, Relationships.HAS_ROLE))
  }

  def getAllButCurrentRolesForUser(user: User)(implicit f:Format[Role]): List[Role]= {
    graph.cypherQuery(CypherQueries.start2Match1WhereNotWithOr2(RefNode.roleRefNode, user, Relationships.ROLE, Relationships.HAS_ROLE))
  }

  implicit object RoleFormat extends Format[Role] {

    def reads(json: JsValue): Role = Role(
      (json \ "id").asOpt[Long].getOrElse(null.asInstanceOf[Long]),
      (json \ "name").as[String]
    )

    def writes(r: Role): JsValue =
      JsObject(List(
        "name" -> JsString(r.name)
      ) ::: (if (r.id != null.asInstanceOf[Long]) {
        List("id" -> JsNumber(r.id))
      } else {
        Nil
      }))

  }
}
