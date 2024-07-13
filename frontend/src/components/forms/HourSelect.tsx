import { Box, FormControl, InputLabel, Select, MenuItem } from "@mui/material"
import { SelectInputProps } from "@mui/material/Select/SelectInput"
import { VALID_HOURS } from "../../model/TimePeriod"

type HourSelectProps = {
  i?: number
  label: string
  value: number
  min?: number,
  max?: number,
  onChange: SelectInputProps['onChange']
}

export default function HourSelect({ i, label, value, min, max, onChange }: HourSelectProps) {

  function generateOptions() {
    if (min !== undefined && max !== undefined) {
      const options: MenuItem[] = []

      for (let v = min; v <= max; v += 4)
        options.push(<MenuItem key={v} value={v}>{v}</MenuItem>)

      return options
    } else {
      return VALID_HOURS.map(v => <MenuItem key={v} value={v}>{v}</MenuItem>)
    }
  }

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
          {generateOptions()}
        </Select>
      </FormControl>
    </Box>
  )
}