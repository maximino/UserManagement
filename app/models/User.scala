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

  def addSupervisee(supervisee: User){
    graph.createRelationship(this, User.SUPERVISES, supervisee)
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

  def getAllButThisUser (id: Long)(implicit m:ClassManifest[User], f:Format[User])= {
    graph.relationTargets(CypherQueries.match1Where1(graph.root, graph.getNode(id).get, Model.kindOf[User]))
  }

  def getUserById(id: Long) = Model.one[User](id)
}
