import { TimePeriod } from "./TimePeriod"

export type Employee = {
  id: number,
  account?: {
    username: string,
    name: string
  },
  hoursPerDayRange: TimePeriod,
  hoursPerWeekRange: TimePeriod
  availabilities: TimePeriod[]
}