import { Employee } from "./Employee"

export type Shift = {
  employee: Employee
  date: Date
  startHour: number
  endHour: number
}