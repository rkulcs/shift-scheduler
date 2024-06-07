export type UserDetails = {
  username: string
  role: string
}

export default class User {
  username: string
  role: string

  constructor(username: string, role: string) {
    this.username = username 
    this.role = role
  }

  serialize() {
    return {
      username: this.username,
      role: this.role
    }
  }
}
