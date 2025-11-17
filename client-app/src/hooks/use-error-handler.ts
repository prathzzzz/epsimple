import { AxiosError } from 'axios'
import { toast } from 'sonner'

interface UseErrorHandlerOptions {
  showToast?: boolean
  onError?: (error: { statusCode?: number; message: string; originalError: unknown }) => void
}

export function useErrorHandler(options: UseErrorHandlerOptions = {}) {
  const { showToast = true, onError } = options

  const handleError = (error: unknown) => {
    let errorMessage = 'Something went wrong!'
    let statusCode: number | undefined

    if (error instanceof AxiosError) {
      statusCode = error.response?.status
      // First, try to get the custom message from backend
      const backendMessage = error.response?.data?.message || error.response?.data?.title
      
      if (backendMessage) {
        // If backend provided a message, use it
        errorMessage = backendMessage
      } else {
        // Otherwise, use generic messages based on status codes
        errorMessage = error.message
        
        switch (statusCode) {
          case 400:
            errorMessage = 'Invalid request. Please check your input.'
            break
          case 401:
            errorMessage = 'Unauthorized. Please login again.'
            break
          case 403:
            errorMessage = 'Access forbidden. You do not have permission.'
            break
          case 404:
            errorMessage = 'Resource not found.'
            break
          case 422:
            errorMessage = 'Validation failed. Please check your input.'
            break
          case 429:
            errorMessage = 'Too many requests. Please try again later.'
            break
          case 500:
            errorMessage = 'Internal server error. Please try again later.'
            break
          case 503:
            errorMessage = 'Service unavailable. Please try again later.'
            break
        }
      }
    }

    if (showToast) {
      toast.error(errorMessage)
    }

    if (onError) {
      onError({ statusCode, message: errorMessage, originalError: error })
    }

    return { statusCode, message: errorMessage }
  }

  return { handleError }
}
