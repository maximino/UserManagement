package utils.dispatch

/**
 * ndidialaneme
 */

import dispatch._
import play.api.libs.json._
import play.api.libs.json.Json._


trait ImplicitPlayJsonHandlers {
  /** Add JSON-processing method >! to dispatch.Request */
  implicit def handlerToPlayJsonHandlers(r: HandlerVerbs) = new PlayJsonHandlers(r)
  implicit def requestToPlayJsonHandlers(r: Request) = new PlayJsonHandlers(r)
  implicit def stringToPlayJsonHandlers(r: String) = new PlayJsonHandlers(new Request(r))
}

object PlayJsonDispatchHttp extends ImplicitPlayJsonHandlers

class PlayJsonHandlers(subject: HandlerVerbs) {

  /**Process response as JsValue in block */
  def >![T](block: (JsValue) => T) = subject >- {
    (str) =>
      block(parse(str))
  }
}