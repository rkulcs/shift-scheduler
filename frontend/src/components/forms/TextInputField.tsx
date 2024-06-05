import { Control, Controller } from "react-hook-form"
import { TextField } from "@mui/material"

type TextInputFieldProps = {
  name: string,
  control: Control<any, any>
  label: string,
  password?: boolean
  error?: string
}

export default function TextInputField({ name, control, label, password, error }: TextInputFieldProps) {
  return (
    <Controller
      name={name}
      control={control}
      render={({ field }) => {
        return (
          <TextField
            required
            error={error ? true : undefined}
            helperText={error ? error : undefined}
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