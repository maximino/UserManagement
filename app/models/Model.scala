package models

import play.api.libs.json.Format
import utils.persistance.graph
import reflect.ClassManifest

/**
 * ndidialaneme
 */

abstract class Model[A <: Model[A]] {
  val id:Long
  val name: String

  def save(savedNode: A, relationship: String, refNode: Model[_])(implicit f:Format[A]): A = {
    graph.saveNodeAndCreateRelationship(savedNode, relationship, refNode)
  }
}

object Model {
  def one[T <: Model[_]](id:Long)(implicit m:ClassManifest[T], f:Format[T]) = graph.getNode[T](id).get

}