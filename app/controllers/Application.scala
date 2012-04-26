package controllers

import play.api.mvc._

import views._
import models.User
import play.api.data.Form
import play.api.data.Forms._

object Application extends Controller {

  def index = Action {
    Ok(html.index(""))
  }

  def admin = Action {
    Ok(html.admin(""))
  }

  val loginForm = Form(
    tuple(
      "email" -> text,
      "password" -> text
    ).verifying ("Invalid email or password", result => result match {
      case (email, password) => User.authenticate(email, password).isDefined
    })
  )

  def login = Action { implicit request =>
    Ok(html.login(loginForm))
  }

  def authenticate = Action { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => BadRequest(html.login(formWithErrors)),
      user => Redirect(routes.Users.index()).withSession("email" -> user._1)
    )
  }

  trait Secured {
    private def email(request: RequestHeader) = request.session.get("email")

    private def onUnauthorized(request: RequestHeader) = Results.Redirect(routes.Application.login())

    def IsAuthenticated(f: => String => Request[AnyContent] => Result) = Security.Authenticated(email, onUnauthorized) { user =>
      Action(request => f(user)(request))
    }
  }
}