import { Backdrop, Button, CircularProgress, Container, Grid, Input, Paper, TextField } from "@mui/material"
import { FormEvent, useState } from "react"
import { Controller, SubmitHandler, useForm } from "react-hook-form"
import FormSection from "../components/forms/FormSection"
import HourSelect from "../components/forms/HourSelect"
import { postRequest } from "../components/client/client"
import { useNavigate } from "react-router-dom"

type ScheduleGenerationRequest = {
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
      numEmployeesPerHour: 0
    }
  })

  const [hours, setHours] = useState(0)
  const [showBackdrop, setShowBackdrop] = useState(false)

  const onSubmit: SubmitHandler<ScheduleGenerationRequest> = (data) => {
    postRequest('manager/generate-schedules', data)
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
      <form onSubmit={handleSubmit(onSubmit)}>
        <FormSection title="Schedule Generation">
          <Grid container spacing={1}>
            <Grid item xs={100}>
              <Paper sx={{ padding: 0.5 }}>
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
              </Paper>
            </Grid>
          </Grid>
        </FormSection>

        <Button variant="contained" type="submit">Generate</Button>
      </form>
      <Backdrop open={showBackdrop}>
        <CircularProgress/>
      </Backdrop>
    </Container>
  )
}