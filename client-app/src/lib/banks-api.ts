import api from './api'
import { BackendPageResponse, flattenPageResponse } from './api-utils'

export interface Bank {
  id: number
  bankName: string
  rbiBankCode: string | null
  epsBankCode: string | null
  bankCodeAlt: string | null
  bankLogo: string | null
  createdAt: string
  updatedAt: string
}

export interface BankRequest {
  bankName: string
  rbiBankCode?: string
  epsBankCode?: string
  bankCodeAlt?: string
  bankLogo?: string
}

export interface PageResponse<T> {
  content: T[]
  page: {
    size: number
    number: number
    totalElements: number
    totalPages: number
  }
  first: boolean
  last: boolean
  empty: boolean
}

export interface ApiResponse<T> {
  data: T
  message: string
  success: boolean
  timestamp: string
}

// Get all banks with pagination and sorting
export const getAllBanks = async (
  page: number = 0,
  size: number = 10,
  sortBy: string = 'id',
  sortDirection: 'ASC' | 'DESC' = 'ASC'
) => {
  const response = await api.get<ApiResponse<BackendPageResponse<Bank>>>('/api/banks', {
    params: { page, size, sortBy, sortDirection },
  })
  const apiData = response.data
  return {
    ...apiData,
    data: flattenPageResponse(apiData.data)
  }
}

// Search banks with pagination
export const searchBanks = async (
  search: string,
  page: number = 0,
  size: number = 10,
  sortBy: string = 'id',
  sortDirection: 'ASC' | 'DESC' = 'ASC'
) => {
  const response = await api.get<ApiResponse<BackendPageResponse<Bank>>>('/api/banks/search', {
    params: { search, page, size, sortBy, sortDirection },
  })
  const apiData = response.data
  return {
    ...apiData,
    data: flattenPageResponse(apiData.data)
  }
}

// Get all banks as list (without pagination)
export const getAllBanksList = async () => {
  const response = await api.get<ApiResponse<Bank[]>>('/api/banks/list')
  return response.data
}

// Get bank by ID
export const getBankById = async (id: number) => {
  const response = await api.get<ApiResponse<Bank>>(`/api/banks/${id}`)
  return response.data
}

// Create bank with logo
export const createBank = async (bankData: BankRequest, logo?: File) => {
  const formData = new FormData()
  
  // Append bank data
  formData.append('bankName', bankData.bankName)
  if (bankData.rbiBankCode) formData.append('rbiBankCode', bankData.rbiBankCode)
  if (bankData.epsBankCode) formData.append('epsBankCode', bankData.epsBankCode)
  if (bankData.bankCodeAlt) formData.append('bankCodeAlt', bankData.bankCodeAlt)
  if (bankData.bankLogo) formData.append('bankLogo', bankData.bankLogo)
  
  // Append logo file if provided
  if (logo) {
    formData.append('logo', logo)
  }
  
  const response = await api.post<ApiResponse<Bank>>('/api/banks', formData, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  })
  return response.data
}

// Update bank with logo
export const updateBank = async (id: number, bankData: BankRequest, logo?: File) => {
  const formData = new FormData()
  
  // Append bank data
  formData.append('bankName', bankData.bankName)
  if (bankData.rbiBankCode) formData.append('rbiBankCode', bankData.rbiBankCode)
  if (bankData.epsBankCode) formData.append('epsBankCode', bankData.epsBankCode)
  if (bankData.bankCodeAlt) formData.append('bankCodeAlt', bankData.bankCodeAlt)
  if (bankData.bankLogo) formData.append('bankLogo', bankData.bankLogo)
  
  // Append logo file if provided
  if (logo) {
    formData.append('logo', logo)
  }
  
  const response = await api.put<ApiResponse<Bank>>(`/api/banks/${id}`, formData, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  })
  return response.data
}

// Delete bank
export const deleteBank = async (id: number) => {
  const response = await api.delete<ApiResponse<void>>(`/api/banks/${id}`)
  return response.data
}
