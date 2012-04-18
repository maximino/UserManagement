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
}
