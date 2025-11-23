import { AxiosError } from 'axios'
import { toast } from 'sonner'

interface ErrorHandlerOptions {
  showToast?: boolean
  redirectOnError?: boolean
}

export function handleServerError(error: unknown, options: ErrorHandlerOptions = {}) {
  const { showToast = true, redirectOnError = false } = options
  
  // Log error for debugging in development only
  if (import.meta.env.DEV) {
    // eslint-disable-next-line no-console
    console.log('Server error:', error)
  }

  let errMsg = 'Something went wrong!'
  let statusCode: number | undefined

  if (
    error &&
    typeof error === 'object' &&
    'status' in error &&
    Number(error.status) === 204
  ) {
    errMsg = 'Content not found.'
    statusCode = 204
  }

  if (error instanceof AxiosError || (error instanceof Error && 'response' in error)) {
    const err = error as any
    statusCode = err.response?.status
    
    // Extract error message from various possible locations in the response
    const responseData = err.response?.data
    errMsg = responseData?.message || responseData?.title || responseData?.error || err.message
    
    // Handle specific error cases (only override if we don't have a server message)
    switch (statusCode) {
      case 400:
        // Use server message for 400 errors (validation, constraint violations, etc.)
        // Don't override with generic message
        break
      case 401:
        if (!responseData?.message) {
          errMsg = 'Unauthorized. Please login again.'
        }
        if (redirectOnError) {
          // Redirect to login or show 401 page
          window.location.href = '/sign-in'
          return
        }
        break
      case 403:
        if (!responseData?.message) {
          errMsg = 'Access forbidden. You do not have permission.'
        }
        if (redirectOnError) {
          // Could redirect to 403 page
          window.location.href = '/errors/403'
          return
        }
        break
      case 404:
        if (!responseData?.message) {
          errMsg = 'Resource not found.'
        }
        break
      case 500:
        if (!responseData?.message) {
          errMsg = 'Internal server error. Please try again later.'
        }
        if (redirectOnError) {
          // Could redirect to 500 page
          window.location.href = '/errors/500'
          return
        }
        break
      default:
        // Use the message from the server if available
        break
    }
  }

  if (showToast) {
    toast.error(errMsg)
  }

  return { statusCode, message: errMsg }
}
