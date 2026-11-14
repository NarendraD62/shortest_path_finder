import type { ApiError, CompareRequest, CompareResponse, PathRequest, PathResult } from './types'

const API_BASE_URL = (import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080').replace(/\/$/, '')

async function post<T>(path: string, body: unknown): Promise<T> {
  let response: Response
  try {
    response = await fetch(`${API_BASE_URL}${path}`, {
      method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(body),
    })
  } catch {
    throw new Error(`Could not reach the API at ${API_BASE_URL}. Check that the backend is running.`)
  }
  if (!response.ok) {
    const error = await response.json().catch(() => ({})) as ApiError
    const detail = error.details ? ` ${Object.values(error.details).join(' ')}` : ''
    throw new Error(`${error.message || `Request failed (${response.status})`}${detail}`)
  }
  return response.json() as Promise<T>
}

export const findPath = (request: PathRequest) => post<PathResult>('/api/pathfinding/find-path', request)
export const comparePaths = (request: CompareRequest) => post<CompareResponse>('/api/pathfinding/compare', request)
