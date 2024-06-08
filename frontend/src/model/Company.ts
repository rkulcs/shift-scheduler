import { TimePeriod } from "./TimePeriod"

export default class Company {
  name: string
  location: string
  hoursOfOperation: TimePeriod[]

  constructor(name: string, location: string, hoursOfOperation: TimePeriod[]) {
    this.name = name
    this.location = location
    this.hoursOfOperation = hoursOfOperation
  }
}
