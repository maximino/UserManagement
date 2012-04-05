package controllers

import play.api._
import play.api.mvc._

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def admin = TODO

  def roles = TODO
  def newRole = TODO
  def deleteRole(id: Long) = TODO

  def users = TODO
  def newUser = TODO
  def deleteUser(id: Long) = TODO

}