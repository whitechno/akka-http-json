package aeaea.json.json4s

private object JsonSerDeWithJson4sJacksonApp extends App {

  import aeaea.json.base.JsonSerDe

  final case class Foo(bar: String)

  {
    object JsonSerDeWithJson4sJackson extends JsonSerDe with WithJson4sJackson {
      final case class InnerFoo(bar: String)
    }
    import JsonSerDeWithJson4sJackson._
    println(fromJSON[InnerFoo]("""{ "bar": "bar1" }"""))
    println(InnerFoo("implicit via parent").toJSON)
    // doesn't compile:
    //println(Foo("implicit via import").toJSON)
  }

  { // this works:
    import JsonSerDe._
    import WithJson4sJackson._
    println(fromJSON[Foo]("""{ "bar": "bar2" }"""))
    println(Foo("implicit via import").toJSON)
  }

  {
    final case class ExtendedFoo(bar: String) extends JsonSerDe with WithJson4sJackson
    println(JsonSerDe.fromJSON[ExtendedFoo]("""{ "bar": "bar3" }"""))
    println(ExtendedFoo("implicit via extention").toJSON)
  }

}
