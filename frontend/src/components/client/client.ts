export function getRequest(endpoint: string): Promise<Response> {
  return fetch(`${import.meta.env.VITE_API_URL}/${endpoint}`,
    {
      method: 'GET',
      headers: {
        'Accept': 'application/json',
        'Authorization': localStorage.getItem('token') as string
      },
    }
  )
}

export function unauthenticatedGetRequest(endpoint: string): Promise<Response> {
  return fetch(`${import.meta.env.VITE_API_URL}/${endpoint}`,
    {
      method: 'GET',
      headers: {
        'Accept': 'application/json'
      },
    }
  )
}

export function postRequest(endpoint: string, payload: any): Promise<Response> {
  return fetch(`${import.meta.env.VITE_API_URL}/${endpoint}`,
    {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'Authorization': localStorage.getItem('token') as string
      },
      body: JSON.stringify(payload)
    }
  )
}

export function unauthenticatedPostRequest(endpoint: string, payload: any): Promise<Response> {
  return fetch(`${import.meta.env.VITE_API_URL}/${endpoint}`,
    {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
      },
      body: JSON.stringify(payload)
    }
  )
}