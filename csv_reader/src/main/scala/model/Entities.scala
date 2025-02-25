package reader.model

enum Entities {
  case CANDLES
  case VALUES
  case UNDEFINED
}

object Entities {
  def fromString(list: List[String]): Entities =
    list match
      case x if x.length == 2 => VALUES
      case x if x.length == 8 => CANDLES
      case _                  => UNDEFINED
}
