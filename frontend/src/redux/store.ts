import { configureStore } from '@reduxjs/toolkit'
import user, { setUser } from './user'

const store = configureStore({
  reducer: user    
})

export function getUsername() {
  return store.getState().user.username
}

export function getUserRole() {
  return store.getState().user.role
}

export default store
