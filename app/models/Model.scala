package models

import play.api.libs.json.Format
import utils.persistance.graph
import scala.collection.mutable
import reflect.ClassManifest
import play.api.data.Form


/**
 * ndidialaneme
 */

abstract class Model[A <: Model[A]] {
  val id:Long

  def save(implicit m:ClassManifest[A], f:Format[A]):A = graph.saveNode[A](this.asInstanceOf[A])
//
//  def index(indexName:String, key:String, value:String)(implicit m:ClassManifest[A], f:Format[A]) {
//    graph.indexNode[A](this.asInstanceOf[A], indexName, key, value)
//  }
//
}

object Model {
  val models:mutable.Map[String, ClassManifest[_ <: Model[_]]] = new mutable.HashMap[String, ClassManifest[_ <: Model[_]]]()

  def register[T <: Model[_]](kind:String)(implicit m:ClassManifest[T]) {
    models.put(kind, m)
  }

//  def all[T <: Model[_]](implicit m:ClassManifest[T], f:Format[T]) = graph.allNodes[T]

  def one[T <: Model[_]](id:Long)(implicit m:ClassManifest[T], f:Format[T]) = graph.getNode[T](id).get

  def kindOf[T <: Model[_]] (implicit m:ClassManifest[T]):String = models.find(_._2.equals(m)).get._1
}