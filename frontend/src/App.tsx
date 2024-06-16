import { BrowserRouter, Routes, Route } from "react-router-dom"
import NavBar from "./components/NavBar"
import Home from "./pages/Home"
import Login from "./pages/Login"
import { Box, Container, Stack } from "@mui/material"
import CompanyRegistration from "./pages/CompanyRegistration"
import EmployeeRegistration from "./pages/EmployeeRegistration"
import HoursOfOperationForm from "./pages/HoursOfOperationForm"
import Availabilities from "./pages/Availabilities"
import ScheduleGeneration from "./pages/ScheduleGeneration"
import ScheduleSelection from "./pages/ScheduleSelection"

function App() {
  return (
    <BrowserRouter>
      <Stack>
        <Box>
          <NavBar />
        </Box>
        <Box mt={6}>
          <Container>
            <Routes>
              <Route path="/" element={<Home />} />
              <Route path="/login" element={<Login />} />
              <Route path="/register-company" element={<CompanyRegistration />} />
              <Route path="/register-employee" element={<EmployeeRegistration />} />

              <Route path="/hours" element={<HoursOfOperationForm />} />
              <Route path="/generate-schedules" element={<ScheduleGeneration />} />
              <Route path="/select-schedule" element={<ScheduleSelection />} />

              <Route path="/availabilities" element={<Availabilities />} />
            </Routes>
          </Container>
        </Box>
      </Stack>
    </BrowserRouter>
  )
}

export default App
