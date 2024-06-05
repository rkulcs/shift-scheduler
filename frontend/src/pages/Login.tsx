import { useForm, SubmitHandler } from "react-hook-form"
import { Container, Button } from "@mui/material"
import LoginFormInput from "../types/LoginFormInput"
import FormSection from "../components/forms/FormSection"
import TextInputField from "../components/forms/TextInputField"
import { storeJWT } from "../util/jwt"
import { redirect, useNavigate } from "react-router-dom"

export default function Login() {
  const navigate = useNavigate()

  const {
    control,
    handleSubmit,
    watch,
    setError,
    formState: { errors }
  } = useForm<LoginFormInput>()

  const onSubmit: SubmitHandler<LoginFormInput> = (data) => {
    fetch(`${import.meta.env.VITE_API_URL}/user/login`,
      {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json',
        },
        body: JSON.stringify({...data})
      }
    ).then(res => {
      if (res.ok) {
        res.json()
          .then(body => storeJWT(body.token))
          .then(() => {
            navigate('/')
          })
      } else {
        res.json().then(body => {
          const invalidField = (body.error.includes('password')) ?
                                                      'password' : 'username'

          setError(invalidField, { type: 'manual', message: body.error })
        })
      }
    })
  }
  
  return (
    <Container fixed>
      <form onSubmit={handleSubmit(onSubmit)}>
        <FormSection title="Login">
          <TextInputField 
            name="username" 
            label="Username" 
            error={errors.username ? errors.username.message : undefined}
            control={control} />
          <TextInputField 
            name="password" 
            label="Password" 
            error={errors.password ? errors.password.message : undefined}
            control={control} 
            password />
        </FormSection>

        <Button variant="contained" type="submit">Log In</Button>
      </form>
    </Container>    
  )
}