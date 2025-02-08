package pegadapet.structure.routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import pegadapet.structure.adapters.ResponseAdaptor
import pegadapet.structure.database.repository.CategoriaRepository
import pegadapet.structure.directives.{CategoriaDirectives, RestDirectives}
import pegadapet.structure.injection.ApiModule
import pegadapet.structure.payload.TypeRequests.CategoriaRequest

class CategoriaRoutes()(implicit module: ApiModule) extends RestDirectives with CategoriaDirectives {

  val repository: CategoriaRepository = module.pgsql.categoryRepository

  def routes: Route = concat(
    create~ list~ remove
  )

  def create: Route = path("add") {
    post {
      entity(as[CategoriaRequest]) { request =>
        println(request)
        create(request)(repository) { id =>
          complete(StatusCodes.Created, module.headers, ResponseAdaptor.toCreatedId(id))
        }
      }
    }
  }

  def list: Route = path("list") {
    get {
      listAll(repository) { categorias =>
        complete(StatusCodes.OK, module.headers, categorias.map(ResponseAdaptor.toItemCategoria))
      }
    }
  }

  def remove: Route = path("remove" / IntNumber) { categoriaId =>
    delete {
      verifyExistsCategoria(categoriaId)(repository) {
        remove(categoriaId)(repository) {
          complete(StatusCodes.OK, module.headers, ResponseAdaptor.toCreatedId(categoriaId))
        }
      }
    }
  }
}

object CategoriaRoutes {
  def apply()(implicit module: ApiModule): Route = new CategoriaRoutes().routes
}
