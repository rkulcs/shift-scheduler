import { useForm, SubmitHandler } from "react-hook-form"
import { Container, Button } from "@mui/material"
import { useNavigate } from "react-router-dom"
import { JwtPayload, jwtDecode } from "jwt-decode"
import LoginFormInput from "../types/LoginFormInput"
import FormSection from "../components/forms/FormSection"
import TextInputField from "../components/forms/TextInputField"
import { getJWT, storeJWT } from "../util/jwt"
import User from "../model/User"
import { useDispatch } from "react-redux"
import { setUser } from "../redux/user"
import { unauthenticatedPostRequest } from "../components/client/client"

interface TokenPayload extends JwtPayload {
  role?: string
}

export default function Login() {
  const navigate = useNavigate()
  const dispatch = useDispatch()

  const {
    control,
    handleSubmit,
    setError,
    formState: { errors }
  } = useForm<LoginFormInput>()

  const onSubmit: SubmitHandler<LoginFormInput> = (data) => {
    unauthenticatedPostRequest('user/login', {...data})
      .then(res => {
        if (res.ok) {
            res.text()
            .then((token: string) => storeJWT(token))
            .then(() => {
              // Store username and role locally
              const payload: TokenPayload = jwtDecode(getJWT() as string)
              const user = new User(payload.sub as string, payload.role as string)
              localStorage.setItem('username', user.username)
              localStorage.setItem('role', user.role)
              dispatch(setUser(user.serialize()))
            })
            .then(() => navigate('/'))
        } else {
          res.json().then(body => {
            const error = body.errors[0]
            const invalidField = (error.includes('password')) ? 'password' : 'username'

            setError(invalidField, { type: 'manual', message: error })
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

        <Button id="login-button" variant="contained" type="submit">Log In</Button>
      </form>
    </Container>    
  )
}