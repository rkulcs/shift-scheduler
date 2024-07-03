import { Alert, Button, Container, Divider, Grid, Typography } from "@mui/material"
import { DatePicker, LocalizationProvider } from "@mui/x-date-pickers"
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs'
import dayjs, { Dayjs } from "dayjs"
import { useEffect, useState } from "react"
import { getRequest } from "../components/client/client"
import { WeeklySchedule } from "../model/WeeklySchedule"
import Schedule from "../components/Schedule"
import { useLocation } from "react-router-dom"
import ArrowLeftIcon from '@mui/icons-material/ArrowLeft'
import ArrowRightIcon from '@mui/icons-material/ArrowRight'

const WEEK_LENGTH_MS = 7*24*3600*1000;

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
      .then(res => res.ok ? res.json() : null)
      .then(data => setSchedule(data))
      .catch(() => setSchedule(null))
  }, [date])

  function goToPreviousWeek() {
    const currentDate = ((date as Dayjs).toDate())
    const newDate = currentDate.getTime() - WEEK_LENGTH_MS
    setDate(dayjs(newDate))
  }

  function goToNextWeek() {
    const currentDate = ((date as Dayjs).toDate())
    const newDate = currentDate.getTime() + WEEK_LENGTH_MS
    setDate(dayjs(newDate))
  }

  return (
    <Container fixed>
      <Typography variant="subtitle1">Schedules</Typography>
      <Divider />
      <LocalizationProvider dateAdapter={AdapterDayjs}>
        <Grid container spacing={1}>
          <Grid item xs={100} mt={1}>
            <Button 
              variant="outlined" 
              sx={{ height: '100%', marginRight: 1}}
              onClick={() => goToPreviousWeek()}
            >
              <ArrowLeftIcon fontSize="large"/>
            </Button>
            <DatePicker
              label="Date"
              value={date}
              onChange={date => setDate(date)}
            />
            <Button 
              variant="outlined" 
              sx={{ height: '100%', marginLeft: 1}}
              onClick={() => goToNextWeek()}
            >
              <ArrowRightIcon fontSize="large"/>
            </Button>
          </Grid>
          <Grid item xs={100}>
            {schedule && <Schedule schedule={schedule}/>}
            {!schedule && <Alert severity="info">No schedules were found for this week</Alert>}
          </Grid>
        </Grid>
      </LocalizationProvider>
    </Container>
  )
}