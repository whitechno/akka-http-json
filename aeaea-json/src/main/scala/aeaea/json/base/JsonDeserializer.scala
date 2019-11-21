package aeaea.json.base

trait JsonDeserializer[A] {
  def apply(json: String): A
}

object JsonDeserializer {
  def apply[A](f: String => A): JsonDeserializer[A] = (json: String) => f(json)
}
