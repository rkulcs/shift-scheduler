import { useEffect, useState } from "react"
import { Container, Card, Button, Typography, Paper, Checkbox, FormControlLabel, Grid } from "@mui/material"
import { useForm, SubmitHandler, Controller } from "react-hook-form"

import Company from "../model/Company"
import UserRegistrationFormInput from "../types/UserRegistrationFormInput"
import { Day } from "../model/Day"
import { TimePeriod } from "../model/TimePeriod"
import TextInputField from "../components/forms/TextInputField"

type HoursOfOperationFormInput = {
  periods: TimePeriod[]
}

export default function HoursOfOperationForm() {
  const {
    control,
    handleSubmit,
    getValues,
    setValue,
    formState: { errors }
  } = useForm<HoursOfOperationFormInput>({
    defaultValues: {
      periods: Object.keys(Day).filter(key => isNaN(Number(key))).map(day => {
        return { day: day, startHour: 0, endHour: 0, active: false }
      })
    }
  })

  const [company, setCompany] = useState(new Company('', ''))

  const onSubmit: SubmitHandler<HoursOfOperationFormInput> = (data) => console.log(data)

  useEffect(() => {
    fetch(`${import.meta.env.VITE_API_URL}/company`,
      {
        method: 'GET',
        headers: {
          'Accept': 'application/json',
          'Authorization': localStorage.getItem('token') as string
        },
      }
    )
    .then(res => res.json())
    .then(data => setCompany(data))
  }, [])

  useEffect(() => {
    company.hoursOfOperation?.forEach(entry => {
      const index: number = Day[entry.day as keyof typeof Day]
      setValue(`periods.${index}`, {...entry, active: true})
    })
  }, [company])

  return (
    <Container fixed>
      <form onSubmit={handleSubmit(onSubmit)}>
        <Grid container spacing={1}>
        {Object.keys(Day).filter(key => !isNaN(Number(key))).map(i => {
          return (
          <Grid item xs={1.7}>
            <Paper key={i}>
              <Container>
              <FormControlLabel
                control={
                  <Controller
                    name={`periods.${i}.active`}
                    control={control}
                    render={({ field: { onChange, value } }) => <Checkbox checked={value} onChange={onChange} />}
                  />
                }
                label={getValues().periods[i].day}
              />
              </Container>
              <TextInputField name={`periods.${i}.startHour`} label="Start Hour" control={control} />
              <TextInputField name={`periods.${i}.endHour`} label="End Hour" control={control} />
            </Paper>
            </Grid>
          ) 
        })}
        </Grid>

        <Button variant="contained" type="submit">Update</Button>
      </form>
    </Container>
  )
}