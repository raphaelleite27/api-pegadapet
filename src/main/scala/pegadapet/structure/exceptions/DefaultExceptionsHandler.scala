package pegadapet.structure.exceptions

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.model.StatusCodes.{BadRequest, MethodNotAllowed, NotFound}
import akka.http.scaladsl.server.Directives.complete
import akka.http.scaladsl.server._
import de.heikoseeberger.akkahttpjson4s.Json4sSupport
import org.json4s.{Formats, Serialization}
import org.slf4j.Logger

trait DefaultExceptionsHandler extends Json4sSupport{

  val logger: Logger
  implicit val formats      : Formats
  implicit val serialization: Serialization

  implicit val rejectionHandler: RejectionHandler =
      RejectionHandler
        .newBuilder()
        .handle {
          case MalformedRequestContentRejection(msg, _) =>
            complete(BadRequest, ErrorResponse("rejection", "malformed", msg))
          case ValidationRejection(msg, _) =>
            complete(BadRequest, ErrorResponse("rejection", "validation", msg))
        }
        .handleAll[MethodRejection] { methodRejections =>
          val names = methodRejections.map(_.supported.name)
          complete(MethodNotAllowed, ErrorResponse("rejection", "invalid method", s"Supports: ${names.mkString(" or ")}"))
        }
        .handleNotFound(complete((NotFound, "Not here!")))
        .result()


  implicit val exceptionHandler = ExceptionHandler {
          case r: DefaultException =>
            complete(r.status, ErrorResponse("default", r.title, r.detail, r.violations))
          case e =>
            logger.error("failure", e)
            complete(StatusCodes.InternalServerError, ErrorResponse("ErroNoSistema", "Erro generico", "Contactar o administrador"))
        }

}
