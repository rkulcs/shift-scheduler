import { Day } from "./Day"

export const VALID_HOURS = [0, 4, 8, 12, 16, 20, 24]

export type TimePeriod = {
  day?: string
  startHour: number
  endHour: number
  active?: boolean
}