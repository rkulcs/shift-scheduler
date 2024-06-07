import { createSlice } from '@reduxjs/toolkit'
import User from '../model/User'

const { actions, reducer } = createSlice({
  name: 'user',
  initialState: {
    user: (new User('', '')).serialize()
  },
  reducers: {
    setUser: (state, data) => {
      state.user = data.payload 
    },
    removeUser: (state) => {
      state.user = (new User('', '')).serialize()
    }
  }
})

export const { setUser, removeUser } = actions
export default reducer 