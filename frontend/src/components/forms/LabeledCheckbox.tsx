import { Container, FormControlLabel, Checkbox } from "@mui/material"
import { Control, Controller, FieldValues } from "react-hook-form"

type LabeledCheckboxProps = {
  name: string
  label: string
  control: Control<FieldValues>
}

export default function LabeledCheckbox({ name, label, control }: LabeledCheckboxProps) {
  return (
    <Container>
      <FormControlLabel
        control={
          <Controller
            name={name}
            control={control}
            render={({ field: { onChange, value } }) => <Checkbox checked={value} onChange={onChange} />}
          />
        }
        label={label}
      />
    </Container>
  )
}