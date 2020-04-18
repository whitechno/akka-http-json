package aeaea.json.json4s.example

// with Json4s Native
private object WithJson4sNativeMain extends App {
  import aeaea.json.base.FromJSON
  import aeaea.json.base.JsonSerDe._           // for fromJSON and implicit toJSON
  import aeaea.json.json4s.WithJson4sJackson._ // for implicit serialization

  final case class Foo(bar: String)

  // deserialize json -> Foo using utility class `FromJSON`
  val foo: Foo = FromJSON("""{ "bar": "bar1" }""").to[Foo]
  println(foo)

  // ... or calling `fromJSON` function
  println(fromJSON[Foo]("""{ "bar": "bar2" }"""))

  // serialize Foo -> json
  println(Foo("bar3").toJSON)
}
