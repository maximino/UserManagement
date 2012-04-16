import models._
import play.api._

/**
 * ndidialaneme
 */

object Global extends GlobalSettings {
  override def onStart(app : Application) {
    Model.register[User]("users")
    Model.register[Role]("roles")
  }
}