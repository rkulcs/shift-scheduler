import { SubmitHandler, useForm } from "react-hook-form"
import { TimePeriodFormInput } from "../types/TimePeriodFormInput"
import { Day } from "../model/Day"
import { useEffect, useState } from "react"
import { TimePeriod } from "../model/TimePeriod"
import { Button, Container, Grid, Paper } from "@mui/material"
import FormSection from "../components/forms/FormSection"
import LabeledCheckbox from "../components/forms/LabeledCheckbox"
import HourSelect from "../components/forms/HourSelect"

export default function Availabilities() {
  const {
    control,
    handleSubmit,
    getValues,
    setValue,
    formState: { errors }
  } = useForm<TimePeriodFormInput>({
    defaultValues: {
      periods: Object.keys(Day).filter(key => isNaN(Number(key))).map(day => {
        return { day: day, startHour: 0, endHour: 0, active: false }
      })
    }
  })

  const onSubmit: SubmitHandler<TimePeriodFormInput> = (data) => console.log(data)

  const [availabilities, setAvailabilities] = useState<TimePeriod[]>(getValues().periods);

  useEffect(() => {
    fetch(`${import.meta.env.VITE_API_URL}/employee/availability`,
      {
        method: 'GET',
        headers: {
          'Accept': 'application/json',
          'Authorization': localStorage.getItem('token') as string
        },
      }
    )
    .then(res => res.json())
    .then((data: TimePeriod[]) => {
      let updatedPeriods: TimePeriod[] = getValues().periods

      data.forEach((entry: TimePeriod) => {
        const index: number = Day[entry.day as keyof typeof Day]
        updatedPeriods[index] = {...entry, active: true}
      })

      setValue('periods', updatedPeriods)

      return updatedPeriods 
    })
    .then(data => setAvailabilities(data))
  }, [])

  useEffect(() => {
    setValue('periods', [...availabilities])
  }, [availabilities])

  return (
    <Container fixed>
      <form onSubmit={handleSubmit(onSubmit)}>
        <FormSection title="Availabilities">
          <Grid container spacing={1}>
            {Object.keys(Day).filter(key => !isNaN(Number(key))).map(key => Number(key)).map((i: number) => {
              return (
                <Grid item key={i} xs={1.7}>
                  <Paper sx={{ padding: 0.5 }}>
                    <LabeledCheckbox
                      name={`periods.${i}.active`}
                      label={getValues().periods[i].day}
                      control={control}
                    />

                    <HourSelect
                      i={i}
                      label="Start Hour"
                      value={availabilities[i].startHour}
                      onChange={e => {
                        let updatedHours: TimePeriod[] = availabilities
                        updatedHours[i].startHour = e.target.value as number
                        setAvailabilities(updatedHours)
                      }}
                    />

                    <HourSelect
                      i={i}
                      label="End Hour"
                      value={availabilities[i].endHour}
                      onChange={e => {
                        let updatedHours: TimePeriod[] = availabilities
                        updatedHours[i].endHour = e.target.value as number
                        setAvailabilities(updatedHours)
                      }}
                    />
                  </Paper>
                </Grid>
              )
            })}
          </Grid>
        </FormSection>

        <Button variant="contained" type="submit">Update</Button>
      </form>
    </Container>
  )
}