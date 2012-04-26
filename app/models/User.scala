package models

import utils.cypher.CypherQueries
import utils.persistance.graph
import play.api.libs.json._
import models.User.UserFormat

/**
 * ndidialaneme
 */

case class User(id: Long, name: String, email: String, password: String) extends Model[User]{

  def save[T <: Model[_]](implicit f:Format[User]):User = {
    super.save(this, Relationships.USER, RefNode.userRefNode)
  }

  def addSupervisee(supervisee: User): User = {
    graph.createRelationship(this, Relationships.SUPERVISES, supervisee)
    inheritSuperviseesRoles(supervisee)
    User.makeUserASupervisor(this)(UserFormat)
    this
  }

  def addRole(role: Role): User = {
    graph.createRelationship(this, Relationships.HAS_ROLE, role)
    this
  }

  def inheritSuperviseesRoles(supervisee: User){
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
  def authenticate(email: String, password: String):Option[User] = {
    null
  }

  def getUserById(id: Long)(implicit f:Format[User])= Model.one[User](id)

  def getAllUsers(implicit f:Format[User]) = graph.cypherQuery(CypherQueries.match1(RefNode.userRefNode, Relationships.USER))

  def getAllSupervisees(user: User)(implicit f:Format[User])=graph.cypherQuery(CypherQueries.match1(user, Relationships.SUPERVISES))

  def makeUserASupervisor(user: User)(implicit f:Format[User]) ={
    if(graph.cypherQuery(CypherQueries.start1Match1End1(RefNode.supervisorRefNode, Relationships.SUPERVISOR, user)).isEmpty){
      graph.createRelationship(RefNode.supervisorRefNode, Relationships.SUPERVISOR, user)
    }
    user
  }

  def getAllUsersButThisUserAndSuperviseRelationships (user: User)(implicit f:Format[User])= {
    graph.cypherQuery(CypherQueries.start2Match1WhereNotWithOr2(RefNode.userRefNode, user, Relationships.USER, Relationships.SUPERVISES))
  }

  def updateRolesFromSupervisees(user: User)(implicit f:Format[User]){
    User.getAllSupervisees(user) map { supervisee =>
        user.inheritSuperviseesRoles(supervisee)
    }
  }

  implicit object UserFormat extends Format[User] {
    def reads(json: JsValue): User = User(
    (json \ "id").asOpt[Long].getOrElse(null.asInstanceOf[Long]),
    (json \ "name").as[String],
    (json \ "email").as[String],
    (json \ "password").as[String]
    )

    def writes(u: User): JsValue =
    JsObject(List(
    "name" -> JsString(u.name),
    "email" -> JsString(u.email),
    "password" -> JsString(u.password)
    ) ::: (if (u.id != null.asInstanceOf[Long]) {
        List("id" -> JsNumber(u.id))
      } else {
        Nil
      }))
  }

  //TODO Remove later
  def usersNotSupervising(implicit f:Format[User]):List[User] = {
    graph.cypherQuery(CypherQueries.start1Match1WhereNotWithRelationship1(RefNode.userRefNode, Relationships.USER, Relationships.SUPERVISES))
  }

  def usersNotSupervisingExcept(user: User)(implicit f:Format[User]):List[User] = {
    graph.cypherQuery(CypherQueries.start1Match1WhereNotWithRelationshipOrIs(RefNode.userRefNode, user, Relationships.USER, Relationships.SUPERVISES))
  }
}
