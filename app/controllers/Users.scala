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
    Ok(html.users.create(userByNameForm));
  }

  def submit = Action { implicit request =>
    userByNameForm.bindFromRequest.fold(
      errors => BadRequest(html.users.create(errors)),
      (user:User)=> Ok(toJson(user.save))
    )
    Redirect("/admin/users")
  }

  def roles(id: Long) = Action {
    Ok(html.users.roles("Add Roles", User.getUserById(id), Role.getAllButCurrentRolesForUser(id), newRoleForm))
  }

  def rolesSubmit(id: Long) = Action { implicit request =>
    newRoleForm.bindFromRequest.fold(
      errors => BadRequest("Whoops"),
      role => Ok(html.users.detail(User.getUserById(id).addRole(role)))
    )
  }

  def detail(id: Long)= Action{
    Ok(html.users.detail(User.getUserById(id)))
  }

  def supervisor(id: Long) = Action {
    Ok(html.users.supervise("Add Supervisee", User.getUserById(id), User.getAllUsersButThisUserAndSuperviseRelationships(id), newByIdForm))
  }

  def superviseSubmit(id: Long) = Action { implicit request =>
    newByIdForm.bindFromRequest.fold(
      errors => BadRequest("Whoops"),
      user => Ok(html.users.detail(User.getUserById(id).addSupervisee(user)))
    )
  }

  val userByNameForm: Form[User] = Form(
    mapping(
      "name" -> nonEmptyText
    ){
      //Binding: Create User from mapping result
      (name: String) => User(null.asInstanceOf[Int], name)
    }{
     //Unbinding:Create the mapping values from an existing User value
      user => Some(user.name)
    }
    .verifying("This name is not available",user => !Seq("admin", "guest").contains(user.name))
  )

  val newByIdForm: Form[User] = Form(
    mapping(
      "id" -> longNumber
    ){
      (id) => User.getUserById(id)
    }{
      (user) => Some(user.id)
    }
  )

  val newRoleForm: Form[Role] = Form(
    mapping(
      "id" -> longNumber
    ){
      (id) => Model.one[Role](id)
    }{
      (role) => Some(role.id)
    }
  )

}