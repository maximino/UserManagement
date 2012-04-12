package models

import play.api.libs.json._


/**
 * ndidialaneme
 */

case class Role(id: Int) extends Model[Role]

object Role{
  implicit object RoleFormat extends Format[Role] {
    def reads(json: JsValue) = null

    def writes(r: Role) = null
  }
}
