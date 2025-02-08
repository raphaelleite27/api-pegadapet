package pegadapet.structure.adapters

import pegadapet.structure.database.entities.{Categoria, Produto}
import pegadapet.structure.payload.TypeResponses.{CreatedId, ItemCategoria, ItemProduto}
import pegadapet.structure.utils.DateUtils

import java.util.Date
import scala.math.BigDecimal.RoundingMode

object ResponseAdaptor {
  val formatDate = "yyyy-MM-dd HH:mm:ss"

  def toItemProduto(p: Produto): ItemProduto = ItemProduto(
    id = p.id,
    nome = p.nome,
    estoque = p.estoque,
    preco = p.preco.setScale(2, RoundingMode.HALF_UP),
    categoriaId = p.categoriaId,
    createdAt =  DateUtils.format(formatDate, Date.from(p.dataInclusao.toInstant)),
    updatedAt = p.dataAlteracao.fold("")(dt => DateUtils.format(formatDate, Date.from(dt.toInstant)))
  )

  def toItemCategoria(p: Categoria): ItemCategoria = ItemCategoria(
    id = p.id,
    nome = p.nome,
    createdAt =  DateUtils.format(formatDate, Date.from(p.dataInclusao.toInstant)),
    updatedAt = p.dataAlteracao.fold("")(dt => DateUtils.format(formatDate, Date.from(dt.toInstant)))
  )

  def toCreatedId(id: Int) = CreatedId(id)
}
