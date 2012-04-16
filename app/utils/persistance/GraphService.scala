package utils.persistance

import play.api.libs.json.Format

/**
 * ndidialaneme
 */

trait GraphService[Node] {

  def root: Node

  def getNode[T <: Node](id: Long)(implicit m: ClassManifest[T], f: Format[T]): Option[T]

//  def allNodes[T <: Node](implicit m: ClassManifest[T], f: Format[T]): List[T]

  def saveNode[T <: Node](t: T)(implicit m: ClassManifest[T], f: Format[T]): T

  def createRelationship(start: Node, rel: String, end: Node)
}
