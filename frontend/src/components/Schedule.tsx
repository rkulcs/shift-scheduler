import { Alert, Box, List, ListItem, ListItemAvatar, ListItemText, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow } from "@mui/material"
import { WeeklySchedule } from "../model/WeeklySchedule"
import { Day } from "../model/Day"
import { VALID_HOURS } from "../model/TimePeriod"
import { Shift } from "../model/Shift"
import PersonIcon from '@mui/icons-material/Person'

export default function Schedule({ schedule }: { schedule: WeeklySchedule }) {
  // Store each shift in a map for easier access
  const blocks = new Map<string, Map<Number, Shift[]>>()
  Object.keys(Day).filter(key => isNaN(parseInt(key))).forEach(day => blocks.set(day, new Map()))
  blocks.forEach(entry => VALID_HOURS.slice(0, -1).forEach(hour => entry.set(hour, new Array())))

  schedule.dailySchedules.forEach(dailySchedule => {
    dailySchedule.shifts.forEach(shift => {
      for (let i = shift.startHour; i < shift.endHour; i += 4) {
        blocks.get(dailySchedule.day.toString())?.get(i)?.push(shift)
      }
    })
  })

  function hasConstraintViolations(schedule: WeeklySchedule): boolean {
    return (schedule.constraintViolations && schedule.constraintViolations.length > 0) as boolean
  }

  function getCellColour(shifts: Shift[] | undefined) {
    if (shifts === undefined || shifts.length === 0)
      return '#8ca18c'

    const username = localStorage.getItem('username')

    // Use a different colour for blocks in which the user is scheduled to work
    for (let shift of shifts) {
      if (shift.employee.account?.username === username)
        return '#81d4fa'
    }
    
    return '#6fbf73'
  }

  return (
    <>
    {hasConstraintViolations(schedule) &&
      <Box mb={2}>
        <Alert severity="warning">
        The following issues were detected in this schedule:
        <ul>
          {schedule.constraintViolations?.map(violation => {
            return <li>{violation.description}</li>
          })}
        </ul>
      </Alert>
      </Box>}
    <TableContainer component={Paper}>
      <Table>
        <TableHead>
          <TableRow>
            <TableCell>Time</TableCell>
            {Object.keys(Day).filter(key => isNaN(parseInt(key))).map(day => <TableCell key={day}>{day}</TableCell>)}
          </TableRow>
        </TableHead>
        <TableBody>
          {VALID_HOURS.slice(0, -1).map(hour => {
            return (
              <TableRow key={hour}>
                <TableCell>{`${hour}:00`}</TableCell>
                {Object.keys(Day).filter(key => isNaN(parseInt(key))).map(day => {
                  const shifts = blocks.get(day.toString())?.get(hour)

                  return (
                    <TableCell
                      key={day}
                      size="small"
                      padding="none"
                      align="center"
                      sx={{
                        border: 1,
                        backgroundColor: getCellColour(shifts) 
                      }}
                    >
                      <List sx={{ padding: 0.5 }} dense={true}>
                        {shifts && shifts.map((shift: Shift, i: number) => {
                          return (
                            <ListItem key={i} sx={{ padding: 0 }}>
                              <ListItemAvatar sx={{ width: '20%', minWidth: 0 }}><PersonIcon/></ListItemAvatar>
                              <ListItemText primaryTypographyProps={{ fontSize: '80%' }} primary={shift.employee.account?.name}/>
                            </ListItem>
                          )
                        })}
                      </List>
                    </TableCell>
                  )
                })}
              </TableRow>
            )
          })}
        </TableBody>
      </Table>
    </TableContainer>
    </>
  )
}