package models

import utils.cypher.CypherQueries
import utils.persistance.graph
import play.api.libs.json._

/**
 * ndidialaneme
 */

case class User(id: Long, name: String) extends Model[User]{

  def save[T <: Model[_]](implicit f:Format[User]):User = {
    super.save(this, Relationships.USER, RefNode.userRefNode)
  }

  def addSupervisee(supervisee: User): User = {
    graph.createRelationship(this, Relationships.SUPERVISES, supervisee)
    inheritSuperviseesRoles(supervisee)
    this
  }

  def addRole(role: Role): User = {
    graph.createRelationship(this, Relationships.HAS_ROLE, role)
    this
  }

  def inheritSuperviseesRoles(supervisee: User){
    //Needs to also inherit roles that were added to supervisees after they became supervisees.
    val superviseesRoles = Role.getAllRolesForUser(supervisee)
    val thisUserRoles = Role.getAllRolesForUser(this)

    superviseesRoles map { role =>
      if (!thisUserRoles.contains(role)){
        this.addRole(role)
      }
    }
  }
}

object User {

  def getUserById(id: Long)(implicit f:Format[User])= Model.one[User](id)

  def getAllUsers(implicit f:Format[User]) = graph.cypherQuery(CypherQueries.match1(RefNode.userRefNode, Relationships.USER))

  def getAllSupervisees(user: User)(implicit f:Format[User])=graph.cypherQuery(CypherQueries.match1(user, Relationships.SUPERVISES))

  def getAllUsersButThisUserAndSuperviseRelationships (user: User)(implicit f:Format[User])= {
    graph.cypherQuery(CypherQueries.start2Match1WhereNotWithOr2(RefNode.userRefNode, user, Relationships.USER, Relationships.SUPERVISES))
  }

  implicit object UserFormat extends Format[User] {
    def reads(json: JsValue): User = User(
    (json \ "id").asOpt[Long].getOrElse(null.asInstanceOf[Long]),
    (json \ "name").as[String]
    )

    def writes(u: User): JsValue =
    JsObject(List(
    "name" -> JsString(u.name)
    ) ::: (if (u.id != null.asInstanceOf[Long]) {
        List("id" -> JsNumber(u.id))
      } else {
        Nil
      }))
  }

}
