import { Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow } from "@mui/material";
import { WeeklySchedule } from "../model/WeeklySchedule";
import { Day } from "../model/Day";
import { VALID_HOURS } from "../model/TimePeriod";

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

  return (
    <TableContainer component={Paper}>
      <Table>
        <TableHead>
          <TableRow>
            <TableCell>Time</TableCell>
            {Object.keys(Day).filter(key => isNaN(key)).map(day => <TableCell>{day}</TableCell>)}
          </TableRow>
        </TableHead>
        <TableBody>
          {VALID_HOURS.slice(0, -1).map(hour => {
            return (
              <TableRow>
                <TableCell>{`${hour}:00`}</TableCell>
                {Object.keys(Day).filter(key => isNaN(key)).map(day => {
                  const shifts = blocks[day][hour]

                  return (
                    <TableCell 
                      sx={{ 
                        backgroundColor: shifts.length === 0 ? '#8ca18c' : '#6fbf73' 
                      }}
                    >
                      {shifts.length}
                      {/* {shifts && <p>{shifts.map((shift: Shift) => shift.employee.id).join('\n')}</p>} */}
                    </TableCell>
                  )
                })}
              </TableRow>
            )
          })}
        </TableBody>
      </Table>
    </TableContainer>
  )
}