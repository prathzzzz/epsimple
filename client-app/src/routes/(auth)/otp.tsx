import { createFileRoute, redirect } from '@tanstack/react-router'
import { useAuthStore } from '@/stores/auth-store'
import { Otp } from '@/features/auth/otp'

export const Route = createFileRoute('/(auth)/otp')({
  component: Otp,
  beforeLoad: () => {
    const { user } = useAuthStore.getState()
    if (user) {
      throw redirect({
        to: '/',
      })
    }
  },
})
