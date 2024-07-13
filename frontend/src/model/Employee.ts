import { TimePeriod } from "./TimePeriod"

export type Employee = {
  id: number,
  account?: {
    username: string,
    name: string
  }
  minHoursPerDay: number,
  maxHoursPerDay: number,
  minHoursPerWeek: number,
  maxHoursPerWeek: number,
  availabilities: TimePeriod[]
}