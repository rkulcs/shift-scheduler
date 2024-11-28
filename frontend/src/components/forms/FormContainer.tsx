import { Container } from "@mui/material";

export default function FormContainer({ children }: { children: undefined | any }) {
  return (
    <Container fixed sx={{ padding: 3, width: '80vw' }}>
      {children}
    </Container>
  )
}