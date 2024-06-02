import { Grid, Button } from "@mui/material";
import { Link } from "react-router-dom";

const buttons = [
  {
    label: 'Login',
    route: 'login'
  },
  {
    label: 'Company Registration',
    route: 'register'
  },
  {
    label: 'Employee Registration',
    route: 'register'
  }
]

export default function Home() {
  return (
    <Grid mt={7} container spacing={4} alignItems="center" justifyContent="center">
      <Grid container justifyContent="center" spacing={2}>
        {buttons.map((button, i) => (
          <Grid key={i} item>
            <Button
              variant="contained"
              sx={{
                height: 140,
                width: 140
              }}
            >
              <Link style={{ color: 'inherit', textDecoration: 'none' }} to={button.route}>{button.label}</Link>
            </Button>
          </Grid>
        ))}
      </Grid>
    </Grid>
  )
}