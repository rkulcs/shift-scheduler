export function storeJWT(token: string) {
    localStorage.setItem('token', `Bearer ${token}`)
}

export function getJWT() {
    return localStorage.getItem('token')
}
