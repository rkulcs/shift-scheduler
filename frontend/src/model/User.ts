export default class User {
  static new(username: string, role: string) {
    return {
      username: username,
      role: role
    }
  }
}