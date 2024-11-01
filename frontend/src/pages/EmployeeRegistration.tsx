import { useForm, SubmitHandler } from "react-hook-form"
import UserRegistrationFormInput from "../types/UserRegistrationFormInput"
import { Container, Button, Select, MenuItem, FormControl, InputLabel, Alert } from "@mui/material"
import FormSection from "../components/forms/FormSection"
import TextInputField from "../components/forms/TextInputField"
import { useEffect, useState } from "react"
import Company from "../model/Company"
import { getRequest, unauthenticatedGetRequest, unauthenticatedPostRequest } from "../components/client/client"
import User from "../model/User"
import { login } from "../util/session"
import { useDispatch } from "react-redux"
import { setUser } from "../redux/user"
import { useNavigate } from "react-router-dom"

export default function EmployeeRegistration() {
  const dispatch = useDispatch()
  const navigate = useNavigate()

  const [companies, setCompanies] = useState<Company[]>([new Company('', '', [])])
  const [selectedCompany, setSelectedCompany] = useState<number>(0)
  const [error, setError] = useState<string>('')

  const {
    control,
    handleSubmit,
    setValue,
    formState: { errors }
  } = useForm<UserRegistrationFormInput>({
    defaultValues: {
      account: {
        username: '',
        name: '',
        role: 'EMPLOYEE'
      },
      company: {
        name: '',
        location: ''
      }
    }
  })

  const onSubmit: SubmitHandler<UserRegistrationFormInput> = (data) => {
    unauthenticatedPostRequest('user/register', {...data})
      .then(res => {
        return (async () => {
          if (res.ok) {
            setError('')
            const user = new User(data.account.username, 'EMPLOYEE')
            const token = await res.text()
            login(user, token)
            dispatch(setUser(user.serialize()))
            return true
          } else {
            const body = await res.json()
            setError(body.errors[0])
            return false
          }
        })()
      })
      .then(ok => ok && navigate('/'))
      .catch(() => setError('Invalid registration details'))
  }

  // Get the names and locations of all registered companies for the user to choose from
  useEffect(() => {
    unauthenticatedGetRequest('company/all')
      .then(res => res.json())
      .then(data => setCompanies(data))
      .then(() => setError(''))
      .catch(() => setError('Failed to load companies'))
  }, [])

  // Select the first company by default once the companies are loaded
  useEffect(() => {
    if (companies.length > 0) {
      setSelectedCompany(0)
      setValue("company", companies[0])
    }
  }, [companies])

  // Update the selected company in the form
  useEffect(() => {
    setValue("company", companies[selectedCompany])
  }, [selectedCompany])

  return (
    <Container fixed>
      <form onSubmit={handleSubmit(onSubmit)}>
        {error && <Alert severity="error">{error}</Alert>}
        
        <FormSection title="Employee Details">
          <TextInputField name="account.username" label="Username" control={control} />
          <TextInputField name="account.name" label="Name" control={control} />
          <TextInputField name="account.password" label="Password" control={control} password />

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

        <Button id="register-button" variant="contained" type="submit">Register</Button>
      </form>
    </Container>
  )
}