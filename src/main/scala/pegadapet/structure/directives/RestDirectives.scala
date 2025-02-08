package pegadapet.structure.directives

import akka.http.scaladsl.server.directives.{MarshallingDirectives, MethodDirectives, RouteDirectives}
import de.heikoseeberger.akkahttpjson4s.Json4sSupport
import org.json4s.native.Serialization
import org.json4s.{DefaultFormats, Formats, Serialization}

trait RestDirectives extends RouteDirectives with MethodDirectives with Json4sSupport with MarshallingDirectives{

  implicit val formats      : Formats       = DefaultFormats
  implicit val serialization: Serialization = Serialization

}
