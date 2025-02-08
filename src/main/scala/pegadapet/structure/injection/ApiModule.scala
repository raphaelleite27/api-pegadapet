package pegadapet.structure.injection

import akka.actor.typed.ActorSystem
import akka.http.scaladsl.model.{HttpHeader, HttpMethods}
import akka.http.scaladsl.model.headers.{`Access-Control-Allow-Headers`, `Access-Control-Allow-Methods`, `Access-Control-Allow-Origin`}
import com.typesafe.config.Config
import pegadapet.structure.database.FactoryRepository

class ApiModule(implicit val system: ActorSystem[_]) extends ConfigModule {
  val config: Config = jvmConfig.withFallback(fileConfig)
  val pgsql: FactoryRepository = FactoryRepository(config)
  val headers: List[HttpHeader] = List(
    `Access-Control-Allow-Origin`.*,
    `Access-Control-Allow-Methods`(HttpMethods.GET, HttpMethods.POST, HttpMethods.PUT, HttpMethods.DELETE, HttpMethods.OPTIONS),
    `Access-Control-Allow-Headers`("Authorization", "Content-Type")
  )
}

object ApiModule {
  def apply()(implicit system: ActorSystem[_]): ApiModule = new ApiModule()
}
