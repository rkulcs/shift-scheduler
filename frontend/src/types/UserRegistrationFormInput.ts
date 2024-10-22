import Company from "../model/Company"

type UserRegistrationFormInput = { 
  account: {
    username: string,
    name: string,
    password: string,
    role: string
  },
  company: {
    name: string,
    location: string
  }
}

export default UserRegistrationFormInput
