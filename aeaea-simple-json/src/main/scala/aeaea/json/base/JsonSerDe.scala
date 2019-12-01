package aeaea.json.base

trait JsonSerDe {
  def fromJSON[A](json: String)(implicit jd: JsonDeserializer[A]): A = jd(json)
}

object JsonSerDe extends JsonSerDe {
  //def fromJSON[A](json: String)(implicit jd: JsonDeserializer[A]): A = jd(json)

  implicit class ToJSON[A](val a: A) extends AnyVal {
    def toJSON(implicit js: JsonSerializer[A]): String = js(a)
  }

}

/**
  * Utility wrapper class for json string
  * @param json
  */
class FromJSON(val json: String) {
  def to[A](implicit jd: JsonDeserializer[A]): A = jd(json)
}

object FromJSON {
  def apply(json: String): FromJSON = new FromJSON(json)
}
