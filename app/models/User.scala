package models

import org.neo4j.graphdb._
import traversal.TraversalDescription
import org.neo4j.kernel.Traversal

/**
 * ndidialaneme
 */

class User(private val underlyingNode: Node) {

  def getName = {
    underlyingNode.getProperty(User.NAME)
  }

  def getUnderlyingNode: Node = {
    underlyingNode
  }

  // Overriding is compulsory
  override def hashCode: Int = {
    underlyingNode.hashCode
  }

  override def equals(that: Any): Boolean = {
    that.isInstanceOf[User] && {
      val o = that.asInstanceOf[User]
      underlyingNode.equals(o.getUnderlyingNode)
    }
  }

  override def toString: String = {
    "User[" + getName + "]"
  }

}

object User{
  val NAME = "name"
}

