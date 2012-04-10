package controllers

import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import models.User
import views._

/**
 * ndidialaneme
 */

object Users extends Controller {

  //User
  val newUserForm: Form[User] = Form(
    mapping(
      "username" -> text(minLength = 4),
      "email" -> email,
      "password" -> tuple(
        "main" -> text(minLength = 6),
        "confirm" -> text
      ).verifying("Passwords don't match", passwords => passwords._1 == passwords._2),
      "accept" -> checked("You must accept the conditions")
    )
    {
      //Binding: Create User from mapping result
      (username, email, passwords, _) => User(username, passwords._1, email)
    }
    {
      //Unbinding:Create the mapping values from an existing User value
      user => Some(user.username, user.email, (user.password, ""), false)
    }.verifying("This username is not available",user => !Seq("admin", "guest").contains(user.username))
  )

  def index = Action {
    Ok(html.users.index(""))
  }

  def create = Action {
    Ok(html.users.create(newUserForm));
  }

  def submit = Action { implicit request =>
    newUserForm.bindFromRequest.fold(
      errors => BadRequest(html.users.create(errors)),
      user => Ok(html.users.create(newUserForm))
    )
  }

  def delete(id: Long) = TODO

}
