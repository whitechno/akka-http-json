package aeaea.simple.time.joda

import aeaea.simple.time.base.SimpleDateTime

trait SimpleDateTimeWithJoda extends SimpleDateTime {
  import org.joda.time.DateTime
  import org.joda.time.DateTimeZone.UTC
  import org.joda.time.format.DateTimeFormat

  private def nowStartOfDayUtcJot: DateTime =
    DateTime.now(UTC).withTimeAtStartOfDay
  def nowStartOfDayUTC(fmt: String = "yyyy_MM_dd"): String =
    DateTimeFormat.forPattern(fmt).print(nowStartOfDayUtcJot)
}

object SimpleDateTimeWithJoda extends SimpleDateTimeWithJoda
