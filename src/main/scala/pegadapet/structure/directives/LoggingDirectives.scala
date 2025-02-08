package pegadapet.structure.directives

import akka.http.scaladsl.model.{HttpEntity, HttpRequest}
import akka.http.scaladsl.server.{Directive0, RouteResult}
import akka.http.scaladsl.server.directives.{DebuggingDirectives, LoggingMagnet}
import org.slf4j.Logger

trait LoggingDirectives {

    val logger: Logger

    def logPersonalized: Directive0 = DebuggingDirectives.logRequestResult(LoggingMagnet(_ => sourceLogger))

    def sourceLogger(request: HttpRequest): RouteResult => Unit = {
        case RouteResult.Complete(response)   =>
            (request.entity, response.entity) match {
                case (HttpEntity.Strict(_, rq), HttpEntity.Strict(_, rp)) =>
                    logger.info("source request - "+
                        s"path: \"${request.uri.path.toString()}\", " +
                      s"method: \"${request.method.name}\", " +
                      s"status: \"${response.status.toString}\", " +
                     s"request: \"${rq.utf8String}\", "+
                    s"response: \"${ rp.utf8String}\"")
                case _                                                    =>
                    logger.info("source request - "+
                        s"path: \"${request.uri.path.toString()}\", " +
                      s"method: \"${request.method.name}\", " +
                      s"status: \"${response.status.toString}\"")
            }

        case RouteResult.Rejected(rejections) =>
            logger.info(s"rejections: \"${rejections.mkString(", ")}\"")
            request.entity match {
                case HttpEntity.Strict(_, rq) =>
                    logger.info("source request - "+
                        s"path: \"${request.uri.path.toString()}\", " +
                      s"method: \"${request.method.name}\", " +
                     s"request: \"${rq.utf8String}\"")
                case _                        =>
                    logger.info("source request - "+
                        s"path: \"${request.uri.path.toString()}\", " +
                      s"method: \"${request.method.name}\"")
            }
    }

}
