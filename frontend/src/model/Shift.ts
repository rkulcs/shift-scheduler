import { Employee } from "./Employee"
import { TimePeriod } from "./TimePeriod"

export type Shift = {
  employee: Employee
  timePeriod: TimePeriod
}