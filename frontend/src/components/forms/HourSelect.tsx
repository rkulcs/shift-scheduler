import { Box, FormControl, InputLabel, Select, MenuItem } from "@mui/material"
import { SelectInputProps } from "@mui/material/Select/SelectInput"
import { VALID_HOURS } from "../../model/TimePeriod"

type HourSelectProps = {
  i: number
  label: string
  value: number
  onChange: SelectInputProps['onChange']
}

export default function HourSelect({ i, label, value, onChange }: HourSelectProps) {

  return (
    <Box mt={1} mb={1}>
      <FormControl fullWidth>
        <InputLabel id={`hour-label-${i}`}>{label}</InputLabel>
        <Select
          labelId={`hour-label-${i}`}
          label={label}
          value={value}
          onChange={onChange}
        >
          {VALID_HOURS.map(v => <MenuItem key={v} value={v}>{v}</MenuItem>)}
        </Select>
      </FormControl>
    </Box>
  )
}