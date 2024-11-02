import { Grid, Paper, Typography } from "@mui/material"
import { MouseEventHandler, ReactElement } from "react"
import { Link } from "react-router-dom"
import ArrowCircleRightIcon from '@mui/icons-material/ArrowCircleRight'

export default function PageMenu({ children }: { children: undefined | ReactElement<any, any> | ReactElement<any, any>[] }) {
  return (
    <>
      <Grid container spacing={2} justifyContent={'center'}>
        {children}
      </Grid>
    </>
  )
}

type PageMenuItemProps = {
  icon: ReactElement<any, any> 
  title: string
  description: string
  onClick: MouseEventHandler<HTMLAnchorElement>
}

export function PageMenuItem({ icon, title, description, onClick }: PageMenuItemProps) {

  return (
    <Grid item xs={3}>
      <a className="page-menu-item" onClick={onClick}>
        <Paper sx={{ padding: 2 }}>
          {icon}
          <Typography variant="h6">{title}</Typography>
          <Typography sx={{ marginTop: 2, marginBottom: 2 }}>
            {description}
          </Typography>
          <ArrowCircleRightIcon />
        </Paper>
      </a>
    </Grid>
  )
}