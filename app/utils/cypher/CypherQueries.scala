package utils.cypher

import models.Model

/**
 * ndidialaneme
 */

object CypherQueries {

  def match1 (start: Model[_], rel: String): String = {
    """
    start x=node({ref})
    match x-[:{rel}]->t
    return t
    """
    .replaceAllLiterally("{ref}", start.id.toString)
    .replaceAllLiterally("{rel}", rel)
  }

  def match1WhereName1 (start: Model[_], rel: String, name: String): String = {
    """
    START x=node({ref})
    MATCH x-[:{rel}]->t
    WHERE t.name = {name}
    RETURN t
    """
    .replaceAllLiterally("{ref}", start.id.toString)
    .replaceAllLiterally("{rel}", rel)
    .replaceAllLiterally("{name}", name)
  }

  def start1Match1End1 (start: Model[_], rel: String, end: Model[_]): String = {
    """
    START x=node({ref})
    MATCH x-[:{rel}]->t
    WHERE t.id = {id}
    RETURN t
    """
    .replaceAllLiterally("{ref}", start.id.toString)
    .replaceAllLiterally("{rel}", rel)
    .replaceAllLiterally("{id}", end.id.toString)
  }

  def start2Match1WhereNotWithOr2 (start: Model[_], clause: Model[_], rel: String, rel2:String): String = {
    """
    START x=node({ref}), y=node({ref2})
    MATCH x-[:{rel}]->t
    WHERE NOT(t.id={ref2} OR y-[:{rel2}]-t)
    RETURN t
    """
    .replaceAllLiterally("{ref}", start.id.toString)
    .replaceAllLiterally("{ref2}", clause.id.toString)
    .replaceAllLiterally("{rel}", rel)
    .replaceAllLiterally("{rel2}", rel2)
  }

  def getAllWithThisRelationship (start: Model[_], rel: String): String = {
    """
    START x=node({ref})
    MATCH x-[:{rel}]-t
    RETURN t
    """
    .replaceAllLiterally("{ref}", start.id.toString)
    .replaceAllLiterally("{rel}", rel)
  }

  def start1Match1WhereNotWithRelationship1 (start: Model[_], rel: String, rel2: String): String = {
    """
    START x=node({ref})
    MATCH x-[:{rel}]-t
    WHERE not (t-[:{rel2}]-())
    RETURN t
    """
    .replaceAllLiterally("{ref}", start.id.toString)
    .replaceAllLiterally("{rel}", rel)
    .replaceAllLiterally("{rel2}", rel2)
  }

  def start1Match1WhereNotWithRelationshipOrIs (start: Model[_], clause: Model[_], rel: String, rel2: String): String = {
    """
    START x=node({ref})
    MATCH x-[:{rel}]-t
    WHERE not (t-[:{rel2}]-() or t.id={ref2})
    RETURN t
    """
    .replaceAllLiterally("{ref}", start.id.toString)
    .replaceAllLiterally("{ref2}", clause.id.toString)
    .replaceAllLiterally("{rel}", rel)
    .replaceAllLiterally("{rel2}", rel2)
  }
}
