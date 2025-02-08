package pegadapet.structure.utils

import java.text.SimpleDateFormat
import java.time.chrono.{ChronoLocalDate, ChronoLocalDateTime}
import java.time.temporal.ChronoUnit
import java.time._
import java.util.{Calendar, Date}
import scala.util.{Failure, Success, Try}

object DateUtils {

  def format(f: String, d: Date): String = new SimpleDateFormat(f).format(d)

  def parse(s: String, dtStr: String): Date = new SimpleDateFormat(s).parse(dtStr)

  def difference(aDate: Date, that: Date): Long = that.getTime - aDate.getTime

  def calculateDate(dtParms: Option[Date] = None, qtdOfDays: Int = 0, hourOfDay: Int = 0, minOfDay: Int = 0, secOfDay: Int = 0): Date = {
    val c =  Calendar.getInstance()
    dtParms.foreach(c.setTime)
    c.add(Calendar.DAY_OF_MONTH, qtdOfDays)
    c.add(Calendar.MINUTE, minOfDay)
    c.add(Calendar.HOUR_OF_DAY, hourOfDay)
    c.add(Calendar.SECOND, secOfDay)
    c.add(Calendar.MILLISECOND, 0)
    c.getTime
  }

  def checkDateExceeded(dt: Date, isMenor: Boolean = true, dtParms: Date): Boolean = {
    dt.compareTo(dtParms) match {
      case -1 => if (isMenor) true else false
      case  0 => false
      case  1 => if (isMenor) false else true
    }
  }

  def todayMidnight(zone: ZoneId = ZoneId.of("America/Campo_Grande")): LocalDateTime = {
    val midnight = LocalTime.MIDNIGHT
    val today    = LocalDate.now(zone)
    LocalDateTime.of(today, midnight).plusDays(1)
  }

  def tomorrowMidnight(zone: ZoneId = ZoneId.of("America/Campo_Grande")): LocalDateTime =
    todayMidnight(zone).plusDays(1)

  /**
   * Implicit class for useful extension methods to java.util.Date
   */
  implicit final class DateOpts(private val self: Date) extends AnyVal {

    /**
     * Compare if this Date is between the init and end inclusive.
     */
    def isBetween(init: Date, end: Date): Boolean =
      self.compareTo(init) >= 0 && self.compareTo(end) <= 0

    def unitsFrom(date: Date, unit: ChronoUnit): Long =
      date.toInstant.until(self.toInstant, unit)
  }

  /**
   * Implicit class for useful extension methods to java.time.LocalDateTime
   */
  implicit final class ChronoLocalDateTimeOps[A <: ChronoLocalDate](private val self: ChronoLocalDateTime[A])
    extends AnyVal {

    /**
     * Compare if this Date is between the init and end inclusive.
     */
    def isBetween(init: ChronoLocalDateTime[A], end: ChronoLocalDateTime[A]): Boolean =
      self.compareTo(init) >= 0 && self.compareTo(end) <= 0

    def unitsFrom(
                   date: ChronoLocalDateTime[A],
                   unit: ChronoUnit,
                   zoneOffset: ZoneOffset = ZoneOffset.of("America/Campo_Grande")
                 ): Long =
      date.toInstant(zoneOffset).until(self.toInstant(zoneOffset), unit)
  }

  def isValid(f: String, dtStr: String): Boolean = Try(parse(f, dtStr)) match {
    case Success(_) => true
    case Failure(_) => false
  }

}
