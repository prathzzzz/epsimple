import axios from 'axios'

// Create axios instance with base configuration
const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || '',
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
    // Extract error message from backend response
    const backendMessage = error.response?.data?.message
    
    // Handle common errors
    if (error.response?.status === 401) {
      // Ignore 401 for the 'me' endpoint as it just means user is not logged in
      if (error.config?.url?.includes('/auth/me')) {
        return Promise.reject(error)
      }

      // Ignore 401 for change-password endpoint - it means incorrect current password
      if (error.config?.url?.includes('/auth/change-password')) {
        const customError = new Error(backendMessage || 'Current password is incorrect')
        Object.assign(customError, { response: error.response, config: error.config })
        return Promise.reject(customError)
      }

      // Unauthorized - redirect to login
      // Only redirect if we're not already on login/auth pages
      const currentPath = window.location.pathname
      const isAuthPage = currentPath.includes('/sign-in') || 
                        currentPath.includes('/sign-up') || 
                        currentPath.includes('/forgot-password') ||
                        currentPath.includes('/reset-password')
      
      if (!isAuthPage) {
        // Clear any stored auth data
        document.cookie = 'jwt-token=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;'
        
        // Redirect to login with current path for redirect after login
        const redirectTo = encodeURIComponent(currentPath)
        window.location.href = `/sign-in?redirectTo=${redirectTo}`
      }
    }
    
    if (error.response?.status === 403) {
      // Forbidden
      // Request forbidden - user doesn't have permission
    }
    
    if (error.response?.status >= 500) {
      // Server errors
      // Server error occurred
    }
    
    // Create custom error with backend message if available
    if (backendMessage) {
      const customError = new Error(backendMessage)
      // Preserve original error properties
      Object.assign(customError, { response: error.response, config: error.config })
      return Promise.reject(customError)
    }
    
    return Promise.reject(error)
  }
)

export default api
