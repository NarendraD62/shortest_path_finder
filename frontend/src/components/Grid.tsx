import { useCallback, useEffect, useRef, useState } from 'react'
import type { Position } from '../types'

export type Tool = 'wall' | 'erase' | 'start' | 'end'
export interface CellState { walls: Set<string>; start: Position; end: Position }
export const keyOf = (p: Position) => `${p.row}:${p.column}`
const same = (a: Position, b: Position) => a.row === b.row && a.column === b.column

interface Props {
  rows: number; columns: number; state: CellState; tool: Tool; visited: Set<string>; path: Set<string>
  onChange: (state: CellState) => void; disabled?: boolean
}
export function Grid({ rows, columns, state, tool, visited, path, onChange, disabled }: Props) {
  const [drawing, setDrawing] = useState(false)
  const drawMode = useRef<'wall' | 'erase'>('wall')
  const apply = useCallback((position: Position) => {
    if (disabled) return
    let next: CellState
    if (tool === 'start') {
      if (state.walls.has(keyOf(position)) || same(position, state.end)) return
      next = { ...state, start: position }
    } else if (tool === 'end') {
      if (state.walls.has(keyOf(position)) || same(position, state.start)) return
      next = { ...state, end: position }
    } else {
      const mode = drawing ? drawMode.current : tool === 'erase' ? 'erase' : (state.walls.has(keyOf(position)) ? 'erase' : 'wall')
      const walls = new Set(state.walls)
      if (mode === 'wall') { if (same(position, state.start) || same(position, state.end)) return; walls.add(keyOf(position)) }
      else walls.delete(keyOf(position))
      next = { ...state, walls }
    }
    onChange(next)
  }, [disabled, drawing, onChange, state, tool])

  useEffect(() => {
    const stop = () => setDrawing(false)
    window.addEventListener('pointerup', stop)
    return () => window.removeEventListener('pointerup', stop)
  }, [])

  return <div className="grid-scroll" onContextMenu={e => e.preventDefault()}>
    <div className="grid-board" role="grid" aria-label={`${rows} by ${columns} pathfinding grid`} style={{ gridTemplateColumns: `repeat(${columns}, minmax(0, 1fr))` }}>
      {Array.from({ length: rows * columns }, (_, index) => {
        const p = { row: Math.floor(index / columns), column: index % columns }, key = keyOf(p)
        const isStart = same(p, state.start), isEnd = same(p, state.end), isWall = state.walls.has(key)
        const classes = ['cell', isWall && 'wall', !isWall && visited.has(key) && 'visited', !isWall && path.has(key) && 'route', isStart && 'start', isEnd && 'end'].filter(Boolean).join(' ')
        return <button key={key} type="button" role="gridcell" aria-label={`${isStart ? 'Start, ' : ''}${isEnd ? 'Destination, ' : ''}row ${p.row + 1}, column ${p.column + 1}${isWall ? ', wall' : ''}`}
          className={classes} onPointerDown={e => { e.preventDefault(); if (tool === 'wall' || tool === 'erase') { drawMode.current = tool === 'erase' || isWall ? 'erase' : 'wall'; setDrawing(true) } apply(p) }}
          onPointerEnter={() => drawing && apply(p)} disabled={disabled}>
          {isStart ? <span className="node-marker">S</span> : isEnd ? <span className="node-marker">E</span> : null}
        </button>
      })}
    </div>
  </div>
}
