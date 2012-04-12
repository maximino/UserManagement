package utils.neo4j

import play.api.libs.json.Json._
import play.api.libs.json._
import models._
import dispatch._
import utils.persistance.GraphService
import utils.dispatch.PlayJsonDispatchHttp._
import utils.dispatch.ModelDispatchHttp._

/**
 * ndidialaneme
 */

trait Neo4jRestService extends GraphService[Model[_]]{

  val neoRest = :/("localhost", 7474)
  val neoRestBase = neoRest / "db" / "data"
  val neoRestNode = neoRestBase / "node"
  val neoRestRel = neoRestBase / "relationship"
  val neoRestCypher = neoRestBase / "cypher"

  def selfRestUriToId(uri: String) = uri.substring(uri.lastIndexOf('/') + 1).toLong

  def buildUrl(u: String) = url(u)

  def neoRestNodeById(id: Long) = neoRestNode / id.toString

  implicit def conforms: (JsValue) => JsValue = {
    (_: JsValue) \ "data"
  }

  implicit def defaultResultsFilter: (JsValue) => Iterable[JsValue] = {
    jsValue: JsValue =>
    //the first list is the global result
    //underneath lists are composing all returnable
    //each JsValue is then a returnable
      (jsValue \ "data").as[List[List[JsValue]]].map {
        l => {
          //here only one returnable
          val v = l.head
          v \ "data"
        }
      }
  }

  override lazy val root: Model[_] = Http(neoRestBase <:< Map("Accept" -> "application/json") >! {
    jsValue => new Model() { val id:Long = selfRestUriToId((jsValue \ "reference_node").as[String])}
  })

  def getNode[T <: Model[_]](id: Long)(implicit m: ClassManifest[T], f: Format[T]): Option[T] = {
    try {
      Http(neoRestNodeById(id) <:< Map("Accept" -> "application/json") >^> (Some(_: T)))
    } catch {
      //todo check 404
      case x => None
    }
  }

  def allNodes[T <: Model[_]](implicit m: ClassManifest[T], f: Format[T]): List[T] = relationTargets(root, Model.kindOf[T])

  def saveNode[T <: Model[_]](t: T)(implicit m: ClassManifest[T], f: Format[T]): T = {
    val (id: Long, property: String) = Http(
      (neoRestNode <<(stringify(toJson(t)), "application/json"))
        <:< Map("Accept" -> "application/json")
        >! {
        jsValue =>
          val id: Long = selfRestUriToId((jsValue \ "self").as[String])
          (id, (jsValue \ "property").as[String])
      }
    )

    //update the id property
    Http(
      (buildUrl(property.replace("{key}", "id")) <<(id.toString, "application/json") PUT)
        <:< Map("Accept" -> "application/json") >| //no content
    )

    val model = getNode[T](id).get

    //create the rel for the kind
    linkToRoot(Model.kindOf[T], model)

    model
  }

  def createRelationship(start: Model[_], rel: String, end: Model[_]) {
    //retrieve the creation rel url for the kind
    val createRelationship = Http(neoRestNodeById(start.id) <:< Map("Accept" -> "application/json") >! {
      jsValue => (jsValue \ "create_relationship").as[String]
    })

    //create the relationship 'rel' to the created node
    //the payload
    val props = JsObject(Seq(
      "to" -> JsString(neoRestNodeById(end.id).path),
      "type" -> JsString(rel)
    ))
    //the request
    Http(
      (buildUrl(createRelationship) <<(stringify(props), "application/json"))
        <:< Map("Accept" -> "application/json")
        >! {
        jsValue => //((jsValue \ "self").as[String], (jsValue \ "data").as[JsObject])
      })
  }

  def linkToRoot(rel: String, end: Model[_]) {
    createRelationship(root, rel, end)
  }

  def relationTargets[T <: Model[_]](start: Model[_], rel: String)(implicit m: ClassManifest[T], f: Format[T]): List[T] = {
    val cypher = """
      start x=node({ref})
      match x-[:{rel}]->t
      return t
      """
      .replaceAllLiterally("{ref}", start.id.toString)
      .replaceAllLiterally("{rel}", rel)

    val props = JsObject(Seq(
      "query" -> JsString(cypher),
      "params" -> JsObject(Seq())
    ))
    Http(neoRestCypher <<(stringify(props), "application/json") >^*> { (_: Iterable[T]).toList })
  }
}