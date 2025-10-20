import { create } from 'zustand'
import { persist } from 'zustand/middleware'
import { authApi, type AuthUser } from '@/lib/auth-api'

interface AuthState {
  user: AuthUser | null
  isInitializing: boolean
  isLoading: boolean
  error: string | null
}

interface AuthActions {
  setUser: (user: AuthUser | null) => void
  setLoading: (loading: boolean) => void
  setError: (error: string | null) => void
  clearError: () => void
  initialize: () => Promise<void>
  login: (email: string, password: string) => Promise<void>
  register: (email: string, password: string, name: string) => Promise<void>
  logout: () => Promise<void>
  getCurrentUser: () => Promise<void>
  forgotPassword: (email: string) => Promise<void>
  resetPassword: (token: string, newPassword: string) => Promise<void>
  reset: () => void
}

type AuthStore = AuthState & AuthActions

export const useAuthStore = create<AuthStore>()(
  persist(
    (set, get) => ({
      // State
      user: null,
      isInitializing: true,
      isLoading: false,
      error: null,

      // Actions
      setUser: (user: AuthUser | null) => set({ user }),
      
      setLoading: (isLoading: boolean) => set({ isLoading }),
      
      setError: (error: string | null) => set({ error }),

      clearError: () => set({ error: null }),

      initialize: async () => {
        set({ isInitializing: true, error: null })
        try {
          await get().getCurrentUser()
        } catch (error) {
          console.log('No authenticated user found during initialization')
          set({ user: null })
        } finally {
          set({ isInitializing: false })
        }
      },

      getCurrentUser: async () => {
        try {
          const response = await authApi.me()
          set({ user: response.data, error: null })
        } catch (error: any) {
          set({ user: null, error: error.message })
          throw error
        }
      },

      login: async (email: string, password: string) => {
        set({ isLoading: true, error: null })
        try {
          const response = await authApi.login({ email, password })
          set({ user: response.data.user, isLoading: false })
        } catch (error: any) {
          set({ error: error.message, isLoading: false })
          throw error
        }
      },

      register: async (email: string, password: string, name: string) => {
        set({ isLoading: true, error: null })
        try {
          const response = await authApi.register({ email, password, name })
          set({ user: response.data.user, isLoading: false })
        } catch (error: any) {
          set({ error: error.message, isLoading: false })
          throw error
        }
      },

      logout: async () => {
        set({ isLoading: true })
        try {
          await authApi.logout()
          set({ user: null, isLoading: false, error: null })
        } catch (error: any) {
          set({ user: null, isLoading: false, error: error.message })
          throw error
        }
      },

      forgotPassword: async (email: string) => {
        set({ isLoading: true, error: null })
        try {
          await authApi.forgotPassword({ email })
          set({ isLoading: false })
        } catch (error: any) {
          set({ error: error.message, isLoading: false })
          throw error
        }
      },

      resetPassword: async (token: string, newPassword: string) => {
        set({ isLoading: true, error: null })
        try {
          await authApi.resetPassword({ token, newPassword })
          set({ isLoading: false })
        } catch (error: any) {
          set({ error: error.message, isLoading: false })
          throw error
        }
      },

      reset: () => {
        set({
          user: null,
          isInitializing: false,
          isLoading: false,
          error: null,
        })
      },
    }),
    {
      name: 'auth-storage',
    }
  )
)
