import { useEffect, useState } from "react"
import { Container, Button, Paper, Checkbox, FormControlLabel, Grid, FormControl, InputLabel, Select, MenuItem, Box, Alert } from "@mui/material"
import { useForm, SubmitHandler, Controller } from "react-hook-form"

import Company from "../model/Company"
import { Day } from "../model/Day"
import { TimePeriod, VALID_HOURS } from "../model/TimePeriod"
import { TimePeriodFormInput } from "../types/TimePeriodFormInput"
import FormSection from "../components/forms/FormSection"
import HourSelect from "../components/forms/HourSelect"
import LabeledCheckbox from "../components/forms/LabeledCheckbox"
import { getRequest, postRequest } from "../components/client/client"

export default function HoursOfOperationForm() {
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

  const [company, setCompany] = useState<Company>(new Company('', '', getValues().periods))
  const [submissionStatus, setSubmissionStatus] = useState({ type: '', message: '' })

  const onSubmit: SubmitHandler<TimePeriodFormInput> = (data) => {
    const payload: TimePeriod[] = data.periods.filter(entry => entry.active)

    postRequest('manager/hours-of-operation', payload)
      .then(res => {
        if (res.ok) {
          setSubmissionStatus({ type: 'success', message: 'Hours updated' })
        } else {
          setSubmissionStatus({ type: 'error', message: 'Failed to update hours of operation' })
        }
      }).catch(e => {
        setSubmissionStatus({ type: 'error', message: 'Failed to update hours of operation' })
      })
  }

  useEffect(() => {
    getRequest('company')
      .then(res => res.json())
      .then(data => {
        let updatedPeriods: TimePeriod[] = getValues().periods

        data.hoursOfOperation?.forEach((entry: TimePeriod) => {
          const index: number = Day[entry.day as keyof typeof Day]
          updatedPeriods[index] = {...entry, active: true}
        })

        setValue('periods', updatedPeriods)
        data.hoursOfOperation = updatedPeriods

        return data
      })
      .then(data => setCompany(data))
  }, [])

  // Handle hour selections
  useEffect(() => {
    setValue('periods', [...(company.hoursOfOperation)])
  }, [company])

  return (
    <Container fixed>
      <form onSubmit={handleSubmit(onSubmit)}>
        <FormSection title="Hours of Operation">
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
                      value={company.hoursOfOperation[i].startHour}
                      onChange={e => {
                        let updatedHours: TimePeriod[] = company.hoursOfOperation
                        updatedHours[i].startHour = e.target.value as number
                        setCompany({ ...company, hoursOfOperation: updatedHours })
                      }}
                    />

                    <HourSelect
                      i={i}
                      label="End Hour"
                      value={company.hoursOfOperation[i].endHour}
                      onChange={e => {
                        let updatedHours: TimePeriod[] = company.hoursOfOperation
                        updatedHours[i].endHour = e.target.value as number
                        setCompany({ ...company, hoursOfOperation: updatedHours })
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