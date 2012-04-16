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

  def match1Where1 (start: Model[_], clause: Model[_],rel: String): String = {
    """
      start x=node({ref})
      match x-[:{rel}]->t
      where not(t.id ={ref2})
      return t
      """
      .replaceAllLiterally("{ref}", start.id.toString)
      .replaceAllLiterally("{ref2}", clause.id.toString)
      .replaceAllLiterally("{rel}", rel)
  }
}
