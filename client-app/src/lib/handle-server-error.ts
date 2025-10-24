import { AxiosError } from 'axios'
import { toast } from 'sonner'

interface ErrorHandlerOptions {
  showToast?: boolean
  redirectOnError?: boolean
}

export function handleServerError(error: unknown, options: ErrorHandlerOptions = {}) {
  const { showToast = true, redirectOnError = false } = options
  
  // eslint-disable-next-line no-console
  console.log('Server error:', error)

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

  if (error instanceof AxiosError) {
    statusCode = error.response?.status
    errMsg = error.response?.data?.message || error.response?.data?.title || error.message
    
    // Handle specific error cases
    switch (statusCode) {
      case 401:
        errMsg = 'Unauthorized. Please login again.'
        if (redirectOnError) {
          // Redirect to login or show 401 page
          window.location.href = '/sign-in'
          return
        }
        break
      case 403:
        errMsg = 'Access forbidden. You do not have permission.'
        if (redirectOnError) {
          // Could redirect to 403 page
          window.location.href = '/errors/403'
          return
        }
        break
      case 404:
        errMsg = 'Resource not found.'
        break
      case 500:
        errMsg = 'Internal server error. Please try again later.'
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
