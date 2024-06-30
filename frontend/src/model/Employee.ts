import { TimePeriod } from "./TimePeriod"

export type Employee = {
  id: number,
  minHoursPerDay: number,
  maxHoursPerDay: number,
  minHoursPerWeek: number,
  maxHoursPerWeek: number,
  availabilities: TimePeriod[]
}