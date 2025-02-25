import scala.io.Source
import reader.service.CsvParser

object MainApp {
  def main(args: Array[String]): Unit = {

    given splitter: String = ","

    // Акции СБЕР
    var csvData = Source.fromFile("src/main/scala/resources/SBER.csv").mkString
    var parser = new CsvParser
    var result = parser.parse(csvData)
    println(s"Акции СБЕР: $result")

    // Ключевая ставка ЦБ РФ
    csvData = Source.fromFile("src/main/scala/resources/KeyRate.csv").mkString
    parser = new CsvParser
    result = parser.parse(csvData)
    println(s"Ключевая ставка ЦБ РФ: $result")
  }
}