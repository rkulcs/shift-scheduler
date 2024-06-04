import { Container, Button } from "@mui/material"
import { useForm, SubmitHandler } from "react-hook-form"
import UserRegistrationFormInput from "../types/UserRegistrationFormInput"
import FormSection from "../components/forms/FormSection"
import TextInputField from "../components/forms/TextInputField"

export default function CompanyRegistration() {
  const {
    control,
    handleSubmit,
    watch,
    formState: { errors }
  } = useForm<UserRegistrationFormInput>()

  const onSubmit: SubmitHandler<UserRegistrationFormInput> = (data) => {
    fetch(`${import.meta.env.VITE_API_URL}/user/register`,
      {
        method: 'POST',
        mode: 'cors',
        headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json',
        },
        body: JSON.stringify({...data, role: "MANAGER"})
      }
    ).then(res => console.log(res))
  }

  return (
    <Container fixed>
      <form onSubmit={handleSubmit(onSubmit)}>
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