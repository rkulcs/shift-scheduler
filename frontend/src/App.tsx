import { BrowserRouter, Routes, Route } from "react-router-dom"
import NavBar from "./components/NavBar"
import Home from "./pages/Home"
import Login from "./pages/Login"
import { Box, Card, Container, createTheme, Paper, Stack, ThemeProvider } from "@mui/material"
import CompanyRegistration from "./pages/CompanyRegistration"
import EmployeeRegistration from "./pages/EmployeeRegistration"
import HoursOfOperationForm from "./pages/HoursOfOperationForm"
import Availabilities from "./pages/Availabilities"
import ScheduleGeneration from "./pages/ScheduleGeneration"
import ScheduleSelection from "./pages/ScheduleSelection"
import ScheduleBrowser from "./pages/ScheduleBrowser"
import { blueGrey, grey } from "@mui/material/colors"

const theme = createTheme({
  palette: {
    primary: blueGrey,
    secondary: grey,
  },
});

function App() {
  return (
    <ThemeProvider theme={theme}>
      <BrowserRouter>
        <Stack>
          <Box>
            <NavBar />
          </Box>
          <Box mt={2}>
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

                <Route path="/schedules" element={<ScheduleBrowser />} />
              </Routes>
            </Container>
          </Box>
        </Stack>
      </BrowserRouter>
    </ThemeProvider>
  )
}

export default App
