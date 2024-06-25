import { Backdrop, Button, CircularProgress, Container, Grid, Input, Paper, TextField } from "@mui/material"
import { FormEvent, useState } from "react"
import { Controller, SubmitHandler, useForm } from "react-hook-form"
import FormSection from "../components/forms/FormSection"
import { postRequest } from "../components/client/client"
import { useNavigate } from "react-router-dom"
import { DatePicker, LocalizationProvider } from "@mui/x-date-pickers"
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs'
import dayjs, { Dayjs } from "dayjs"

type ScheduleGenerationRequest = {
  week: Dayjs 
  numEmployeesPerHour: number
}

export default function ScheduleGeneration() {
  const navigate = useNavigate()

  const {
    control,
    handleSubmit,
    formState: { errors }
  } = useForm<ScheduleGenerationRequest>({
    defaultValues: {
      week: dayjs(),
      numEmployeesPerHour: 0
    }
  })

  const [hours, setHours] = useState(0)
  const [showBackdrop, setShowBackdrop] = useState(false)

  const onSubmit: SubmitHandler<ScheduleGenerationRequest> = (data) => {
    const payload = {
      date: data.week.toDate().toISOString().split('T')[0],
      numEmployeesPerHour: data.numEmployeesPerHour
    }

    postRequest('schedule/generate', payload)
      .then(res => res.json())
      .then(schedules => navigate('/select-schedule', { state: schedules }))
    
    setShowBackdrop(true)
  }

  function handleInputChange(e: FormEvent<HTMLDivElement>) {
    let input: number = parseInt(e.target.value)

    if (input < 0) {
      input = 0
      e.target.value = input
    }
    
    setHours(input)
  }

  return (
    <Container fixed>
      <LocalizationProvider dateAdapter={AdapterDayjs}>
        <form onSubmit={handleSubmit(onSubmit)}>
          <FormSection title="Schedule Generation">
            <Grid container spacing={1}>
              <Grid item xs={100}>
                <Paper sx={{ padding: 0.5 }}>
                  <Grid container spacing={2}>
                    <Grid item xs={100} mt={1}>
                      <Controller
                        name="week"
                        control={control}
                        render={({ field }) => <DatePicker
                          label="Week"
                          value={field.value}
                          inputRef={field.ref}
                          onChange={date => field.onChange(date)} />
                        }
                      />
                    </Grid>
                    <Grid item xs={100}>
                      <Controller
                        name="numEmployeesPerHour"
                        control={control}
                        render={({ field }) => (
                          <TextField
                            label="Number of Employees per Hour"
                            type="number"
                            defaultValue={0}
                            onInput={e => handleInputChange(e)}
                            {...field}
                            fullWidth
                          />)
                        }
                      />
                    </Grid>
                  </Grid>
                </Paper>
              </Grid>
            </Grid>
          </FormSection>

          <Button variant="contained" type="submit">Generate</Button>
        </form>
      </LocalizationProvider>
      <Backdrop open={showBackdrop}>
        <CircularProgress />
      </Backdrop>
    </Container>
  )
}