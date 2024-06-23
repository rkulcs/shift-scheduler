import { removeJWT } from "./jwt";

export function logout() {
  removeJWT()
  localStorage.removeItem('username')
  localStorage.removeItem('role')
}