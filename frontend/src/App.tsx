import { BrowserRouter, Routes, Route } from "react-router-dom"
import NavBar from "./components/NavBar"
import Home from "./pages/Home"
import Login from "./pages/Login"
import { Box, Container, Stack } from "@mui/material"
import CompanyRegistration from "./pages/CompanyRegistration"
import EmployeeRegistration from "./pages/EmployeeRegistration"

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
              <Route path="/company-registration" element={<CompanyRegistration />} />
              <Route path="/employee-registration" element={<EmployeeRegistration />} />
            </Routes>
          </Container>
        </Box>
      </Stack>
    </BrowserRouter>
  )
}

export default App
