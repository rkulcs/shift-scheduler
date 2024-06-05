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
