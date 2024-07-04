import { Alert, List, ListItem, ListItemAvatar, ListItemIcon, ListItemText, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Typography } from "@mui/material"
import { WeeklySchedule } from "../model/WeeklySchedule"
import { Day } from "../model/Day"
import { VALID_HOURS } from "../model/TimePeriod"
import { Shift } from "../model/Shift"
import PersonIcon from '@mui/icons-material/Person'

export default function Schedule({ schedule }: { schedule: WeeklySchedule }) {
  // Store each shift in a map for easier access
  const blocks = new Object()
  Object.keys(Day).filter(key => isNaN(key)).map((day: string) => blocks[day] = new Object())
  Object.keys(blocks).map(day => VALID_HOURS.slice(0, -1).map(hour => blocks[day][hour] = new Array()))

  schedule.dailySchedules.forEach(schedule => {
    schedule.shifts.forEach(shift => {
      for (let i = shift.startHour; i < shift.endHour; i += 4) {
        blocks[schedule.day][i].push(shift)
      }
    })
  })

  function hasConstraintViolations(schedule: WeeklySchedule): boolean {
    return (schedule.constraintViolations && schedule.constraintViolations.length > 0) as boolean
  }

  function getCellColour(shifts: Shift[]) {
    if (shifts.length === 0)
      return '#8ca18c'

    const username = localStorage.getItem('username')

    // Use a different colour for blocks in which the user is scheduled to work
    for (let shift of shifts) {
      if (shift.employee.account.username === username)
        return '#35baf6'
    }
    
    return '#6fbf73'
  }

  return (
    <>
    {hasConstraintViolations(schedule) && <Alert severity="warning">
      The following issues were detected with this schedule:
      <ul>
        {schedule.constraintViolations.map(violation => {
          return <li>{violation.description}</li>
        })}
      </ul>
    </Alert>}
    <TableContainer component={Paper}>
      <Table>
        <TableHead>
          <TableRow>
            <TableCell>Time</TableCell>
            {Object.keys(Day).filter(key => isNaN(key)).map(day => <TableCell key={day}>{day}</TableCell>)}
          </TableRow>
        </TableHead>
        <TableBody>
          {VALID_HOURS.slice(0, -1).map(hour => {
            return (
              <TableRow key={hour}>
                <TableCell>{`${hour}:00`}</TableCell>
                {Object.keys(Day).filter(key => isNaN(key)).map(day => {
                  const shifts = blocks[day][hour]

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
                              <ListItemAvatar sx={{ width: '20%', minWidth: 0 }}><PersonIcon fontSize="5%"/></ListItemAvatar>
                              <ListItemText primaryTypographyProps={{ fontSize: '80%' }} primary={shift.employee.account.name}/>
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