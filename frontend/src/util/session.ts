import { removeJWT } from "./jwt";

export function logout() {
  removeJWT()
  localStorage.removeItem('username')
  localStorage.removeItem('role')
}

export function isUserEmployee() {
  return localStorage.getItem('role') === 'EMPLOYEE'
}

export function isUserManager() {
  return localStorage.getItem('role') === 'MANAGER'
}