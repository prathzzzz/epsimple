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
