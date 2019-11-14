package aeaea.explore

import com.whitechno.explore.json4s.Json4sJacksonSerDe

object ExploreRestAPIWithGenerics extends App {

  trait SimpleRestAPI[REQ <: AnyRef, RES <: AnyRef] {}

}
