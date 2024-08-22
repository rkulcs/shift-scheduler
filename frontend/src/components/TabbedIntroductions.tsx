import { Box, Button, Paper, Tab, Tabs, Typography } from "@mui/material";
import { ReactNode, useState } from "react";
import { Link } from "react-router-dom";

type ButtonProps = {
  label: string,
  route: string
}

const buttons: ButtonProps[] = [
  {
    label: 'Log In',
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

export default function TabbedIntroductions() {
  const [selection, setSelection] = useState<number>(0)

  return (
    <Box className="introduction" sx={{ flexGrow: 1, display: 'flex' }}>
      <Tabs
        orientation="vertical"
        value={selection}
        onChange={(_, value) => setSelection(value)}
        aria-label="Vertical tabs example"
        sx={{ borderRight: 1, borderColor: 'divider', minWidth: '15%' }}
      >
        <Tab id="about-tab" label="About" />
        <Tab id="managers-tab" label="Managers" />
        <Tab id="employees-tab" label="Employees" />
      </Tabs>
      <TabPanel value={selection} index={0}>
        <Description
          header="Simplified schedule management"
          description="Shift Scheduler is an application that aims to automate 
                         the creation of workplace schedules which meet the needs
                         of businesses, while also respecting the availabilities
                         of their employees."
          button={buttons[0]}
        />
      </TabPanel>
      <TabPanel value={selection} index={1}>
        <Description
          header="Automatic schedule generation"
          description="Generate schedules that meet the needs of your business and 
                         employees."
          button={buttons[1]}
        />
      </TabPanel>
      <TabPanel value={selection} index={2}>
        <Description
          header="View schedules and update availabilities"
          description="Keep track of your scheduled shifts, and update the times 
                         during which you are available to work."
          button={buttons[2]}
        />
      </TabPanel>
    </Box>
  )
}

function TabPanel(props: {children?: ReactNode, index: number, value: number}) {
  const { children, value, index, ...other } = props;

  return (
    <div
      role="tabpanel"
      hidden={value !== index}
      id={`vertical-tabpanel-${index}`}
      aria-labelledby={`vertical-tab-${index}`}
      {...other}
    >
      {value === index && (
        <Box sx={{ p: 3 }}>
          <Typography>{children}</Typography>
        </Box>
      )}
    </div>
  );
}

type DescriptionProps = {
  header: string,
  description: string,
  button: ButtonProps
}

function Description({header, description, button}: DescriptionProps) {
  return (
    <>
      <Typography variant="h4">{header}</Typography>
      <Typography variant="subtitle1" mt={2} mb={2}>{description}</Typography>
      <Link to={button.route}>
        <Button className="home-button" variant="contained">
          {button.label}
        </Button>
      </Link>
    </>
  )
}