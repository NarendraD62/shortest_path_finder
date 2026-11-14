export type Algorithm = 'DIJKSTRA' | 'ASTAR' | 'BFS' | 'DFS' | 'FLOYD_WARSHALL'
export type Position = { row: number; column: number }
export interface GridRequest { rows: number; columns: number; obstacles: [number, number][] }
export interface PathRequest { algorithm: Algorithm; grid: GridRequest; start: Position; end: Position }
export interface CompareRequest { algorithms: Algorithm[]; grid: GridRequest; start: Position; end: Position }
export interface PathResult {
  algorithm: Algorithm; pathFound: boolean; visitedNodes: Position[]; path: Position[]
  pathLength: number; visitedNodeCount: number; executionTimeMs: number
}
export interface CompareResponse { results: PathResult[] }
export interface ApiError { message?: string; details?: Record<string, string> }

export const ALGORITHMS: { value: Algorithm; label: string; short: string; color: string }[] = [
  { value: 'DIJKSTRA', label: "Dijkstra's Algorithm", short: 'Dijkstra', color: '#5b7cfa' },
  { value: 'ASTAR', label: 'A* Search', short: 'A* Search', color: '#9a70ef' },
  { value: 'BFS', label: 'Breadth-First Search', short: 'BFS', color: '#18a98a' },
  { value: 'DFS', label: 'Depth-First Search', short: 'DFS', color: '#ed9d45' },
  { value: 'FLOYD_WARSHALL', label: 'Floyd–Warshall', short: 'Floyd–Warshall', color: '#e46f91' },
]
