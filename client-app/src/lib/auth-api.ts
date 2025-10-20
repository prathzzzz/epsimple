import api from '@/lib/api'

export interface AuthUser {
  id: number
  email: string
  name: string
  isActive: boolean
  roles: Array<{
    id: number
    name: string
    description: string
    permissions: Array<{
      id: number
      name: string
      description: string
    }>
  }>
}

export interface LoginRequest {
  email: string
  password: string
}

export interface RegisterRequest {
  email: string
  password: string
  name: string
}

export interface ForgotPasswordRequest {
  email: string
}

export interface ResetPasswordRequest {
  token: string
  newPassword: string
}

export interface AuthResponse {
  data: {
    user: AuthUser
  }
  message?: string
}

export interface MeResponse {
  data: AuthUser
  message?: string
}

// Auth API functions
export const authApi = {
  // Register new user
  register: async (data: RegisterRequest): Promise<AuthResponse> => {
    const response = await api.post('/api/auth/register', data)
    return response.data
  },

  // Login user
  login: async (data: LoginRequest): Promise<AuthResponse> => {
    const response = await api.post('/api/auth/login', data)
    return response.data
  },

  // Get current user
  me: async (): Promise<MeResponse> => {
    const response = await api.get('/api/auth/me')
    return response.data
  },

  // Logout user
  logout: async (): Promise<void> => {
    await api.post('/api/auth/logout')
  },

  // Forgot password
  forgotPassword: async (data: ForgotPasswordRequest): Promise<{ message: string }> => {
    const response = await api.post('/api/auth/forgot-password', data)
    return response.data
  },

  // Reset password
  resetPassword: async (data: ResetPasswordRequest): Promise<{ message: string }> => {
    const response = await api.post('/api/auth/reset-password', data)
    return response.data
  },

 
}
