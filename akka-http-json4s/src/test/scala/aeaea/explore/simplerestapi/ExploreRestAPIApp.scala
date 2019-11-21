package aeaea.explore.simplerestapi

private object ExploreRestAPIApp extends App {

  import API0._

  val req     = Req()
  val reqJSON = req.toJSON
  println(reqJSON)
  println(reqFromJSON(reqJSON))

  val resWithLog = ResWithLog(req = Some(req))
  resWithLog.res = Some(Res())
  val resWithLogJSON = resWithLog.toJSON
  println(resWithLog)
  println(resWithLogJSON)

  println(ResWithLog(resWithLogJSON))

  final case class Foo(bar: String)
  // doesn't compile:
  //println(Foo("bar2").toJSON)
  { // this works:
    import com.whitechno.explore.json4s.JsonSerDerApi._
    println(Foo("bar2").toJSON)
  }

}
