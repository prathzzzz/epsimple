import { createFileRoute, redirect } from '@tanstack/react-router'
import { useAuthStore } from '@/stores/auth-store'
import { ResetPassword } from '@/features/auth/reset-password/index'

export const Route = createFileRoute('/(auth)/reset-password')({
  component: ResetPassword,
  beforeLoad: () => {
    const { user } = useAuthStore.getState()
    if (user) {
      throw redirect({
        to: '/',
      })
    }
  },
})
