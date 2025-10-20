import axios from 'axios'

// Create axios instance with base configuration
const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || 'http://localhost:8080/epsone',
  withCredentials: true, // Important for JWT cookies
  headers: {
    'Content-Type': 'application/json',
  },
})

// Request interceptor
api.interceptors.request.use(
  (config) => {
    // You can add auth headers here if needed
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// Response interceptor for error handling
api.interceptors.response.use(
  (response) => {
    return response
  },
  (error) => {
    // Handle common errors
    if (error.response?.status === 401) {
      // Unauthorized - redirect to login
      console.warn('Unauthorized request - redirecting to login')
      
      // Only redirect if we're not already on login/auth pages
      const currentPath = window.location.pathname
      const isAuthPage = currentPath.includes('/sign-in') || 
                        currentPath.includes('/sign-up') || 
                        currentPath.includes('/forgot-password') ||
                        currentPath.includes('/reset-password')
      
      if (!isAuthPage) {
        // Clear any stored auth data
        document.cookie = 'jwt-token=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/epsone;'
        
        // Redirect to login with current path for redirect after login
        const redirectTo = encodeURIComponent(currentPath)
        window.location.href = `/epsone/sign-in?redirectTo=${redirectTo}`
      }
    }
    
    if (error.response?.status === 403) {
      // Forbidden
      console.warn('Forbidden request')
    }
    
    if (error.response?.status >= 500) {
      // Server errors
      console.error('Server error:', error.response.status)
    }
    
    return Promise.reject(error)
  }
)

export default api
