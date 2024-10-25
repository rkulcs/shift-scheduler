import { ReactElement, useEffect, useState } from "react"
import { Container, Button, Paper, Grid, Box, Alert } from "@mui/material"
import { useForm, SubmitHandler } from "react-hook-form"

import Company from "../model/Company"
import { Day } from "../model/Day"
import { TimePeriod } from "../model/TimePeriod"
import { TimePeriodFormInput, TimePeriodInput } from "../types/TimePeriodFormInput"
import FormSection from "../components/forms/FormSection"
import HourSelect from "../components/forms/HourSelect"
import LabeledCheckbox from "../components/forms/LabeledCheckbox"
import { getRequest, postRequest } from "../components/client/client"
import { FormSubmissionStatus } from "../types/FormSubmissionStatus"

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

  const [hoursOfOperation, setHoursOfOperation] = useState<TimePeriodInput[]>(getValues().periods as TimePeriodInput[])
  const [submissionStatus, setSubmissionStatus] = useState<FormSubmissionStatus>({ type: undefined, message: '' })

  const onSubmit: SubmitHandler<TimePeriodFormInput> = (data) => {
    const payload = {
      timePeriods: data.periods.filter(entry => entry.active)
    }

    postRequest('company/hours', payload)
      .then(res => {
        if (res.ok) {
          setSubmissionStatus({ type: 'success', message: 'Hours updated' })
        } else {
          setSubmissionStatus({ type: 'error', message: 'Failed to update hours of operation' })
        }
      }).catch(() => {
        setSubmissionStatus({ type: 'error', message: 'Failed to update hours of operation' })
      })
  }

  useEffect(() => {
    getRequest('company')
      .then(res => res.json())
      .then((company: Company) => {
        let updatedPeriods: TimePeriodInput[] = getValues().periods

        company.hoursOfOperation.forEach((entry: TimePeriod) => {
          const index: number = Day[entry.day as keyof typeof Day]
          updatedPeriods[index] = {...entry, active: true}
        })

        setValue('periods', updatedPeriods)
        setHoursOfOperation(updatedPeriods)
      })
  }, [])

  // Handle hour selections
  useEffect(() => {
    setValue('periods', hoursOfOperation)
  }, [hoursOfOperation])

  function getStatusBox(): ReactElement {
    return (
      <Box mb={2}>
        <Alert severity={submissionStatus.type}>{submissionStatus.message}</Alert>
      </Box>
    )
  }

  return (
    <Container fixed>
      <form onSubmit={handleSubmit(onSubmit)}>
        <FormSection title="Hours of Operation">
          {submissionStatus.type ? getStatusBox() : <></>}
          <Grid container spacing={1}>
            {Object.keys(Day).filter(key => !isNaN(Number(key))).map(key => Number(key)).map((i: number) => {
              return (
                <Grid item key={i} xs={1.7} id={`tile-${i}`}>
                  <Paper sx={{ padding: 0.5 }}>
                    <LabeledCheckbox
                      name={`periods.${i}.active`}
                      label={getValues().periods[i].day as string}
                      control={control as any}
                    />

                    <HourSelect
                      i={i}
                      label="Start Hour"
                      value={hoursOfOperation[i].startHour}
                      onChange={e => {
                        let updatedHours: TimePeriodInput[] = getValues().periods
                        updatedHours[i].startHour = e.target.value as number
                        setHoursOfOperation(updatedHours)
                      }}
                    />

                    <HourSelect
                      i={i}
                      label="End Hour"
                      value={hoursOfOperation[i].endHour}
                      onChange={e => {
                        let updatedHours: TimePeriodInput[] = getValues().periods
                        updatedHours[i].endHour = e.target.value as number
                        setHoursOfOperation(updatedHours)
                      }}
                    />
                  </Paper>
                </Grid>
              )
            })}
          </Grid>
        </FormSection>

        <Button id="update-button" variant="contained" type="submit">Update</Button>
      </form>
    </Container>
  )
}