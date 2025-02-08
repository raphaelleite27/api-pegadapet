package pegadapet.structure.payload

object TypeResponses {
  case class ItemProduto(
                          id: Int,
                          nome: String,
                          estoque: Int,
                          preco: BigDecimal,
                          categoriaId: Int,
                          createdAt: String,
                          updatedAt: String
                        )

  case class ItemCategoria(
                          id: Int,
                          nome: String,
                          createdAt: String,
                          updatedAt: String
                        )

  case class CreatedId(id: Int)
}
