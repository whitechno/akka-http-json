package aeaea.simple.time.joda

private object SimpleDateTimeWithJodaApp extends App {
  { // either import function ...
    import aeaea.simple.time.joda.SimpleDateTimeWithJoda.nowStartOfDayUTC
    println("either import function: " + nowStartOfDayUTC())
  }

  { // ... or call object's method
    val sdt: aeaea.simple.time.base.SimpleDateTime = aeaea.simple.time.joda.SimpleDateTimeWithJoda
    println("or call object's method: " + sdt.nowStartOfDayUTC())
  }
}
