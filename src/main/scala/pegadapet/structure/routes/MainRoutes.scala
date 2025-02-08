package pegadapet.structure.routes

import akka.actor.typed.ActorSystem
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.{BasicDirectives, RouteDirectives}
import org.json4s.native.Serialization
import org.json4s.{DefaultFormats, Formats, Serialization}
import org.slf4j.{Logger, LoggerFactory}
import pegadapet.structure.exceptions.DefaultExceptionsHandler
import pegadapet.structure.injection.ApiModule

import scala.concurrent.ExecutionContext

class MainRoutes(implicit system: ActorSystem[_], module: ApiModule) extends DefaultExceptionsHandler with BasicDirectives with RouteDirectives {

  override implicit val formats: Formats = DefaultFormats
  override implicit val serialization: Serialization = Serialization
  override val logger: Logger = LoggerFactory.getLogger(getClass)

  implicit val executor: ExecutionContext = system.executionContext

  def routes(): Route =
        Route.seal{
          options {
            complete(HttpResponse(StatusCodes.OK).withHeaders(module.headers))
          } ~
            pathPrefix("api" / "v1") {
              concat(
                pathPrefix("produto")(ProdutoRoutes()) ~
                  pathPrefix("categoria")(CategoriaRoutes())
              )
            }
        }
}

object MainRoutes {
  def apply()(implicit system: ActorSystem[_], module: ApiModule): Route = new MainRoutes().routes()
}
