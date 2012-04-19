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

  def save(implicit f:Format[A]):A = graph.saveNode[A](this.asInstanceOf[A])
}

object Model {
  def one[T <: Model[_]](id:Long)(implicit m:ClassManifest[T], f:Format[T]) = graph.getNode[T](id).get

}