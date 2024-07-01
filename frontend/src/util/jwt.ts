import { jwtDecode } from "jwt-decode"

export function storeJWT(token: string) {
    localStorage.setItem('token', `Bearer ${token}`)
}

export function getJWT() {
    return localStorage.getItem('token')
}

export function removeJWT() {
    // TODO: Implement server-side logout
    localStorage.removeItem('token')
}

export function isValidJWTStored(): boolean {
    const token = localStorage.getItem('token')

    if (token === null)
        return false

    const decodedToken = jwtDecode(token)

    if (decodedToken.exp)
        return decodedToken.exp*1000 >= Date.now()
    else
        return false
}
