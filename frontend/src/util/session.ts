import User from "../model/User";
import { removeJWT, storeJWT } from "./jwt";

export function login(user: User, token: string) {
  localStorage.setItem('username', user.username)
  localStorage.setItem('role', user.role)
  storeJWT(token)
}

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