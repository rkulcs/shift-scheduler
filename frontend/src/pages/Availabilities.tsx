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

type EmployeeFormInput = {
  employee: Employee
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
      employee: {
        id: 0,
        minHoursPerDay: 0,
        maxHoursPerDay: 0,
        minHoursPerWeek: 0,
        maxHoursPerWeek: 0,
        availabilities: Object.keys(Day).filter(key => isNaN(Number(key))).map(day => {
          return { day: day, startHour: 0, endHour: 0, active: false }
        })
      }
    }
  })

  const [employee, setEmployee] = useState<Employee>(getValues().employee)

  const [submissionStatus, setSubmissionStatus] = useState({ type: '', message: '' })

  const onSubmit: SubmitHandler<EmployeeFormInput> = (data) => {
    const activeAvailabilities: TimePeriod[] = data.employee.availabilities.filter(entry => entry.active)
    data.employee.availabilities = activeAvailabilities

    postRequest('employee', data.employee)
      .then(res => {
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
    getRequest('employee')
      .then(res => res.json())
      .then((employee: Employee) => {
        let updatedPeriods: TimePeriod[] = getValues().employee.availabilities

        employee.availabilities.forEach((entry: TimePeriod) => {
          const index: number = Day[entry.day as keyof typeof Day]
          updatedPeriods[index] = { ...entry, active: true }
        })

        employee.availabilities = updatedPeriods

        setValue('employee', employee)

        return employee
      })
      .then(employee => setEmployee(employee))
  }, [])

  useEffect(() => {
    setValue('employee', {...employee})
  }, [employee])

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
            value={employee.minHoursPerDay}
            onChange={e => {
              setEmployee({...employee, minHoursPerDay: e.target.value})
              return
            }}
          />

          <HourSelect
            label="Maximum"
            value={employee.maxHoursPerDay}
            onChange={e => {
              setEmployee({...employee, maxHoursPerDay: e.target.value})
              return
            }}
          />
        </FormSection>
        <FormSection title="Hours per Week">
          <HourSelect
            label="Minimum"
            value={employee.minHoursPerWeek}
            min={MIN_WEEKLY_HOURS}
            max={MAX_WEEKLY_HOURS}
            onChange={e => {
              setEmployee({...employee, minHoursPerWeek: e.target.value})
              return
            }}
          />

          <HourSelect
            label="Maximum"
            value={employee.maxHoursPerWeek}
            min={MIN_WEEKLY_HOURS}
            max={MAX_WEEKLY_HOURS}
            onChange={e => {
              setEmployee({...employee, maxHoursPerWeek: e.target.value})
              return
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
                      name={`employee.availabilities.${i}.active`}
                      label={getValues().employee.availabilities[i].day}
                      control={control}
                    />

                    <HourSelect
                      i={i}
                      label="Start Hour"
                      value={employee.availabilities[i].startHour}
                      onChange={e => {
                        let updatedHours: TimePeriod[] = [...employee.availabilities]
                        updatedHours[i].startHour = e.target.value as number
                        setEmployee({ ...employee, availabilities: updatedHours })
                      }}
                    />

                    <HourSelect
                      i={i}
                      label="End Hour"
                      value={employee.availabilities[i].endHour}
                      onChange={e => {
                        let updatedHours: TimePeriod[] = [...employee.availabilities]
                        updatedHours[i].endHour = e.target.value as number
                        setEmployee({ ...employee, availabilities: updatedHours })
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