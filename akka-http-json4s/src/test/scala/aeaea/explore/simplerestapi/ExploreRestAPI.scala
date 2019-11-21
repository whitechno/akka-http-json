package aeaea.explore.simplerestapi

object ExploreRestAPI extends App {

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

}

// "Ready-to-go" basic implementation of SimpleRestAPI with empty RES and REQ,
// and with Json4s.Jackson serializer.
import com.whitechno.explore.json4s.Json4sJacksonSerDerApi
object API0 extends SimpleRestAPI with Json4sJacksonSerDerApi {

  // define your Req/Res here
  final case class Req()
  final case class Res()

  type REQ = Req
  type RES = Res

  // boilerplate code because `A:Manifest` implicit
  // is needed in serialization.read[A](json)
  def reqFromJSON(json: String): REQ = fromJSON[REQ](json)
  def resFromJSON(json: String): RES = fromJSON[RES](json)

}

/**
  * In order to implement SimpleRestAPI, two things are needed:
  * 1. Define two case classes: REQ for request and RES for response
  * 2. Use suitable JSON serializer/deserializer to
  *    a) implement `reqFromJSON` and `resFromJSON`, as well as,
  *    b) provide enhanced `toJSON` method to REQ and RES classes via implicit class
  */
import com.whitechno.explore.json4s.JsonSerDerApi
trait SimpleRestAPI extends JsonSerDerApi {

  type REQ <: AnyRef
  type RES <: AnyRef

  def reqFromJSON(json: String): REQ //= fromJSON[REQ](json)
  def resFromJSON(json: String): RES //= fromJSON[RES](json)

  // generally, we should avoid classes defined in traits or classes,
  // but in this case we want to define a shell structure of response case class
  // and make it a part of SimpleRestAPI interface
  case class ResWithLog(
      var req: Option[REQ] = None,
      log: CommonLog = CommonLog(),
      var res: Option[RES] = None
  )
  object ResWithLog {
    def apply(json: String): ResWithLog = {

      val req: Option[REQ] = {
        val reqJSON = extractSubJSON(json, sub = "req")
        if (reqJSON != "") Some(reqFromJSON(reqJSON)) else None
      }

      val log = fromJSON[CommonLog](extractSubJSON(json, sub = "log"))

      val res: Option[RES] = {
        val resJSON = extractSubJSON(json, sub = "res")
        if (resJSON != "") Some(resFromJSON(resJSON)) else None
      }

      ResWithLog(req = req, log = log, res = res)
    }
  }
}

// CommonLog is a common part of every response:
// TODO: Avoid classes defined in traits or classes!
case class CommonLog(var err: Option[String] = None)
