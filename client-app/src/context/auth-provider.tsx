import { createContext, useContext, useEffect, type ReactNode } from 'react'
import { useAuthStore } from '@/stores/auth-store'
import type { AuthUser } from '@/lib/auth-api'

interface AuthContextType {
  isAuthenticated: boolean
  isLoading: boolean
  user: AuthUser | null
}

const AuthContext = createContext<AuthContextType | undefined>(undefined)

export function AuthProvider({ children }: { children: ReactNode }) {
  const { user, isInitializing, initialize } = useAuthStore()

  useEffect(() => {
    initialize()
  }, [initialize])

  const value: AuthContextType = {
    isAuthenticated: !!user,
    isLoading: isInitializing,
    user,
  }

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}

export function useAuth() {
  const context = useContext(AuthContext)
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider')
  }
  return context
}
