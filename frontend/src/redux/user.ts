import { createSlice } from '@reduxjs/toolkit'
import User from '../model/User'

const { actions, reducer } = createSlice({
  name: 'user',
  initialState: {
    user: User.new('', '')
  },
  reducers: {
    setUser: (state, data) => {
      state.user = data.payload 
    }
  }
})

export const { setUser } = actions
export default reducer 