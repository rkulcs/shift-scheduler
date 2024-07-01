import { Grid, Button } from "@mui/material";
import { Link } from "react-router-dom";
import { isValidJWTStored } from "../util/jwt";

const buttons = [
  {
    label: 'Login',
    route: 'login'
  },
  {
    label: 'Company Registration',
    route: 'register-company'
  },
  {
    label: 'Employee Registration',
    route: 'register-employee'
  }
]

export default function Home() {
  function renderUnauthenticatedHome() {
    return (
      <Grid mt={7} container spacing={4} alignItems="center" justifyContent="center">
        <Grid container justifyContent="center" spacing={2}>
          {buttons.map((button, i) => (
            <Grid key={i} item>
              <Link to={button.route}>
                <Button
                  variant="contained"
                  sx={{
                    height: 140,
                    width: 140
                  }}
                >
                  {button.label}
                </Button>
              </Link>
            </Grid>
          ))}
        </Grid>
      </Grid>
    )
  }

  return (
    <>
      {!isValidJWTStored() && renderUnauthenticatedHome()}
    </>
  )
}