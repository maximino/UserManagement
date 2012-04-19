package utils.persistance

import play.api.libs.json.Format

/**
 * ndidialaneme
 */

trait GraphService[Node] {

  def root: Node

  def getNode[T <: Node](id: Long)(implicit f: Format[T]): Option[T]

  def saveNode[T <: Node](t: T)(implicit f: Format[T]): T

  def createRelationship(start: Node, rel: String, end: Node)
}
