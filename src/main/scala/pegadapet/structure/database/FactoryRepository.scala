package pegadapet.structure.database

import com.typesafe.config.Config
import pegadapet.structure.database.repository.{CategoriaRepository, ProdutoRepository}

class FactoryRepository(config: Config){
  private val connector: Connector = Connector(config)
  implicit val productRepository: ProdutoRepository = ProdutoRepository()(connector)
  implicit val categoryRepository: CategoriaRepository = CategoriaRepository()(connector)
}

object FactoryRepository {
  def apply(config: Config): FactoryRepository = new FactoryRepository(config)
}
