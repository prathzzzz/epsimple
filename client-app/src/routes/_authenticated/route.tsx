import { createFileRoute, redirect } from '@tanstack/react-router'
import { useAuthStore } from '@/stores/auth-store'
import { AuthenticatedLayout } from '@/components/layout/authenticated-layout'
import { AuthLoading } from '@/components/auth-loading'

export const Route = createFileRoute('/_authenticated')({
  component: AuthenticatedLayout,
  pendingComponent: AuthLoading,
  beforeLoad: async ({ location }) => {
    const { user, isInitializing, initialize } = useAuthStore.getState()
    
    // Initialize auth state if not already done
    if (!user && !isInitializing) {
      try {
        await initialize()
      } catch (error) {
        console.error('Auth initialization failed:', error)
        // If initialization fails, redirect to sign-in
        throw redirect({
          to: '/sign-in',
          search: {
            redirect: location.href,
          },
        })
      }
    }
    
    // Check if user is authenticated after initialization
    const updatedState = useAuthStore.getState()
    
    if (!updatedState.user) {
      throw redirect({
        to: '/sign-in',
        search: {
          redirect: location.href,
        },
      })
    }
  },
})
