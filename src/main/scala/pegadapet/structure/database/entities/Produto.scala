package pegadapet.structure.database.entities

import java.time.OffsetDateTime

case class Produto(
                    id: Int,
                    nome: String,
                    estoque: Int,
                    preco: BigDecimal,
                    categoriaId: Int,
                    dataInclusao: OffsetDateTime,
                    dataAlteracao: Option[OffsetDateTime] = None
                  )
