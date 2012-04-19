package models

import reflect.ClassManifest
import utils.cypher.CypherQueries
import utils.persistance.graph
import play.api.libs.json._

/**
 * ndidialaneme
 */

case class Role(id: Long, name: String) extends Model[Role]{

  override def save(implicit m: ClassManifest[Role], f:Format[Role]):Role = {
    super.save
  }
}

object Role{
  def getRoleById(id: Long) = Model.one[Role](id)

  def getAllRoles (implicit m:ClassManifest[Role], f:Format[Role])= graph.relationTargets(CypherQueries.match1(graph.root, Model.kindOf[Role]))

  def getAllButCurrentRolesForUser(id: Long)(implicit m:ClassManifest[Role], f:Format[Role]): List[Role]= {
    graph.relationTargets(CypherQueries.start2Match1WhereNotWithOr2(graph.root, graph.getNode(id).get, Model.kindOf[Role], Relationships.HAS_ROLE))
  }

  implicit object RoleFormat extends Format[Role] {

    def reads(json: JsValue): Role = Role(
      (json \ "id").asOpt[Long].getOrElse(null.asInstanceOf[Long]),
      (json \ "name").as[String]
    )

    def writes(r: Role): JsValue =
      JsObject(List(
        "_class_" -> JsString(Role.getClass.getName),
        "name" -> JsString(r.name)
      ) ::: (if (r.id != null.asInstanceOf[Long]) {
        List("id" -> JsNumber(r.id))
      } else {
        Nil
      }))

  }
}
