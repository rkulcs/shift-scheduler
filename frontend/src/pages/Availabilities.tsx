import { SubmitHandler, useForm } from "react-hook-form"
import { Day } from "../model/Day"
import { useEffect, useState } from "react"
import { TimePeriod } from "../model/TimePeriod"
import { Button, Container, Grid, Paper, Box, Alert } from "@mui/material"
import FormSection from "../components/forms/FormSection"
import LabeledCheckbox from "../components/forms/LabeledCheckbox"
import HourSelect from "../components/forms/HourSelect"
import { getRequest, postRequest } from "../components/client/client"
import { Employee } from "../model/Employee"
import { FormSubmissionStatus } from "../types/FormSubmissionStatus"

type Availability = TimePeriod & { active: boolean }

type EmployeeFormInput = {
  hoursPerDayRange: {
    start: number,
    end: number
  } 
  hoursPerWeekRange: {
    start: number,
    end: number
  },
  availabilities: Availability[]
}

const MIN_WEEKLY_HOURS = 0
const MAX_WEEKLY_HOURS = 72

export default function Availabilities() {
  const {
    control,
    handleSubmit,
    getValues,
    setValue,
    formState: { errors }
  } = useForm<EmployeeFormInput>({
    defaultValues: {
      hoursPerDayRange: { start: 0, end: 0 },
      hoursPerWeekRange: { start: 0, end: 0 },
      availabilities: Object.keys(Day).filter(key => isNaN(Number(key))).map(day => {
        return { day: day, startHour: 0, endHour: 0, active: false }
      })
    }
  })

  const [minHoursPerDay, setMinHoursPerDay] = useState<number>(0)
  const [maxHoursPerDay, setMaxHoursPerDay] = useState<number>(0)
  const [minHoursPerWeek, setMinHoursPerWeek] = useState<number>(0)
  const [maxHoursPerWeek, setMaxHoursPerWeek] = useState<number>(0)
  const [availabilities, setAvailabilities] = useState<Availability[]>(getValues().availabilities)

  const [submissionStatus, setSubmissionStatus] = useState<FormSubmissionStatus>({ type: undefined, message: '' })

  const onSubmit: SubmitHandler<EmployeeFormInput> = (data) => {
    const activeAvailabilities: Availability[] = getValues().availabilities.filter(entry => entry.active)
    data.availabilities = activeAvailabilities
    data.hoursPerDayRange = { start: minHoursPerDay, end: maxHoursPerDay }
    data.hoursPerWeekRange = { start: minHoursPerWeek, end: maxHoursPerWeek }

    postRequest('employee', data)
      .then(res => {
        if (res.ok) {
          setSubmissionStatus({ type: 'success', message: 'Hours updated' })
        } else {
          setSubmissionStatus({ type: 'error', message: 'Failed to update availabilities' })
        }
      }).catch(() => {
        setSubmissionStatus({ type: 'error', message: 'Failed to update availabilities' })
      })
  }

  useEffect(() => {
    getRequest('employee')
      .then(res => res.json())
      .then((employee: Employee) => {
        setMinHoursPerDay(employee.hoursPerDayRange.startHour)
        setMaxHoursPerDay(employee.hoursPerDayRange.endHour)
        setMinHoursPerWeek(employee.hoursPerWeekRange.startHour)
        setMaxHoursPerWeek(employee.hoursPerWeekRange.endHour)
        let updatedPeriods: Availability[] = getValues().availabilities

        employee.availabilities.forEach((entry: TimePeriod) => {
          const index: number = Day[entry.day as keyof typeof Day]
          updatedPeriods[index] = { ...entry, active: true }
        })

        setValue('availabilities', updatedPeriods)
        setAvailabilities(updatedPeriods)
      })
  }, [])

  return (
    <Container fixed>
      <form onSubmit={handleSubmit(onSubmit)}>
        {submissionStatus.type &&
          <Box mb={2}>
            <Alert severity={submissionStatus.type}>{submissionStatus.message}</Alert>
          </Box>}
        <FormSection title="Hours per Day">
          <HourSelect
            label="Minimum"
            value={minHoursPerDay}
            onChange={e => {
              const value = e.target.value as number
              setMinHoursPerDay(value)
            }}
          />

          <HourSelect
            label="Maximum"
            value={maxHoursPerDay}
            onChange={e => {
              const value = e.target.value as number
              setMaxHoursPerDay(value)
            }}
          />
        </FormSection>
        <FormSection title="Hours per Week">
          <HourSelect
            label="Minimum"
            value={minHoursPerWeek}
            min={MIN_WEEKLY_HOURS}
            max={MAX_WEEKLY_HOURS}
            onChange={e => {
              const value = e.target.value as number
              setMinHoursPerWeek(value)
            }}
          />

          <HourSelect
            label="Maximum"
            value={maxHoursPerWeek}
            min={MIN_WEEKLY_HOURS}
            max={MAX_WEEKLY_HOURS}
            onChange={e => {
              const value = e.target.value as number
              setMaxHoursPerWeek(value)
            }}
          />
        </FormSection>
        <FormSection title="Availabilities">
          <Grid container spacing={1}>
            {Object.keys(Day).filter(key => !isNaN(Number(key))).map(key => Number(key)).map((i: number) => {
              return (
                <Grid item key={i} xs={1.7}>
                  <Paper sx={{ padding: 0.5 }}>
                    <LabeledCheckbox
                      name={`availabilities.${i}.active`}
                      label={availabilities[i].day as string}
                      control={control as any}
                    />

                    <HourSelect
                      i={i}
                      label="Start Hour"
                      value={availabilities[i].startHour}
                      onChange={e => {
                        let updatedHours: Availability[] = [...getValues().availabilities]
                        updatedHours[i].startHour = e.target.value as number
                        setValue('availabilities', updatedHours)
                        setAvailabilities(updatedHours)
                       }}
                    />

                    <HourSelect
                      i={i}
                      label="End Hour"
                      value={availabilities[i].endHour}
                      onChange={e => {
                        let updatedHours: Availability[] = [...getValues().availabilities]
                        updatedHours[i].endHour = e.target.value as number
                        setValue('availabilities', updatedHours)
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