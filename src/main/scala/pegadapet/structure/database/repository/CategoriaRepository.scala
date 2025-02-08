package pegadapet.structure.database.repository

import doobie._
import doobie.implicits._
import doobie.postgres.implicits._
import pegadapet.structure.database.Connector
import pegadapet.structure.database.entities.Categoria
import pegadapet.structure.payload.TypeRequests.CategoriaRequest

import java.util.Date
import scala.concurrent.Future

class CategoriaRepository()(implicit connector: Connector) {

  import connector._

  def insert(request:CategoriaRequest): Future[Int] = {
    val sql = sql"INSERT INTO categorias(nome, data_inclusao) VALUES (${request.nome}, ${new Date().toInstant})"

    sql
      .update
      .withUniqueGeneratedKeys[Int]("id")
      .execute
      .unsafeToFuture()
  }

  def find(entityId: Int): Future[Option[Categoria]] = {
    val query = "select * from categorias where id = ?"
    Query[Int, Categoria](query).option(entityId).execute.unsafeToFuture()
  }

  def list: Future[List[Categoria]] = {
    val sql = sql"select id, nome, data_inclusao, data_alteracao from categorias"

    sql
      .query[Categoria]
      .to[List]
      .execute
      .unsafeToFuture()
  }

  def delete(entityId: Int): Future[Int] = {
    val sql = sql"delete from categorias where id = $entityId"
      sql
        .update
        .run
        .execute
        .unsafeToFuture()
  }
}

object CategoriaRepository {
  def apply()(implicit connector: Connector): CategoriaRepository = new CategoriaRepository()
}
