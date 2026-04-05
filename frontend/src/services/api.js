import axios from 'axios'

const api = axios.create({
  baseURL: '/api/v1',
  headers: { 'Content-Type': 'application/json' }
})

// Request interceptor: attach JWT
api.interceptors.request.use(config => {
  const token = localStorage.getItem('hirehub_token')
  if (token) config.headers.Authorization = 'Bearer ' + token
  return config
})

// Response interceptor: handle 401
api.interceptors.response.use(
  response => response,
  async error => {
    if (error.response?.status === 401 && !error.config._retry) {
      error.config._retry = true
      const refresh = localStorage.getItem('hirehub_refresh')
      if (refresh) {
        try {
          const { data } = await axios.post('/api/v1/auth/refresh', { refreshToken: refresh })
          localStorage.setItem('hirehub_token', data.accessToken)
          localStorage.setItem('hirehub_refresh', data.refreshToken)
          error.config.headers.Authorization = 'Bearer ' + data.accessToken
          return api(error.config)
        } catch {
          localStorage.removeItem('hirehub_token')
          localStorage.removeItem('hirehub_refresh')
          localStorage.removeItem('hirehub_user')
          window.location.href = '/login'
        }
      }
    }
    return Promise.reject(error)
  }
)

export default api
