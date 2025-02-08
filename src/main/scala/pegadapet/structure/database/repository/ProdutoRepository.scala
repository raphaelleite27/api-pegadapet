package pegadapet.structure.database.repository


import doobie._
import doobie.implicits._
import doobie.postgres.implicits._
import pegadapet.structure.database.Connector
import pegadapet.structure.database.entities.Produto
import pegadapet.structure.payload.TypeRequests.ProdutoRequest

import java.util.Date
import scala.concurrent.Future

class ProdutoRepository()(implicit connector: Connector) {

  import connector._

  def insert(request:ProdutoRequest): Future[Int] = {
    val sql = sql"INSERT INTO produtos(nome, preco, estoque, categoria_id, data_inclusao) VALUES (${request.nome}, ${request.preco}, ${request.estoque}, ${request.categoriaId}, ${new Date().toInstant})"

    sql
      .update
      .withUniqueGeneratedKeys[Int]("id")
      .execute
      .unsafeToFuture()
  }

  def find(entityId: Int): Future[Option[Produto]] = {
    val query = "select * from produtos where id = ?"
    Query[Int, Produto](query).option(entityId).execute.unsafeToFuture()
  }

  def list: Future[List[Produto]] = {
    val sql = sql"select id, nome, estoque, preco, categoria_id, data_inclusao, data_alteracao from produtos"

    sql
      .query[Produto]
      .to[List]
      .execute
      .unsafeToFuture()
  }

  def delete(entityId: Int): Future[Int] = {
    val sql = sql"delete from produtos where id = $entityId"
      sql
        .update
        .run
        .execute
        .unsafeToFuture()
  }
}

object ProdutoRepository {
  def apply()(implicit connector: Connector): ProdutoRepository = new ProdutoRepository()
}
