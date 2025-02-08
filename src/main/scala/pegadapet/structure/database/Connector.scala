package pegadapet.structure.database

import cats.effect._
import cats.effect.unsafe.IORuntime
import com.typesafe.config.Config
import com.zaxxer.hikari.HikariDataSource
import doobie.ConnectionIO
import doobie.hikari.HikariTransactor
import doobie.implicits._
import doobie.util.ExecutionContexts
import doobie.util.transactor.Transactor


class Connector(config: Config) {

  implicit val runtime: IORuntime = cats.effect.unsafe.IORuntime.global
  private val numberThreads = config.getInt("api-pegadapet.hikari.maximum-pool-size")

  val transactor: Resource[IO, HikariTransactor[IO]] = for {
    ds <- Resource.pure(createDataSource(config))
    ex <- ExecutionContexts.fixedThreadPool[IO](numberThreads)
  } yield Transactor.fromDataSource[IO](ds,ex)

  def createDataSource(config: Config): HikariDataSource = {
    val hds = new HikariDataSource()
    hds.setPoolName(config.getString("api-pegadapet.hikari.pool-name"))
    hds.setDriverClassName(config.getString("api-pegadapet.hikari.driver"))
    hds.setJdbcUrl(config.getString("api-pegadapet.hikari.jdbc-url"))
    hds.setUsername(config.getString("api-pegadapet.hikari.username"))
    hds.setPassword(config.getString("api-pegadapet.hikari.password"))
    hds.setConnectionTimeout(config.getLong("api-pegadapet.hikari.connection-timeout"))
    hds.setMaxLifetime(config.getLong("api-pegadapet.hikari.maximum-lifetime"))
    hds.setIdleTimeout(config.getLong("api-pegadapet.hikari.idle-timeout"))
    hds.setMaximumPoolSize(config.getInt("api-pegadapet.hikari.maximum-pool-size"))
    hds.setMinimumIdle(config.getInt("api-pegadapet.hikari.minimum-idle"))
    hds.setAutoCommit(config.getBoolean("api-pegadapet.hikari.auto-commit"))
    hds.setReadOnly(config.getBoolean("api-pegadapet.hikari.read-only"))
    hds.setRegisterMbeans(true)
    hds.setLeakDetectionThreshold(10000)
    hds
  }

  implicit class ConnectionIOOps[T](statement: ConnectionIO[T]) {
    def execute: IO[T] = transactor.use(xa => statement.transact(xa))
  }
}

object Connector {
  def apply(config: Config): Connector = new Connector(config)
}
