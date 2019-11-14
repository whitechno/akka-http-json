package com.whitechno.explore.json4s

import org.json4s.{ Formats, Serialization }
import org.json4s.{ DefaultFormats, jackson }

// SerDe

object Json4sJacksonSerDe extends Json4sJacksonSerDe

trait Json4sJacksonSerDe extends Json4sSerDe {
  implicit val serialization = jackson.Serialization // or native.Serialization
}

trait Json4sSerDe extends JsonSerDe {
  implicit val serialization: Serialization
  implicit val formats: Formats                       = DefaultFormats
  override def toJSON[A <: AnyRef](a: A): String      = serialization.write[A](a)
  override def fromJSON[A: Manifest](json: String): A = serialization.read[A](json)
}

trait JsonSerDe {
  def toJSON[A <: AnyRef](a: A): String
  def fromJSON[A: Manifest](json: String): A
}

// SerDer

object JsonSerDer {
  implicit class ToJSON[T <: AnyRef](val req: T) extends AnyVal {
    def toJSON(implicit serde: JsonSerDe): String = serde.toJSON[T](req)
  }
}
trait JsonSerDer {
  implicit val serde: JsonSerDe
  def fromJSON[T: Manifest](json: String): T = serde.fromJSON[T](json)
}
trait Json4sJacksonSerDer extends JsonSerDer {
  implicit val serde = Json4sJacksonSerDe
}
object Json4sJacksonSerDer extends Json4sJacksonSerDer {
  implicit class ToJSON[T <: AnyRef](val req: T) extends AnyVal {
    def toJSON: String = serde.toJSON[T](req)
  }
}
