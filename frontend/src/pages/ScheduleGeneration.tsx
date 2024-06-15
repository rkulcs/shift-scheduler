import { Button, Container, Grid, Input, Paper, TextField } from "@mui/material"
import { FormEvent, useState } from "react"
import { SubmitHandler, useForm } from "react-hook-form"
import FormSection from "../components/forms/FormSection"
import HourSelect from "../components/forms/HourSelect"

type ScheduleGenerationRequest = {
  numEmployeesPerHour: number
}

export default function ScheduleGeneration() {
  const {
    control,
    handleSubmit,
    getValues,
    setValue,
    formState: { errors }
  } = useForm<ScheduleGenerationRequest>({
    defaultValues: {
      numEmployeesPerHour: 0
    }
  })

  const [hours, setHours] = useState(0)

  const onSubmit: SubmitHandler<ScheduleGenerationRequest> = (data) => console.log(data)

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
                <TextField
                  label="Number of Employees per Hour"
                  type="number"
                  defaultValue={0}
                  onInput={e => handleInputChange(e)}
                  fullWidth
                />
              </Paper>
            </Grid>
          </Grid>
        </FormSection>

        <Button variant="contained" type="submit">Generate</Button>
      </form>
    </Container>
  )
}