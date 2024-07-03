import { useState, useEffect, MouseEvent } from 'react'
import AppBar from '@mui/material/AppBar'
import Box from '@mui/material/Box'
import Toolbar from '@mui/material/Toolbar'
import IconButton from '@mui/material/IconButton'
import Typography from '@mui/material/Typography'
import Menu from '@mui/material/Menu'
import Container from '@mui/material/Container'
import Avatar from '@mui/material/Avatar'
import Button from '@mui/material/Button'
import Tooltip from '@mui/material/Tooltip'
import MenuItem from '@mui/material/MenuItem'
import { Link, useNavigate } from 'react-router-dom'

import { removeJWT } from '../util/jwt'
import { UserDetails } from '../model/User'
import { useDispatch, useStore } from 'react-redux'
import { removeUser } from '../redux/user'
import { getUser, getUsername } from '../redux/store'
import { logout } from '../util/session'

type PageMapping = {
  label: string
  route: string
}

const commonPages: PageMapping[] = [
  {
    label: 'Home',
    route: '/'
  },
]

const staffPages: PageMapping[] = [
  {
    label: 'View Schedules',
    route: '/schedules'
  }
]

const managerPages: PageMapping[] = [
  ...commonPages,
  {
    label: 'Hours of Operation',
    route: '/hours'
  },
  {
    label: 'New Schedules',
    route: '/generate-schedules'
  },
  ...staffPages
]

const employeePages: PageMapping[] = [
  ...commonPages,
  {
    label: 'Availabilities',
    route: '/availabilities'
  },
  ...staffPages
]

/**
 * Navbar component based on sample MUI code from https://codesandbox.io/p/sandbox/rough-morning-8hwp3y?file=%2Fsrc%2FDemo.tsx
 */
export default function NavBar() {  
  const navigate = useNavigate()
  const dispatch = useDispatch()
  const store = useStore()

  const [userDetails, setUserDetails] = useState<UserDetails>(getUser())
  const [pages, setPages]= useState<PageMapping[]>(commonPages)

  const [anchorElNav, setAnchorElNav] = useState<null | HTMLElement>(null)
  const [anchorElUser, setAnchorElUser] = useState<null | HTMLElement>(null)

  store.subscribe(() => {
    setUserDetails(getUser())
  })

  useEffect(() => {
    let newPages: PageMapping[] = commonPages 

    switch (userDetails.role) {
      case 'MANAGER':
        newPages = managerPages
        break
      case 'EMPLOYEE':
        newPages = employeePages 
        break
    }

    setPages(newPages)
  }, [userDetails])

  function handleOpenNavMenu(event: MouseEvent<HTMLElement>) {
    setAnchorElNav(event.currentTarget)
  }

  function handleOpenUserMenu(event: MouseEvent<HTMLElement>) {
    setAnchorElUser(event.currentTarget)
  }

  function handleCloseNavMenu() {
    setAnchorElNav(null)
  }

  function handleCloseUserMenu() {
    setAnchorElUser(null)
  }

  function handleLogout() {
    logout()
    dispatch(removeUser())
    handleCloseUserMenu()
    navigate('/')
  }

  return (
    <AppBar position="static">
      <Container maxWidth="xl">
        <Toolbar style={{ height: 0 }} variant="dense" disableGutters>
          <Box sx={{ flexGrow: 1, display: { xs: 'none', md: 'none' } }}>
            <IconButton
              size="large"
              aria-label="account of current user"
              aria-controls="menu-appbar"
              aria-haspopup="true"
              onClick={handleOpenNavMenu}
              color="inherit"
            >
            </IconButton>
            <Menu
              id="menu-appbar"
              anchorEl={anchorElNav}
              anchorOrigin={{
                vertical: 'bottom',
                horizontal: 'left',
              }}
              keepMounted
              transformOrigin={{
                vertical: 'top',
                horizontal: 'left',
              }}
              open={Boolean(anchorElNav)}
              onClose={handleCloseNavMenu}
              sx={{
                display: { xs: 'block', md: 'none' },
              }}
            >
              {pages.map((page) => (
                <MenuItem key={page.label} onClick={handleCloseNavMenu}>
                  <Typography textAlign="center">{page.label}</Typography>
                </MenuItem>
              ))}
            </Menu>
          </Box>
          <Box sx={{ flexGrow: 1, display: { xs: 'none', md: 'flex' } }}>
            {pages.map((page) => (
              <Link key={page.label} to={page.route}>
                <Button
                  onClick={handleCloseNavMenu}
                  sx={{ my: 2, color: 'white', display: 'block' }}
                >
                  {page.label}
                </Button>
              </Link>
            ))}
          </Box>

          {getUsername() && <Box sx={{ flexGrow: 0 }}>
            <Tooltip title={userDetails.username ? userDetails.username : undefined}>
              <IconButton onClick={handleOpenUserMenu} sx={{ p: 0 }}>
                <Avatar alt="User Name" />
              </IconButton>
            </Tooltip>
            <Menu
              sx={{ mt: '45px' }}
              id="menu-appbar"
              anchorEl={anchorElUser}
              anchorOrigin={{
                vertical: 'top',
                horizontal: 'right',
              }}
              keepMounted
              transformOrigin={{
                vertical: 'top',
                horizontal: 'right',
              }}
              open={Boolean(anchorElUser)}
              onClose={handleCloseUserMenu}
            >
              <MenuItem key="logout" onClick={handleLogout}>
                <Typography textAlign="center">Log Out</Typography>
              </MenuItem>
            </Menu>
          </Box>}
        </Toolbar>
      </Container>
    </AppBar>
  )
}