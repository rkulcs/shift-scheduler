import { Day } from "./Day"
import { Shift } from "./Shift"

export type DailySchedule = {
  day: Day
  shifts: Shift[]
}