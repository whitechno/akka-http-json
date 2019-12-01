package aeaea.json.base

trait JsonSerializer[-A] {
  def apply(value: A): String
}

object JsonSerializer {
  def apply[A](f: A => String): JsonSerializer[A] = (value: A) => f(value)

//  implicit class ToJSON[A](val a: A) extends AnyVal {
//    def toJSON(implicit js: JsonSerializer[A]): String = js(a)
//  }
}
