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
    setValue,
    formState: { errors }
  } = useForm<UserRegistrationFormInput>({
    defaultValues: {
      account: {
        username: '',
        name: '',
        role: 'MANAGER'
      },
      company: {
        name: '',
        location: ''
      }
    }
  })

  const [error, setError] = useState<string>('')

  const onSubmit: SubmitHandler<UserRegistrationFormInput> = (data) => {
    console.log(data)

    unauthenticatedPostRequest('user/register', {...data})
      .then(res => {
        return (async () => {
          if (res.ok) {
            setError('')
            const user = new User(data.account.username, 'MANAGER')
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

  return (
    <Container fixed>
      <form onSubmit={handleSubmit(onSubmit)}>
        {error && <Alert severity="error">{error}</Alert>}
        <FormSection title="Manager Details">
          <TextInputField name="account.username" label="Username" control={control} />
          <TextInputField name="account.name" label="Name" control={control} />
          <TextInputField name="account.password" label="Password" control={control} password />
        </FormSection>

        <FormSection title="Company Details">
          <TextInputField name="company.name" label="Name" control={control} />
          <TextInputField name="company.location" label="Location" control={control} />
        </FormSection>

        <Button id="register-button" variant="contained" type="submit">Register</Button>
      </form>
    </Container>
  )
}