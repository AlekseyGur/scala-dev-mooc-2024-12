package reader.service

import java.util.Date
import reader.model.Candles
import reader.model.Values
import reader.model.Entities
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.awt.Window.Type

class CsvParser(using splitter: String) {
  private var fileEntity: Entities = Entities.UNDEFINED
  private val lineSeparator: String = System.lineSeparator()

  def parse[T](input: String) =
    val lines = input.split(lineSeparator)
    val header = lines.head
    fileEntity = Entities.fromString(header.split(splitter).toList)

    // dataLines.map(parser(_).parse).toList
    lines.tail.map(parser.parse).toList

  def DoubleField: Monad[Double, String] = StringField.map(_.toDouble)
  def IntField: Monad[Int, String] = StringField.map(_.toInt)
  def LongField: Monad[Long, String] = StringField.map(_.toLong)
  def BooleanField: Monad[Boolean, String] = StringField.map(_.toBoolean)
  def DateTimeField: Monad[LocalDateTime, String] =
    StringField.map(parseDateTime)
  def parseDateTime(dateString: String): LocalDateTime =
    LocalDateTime.parse(
      dateString,
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    )

  def StringField: Monad[String, String] =
    Monad[String, String] { str =>
      val idx = str.indexOf(splitter)
      if (idx > -1)
        (str.substring(0, idx), str.substring(idx + 1))
      else
        (str, "")
    }

  def parser =
    fileEntity match {
      case Entities.CANDLES =>
        for {
          open <- DoubleField
          close <- DoubleField
          high <- DoubleField
          low <- DoubleField
          value <- DoubleField
          volume <- LongField
          begin <- DateTimeField
          end <- DateTimeField
        } yield Candles(open, close, high, low, value, volume, begin, end)

      case _ =>
        for {
          end <- DateTimeField
          value <- DoubleField
        } yield Values(end, value)
    }
}

object CsvParser {
  def apply(a: String)(using splitter: String): List[Candles | Values] = {
    val p = new CsvParser()
    p.parse(a)
  }
}
