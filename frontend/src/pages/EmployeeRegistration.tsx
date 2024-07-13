import { useForm, SubmitHandler } from "react-hook-form"
import UserRegistrationFormInput from "../types/UserRegistrationFormInput"
import { Container, Button, Select, MenuItem, FormControl, InputLabel } from "@mui/material"
import FormSection from "../components/forms/FormSection"
import TextInputField from "../components/forms/TextInputField"
import { useEffect, useState } from "react"
import Company from "../model/Company"
import { getRequest, unauthenticatedPostRequest } from "../components/client/client"

export default function EmployeeRegistration() {
  const [companies, setCompanies] = useState<Company[]>([new Company('', '', [])])
  const [selectedCompany, setSelectedCompany] = useState<number>(0)

  const {
    control,
    handleSubmit,
    setValue,
    formState: { errors }
  } = useForm<UserRegistrationFormInput>()

  const onSubmit: SubmitHandler<UserRegistrationFormInput> = (data) => {
    unauthenticatedPostRequest('user/register', {...data, role: "EMPLOYEE"})
      .then(res => console.log(res))
  }

  // Get the names and locations of all registered companies for the user to choose from
  useEffect(() => {
    getRequest('company/all')
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
              onChange={e => setSelectedCompany(e.target.value as number)}
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