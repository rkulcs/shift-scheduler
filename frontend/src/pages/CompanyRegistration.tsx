import { Container, Button } from "@mui/material"
import { useForm, SubmitHandler } from "react-hook-form"
import UserRegistrationFormInput from "../types/UserRegistrationFormInput"
import FormSection from "../components/forms/FormSection"
import TextInputField from "../components/forms/TextInputField"
import { unauthenticatedPostRequest } from "../components/client/client"

export default function CompanyRegistration() {
  const {
    control,
    handleSubmit,
    formState: { errors }
  } = useForm<UserRegistrationFormInput>()

  const onSubmit: SubmitHandler<UserRegistrationFormInput> = (data) => {
    unauthenticatedPostRequest('user/register', {...data, role: "MANAGER"})
      .then(res => console.log(res))
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