import { useEffect } from 'react'
import { useAuthStore } from '@/stores/auth-store'

export function useAuthInitializer() {
  const initialize = useAuthStore((state) => state.initialize)
  const isInitializing = useAuthStore((state) => state.isInitializing)

  useEffect(() => {
    initialize()
  }, [initialize])

  return { isInitializing }
}
