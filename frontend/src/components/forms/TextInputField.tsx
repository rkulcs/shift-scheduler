import { Control, Controller } from "react-hook-form"
import { TextField } from "@mui/material"

type TextInputFieldProps = {
  name: string,
  control: Control<any, any>
  label: string,
  password?: boolean
}

export default function TextInputField({ name, control, label, password }: TextInputFieldProps) {
  return (
    <Controller
      name={name}
      control={control}
      render={({ field }) => {
        return (
          <TextField
            required
            label={label}
            {...field}
            type={password ? "password" : "text"}
            sx={{ mb: 2, display: 'block' }}
            fullWidth
          />
        )
      }}
    />
  )
}