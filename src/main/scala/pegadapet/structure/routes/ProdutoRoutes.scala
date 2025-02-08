package pegadapet.structure.routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import pegadapet.structure.adapters.ResponseAdaptor
import pegadapet.structure.database.repository.ProdutoRepository
import pegadapet.structure.directives.{ProdutoDirectives, RestDirectives}
import pegadapet.structure.injection.ApiModule
import pegadapet.structure.payload.TypeRequests.ProdutoRequest

class ProdutoRoutes()(implicit module: ApiModule) extends RestDirectives with ProdutoDirectives {

  val repository: ProdutoRepository = module.pgsql.productRepository

  def routes: Route = concat(
    create~ list~ remove
  )

  def create: Route = path("add") {
    post {
      entity(as[ProdutoRequest]) { request =>
        println(request)
        create(request)(repository) { id =>
          complete(StatusCodes.Created, module.headers, ResponseAdaptor.toCreatedId(id))
        }
      }
    }
  }

  def list: Route = path("list") {
    get {
      listAll(repository) { produtos =>
        complete(StatusCodes.OK, module.headers, produtos.map(ResponseAdaptor.toItemProduto))
      }
    }
  }

  def remove: Route = path("remove" / IntNumber) { produtoId =>
    delete {
      verifyExistsProduto(produtoId)(repository) {
        remove(produtoId)(repository) {
          complete(StatusCodes.OK, module.headers, ResponseAdaptor.toCreatedId(produtoId))
        }
      }
    }
  }
}

object ProdutoRoutes {
  def apply()(implicit module: ApiModule): Route = new ProdutoRoutes().routes
}
