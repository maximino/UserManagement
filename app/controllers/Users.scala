package controllers

import play.api.mvc._
import play.api.libs.json.Json._
import play.api.data._
import play.api.data.Forms._

import views._
import models._
/**
 * ndidialaneme
 */

object Users extends Controller {

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
    Ok(html.users.index(User.getAllUsers))
  }

  def create = Action {
    Ok(html.users.create(newUserForm));
  }

  def submit = Action { implicit request =>
    newUserForm.bindFromRequest.fold(
      errors => BadRequest(html.users.create(errors)),
      (user:User)=> Ok(toJson(user.save))
    )
    Redirect("/admin/users")
  }

  def detail(id: Long)= Action{
    User.getUserById(id).map( u =>
      Ok(views.html.users.detail(u))
    ).getOrElse(NotFound)
  }
  def delete(id: Long) = TODO

  def supervisor(id: Long) = Action {
    Ok(views.html.users.supervise("Add Supervisor", User.getUserById(id).get, User.getAllButThisUser(id)))
  }

//  def addSupervisor(uId: Long, sId: Long) = TODO
}
