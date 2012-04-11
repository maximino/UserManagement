package controllers

import play.api.mvc._
import play.api.libs.json.Json._
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
      "username" -> nonEmptyText,
      "accept" -> checked("You must accept the conditions")
    )
    {
      //Binding: Create User from mapping result
      (username: String, _) => User(null.asInstanceOf[Int], username)
    }
    {
      //Unbinding:Create the mapping values from an existing User value
      user => Some(user.username, false)
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
      (user:User)=> Ok(toJson(user.save))
    )
    Ok(html.users.index(""))
  }

  def delete(id: Long) = TODO

}
