package models

import utils.cypher.CypherQueries
import utils.persistance.graph
import play.api.libs.json._

/**
 * ndidialaneme
 */

case class User(id: Long, name: String) extends Model[User]{

  override def save(implicit f:Format[User]):User = {
    super.save
  }

  def addSupervisee(supervisee: User): User = {
    graph.createRelationship(this, Relationships.SUPERVISES, supervisee)
    this
  }

  def addRole(role: Role): User = {
    graph.createRelationship(this, Relationships.HAS_ROLE, role)
    this
  }
}

object User {

  def getUserById(id: Long)(implicit f:Format[User])= Model.one[User](id)

  //TODO-Ndidi: Broken. Relationships.USER has a value of USER instead of users
  def getAllUsers(implicit f:Format[User]) = graph.relationTargets(CypherQueries.match1(graph.root, Relationships.USER))

  def getAllUsersButThisUserAndSuperviseRelationships (id: Long)(implicit f:Format[User])= {
    graph.relationTargets(CypherQueries.start2Match1WhereNotWithOr2(graph.root, graph.getNode(id).get, Relationships.USER, Relationships.SUPERVISES))
  }

  implicit object UserFormat extends Format[User] {
    def reads(json: JsValue): User = User(
    (json \ "id").asOpt[Long].getOrElse(null.asInstanceOf[Long]),
    (json \ "name").as[String]
    )

    def writes(u: User): JsValue =
    JsObject(List(
    "_class_" -> JsString(User.getClass.getName),
    "name" -> JsString(u.name)
    ) ::: (if (u.id != null.asInstanceOf[Long]) {
        List("id" -> JsNumber(u.id))
      } else {
        Nil
      }))
  }

}
