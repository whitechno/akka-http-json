package aeaea.simple.time.joda

private object SimpleDateTimeWithJodaApp extends App {
  import aeaea.simple.time.joda.SimpleDateTimeWithJoda.nowStartOfDayUTC

  println(nowStartOfDayUTC())

  val sdt: aeaea.simple.time.base.SimpleDateTime = aeaea.simple.time.joda.SimpleDateTimeWithJoda
  println(sdt.nowStartOfDayUTC())
}
