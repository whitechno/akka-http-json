package aeaea.explore.unmarshal

object SimpleRestApiApp extends App {
  import API0._
  //import Json4sJacksonUnmarshaller._

  val reqJSON = """{}"""
  val req     = reqFromJSON(reqJSON)
  println(reqJSON)
  println(req)
  println()
}

trait SimpleRestApi {
  type Req

  def reqFromJSON(json: String)(implicit um: Unmarshaller[Req]): Req = um(json)
}

object API0 extends SimpleRestApi with Json4sJacksonUnmarshaller {
  final case class Req()

}

// ******************************************

object ExploreUnmarshalApp extends App {
  import Json4sJacksonUnmarshaller._
  final case class Foo(bar: String)
  val json = """{ "bar": "bar" }"""

  val foo = FromJSON(json).to[Foo]
  println(foo)
}

// Interface

trait Unmarshaller[A] {
  def apply(json: String): A
}

object Unmarshaller {
  def apply[A](f: String => A): Unmarshaller[A] =
    (json: String) => f(json)
}

case class FromJSON(json: String) {
  def to[A](implicit um: Unmarshaller[A]): A = um(json)
}

// Implementation with Json4s Jackson

trait Json4sUnmarshaller {
  import org.json4s.{ DefaultFormats, Formats, Serialization }
  protected implicit val formats: Formats = DefaultFormats
  protected implicit val serialization: Serialization

  implicit def unmarshaller[A: Manifest]: Unmarshaller[A] =
    Unmarshaller { s =>
      serialization.read(s)
    }
}

trait Json4sJacksonUnmarshaller extends Json4sUnmarshaller {
  import org.json4s.{ Serialization, jackson }
  protected implicit val serialization: Serialization = jackson.Serialization
}
object Json4sJacksonUnmarshaller extends Json4sJacksonUnmarshaller
