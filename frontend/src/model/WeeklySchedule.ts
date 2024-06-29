import { DailySchedule } from "./DailySchedule"

export type WeeklySchedule = {
  firstDay: Date
  dailySchedules: DailySchedule[]
  constraintViolations?: ConstraintViolation[]
}

type ConstraintViolation = {
  description: string
}