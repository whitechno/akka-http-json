package aeaea.json.json4s

// with Json4s Jackson
private object WithJson4sJacksonApp extends App {
  import aeaea.json.base.FromJSON
  import aeaea.json.base.JsonSerDe._ // for toJSON function in implicit class
  import WithJson4sJackson._         // for implicit args JsonSerializer and JsonDeserializer

  final case class Foo(bar: String)

  // deserialize json -> Foo using utility class `FromJSON`
  val foo: Foo = FromJSON("""{ "bar": "bar1" }""").to[Foo]
  println(foo)

  // ... or calling `fromJSON` function
  //println(fromJSON[Foo]("""{ "bar": "bar11" }"""))

  // serialize Foo -> json
  println(Foo("bar2").toJSON)
}

// with Json4s Native
private object WithJson4sNativeApp extends App {
  import aeaea.json.base.FromJSON
  import aeaea.json.base.JsonSerDe._
  import WithJson4sNative._

  final case class Foo(bar: String)

  // deserialize json -> Foo
  val foo: Foo = FromJSON("""{ "bar": "bar3" }""").to[Foo]
  println(foo)

  // serialize Foo -> json
  println(Foo("bar4").toJSON)
}
