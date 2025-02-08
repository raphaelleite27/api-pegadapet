package pegadapet.structure.payload

object TypeRequests{
  case class ProdutoRequest(
                           nome: String,
                           estoque: Int,
                           preco: BigDecimal,
                           categoriaId: Int
                         )

  case class CategoriaRequest(nome: String)
}
