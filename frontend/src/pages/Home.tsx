import { useState } from "react"
import { useStore } from "react-redux"
import { getUserRole } from "../redux/store"
import { CompanyDashboard, EmployeeDashboard } from "../components/Dashboard"

import LoginIcon from '@mui/icons-material/Login'
import StoreIcon from '@mui/icons-material/Store'
import BadgeIcon from '@mui/icons-material/Badge'
import { Container, Typography } from "@mui/material"
import PageMenu, { PageMenuItem } from "../components/PageMenu"

export default function Home() {
  const store = useStore()

  // Change the displayed content depending on the role of the current user
  const [role, setRole] = useState<string>(getUserRole())

  // Update the home page if the user logs in or out
  store.subscribe(() => {
    setRole(getUserRole())
  })

  return (
    <>
      {!role && <HomeMenu/>}
      {role === 'EMPLOYEE' && <EmployeeDashboard/>}
      {role === 'MANAGER' && <CompanyDashboard/>}
    </>
  )
}

function HomeMenu() {
  return (
    <>
      <Container
        id="home-banner-container"
        sx={{ marginTop: -2, marginBottom: 2 }}
        maxWidth={false}
        disableGutters
      >
        <div id="home-banner" style={{ padding: '4%' }}>
          <Typography variant="h2" align="center" marginBottom={3}>
            Simplified Schedule Management
          </Typography>
          <Typography variant="subtitle1" align="center">
            Shift Scheduler is an application that aims to automate the creation of workplace
            schedules which meet the needs of businesses, while also respecting the
            availabilities of their employees.
          </Typography>
        </div>
      </Container>
      <PageMenu>
        <PageMenuItem
          icon={<LoginIcon />}
          title="Log In"
          description="Manage or view your company's employee schedules."
          link="/login"
        />
        <PageMenuItem
          icon={<StoreIcon />}
          title="Company Registration"
          description="Register your company to generate employee schedules."
          link="/register-company"
        />
        <PageMenuItem
          icon={<BadgeIcon />}
          title="Employee Registration"
          description="Create an employee account to view your upcoming shifts."
          link="/register-employee"
        />
      </PageMenu>
    </>
  )
}