import { json, useLocation } from "react-router-dom"
import { WeeklySchedule } from "../model/WeeklySchedule"
import Schedule from "../components/Schedule"
import FormSection from "../components/forms/FormSection"
import { postRequest } from "../components/client/client"
import { Button } from "@mui/material"

export default function ScheduleSelection() {
  const { state } = useLocation()
  const schedules: WeeklySchedule[] = state

  function saveSchedule(schedule: WeeklySchedule) {
    postRequest('manager/save-schedule', schedule)
  }

  return (
    <FormSection title="Select a Schedule">
      {schedules.map(schedule => {
        return (
          <>
            <Schedule schedule={schedule}/>
            <Button onClick={() => saveSchedule(schedule)}>Select</Button>
          </>
        )
      })}
    </FormSection>
  )
}