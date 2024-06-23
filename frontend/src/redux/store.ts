import { configureStore } from '@reduxjs/toolkit'
import user, { setUser } from './user'
import User from '../model/User'

const store = configureStore({
  reducer: user    
})

export function getUser() {
  return store.getState().user
}

export function getUsername() {
  return store.getState().user.username
}

export function getUserRole() {
  return store.getState().user.role
}

/**
 * Initialize user details with the username and role found in localStorage.
 */
export function initStore() {
  const username: string | null = localStorage.getItem('username')
  const role: string | null = localStorage.getItem('role')

  if (username && role)
    store.dispatch(setUser((new User(username, role)).serialize()))
}

export default store
