import { BrowserRouter, Routes, Route } from "react-router-dom"
import NavBar from "./components/NavBar"
import Home from "./pages/Home"
import Login from "./pages/Login"
import { Box, Container, Stack } from "@mui/material"

function App() {
  return (
    <Stack>
      <Box>
        <NavBar />
      </Box>
      <Box mt={6}>
        <Container>
          <BrowserRouter>
            <Routes>
              <Route path="/" element={<Home />} />
              <Route path="/login" element={<Login />} />
            </Routes>
          </BrowserRouter>
        </Container>
      </Box>
    </Stack>
  )
}

export default App
