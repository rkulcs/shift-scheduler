import { SubmitHandler, useForm } from "react-hook-form"
import { TimePeriodFormInput } from "../types/TimePeriodFormInput"
import { Day } from "../model/Day"
import { useEffect, useState } from "react"
import { TimePeriod } from "../model/TimePeriod"
import { Button, Container, Grid, Paper, Box, Alert } from "@mui/material"
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

  const [availabilities, setAvailabilities] = useState<TimePeriod[]>(getValues().periods);
  const [submissionStatus, setSubmissionStatus] = useState({ type: '', message: '' })

  const onSubmit: SubmitHandler<TimePeriodFormInput> = (data) => {
    const payload: TimePeriod[] = data.periods.filter(entry => entry.active)

    fetch(`${import.meta.env.VITE_API_URL}/employee/availability`,
      {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json',
          'Authorization': localStorage.getItem('token') as string
        },
        body: JSON.stringify(payload)
      }
    ).then(res => {
      if (res.ok) {
        setSubmissionStatus({ type: 'success', message: 'Hours updated' })
      } else {
        setSubmissionStatus({ type: 'error', message: 'Failed to update availabilities' })
      }
    }).catch(e => {
      setSubmissionStatus({ type: 'error', message: 'Failed to update availabilities' })
    })
  }

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
          {submissionStatus.type &&
            <Box mb={2}>
              <Alert severity={submissionStatus.type}>{submissionStatus.message}</Alert>
            </Box>}
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
                        let updatedHours: TimePeriod[] = [...availabilities]
                        updatedHours[i].startHour = e.target.value as number
                        setAvailabilities(updatedHours)
                      }}
                    />

                    <HourSelect
                      i={i}
                      label="End Hour"
                      value={availabilities[i].endHour}
                      onChange={e => {
                        let updatedHours: TimePeriod[] = [...availabilities]
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