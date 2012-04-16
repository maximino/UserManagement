package models

import reflect.ClassManifest
import utils.cypher.CypherQueries
import utils.persistance.graph
import play.api.libs.json._
import models.User

/**
 * ndidialaneme
 */

case class User(id: Long, username: String) extends Model[User]{

  override def save(implicit m: ClassManifest[User], f:Format[User]):User = {
    val user = super.save
//    user.index("models", "keyword", username)
    user
  }

  def addSupervisor(supervisor: User){
    graph.createRelationship(this, User.SUPERVISES, supervisor)
  }
}

object User {

  val SUPERVISES = "SUPERVISES"

  implicit object UserFormat extends Format[User] {
    def reads(json: JsValue): User = User(
      (json \ "id").asOpt[Long].getOrElse(null.asInstanceOf[Long]),
      (json \ "username").as[String]
    )

    def writes(u: User): JsValue =
      JsObject(List(
        "_class_" -> JsString(User.getClass.getName),
        "username" -> JsString(u.username)
      ) ::: (if (u.id != null.asInstanceOf[Long]) {
        List("id" -> JsNumber(u.id))
      } else {
        Nil
      }))
  }

  def getAllUsers (implicit m:ClassManifest[User], f:Format[User])= graph.relationTargets(CypherQueries.match1(graph.root, Model.kindOf[User]))

  def getAllButThisUser (id: Long)(implicit m:ClassManifest[User], f:Format[User])= {
    graph.relationTargets(CypherQueries.match1Where1(graph.root, graph.getNode(id).get, Model.kindOf[User]))
  }

  def getUserById(id: Long) = Model.one[User](id)
}
