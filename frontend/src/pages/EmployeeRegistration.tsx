import { useForm, SubmitHandler } from "react-hook-form"
import UserRegistrationFormInput from "../types/UserRegistrationFormInput"
import { Container, Button, Select, MenuItem, FormControl, InputLabel, FormHelperText } from "@mui/material"
import FormSection from "../components/forms/FormSection"
import TextInputField from "../components/forms/TextInputField"
import { useEffect, useState } from "react"

export default function EmployeeRegistration() {
  const [companies, setCompanies] = useState<Company[]>([{name: 'a', location: ''}])
  const [selectedCompany, setSelectedCompany] = useState<number>(0)

  const {
    control,
    handleSubmit,
    watch,
    setValue,
    formState: { errors }
  } = useForm<UserRegistrationFormInput>()

  const onSubmit: SubmitHandler<UserRegistrationFormInput> = (data) => {
    fetch(`${import.meta.env.VITE_API_URL}/user/register`,
      {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json',
        },
        body: JSON.stringify({...data, role: "EMPLOYEE"})
      }
    ).then(res => console.log(res))
  }

  // Get the names and locations of all registered companies for the user to choose from
  useEffect(() => {
    fetch(`${import.meta.env.VITE_API_URL}/company`,
      {
        method: 'GET',
        headers: {
          'Accept': 'application/json',
        },
      }
    )
    .then(res => res.json())
    .then(data => setCompanies(data))
  }, [])

  // Select the first company by default once the companies are loaded
  useEffect(() => {
    if (companies.length > 0) {
      setSelectedCompany(0)
    }
  }, [companies])

  // Update the selected company in the form
  useEffect(() => {
    setValue("company", companies[selectedCompany])
  }, [selectedCompany])

  return (
    <Container fixed>
      <form onSubmit={handleSubmit(onSubmit)}>
        
        <FormSection title="Employee Details">
          <TextInputField name="username" label="Username" control={control} />
          <TextInputField name="name" label="Name" control={control} />
          <TextInputField name="password" label="Password" control={control} password />

          <FormControl fullWidth>
            <InputLabel id="company-label">Company</InputLabel>
            <Select 
              labelId="company-label"
              label="Company"
              value={selectedCompany}
              onChange={e => setSelectedCompany(e.target.value)}
              required
            >
              {companies.map((company, i) => <MenuItem key={company.name} value={i}>{company.name}</MenuItem>)}
            </Select>
          </FormControl>
        </FormSection>

        <Button variant="contained" type="submit">Register</Button>
      </form>
    </Container>
  )
}