package pegadapet.structure.directives

import akka.http.scaladsl.server.directives.BasicDirectives
import akka.http.scaladsl.server.directives.FutureDirectives._
import akka.http.scaladsl.server.{Directive0, Directive1}
import pegadapet.structure.database.entities.Categoria
import pegadapet.structure.database.repository.CategoriaRepository
import pegadapet.structure.exceptions.{CadastroNaoEncontrado, ErroInterno}
import pegadapet.structure.payload.TypeRequests.CategoriaRequest

import scala.util.{Failure, Success}

trait CategoriaDirectives extends BasicDirectives {
  def listAll(categoriaRepository: CategoriaRepository): Directive1[List[Categoria]] = {
    onComplete(categoriaRepository.list).map {
      case Success(categoria) => categoria
      case Failure(_) => List.empty[Categoria]
    }
  }

  def create(request: CategoriaRequest)(implicit categoriaRepository: CategoriaRepository): Directive1[Int] =
    onComplete(categoriaRepository.insert(request)).map {
      case Success(id) => id
      case Failure(exception) =>
        println(exception)
        throw ErroInterno
    }

  def find(id: Int)(implicit categoriaRepository: CategoriaRepository): Directive1[Option[Categoria]] = {
    onComplete(categoriaRepository.find(id)).map {
      case Success(produto) => produto
      case Success(None) => throw CadastroNaoEncontrado("Categoria")
      case Failure(e) =>
        println(e)
        throw ErroInterno
    }
  }

  def remove(id: Int)(implicit categoriaRepository: CategoriaRepository): Directive0 =
    onComplete(categoriaRepository.delete(id)).flatMap {
      case Failure(exception) =>
        println(exception)
        throw ErroInterno
      case Success(id) =>
        println(s"registro excluido da tabela categoria: $id")
        pass
    }

  def verifyExistsCategoria(id: Int)(implicit categoriaRepository: CategoriaRepository): Directive0 = {
    onComplete(categoriaRepository.find(id)).flatMap {
      case Success(None) => throw CadastroNaoEncontrado("Categoria")
      case Success(Some(produto)) => pass
      case Failure(e) =>
        println(e)
        throw ErroInterno
    }
  }
}
