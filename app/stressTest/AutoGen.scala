package stressTest

import io.Source
import models.User
import play.api.mvc._


/**
 * ndidialaneme
 */

object AutoGen extends Controller{

  def generate = Action {
    generateUsers()
    linkUsers()
    Redirect(controllers.routes.Users.index())
  }

  def generateUsers() {
    Source.fromFile("app/stressTest/RandomNames100.csv").getLines().foreach(
      User(null.asInstanceOf[Long], _).save
    )
  }

  def linkUsers(){
    User.usersNotSupervising.foreach(rootUser =>
      User.usersNotSupervisingExcept(rootUser).take(2).foreach(user =>
        rootUser.addSupervisee(user)
      )
    )
  }
}
