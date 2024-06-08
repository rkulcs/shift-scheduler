import { Typography, Divider, Box } from "@mui/material"

export default function FormSection({ title, children }: { title: string, children: JSX.Element | JSX.Element[] }) {
  return (
    <>
      <Typography variant="subtitle1">{title}</Typography>
      <Divider />
      <Box mt={2} mb={2}>
        {children}
      </Box>
    </>
  )
}