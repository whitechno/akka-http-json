package com.whitechno.explore.json4s

object SerializingClassesDefinedInTraitsApp extends App {

  /** https://github.com/json4s/json4s#serializing-classes-defined-in-traits-or-classes
    *
    * Serializing classes defined in traits or classes
    * We've added support for case classes defined in a trait. But they do need custom formats.
    * I'll explain why and then how.
    *
    * Why?
    * For classes defined in a trait it's a bit difficult to get to their companion object,
    * which is needed to provide default values. We could punt on those but that brings us to the next problem,
    * that the compiler generates an extra field in the constructor of such case classes.
    * The first field in the constructor of those case classes is called $outer and is of type of the defining trait.
    * So somehow we need to get an instance of that object, naively we could scan all classes and collect the ones
    * that are implementing the trait, but when there are more than one: which one to take?
    *
    * How?
    * I've chosen to extend the formats to include a list of companion mappings for those case classes.
    * So you can have formats that belong to your modules and keep the mappings in there.
    * That will then make default values work and provide the much needed $outer field.
    *
    * trait SharedModule {
    * case class SharedObj(name: String, visible: Boolean = false)
    * }
    *
    * object PingPongGame extends SharedModule
    * implicit val formats: Formats =
    *   DefaultFormats.withCompanions(classOf[PingPongGame.SharedObj] -> PingPongGame)
    *
    * val inst = PingPongGame.SharedObj("jeff", visible = true)
    * val extr = Extraction.decompose(inst)
    * extr must_== JObject("name" -> JString("jeff"), "visible" -> JBool(true))
    * extr.extract[PingPongGame.SharedObj] must_== inst
    */
  import org.json4s.jackson.JsonMethods._
  import org.json4s._

  trait SharedModule {
    case class SharedObj(name: String, visible: Boolean = false)
    case class CommonLog(var err: Option[String] = None)
  }

  object PingPongGame extends SharedModule

  implicit val formats: Formats = DefaultFormats
    .withCompanions(classOf[PingPongGame.SharedObj] -> PingPongGame)
    .withCompanions(classOf[PingPongGame.CommonLog] -> PingPongGame)

  val inst = PingPongGame.SharedObj("jeff", visible = true)
  println("... SharedObj ...\n" + inst)

  {

    /** If you uncomment this, the line further below:
      * `println(extr.extract[PingPongGame.SharedObj]) //== inst`
      * will fail with this exception message:
      *
      * Exception in thread "main" org.json4s.package$MappingException: No usable value for $outer
      * No constructor for type SharedModule, JNothing
      *
      * However, with this block commented out, everything works.
      */
    // TODO: Avoid classes defined in traits or classes!

//    import Json4sJacksonSerDer._
//    val instJSON = inst.toJSON
//    println(instJSON)
  }

  // 1
  val extr = Extraction.decompose(inst)
  println(extr)                                 //== JObject("name" -> JString("jeff"), "visible" -> JBool(true))
  println(compact(extr))                        //== """{"name":"jeff","visible":true}"""
  println(extr.extract[PingPongGame.SharedObj]) //== inst

  // 2
  val json       = """{"name":"jeff","visible":true}"""
  val jsonParsed = parse(json)
  println(jsonParsed)                                 //== JObject(List((name,JString(jeff)), (visible,JBool(true))))
  println(jsonParsed.extract[PingPongGame.SharedObj]) //== SharedObj(jeff,true)

  // 3 CommonLog
  val cl = PingPongGame.CommonLog()
  println("... CommonLog ...\n" + cl)
  val clExtr = Extraction.decompose(cl)
  println(clExtr) //== JObject(List())
  println(clExtr.extract[PingPongGame.CommonLog])

  // 4 CommonLog
  val clJSON = compact(clExtr)
  println(clJSON) //== {}
  val clParsed = parse(clJSON)
  println(clParsed)                                 //== JObject(List())
  println(clParsed.extract[PingPongGame.CommonLog]) //== CommonLog(None)
}

object Json4sSerDeApp extends App {

  import Json4sJacksonSerDe._

  val t = TryReservedWord(1)
  println(toJSON(t))

  val json = """{"type":1}"""
  println(fromJSON[TryReservedWord](json))
}

object ExploreJson4sApp {
  import org.json4s.{ DefaultFormats, jackson }
  final case class Foo(bar: String) {
    require(bar == "bar", "bar must be 'bar'!")
  }

  def main(args: Array[String]): Unit = {
    println("... ExploreJson4sApp ...")

    implicit val serialization = jackson.Serialization // or native.Serialization
    implicit val formats       = DefaultFormats

    // Foo

    val foo     = Foo("bar")
    val fooJSON = serialization.write(foo)
    println(fooJSON)
    println(serialization.read[Foo](fooJSON))

    // TryReservedWord

    val t     = TryReservedWord(1)
    val tJSON = serialization.write(t)
    println(tJSON)
    println(serialization.read[TryReservedWord](tJSON))

    // DMC

    val json =
      """
    {
        "id":               "ss.h",
        "name":             "Hourly SS",
        "descriptions":     ["Sequence file session summary hourly partitions"],
        
        "basePath":         "{access}/3d/prod/stateful_ss/hive_sdm2_ss",
        "pathFmt":          "'/{y}/{m}/{dt}/SessDiff'",
        "pathStep":         "PT1H",
        
        "partitions":       {
                              "y": "yyyy", "m": "MM", "dt": "'{dt}'"
                            },
        "params":           ["access"],
        "defaultParams":    {"access" : "/mnt/stateful-session-summaries"},
        
        "dts":              {"dt": {"fmt": "yyyy_MM_dd_HH_00", "toFmt": "_'to'_yyyy_MM_dd_HH_00", "step": "PT1H"}}
    }
      """

    val dmc = serialization.read[DMC](json)
    println(dmc)
    println(serialization.writePretty(dmc))
  }
}

case class TryReservedWord(`type`: Byte)

case class DtPartition(
    fmt: String,                  // "yyyy_MM_dd_HH"
    toFmt: Option[String] = None, // "_'to'_yyyy_MM_dd_HH"
    step: Option[String] = None   // ISO8601 "PT1H"
)

case class DMC(
    id: Option[String] = None,
    name: Option[String] = None,
    description: Option[String] = None,       // either one-liner ...
    descriptions: Option[Seq[String]] = None, // ... or multi-line

    basePath: String, /* basePath is the only non-optional field */
    pathFmt: Option[String] = None,  // first replase partitions and params, then format DateTime
    pathStep: Option[String] = None, // ISO8601

    partitions: Option[Map[String, String]] = None,    // rules to build partitions
    params: Option[Seq[String]] = None,                // list of required params
    defaultParams: Option[Map[String, String]] = None, // for possible default values for params

    dts: Option[Map[String, DtPartition]] = None
)
