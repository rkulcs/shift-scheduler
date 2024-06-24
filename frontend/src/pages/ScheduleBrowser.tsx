import { Container, Divider, Grid, Typography } from "@mui/material"
import { DatePicker, LocalizationProvider } from "@mui/x-date-pickers"
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs'
import dayjs, { Dayjs } from "dayjs"
import { useEffect, useState } from "react"

export default function ScheduleBrowser() {
  const [date, setDate] = useState<Dayjs | null>(dayjs())

  useEffect(() => {

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
              onChange={date => setDate(date)}
            />
          </Grid>
        </Grid>
      </LocalizationProvider>
    </Container>
  )
}