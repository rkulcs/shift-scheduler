import { json, useLocation, useNavigate } from "react-router-dom"
import { WeeklySchedule } from "../model/WeeklySchedule"
import Schedule from "../components/Schedule"
import FormSection from "../components/forms/FormSection"
import { postRequest } from "../components/client/client"
import { Alert, Box, Button } from "@mui/material"
import { ReactElement, useState } from "react"
import { FormSubmissionStatus } from "../types/FormSubmissionStatus"

export default function ScheduleSelection() {
  const { state } = useLocation()
  const navigate = useNavigate()
  const schedules: WeeklySchedule[] = state

  const [submissionStatus, setSubmissionStatus] = useState<FormSubmissionStatus>({ type: undefined, message: '' })

  function saveSchedule(schedule: WeeklySchedule) {
    delete schedule.constraintViolations

    postRequest('schedule', schedule)
      .then(res => {
        if (res.ok) {
          setSubmissionStatus({ type: 'success', message: 'Successfully saved the selected schedule' })

          // Redirect to schedule browser
          navigate('/schedules', { state: schedule.firstDay })
        } else {
          setSubmissionStatus({ type: 'error', message: 'Failed to save the selected schedule' })
        }
      })
  }

  function getScheduleList(schedules: any[]): ReactElement {
    return (
      <>
        {
          schedules.map((schedule, i) => (
            <div key={i}>
              <Schedule schedule={schedule}/>
              <Box mt={2}>
                <Button variant="contained" onClick={() => saveSchedule(schedule)}>Select</Button>
              </Box>
            </div>
          ))
        }
      </>
    )
  }

  return (
    <FormSection title="Select a Schedule">
      <Box mb={2}>
        {submissionStatus.type && 
          <Alert severity={submissionStatus.type}>
            {submissionStatus.message}
          </Alert>}
      </Box>
      {getScheduleList(schedules)}
    </FormSection>
  )
}