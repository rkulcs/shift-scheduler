import { Dialog } from "@mui/material"

type FormDialogProps = {
  onClose: any
  open: any 
  children?: undefined | any
}

export default function FormDialog({ onClose, open, children }: FormDialogProps) {
  return (
    <Dialog onClose={onClose} open={open}>
      {children}
    </Dialog>
  )
}
