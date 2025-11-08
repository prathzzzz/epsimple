/**
 * Utility types and functions for API responses
 */

/**
 * Backend pagination response structure (with nested page object)
 */
export interface BackendPageResponse<T> {
  content: T[];
  page: {
    totalElements: number;
    totalPages: number;
    size: number;
    number: number;
  };
  first?: boolean;
  last?: boolean;
  empty?: boolean;
}

/**
 * Frontend pagination response structure (flattened)
 */
export interface FlatPageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  first?: boolean;
  last?: boolean;
  empty?: boolean;
}

/**
 * Flattens the backend pagination response structure to a flat structure
 * that's easier to work with in the frontend.
 * 
 * @param backendResponse - The response from the backend with nested page object
 * @returns Flattened response with pagination metadata at the root level
 */
export function flattenPageResponse<T>(
  backendResponse: BackendPageResponse<T>
): FlatPageResponse<T> {
  return {
    content: backendResponse.content,
    totalElements: backendResponse.page.totalElements,
    totalPages: backendResponse.page.totalPages,
    size: backendResponse.page.size,
    number: backendResponse.page.number,
    first: backendResponse.first,
    last: backendResponse.last,
    empty: backendResponse.empty,
  };
}

/**
 * Gets the JWT auth token from cookies
 * @returns The auth token or null if not found
 */
export function getAuthToken(): string | null {
  const cookies = document.cookie.split(';')
  const jwtCookie = cookies.find((c) => c.trim().startsWith('jwt-token='))
  if (jwtCookie) {
    return jwtCookie.split('=')[1]
  }
  return null
}

/**
 * Creates headers for fetch requests with authentication
 * @returns Headers object with Authorization header if token exists
 */
export function createAuthHeaders(): Record<string, string> {
  const token = getAuthToken()
  const headers: Record<string, string> = {}

  if (token) {
    headers['Authorization'] = `Bearer ${token}`
  }

  return headers
}

/**
 * Downloads a file from an endpoint using GET request
 * @param endpoint - The API endpoint to download from
 * @param filename - The filename to save as
 */
export async function downloadFile(endpoint: string, filename: string): Promise<void> {
  const response = await fetch(`${import.meta.env.VITE_API_URL}${endpoint}`, {
    method: 'GET',
    credentials: 'include',
    headers: createAuthHeaders(),
  })

  if (!response.ok) {
    throw new Error(`Download failed: ${response.statusText}`)
  }

  const blob = await response.blob()
  const url = window.URL.createObjectURL(blob)
  const link = document.createElement('a')
  link.href = url
  link.download = filename
  link.style.display = 'none'
  document.body.appendChild(link)
  link.click()
  window.URL.revokeObjectURL(url)
  document.body.removeChild(link)
}

/**
 * Downloads a file from an endpoint using POST request with JSON body
 * @param endpoint - The API endpoint to download from
 * @param data - The data to send in the request body
 * @param filename - The filename to save as
 */
export async function downloadFileWithPost(endpoint: string, data: any, filename: string): Promise<void> {
  const headers: Record<string, string> = {
    ...createAuthHeaders(),
    'Content-Type': 'application/json',
  }

  const response = await fetch(`${import.meta.env.VITE_API_URL}${endpoint}`, {
    method: 'POST',
    credentials: 'include',
    headers,
    body: JSON.stringify(data),
  })

  if (!response.ok) {
    throw new Error(`Download failed: ${response.statusText}`)
  }

  const blob = await response.blob()
  const url = window.URL.createObjectURL(blob)
  const link = document.createElement('a')
  link.href = url
  link.download = filename
  link.style.display = 'none'
  document.body.appendChild(link)
  link.click()
  window.URL.revokeObjectURL(url)
  document.body.removeChild(link)
}
