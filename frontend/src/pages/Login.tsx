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
import { postRequest } from "../components/client/client"

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
    postRequest('user/login', {...data})
      .then(res => {
        if (res.ok) {
          res.json()
            .then(body => storeJWT(body.token))
            .then(() => {
              // Store username and role locally
              const payload: JwtPayload = jwtDecode(getJWT() as string)
              const user = new User(payload.sub as string, payload.role as string)
              localStorage.setItem('username', user.username)
              localStorage.setItem('role', user.role)
              dispatch(setUser(user.serialize()))
            })
            .then(() => navigate('/'))
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