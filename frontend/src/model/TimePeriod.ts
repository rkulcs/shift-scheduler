import { Day } from "./Day"

export type TimePeriod = {
  day: string 
  startHour: number
  endHour: number
  active?: boolean
}