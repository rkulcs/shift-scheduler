import Company from "../model/Company"

type UserRegistrationFormInput = { 
  username: string,
  name: string,
  password: string,
  company: Company
}

export default UserRegistrationFormInput
