import { Grid, Button, Paper, Typography } from "@mui/material";
import { Link } from "react-router-dom";
import { useEffect, useState } from "react";
import { Shift } from "../model/Shift";
import { getRequest } from "../components/client/client";
import { useStore } from "react-redux";
import { getUserRole } from "../redux/store";

type EmployeeDashboardData = {
  nextShift: {
    shift: Shift,
    date: Date
  },
  numShifts: number,
  numHours: number
}

type CompanyDashboardData = {
  nextDay: {
    date: Date,
    startHour: number,
    endHour: number
  },
  numEmployees: number,
  totalHours: number
}

const buttons = [
  {
    label: 'Login',
    route: 'login'
  },
  {
    label: 'Company Registration',
    route: 'register-company'
  },
  {
    label: 'Employee Registration',
    route: 'register-employee'
  }
]

export default function Home() {
  const store = useStore()

  const [role, setRole] = useState<string>(getUserRole())

  store.subscribe(() => {
    setRole(getUserRole())
  })

  return (
    <>
      {!role && <AuthenticationOptions/>}
      {role === 'EMPLOYEE' && <EmployeeDashboard/>}
      {role === 'MANAGER' && <CompanyDashboard/>}
    </>
  )
}

function AuthenticationOptions() {
    return (
      <Grid mt={7} container spacing={4} alignItems="center" justifyContent="center">
        <Grid container justifyContent="center" spacing={2}>
          {buttons.map((button, i) => (
            <Grid key={i} item>
              <Link to={button.route}>
                <Button
                  variant="contained"
                  sx={{
                    height: 140,
                    width: 140
                  }}
                >
                  {button.label}
                </Button>
              </Link>
            </Grid>
          ))}
        </Grid>
      </Grid>
    )
}

function EmployeeDashboard() {
  const [data, setData] = useState<EmployeeDashboardData>({} as EmployeeDashboardData)

  useEffect(() => {
    getRequest('employee/dashboard')
      .then(res => res.json())
      .then(body => setData(body))
  }, [])

  function getNextShiftDetails() {
    if (!data.nextShift)
      return '-'

    const date = new Date(data.nextShift.date).toLocaleString('en-us', { weekday: 'long', month: 'long', day: 'numeric' })

    return `${date} (${data.nextShift.shift.startHour}:00 to ${data.nextShift.shift.endHour}:00)`
  }

  return (
    <>
      <Grid container spacing={2}>
        <Grid item xs={100}>
          <Paper sx={{ padding: 2 }}>
            <Typography variant="h5">Next Shift</Typography>
            <Typography variant="body1">
              {getNextShiftDetails()}
            </Typography>
          </Paper>
        </Grid>
        <Grid item xs={100}>
          <Paper sx={{ padding: 2 }}>
            <Typography variant="h5">This Week</Typography>
            <Grid container spacing={4} mt={0}>
              <Grid item xs={6}>
                <Paper>
                  <Typography align="center" variant="subtitle1">Number of Shifts</Typography>
                  <Typography align="center" variant="h1">{data.numShifts || '-'}</Typography>
                </Paper>
              </Grid>
              <Grid item xs={6}>
                <Paper>
                  <Typography align="center" variant="subtitle1">Number of Hours</Typography>
                  <Typography align="center" variant="h1">{data.numHours || '-'}</Typography>
                </Paper>
              </Grid>
            </Grid>
          </Paper>
        </Grid>
      </Grid>
    </>
  )
}

function CompanyDashboard() {
  const [data, setData] = useState<CompanyDashboardData>({} as CompanyDashboardData)

  useEffect(() => {
    getRequest('company/dashboard')
      .then(res => res.json())
      .then(body => setData(body))
  }, [])

  function getNextDayDetails() {
    if (!data.nextDay)
      return '-'

    const date = new Date(data.nextDay.date).toLocaleString('en-us', { weekday: 'long', month: 'long', day: 'numeric' })

    return `${date} (${data.nextDay.startHour}:00 to ${data.nextDay.endHour}:00)`
  }

  console.log(data)

  return (
    <>
      <Grid container spacing={2}>
        <Grid item xs={100}>
          <Paper sx={{ padding: 2 }}>
            <Typography variant="h5">Next Business Day</Typography>
            <Typography variant="body1">
              {getNextDayDetails()}
            </Typography>
          </Paper>
        </Grid>
        <Grid item xs={100}>
          <Paper sx={{ padding: 2 }}>
            <Typography variant="h5">This Week</Typography>
            <Grid container spacing={4} mt={0}>
              <Grid item xs={6}>
                <Paper>
                  <Typography align="center" variant="subtitle1">Number of Employees Working</Typography>
                  <Typography align="center" variant="h1">{data.numEmployees || '-'}</Typography>
                </Paper>
              </Grid>
              <Grid item xs={6}>
                <Paper>
                  <Typography align="center" variant="subtitle1">Total Number of Employee Work Hours</Typography>
                  <Typography align="center" variant="h1">{data.totalHours || '-'}</Typography>
                </Paper>
              </Grid>
            </Grid>
          </Paper>
        </Grid>
      </Grid>
    </>
  )
}