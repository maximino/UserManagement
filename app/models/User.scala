package models

import reflect.ClassManifest
import utils.cypher.CypherQueries
import utils.persistance.graph
import play.api.libs.json._

/**
 * ndidialaneme
 */

case class User(id: Long, name: String) extends Model[User]{

  override def save(implicit m: ClassManifest[User], f:Format[User]):User = {
    super.save
  }

  def addSupervisee(supervisee: User): User = {
    graph.createRelationship(this, User.SUPERVISES, supervisee)
    this
  }
}

object User {

  val SUPERVISES = "SUPERVISES"

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

  def getAllUsers (implicit m:ClassManifest[User], f:Format[User])= graph.relationTargets(CypherQueries.match1(graph.root, Model.kindOf[User]))

  def getAllButThisUserAndSuperviseRels (id: Long)(implicit m:ClassManifest[User], f:Format[User])= {
    graph.relationTargets(CypherQueries.start2Match1WhereNotWithOr2(graph.root, graph.getNode(id).get, Model.kindOf[User], SUPERVISES))
  }

  def getUserById(id: Long) = Model.one[User](id)
}
