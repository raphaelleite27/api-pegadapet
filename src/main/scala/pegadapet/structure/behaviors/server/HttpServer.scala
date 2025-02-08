package pegadapet.structure.behaviors.server

import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorSystem, Behavior}
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.server.Route
import org.slf4j.LoggerFactory
import pegadapet.structure.injection.ApiModule
import pegadapet.structure.routes.MainRoutes

import scala.concurrent.Future
import scala.util.{Failure, Success}

object HttpServer {

  sealed trait Command

  private case object ServerBind extends Command
  private case class ServerConnected(server: ServerBinding) extends Command
  private case class ServerFailConnect(throwable: Throwable) extends Command
  case class ServerShutdown(server: Option[ServerBinding] = None) extends Command

  private val logger = LoggerFactory.getLogger(getClass)

  def apply(): Behavior[Command] = {
    Behaviors.setup[Command] { ctx =>

      implicit val system: ActorSystem[_] = ctx.system
      implicit val module: ApiModule = ApiModule()

      ctx.self ! ServerBind

      Behaviors.receiveMessage{
        case ServerBind =>
          ctx.pipeToSelf(bind(MainRoutes())) {
            case Failure(e)      => ServerFailConnect(e)
            case Success(server) => ServerConnected(server)
          }
          Behaviors.same

        case ServerConnected(server: ServerBinding) =>
          logger.info("server started at {}", server.localAddress)
          Behaviors.same

        case ServerFailConnect(exception) =>
          logger.error("fail start server {}", exception)
          shutdown(None)
          Behaviors.same

        case ServerShutdown(server) =>
          shutdown(server)
          Behaviors.stopped
      }
    }
  }

  private def bind(mainRoutes: Route)(implicit system: ActorSystem[_], module: ApiModule): Future[ServerBinding] = {
    val host = module.config.getString("api-pegadapet.server.host")
    val port = module.config.getInt("api-pegadapet.server.port")
    Http().newServerAt(host, port).bind(mainRoutes)
  }
  private def shutdown(serverBind: Option[ServerBinding]): Behavior[Command] = {
    logger.info("server shutdown")
    serverBind.map(server => server.unbind())
    Behaviors.stopped
  }
}
