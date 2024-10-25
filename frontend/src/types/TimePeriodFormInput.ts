import { TimePeriod } from "../model/TimePeriod"

export type TimePeriodInput = TimePeriod & { active: boolean }

export type TimePeriodFormInput = {
  periods: TimePeriodInput[]
}