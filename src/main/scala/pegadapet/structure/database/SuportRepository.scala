package pegadapet.structure.database

import scala.concurrent.Future

trait SuportRepository[T] {
  def save(entity: T): Future[_]
  def find(entityId: Int): Future[Option[T]]
  def list: Future[List[T]]
}
