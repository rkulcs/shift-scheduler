import { Container, Divider, Grid, Typography } from "@mui/material"
import { DatePicker, LocalizationProvider } from "@mui/x-date-pickers"
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs'
import dayjs, { Dayjs } from "dayjs"
import { useEffect, useState } from "react"
import { getRequest } from "../components/client/client"
import { WeeklySchedule } from "../model/WeeklySchedule"
import Schedule from "../components/Schedule"
import { useLocation } from "react-router-dom"

export default function ScheduleBrowser() {
  // Get the selected date if the page is reached through a redirect
  const { state } = useLocation()

  const [date, setDate] = useState<Dayjs | null>(state ? dayjs(state) : dayjs())
  const [schedule, setSchedule] = useState<WeeklySchedule | null>(null)

  useEffect(() => {
    if (!date)
      return

    const dateString = date.toISOString().split('T')[0]

    getRequest(`schedule/${dateString}`)
      .then(res => res.json())
      .then(data => setSchedule(data))
  }, [date])

  return (
    <Container fixed>
      <Typography variant="subtitle1">Schedules</Typography>
      <Divider />
      <LocalizationProvider dateAdapter={AdapterDayjs}>
        <Grid container spacing={1}>
          <Grid item xs={100} mt={1}>
            <DatePicker
              label="Date"
              value={date}
              onChange={date => setDate(date)}
            />
          </Grid>
          <Grid item xs={100}>
            {schedule && <Schedule schedule={schedule}/>}
          </Grid>
        </Grid>
      </LocalizationProvider>
    </Container>
  )
}