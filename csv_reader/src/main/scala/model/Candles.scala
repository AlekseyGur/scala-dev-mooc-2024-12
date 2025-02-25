package reader.model

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

case class Candles(
    open: Double,
    close: Double,
    high: Double,
    low: Double,
    value: Double,
    volume: Long,
    begin: LocalDateTime,
    end: LocalDateTime
) {
  override def toString: String =
    s"Candles[$open, $close, $high, $low, $value, $volume, $begin, $end]"
}