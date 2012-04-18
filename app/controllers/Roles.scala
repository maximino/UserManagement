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

object Roles extends Controller {

  def index = Action {
    Ok(html.roles.index(Role.getAllRoles))
  }

  def create = Action {
    Ok(html.roles.create(newRoleForm))
  }

  def submit = Action { implicit request =>
    newRoleForm.bindFromRequest.fold(
      errors => BadRequest(html.roles.create(errors)),
      (role:Role)=> Ok(toJson(role.save))
    )
    Redirect("/admin/roles")
  }

  def detail(id: Long)= TODO /*Action{
	Ok(views.html.roles.detail(Role.getRoleById(id)))
  }*/


  val newRoleForm: Form[Role] = Form(
    mapping(
      "name" -> nonEmptyText
    )
    {
      (name: String) => Role(null.asInstanceOf[Int], name)
    }
    {
      role => Some(role.name)
    }
  )
}
