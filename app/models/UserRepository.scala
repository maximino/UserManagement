package models

import org.neo4j.graphdb._
import org.neo4j.graphdb.index.Index
import org.neo4j.helpers.collection.IterableWrapper
import collection.JavaConversions


/**
 * ndidialaneme
 */

//case class UserRepository (private val graphDb: GraphDatabaseService, private val index: Index[Node]) {
//  private val userRefNode: Node = getUsersRootNode(graphDb);
//
//  private def getUsersRootNode (graphDb: GraphDatabaseService): Node = {
//    val rel: Relationship = graphDb.getReferenceNode.getSingleRelationship(REF_USER, Direction.OUTGOING)
//
//    if (rel != null ){
//      rel.getEndNode
//    }else{
//      val tx = this.graphDb.beginTx
//      try{
//        val refNode = this.graphDb.createNode
//        this.graphDb.getReferenceNode.createRelationshipTo(refNode, REF_USER)
//        tx.success()
//        refNode
//      }
//      finally {
//        tx.finish()
//      }
//    }
//  }
//
//  def createUser(name: String): User = {
//    // to guard against duplications we use the lock grabbed on ref node when
//    // creating a relationship and are optimistic about person not existing
//    val tx = graphDb.beginTx
//
//    try{
//      val newUserNode = graphDb.createNode
//      userRefNode.createRelationshipTo(newUserNode, A_USER)
//      // lock taken, check if exists
//      val alreadyExists = index.get(User.NAME, name).getSingle
//      if (alreadyExists != null){
//        tx.failure()
//        throw new Exception("User with this name already exists")
//      }
//      newUserNode.setProperty(User.NAME, name)
//      index.add(newUserNode,User.NAME, name)
//      tx.success()
//      new User(newUserNode)
//    }
//    finally {
//      tx.finish()
//    }
//  }
//
//  def getUserByName(name: String): User = {
//    val userNode = index.get(User.NAME, name).getSingle
//    if (userNode == null){
//      throw new IllegalArgumentException("User[" + name + "] not found")
//    }
//    User(userNode)
//  }
//
//  def deleteUser(user: User) {
//    val tx = graphDb.beginTx
//
//    try{
//      val userNode = user.getUnderlyingNode
//      index.remove(userNode, User.NAME, user.getName)
//
//      //todo-ndidi remove all supervisory relationships
//      userNode.getSingleRelationship(A_USER, Direction.INCOMING).delete()
//
//      //todo-ndidi remove all roles
//
//      userNode.delete
//
//      tx.success()
//    }
//    finally {
//      tx.finish()
//    }
//  }
//
//  def getAllUsers = {
//    JavaConversions.asScalaIterator(
//    new IterableWrapper[User, Relationship](userRefNode.getRelationships(A_USER)){
//      def underlyingObjectToObject(userRel: Relationship): User = {
//        new User(userRel.getEndNode)
//      }
//    }.iterator).toList
//  }
//}