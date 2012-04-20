package stressTest

import play.api.mvc.{Action, Controller}
import io.Source
import models.User


/**
 * ndidialaneme
 */

object AutoGen extends Controller{

  def generate = Action {
    generateUsers()
    linkUsers()
    Redirect("")
  }

  def link = Action {
    linkUsers()
    Redirect("")
  }

  def generateUsers() {
    for (line <- Source.fromFile("app/stressTest/RandomNames10.csv").getLines()){
      User(null.asInstanceOf[Long], line).save
    }
  }

  def linkUsers(){}
}
