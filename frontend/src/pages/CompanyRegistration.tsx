import { Container, Divider, TextField, Typography, Box, Button } from "@mui/material"
import { useForm, SubmitHandler, Controller } from "react-hook-form"

type CompanyRegistrationFormInput = {
  username: string,
  name: string,
  password: string,
  company: Company
}

const userFields = {
  "username": "Username",
  "name": "Name",
  "password": "Password",
}

const companyFields = {
  "company.name": "Name",
  "company.location": "Location"
}

export default function CompanyRegistration() {
  const {
    control,
    handleSubmit,
    watch,
    formState: { errors }
  } = useForm<CompanyRegistrationFormInput>()

  const onSubmit: SubmitHandler<CompanyRegistrationFormInput> = data => console.log(data)

  return (
    <Container fixed>
      <form onSubmit={handleSubmit(onSubmit)}>
        <Typography variant="subtitle1">Manager Details</Typography>
        <Divider />
        <Box mt={2}>
          {Object.keys(userFields).map((fieldName: any) => {
            const fieldLabel: string = userFields[fieldName]

            return (
              <Controller
                name={fieldName}
                control={control}
                render={({ field }) => {
                  return (
                    <TextField
                      required
                      label={fieldLabel}
                      {...field}
                      sx={{ mb: 2, display: 'block' }}
                      fullWidth
                    />
                  )
                }}
              />
            )
          })}
        </Box>

        <Typography variant="subtitle1">Company Details</Typography>
        <Divider />
        <Box mt={2}>
          {Object.keys(companyFields).map((fieldName: any) => {
            const fieldLabel: string = companyFields[fieldName]

            return (
              <Controller
                name={fieldName}
                control={control}
                render={({ field }) => {
                  return (
                    <TextField
                      required
                      label={fieldLabel}
                      {...field} sx={{ mb: 2, display: 'block' }}
                      fullWidth
                    />
                  )
                }}
              />
            )
          })}
        </Box>

        <Button variant="contained" type="submit">Register</Button>
      </form>
    </Container>
  )
}