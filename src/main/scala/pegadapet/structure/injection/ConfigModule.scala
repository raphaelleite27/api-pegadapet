package pegadapet.structure.injection

import akka.actor.typed.ActorSystem
import com.typesafe.config.{Config, ConfigFactory}

trait ConfigModule {
  val system: ActorSystem[_]
  val jvmConfig : Config        = ConfigFactory.systemProperties()
  val fileConfig: Config        = system.settings.config
}
