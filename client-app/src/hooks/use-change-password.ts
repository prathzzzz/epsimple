import { useMutation } from '@tanstack/react-query'
import { toast } from 'sonner'
import api from '@/lib/api'

interface ChangePasswordRequest {
  currentPassword: string
  newPassword: string
  confirmPassword: string
}

const changePassword = async (data: ChangePasswordRequest): Promise<void> => {
  await api.post('/api/auth/change-password', data)
}

export function useChangePassword() {
  return useMutation({
    mutationFn: changePassword,
    onError: (error: Error) => {
      // Extract the message from the error
      const message = error.message || 'Failed to change password'
      toast.error(message)
    },
  })
}
