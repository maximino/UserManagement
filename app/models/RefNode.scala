package models

import play.api.libs.json._
import utils.persistance.graph

/**
 * ndidialaneme
 */

case class RefNode(id: Long, name: String) extends Model[RefNode]{

  def save(implicit f:Format[RefNode]):RefNode = super.save(this, Relationships.REF_NODE, graph.root)

}

object RefNode{
  implicit object RefNodeFormat extends Format[RefNode] {
    def reads(json: JsValue): RefNode = RefNode(
      (json \ "id").asOpt[Long].getOrElse(null.asInstanceOf[Long]),
      (json \ "name").as[String]
    )

    def writes(u: RefNode): JsValue =
      JsObject(List(
       "name" -> JsString(u.name)
      ) ::: (if (u.id != null.asInstanceOf[Long]) {
        List("id" -> JsNumber(u.id))
      } else {
        Nil
      }))
  }

}