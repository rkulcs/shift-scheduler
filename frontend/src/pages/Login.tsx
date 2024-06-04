import { useForm, SubmitHandler } from "react-hook-form"
import { Container, Button } from "@mui/material"
import LoginFormInput from "../types/LoginFormInput"
import FormSection from "../components/forms/FormSection"
import TextInputField from "../components/forms/TextInputField"

export default function Login() {
  const {
    control,
    handleSubmit,
    watch,
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
    ).then(res => console.log(res))
  }
  
  return (
    <Container fixed>
      <form onSubmit={handleSubmit(onSubmit)}>
        <FormSection title="Login">
          <TextInputField name="username" label="Username" control={control} />
          <TextInputField name="password" label="Password" control={control} password />
        </FormSection>

        <Button variant="contained" type="submit">Log In</Button>
      </form>
    </Container>    
  )
}