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

  def roles(id: Long) = TODO

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
  val newUserForm: Form[User] = Form(
    mapping(
      "name" -> nonEmptyText,
      "accept" -> checked("You must accept the conditions")
    )
    {
      //Binding: Create User from mapping result
      (name: String, _) => User(null.asInstanceOf[Int], name)
    }
    {
     //Unbinding:Create the mapping values from an existing User value
      user => Some(user.name, false)
    }
    .verifying("This name is not available",user => !Seq("admin", "guest").contains(user.name))
  )
}
