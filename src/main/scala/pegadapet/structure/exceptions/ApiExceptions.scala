package pegadapet.structure.exceptions

import akka.http.scaladsl.model.{StatusCode, StatusCodes}

import scala.util.control.NoStackTrace

sealed trait CommonType
sealed trait ApiExceptions extends Exception with NoStackTrace

final case class FieldErrorInfo(field: String, message: String) extends CommonType
final case class ErrorResponse(`type`: String, title: String, detail: String, violations: Option[Seq[FieldErrorInfo]] = None) extends CommonType

sealed trait DefaultException extends ApiExceptions {
  val status: StatusCode
  val title: String
  val detail: String
  val violations: Option[Seq[FieldErrorInfo]] = None
}

case object ErroInterno extends DefaultException {
  val status: StatusCode = StatusCodes.InternalServerError
  val title: String      = "Erro interno"
  val detail: String     = "Ocorreu um erro interno no servidor!"
}

case class CadastroNaoEncontrado(nome:String) extends DefaultException {
  val status: StatusCode = StatusCodes.BadRequest
  val title: String      = s"Consulta produto"
  val detail: String     = "Não foi possivel encontrar o registro informado!"
}