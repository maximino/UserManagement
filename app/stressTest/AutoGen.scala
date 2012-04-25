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
    Redirect(controllers.routes.Application.admin())
  }

  def link = Action {
    linkUsers()
    Redirect(controllers.routes.Application.admin())
  }

  def generateUsers() {
    Source.fromFile("app/stressTest/RandomNames10.csv").getLines().foreach(
      User(null.asInstanceOf[Long], _).save
    )
  }

  def linkUsers(){
    User.usersNotSupervising.foreach(_.addSupervisee(User.usersNotSupervising.apply(0)))
  }
}
