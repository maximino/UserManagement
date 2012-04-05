package models

import org.neo4j.graphdb.RelationshipType


/**
 * ndidialaneme
 */

abstract class RelTypes extends RelationshipType
case object REF_USER extends RelTypes{
  override def name()= {
    "REF_USER"
  }
}

case object A_USER extends RelTypes{
  override def name()= {
    "A_USER"
  }
}

case object MEMBER_OF extends RelTypes{
  override def name()= {
    "MEMBER_OF"
  }
}

case object PART_OF extends RelTypes{
  override def name()= {
    "PART_OF"
  }
}

case object SUPERVISES extends RelTypes{
  override def name()= {
    "SUPERVISES"
  }
}