package com.whitechno.explore.json4s

//
// SerDe (JSON serialization/deserialization)
//

trait JsonSerDe {
  def toJSON[A <: AnyRef](a: A): String      //FIXME: get rid of `<: AnyRef`
  def fromJSON[A: Manifest](json: String): A //FIXME: get rid of `: Manifest`
  def extractSubJSON(json: String, sub: String): String
}

// Json4s implementation of SerDe

trait Json4sSerDe extends JsonSerDe {
  import org.json4s.{ DefaultFormats, Formats, Serialization }

  // jackson.Serialization or native.Serialization
  implicit val serialization: Serialization
  implicit val formats: Formats                       = DefaultFormats
  override def toJSON[A <: AnyRef](a: A): String      = serialization.write[A](a)
  override def fromJSON[A: Manifest](json: String): A = serialization.read[A](json)
}

// Json4sJackson SerDe implementation
trait Json4sJacksonSerDe extends Json4sSerDe {
  import org.json4s.{ Serialization, jackson }
  implicit val serialization: Serialization = jackson.Serialization

  import org.json4s.jackson.JsonMethods.{ compact, parse }
  def extractSubJSON(json: String, sub: String): String = compact(parse(json) \ sub)
}
object Json4sJacksonSerDe extends Json4sJacksonSerDe

//
// SerDer (JSON serializer/deserializer for SimpleRestAPI)
//

trait JsonSerDerApi {
  implicit val serde: JsonSerDe
  protected def fromJSON[A: Manifest](json: String)(implicit serde: JsonSerDe): A =
    serde.fromJSON[A](json) //: Manifest
  protected def extractSubJSON(json: String, sub: String)(implicit serde: JsonSerDe): String =
    serde.extractSubJSON(json, sub)
}
//companion object with implicit class enhanced `toJSON` method
object JsonSerDerApi {
  implicit class ToJSON[A <: AnyRef](val a: A) extends AnyVal {
    def toJSON(implicit serde: JsonSerDe): String = serde.toJSON[A](a)
  }
}
// Json4sJackson SerDer implementation
trait Json4sJacksonSerDerApi extends JsonSerDerApi {
  implicit val serde: JsonSerDe = Json4sJacksonSerDe
}
