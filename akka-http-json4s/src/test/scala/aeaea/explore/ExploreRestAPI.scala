package aeaea.explore

import com.whitechno.explore.json4s.Json4sJacksonSerDer

object ExploreRestAPI extends App {

  import SimpleRestAPIWithJson4sJacksonExample.API._

  val req     = Req()
  val reqJSON = req.toJSON
  println(reqJSON)
  println(reqFromJSON(reqJSON))

  val res     = initResWithLog(Some(req))
  val resJSON = res.toJSON
  println(res)
  println(resJSON)

  println(ExperimantalFeature1.resWithLogFromJSON(resJSON))

}

object ExperimantalFeature1 {
  def resWithLogFromJSON(json: String) = { //: ResWithLog
    import org.json4s.native.JsonMethods._
    import org.json4s.DefaultFormats
    implicit val formats = DefaultFormats

    val jV = parse(json)

    //          ResWithLog(
    //            req = (jV \ "req").extract[Option[REQ]],
    //            log = (jV \ "log").extract[CommonLog],
    //            res = (jV \ "res").extract[Option[RES]]
    //          )

    (jV \ "log").extract[CommonLog]
  }
}

object SimpleRestAPIWithJson4sJacksonExample {

  case object API extends SimpleRestAPI with Json4sJacksonSerDer {

    // define your Req/Res here
    case class Req()
    case class Res()

    // boilerplate code (needed because of `A:Manifest` implicit in serialization.read[A](json)
    type REQ = Req
    def reqFromJSON(json: String): REQ = fromJSON[REQ](json)
    type RES = Res
    def resFromJSON(json: String): RES = fromJSON[RES](json)

  }
}

trait SimpleRestAPI {
  type REQ <: AnyRef
  def reqFromJSON(json: String): REQ
  type RES <: AnyRef
  def resFromJSON(json: String): RES //= fromJSON[ResWithLog[REQ, RES]](json)

  case class ResWithLog(
      var req: Option[REQ] = None,
      log: CommonLog = CommonLog(),
      var res: Option[RES] = None
  )
  def initResWithLog(req: Option[REQ] = None): ResWithLog = ResWithLog(req = req)

}

// common part of every response:
// TODO: Avoid classes defined in traits or classes.
case class CommonLog(var err: Option[String] = None)
