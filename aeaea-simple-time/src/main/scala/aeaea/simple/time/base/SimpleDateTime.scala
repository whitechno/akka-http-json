package aeaea.simple.time.base

trait SimpleDateTime {

  /**
    * Obtains a full datetime as a formatted string
    * at the earliest valid time in UTC time zone
    * for the date set to the current system time.
    * For instance, if called at `2019-11-30T23:43:14.324-08:00` (America/Los_Angeles),
    * `nowStartOfDayUTC()` will return "2019_12_01"
    *
    * @param fmt: format pattern specification
    * @return formatted string
    */
  def nowStartOfDayUTC(fmt: String = "yyyy_MM_dd"): String
}
