package aeaea.json.json4s

import aeaea.json.base.{ JsonDeserializer, JsonSerializer }

// FIXME: Split jackson and native implementation into two different modules
//        in order to reduce lib dependencies.

/**
  * Factorfy implementation of JsonSerializer and JsonDeserializer
  * with org.json4s.jackson and org.json4s.native
  */
trait WithJson4s {
  import org.json4s.{ DefaultFormats, Formats, Serialization }
  protected implicit val formats: Formats = DefaultFormats
  protected implicit val serialization: Serialization

  implicit def jd[A: Manifest]: JsonDeserializer[A] =
    JsonDeserializer { s =>
      serialization.read(s)
    }

  implicit def js[A <: AnyRef]: JsonSerializer[A] =
    JsonSerializer { serialization.write[A] }
}

// jackson
trait WithJson4sJackson extends WithJson4s {
  import org.json4s.{ Serialization, jackson }
  protected implicit val serialization: Serialization = jackson.Serialization
}
object WithJson4sJackson extends WithJson4sJackson

// native
trait WithJson4sNative extends WithJson4s {
  import org.json4s.{ Serialization, native }
  protected implicit val serialization: Serialization = native.Serialization
}
object WithJson4sNative extends WithJson4sNative
