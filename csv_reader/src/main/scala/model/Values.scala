package reader.model

import java.time.LocalDateTime

case class Values(
    end: LocalDateTime,
    value: Double
) {
  override def toString: String = s"$end, $value"
    s"Values[$end, $value]"
}
