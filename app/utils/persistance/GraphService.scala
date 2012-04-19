package utils.persistance

import play.api.libs.json.Format
import models.Model

/**
 * ndidialaneme
 */

trait GraphService[Node] {

  def root: Node

  def getNode[T <: Node](id: Long)(implicit f: Format[T]): Option[T]

  def saveNodeAndCreateRelationship[T <: Model[_]](t: T, relationship: String, refNode: Model[_])(implicit f: Format[T]): T

  def createRelationship(start: Node, rel: String, end: Node)
}
