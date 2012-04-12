package models

import reflect.ClassManifest
import play.api.libs.json._

/**
 * ndidialaneme
 */

case class User(id: Int, username: String) extends Model[User]{

  override def save(implicit m: ClassManifest[User], f:Format[User]):User = {
    val user = super.save
//    user.index("models", "keyword", username)
    user
  }
}

object User {
  implicit object UserFormat extends Format[User] {
    def reads(json: JsValue): User = User(
      (json \ "id").asOpt[Int].getOrElse(null.asInstanceOf[Int]),
      (json \ "username").as[String]
    )

    def writes(u: User): JsValue =
      JsObject(List(
        "_class_" -> JsString(User.getClass.getName),
        "username" -> JsString(u.username)
      ) ::: (if (u.id != null.asInstanceOf[Int]) {
        List("id" -> JsNumber(u.id))
      } else {
        Nil
      }))
  }

  def all = Model.all[User]
}
