import { Container, Button, Alert } from "@mui/material"
import { useForm, SubmitHandler } from "react-hook-form"
import UserRegistrationFormInput from "../types/UserRegistrationFormInput"
import FormSection from "../components/forms/FormSection"
import TextInputField from "../components/forms/TextInputField"
import { unauthenticatedPostRequest } from "../components/client/client"
import { useState } from "react"
import { useDispatch } from "react-redux"
import User from "../model/User"
import { setUser } from "../redux/user"
import { storeJWT } from "../util/jwt"
import { useNavigate } from "react-router-dom"
import { login } from "../util/session"

export default function CompanyRegistration() {
  const dispatch = useDispatch()
  const navigate = useNavigate()

  const {
    control,
    handleSubmit,
    formState: { errors }
  } = useForm<UserRegistrationFormInput>()

  const [error, setError] = useState<string>('')

  const onSubmit: SubmitHandler<UserRegistrationFormInput> = (data) => {
    unauthenticatedPostRequest('user/register', {...data, role: "MANAGER"})
      .then(res => res.json())
      .then(body => {
        if (body.error) {
          setError(body.error)
          return false
        } else {
          setError('')
          const user = new User(data.username, 'MANAGER')
          login(user, body.token)
          dispatch(setUser(user.serialize()))
          return true
        }
      })
      .then(ok => ok && navigate('/'))
      .catch(() => setError('Invalid registration details'))
  }

  return (
    <Container fixed>
      <form onSubmit={handleSubmit(onSubmit)}>
        {error && <Alert severity="error">{error}</Alert>}
        <FormSection title="Manager Details">
          <TextInputField name="username" label="Username" control={control} />
          <TextInputField name="name" label="Name" control={control} />
          <TextInputField name="password" label="Password" control={control} password />
        </FormSection>

        <FormSection title="Company Details">
          <TextInputField name="company.name" label="Name" control={control} />
          <TextInputField name="company.location" label="Location" control={control} />
        </FormSection>

        <Button variant="contained" type="submit">Register</Button>
      </form>
    </Container>
  )
}