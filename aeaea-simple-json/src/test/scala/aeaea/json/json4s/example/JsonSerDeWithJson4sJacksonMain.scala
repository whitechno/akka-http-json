package aeaea.json.json4s.example

private object JsonSerDeWithJson4sJacksonMain extends App {

  { // InnerFoo
    import aeaea.json.base.JsonSerDe
    import aeaea.json.json4s.WithJson4sJackson
    object JsonSerDeWithJson4sJackson extends JsonSerDe with WithJson4sJackson {
      final case class InnerFoo(bar: String)
    }
    import JsonSerDeWithJson4sJackson._
    println(fromJSON[InnerFoo]("""{ "bar": "bar1" }"""))
    println(InnerFoo("implicit via parent").toJSON)
    // doesn't compile:
    //println(Foo("implicit via import").toJSON)
  }

  { // most common use case: works with any case class
    final case class Foo(bar: String)
    import aeaea.json.base.JsonSerDe._           // for fromJSON and implicit toJSON
    import aeaea.json.json4s.WithJson4sJackson._ // for implicit serialization
    println(fromJSON[Foo]("""{ "bar": "bar2" }"""))
    println(Foo("implicit via import").toJSON)
  }

  { // ExtendedFoo
    import aeaea.json.base.JsonSerDe
    import aeaea.json.json4s.WithJson4sJackson
    final case class ExtendedFoo(bar: String) extends JsonSerDe with WithJson4sJackson
    println(JsonSerDe.fromJSON[ExtendedFoo]("""{ "bar": "bar3" }"""))
    println(ExtendedFoo("implicit via extention").toJSON)
  }

}
