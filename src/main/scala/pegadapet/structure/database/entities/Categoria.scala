package pegadapet.structure.database.entities

import java.time.OffsetDateTime

case class Categoria(
                    id: Int,
                    nome: String,
                    dataInclusao: OffsetDateTime,
                    dataAlteracao: Option[OffsetDateTime] = None
                    )
