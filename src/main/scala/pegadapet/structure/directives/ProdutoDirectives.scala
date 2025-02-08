package pegadapet.structure.directives


import akka.http.scaladsl.server.directives.BasicDirectives
import akka.http.scaladsl.server.directives.FutureDirectives._
import akka.http.scaladsl.server.{Directive0, Directive1}
import pegadapet.structure.database.entities.Produto
import pegadapet.structure.database.repository.ProdutoRepository
import pegadapet.structure.exceptions.{CadastroNaoEncontrado, ErroInterno}
import pegadapet.structure.payload.TypeRequests.ProdutoRequest

import scala.util.{Failure, Success}

trait ProdutoDirectives extends BasicDirectives {
  def listAll(produtoRepository: ProdutoRepository): Directive1[List[Produto]] = {
    onComplete(produtoRepository.list).map {
      case Success(produtos) => produtos
      case Failure(_) => List.empty[Produto]
    }
  }

  def create(request: ProdutoRequest)(implicit produtoRepository: ProdutoRepository): Directive1[Int] =
    onComplete(produtoRepository.insert(request)).map {
      case Success(id) => id
      case Failure(exception) =>
        println(exception)
        throw ErroInterno
    }

  def find(id: Int)(implicit produtoRepository: ProdutoRepository): Directive1[Option[Produto]] = {
    onComplete(produtoRepository.find(id)).map {
      case Success(produto) => produto
      case Success(None) => throw CadastroNaoEncontrado("Produto")
      case Failure(e) =>
        println(e)
        throw ErroInterno
    }
  }

  def remove(id: Int)(implicit produtoRepository: ProdutoRepository): Directive0 =
    onComplete(produtoRepository.delete(id)).flatMap {
      case Failure(exception) =>
        println(exception)
        throw ErroInterno
      case Success(id) =>
        println(s"registro excluido da tabela produtos: $id")
        pass
    }

  def verifyExistsProduto(id: Int)(implicit produtoRepository: ProdutoRepository): Directive0 = {
    onComplete(produtoRepository.find(id)).flatMap {
      case Success(None) => throw CadastroNaoEncontrado("Produto")
      case Success(Some(produto)) => pass
      case Failure(e) =>
        println(e)
        throw ErroInterno
    }
  }
}
