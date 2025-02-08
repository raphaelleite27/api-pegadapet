import akka.actor.typed.ActorSystem
import pegadapet.structure.behaviors.server.HttpServer
import pegadapet.structure.behaviors.server.HttpServer._

object Start extends App {

  val system = ActorSystem(HttpServer(), "api-pegadapet")

  sys addShutdownHook{
    system ! ServerShutdown()
  }
}
