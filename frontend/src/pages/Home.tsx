import { useState } from "react";
import { useStore } from "react-redux";
import { getUserRole } from "../redux/store";
import TabbedIntroductions from "../components/TabbedIntroductions";
import { CompanyDashboard, EmployeeDashboard } from "../components/Dashboard";

export default function Home() {
  const store = useStore()

  // Change the displayed content depending on the role of the current user
  const [role, setRole] = useState<string>(getUserRole())

  // Update the home page if the user logs in or out
  store.subscribe(() => {
    setRole(getUserRole())
  })

  return (
    <>
      {!role && <TabbedIntroductions/>}
      {role === 'EMPLOYEE' && <EmployeeDashboard/>}
      {role === 'MANAGER' && <CompanyDashboard/>}
    </>
  )
}
