import { useLocation } from "react-router-dom"
import { WeeklySchedule } from "../model/WeeklySchedule"
import Schedule from "../components/Schedule"

export default function ScheduleSelection() {
  const { state } = useLocation()
  const schedules: WeeklySchedule[] = state

  return (
    <>
      {schedules.map(schedule => <Schedule schedule={schedule}/>)}
    </>
  )
}