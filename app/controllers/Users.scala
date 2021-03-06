package controllers

import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import views._
import controllers.Application.Secured
import models._
import play.api.libs.json.Format

/**
 * ndidialaneme
 */

object Users extends Controller with Secured{

  def index = Action {
    Ok(html.users.index(User.getAllUsers))
  }

  def create = Action {
    Ok(html.users.create(userByNameForm))
  }

  def submit = Action { implicit request =>
    userByNameForm.bindFromRequest.fold(
      formWithErrors => BadRequest(html.users.create(formWithErrors)),
      (user:User)=> detailResult(user.save)
    )
  }

  def roles(id: Long) = Action {
    val user = User.getUserById(id)
    Ok(html.users.roles("Add Roles", user, Role.getAllButCurrentRolesForUser(user), formByIdRole))
  }

  def rolesSubmit(id: Long) = Action { implicit request =>
    formByIdRole.bindFromRequest.fold(
      errors => BadRequest("Whoops"),
      role => detailResult((User.getUserById(id).addRole(role)))
    )
  }

  def detail(id: Long)= Action{
    detailResult(User.getUserById(id))
  }

  def supervisor(id: Long) = Action {
    val user = User.getUserById(id)
    Ok(html.users.supervise("Add Supervisee", user, User.getAllUsersButThisUserAndSuperviseRelationships(user), formByIdUser))
  }

  def superviseSubmit(id: Long) = Action { implicit request =>
    formByIdUser.bindFromRequest.fold(
      errors => BadRequest("Whoops"),
      user => detailResult(User.getUserById(id).addSupervisee(user))
    )
  }

  private def detailResult(user: User): Result ={
    User.updateRolesFromSupervisees(user)
    Ok(html.users.detail(user, User.getAllSupervisees(user), Role.getAllRolesForUser(user)))
  }

  val userByNameForm: Form[User] = Form(
    mapping(
      "name" -> nonEmptyText,
      "email" -> nonEmptyText.verifying("This email address is already in use!", email => !(User.getAllUsers map(_.email)).contains(email)),
      "password" -> tuple(
        "main" -> nonEmptyText(minLength = 6),
        "confirm" -> nonEmptyText
      ).verifying("Passwords don't match", passwords => passwords._1 == passwords._2)
    ){
      //Binding: Create User from mapping result
      (name, email, passwords) => User(null.asInstanceOf[Long], name, email, passwords._1)
    }{
     //Unbinding:Create the mapping values from an existing User value
      user => Some(user.name, user.email, (user.password, ""))
    }
  )

  val formByIdUser = byId[User](User.UserFormat)
  val formByIdRole = byId[Role](Role.RoleFormat)

  private def byId[A <: Model[_]](f:Format[A]): Form[A] = Form(
    mapping(
      "id" -> longNumber
    ){
      id => Model.one[A](id)(f)
    }{
      model => Some(model.id)
    }
  )
}