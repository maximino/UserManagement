import models._
import play.api._

/**
 * ndidialaneme
 */

object Global extends GlobalSettings {
  override def onStart(app : Application) {
    val UserRef = RefNode(null.asInstanceOf[Long], "USERS_REFERENCE")
    val RoleRef = RefNode(null.asInstanceOf[Long], "ROLES_REFERENCE")

    UserRef.save
    RoleRef.save
  }
}