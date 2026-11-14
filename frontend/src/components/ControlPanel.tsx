import type { Algorithm } from '../types'
import { ALGORITHMS } from '../types'
import { AlgorithmSelector } from './AlgorithmSelector'
import type { Tool } from './Grid'

interface Props {
  algorithm: Algorithm; onAlgorithm: (value: Algorithm) => void; tool: Tool; onTool: (tool: Tool) => void
  speed: number; onSpeed: (speed: number) => void; loading: boolean; onFind: () => void; onReset: () => void
  onClear: () => void; onRandom: () => void; onCompare: () => void
}
const tools: { id: Tool; label: string; icon: string }[] = [
  { id: 'wall', label: 'Wall', icon: '▦' }, { id: 'erase', label: 'Erase', icon: '⌫' },
  { id: 'start', label: 'Start', icon: 'S' }, { id: 'end', label: 'End', icon: 'E' },
]
export function ControlPanel(props: Props) {
  const { algorithm, onAlgorithm, tool, onTool, speed, onSpeed, loading, onFind, onReset, onClear, onRandom, onCompare } = props
  const selected = ALGORITHMS.find(a => a.value === algorithm)!
  return <aside className="control-panel">
    <div className="panel-heading"><div><span className="eyebrow">WORKSPACE</span><h2>Setup your search</h2></div><span className="live-dot" title="Ready" /></div>
    <AlgorithmSelector value={algorithm} onChange={onAlgorithm} />
    <div className="algorithm-note"><span className="algorithm-dot" style={{ background: selected.color }} />{algorithm === 'ASTAR' ? 'Uses Manhattan distance to guide the search.' : algorithm === 'FLOYD_WARSHALL' ? 'All-pairs search · limited to 400 cells.' : `${selected.short} explores the map to find a route.`}</div>
    <div className="form-divider" />
    <div className="field-label">PLACE ON GRID <span className="sub-label">Click and drag to draw</span></div>
    <div className="tool-grid">{tools.map(item => <button type="button" key={item.id} className={`tool-button ${tool === item.id ? 'selected' : ''}`} onClick={() => onTool(item.id)}><span className={`tool-glyph ${item.id}`}>{item.icon}</span>{item.label}</button>)}</div>
    <div className="form-divider" />
    <div className="speed-heading"><span className="field-label">ANIMATION SPEED</span><span className="speed-value">{['Slow', 'Medium', 'Fast'][speed]}</span></div>
    <input className="speed-slider" type="range" min="0" max="2" step="1" value={speed} onChange={e => onSpeed(Number(e.target.value))} aria-label="Animation speed" />
    <div className="speed-labels"><span>Slow</span><span>Medium</span><span>Fast</span></div>
    <div className="panel-actions">
      <button type="button" className="button-primary" onClick={onFind} disabled={loading}><span>{loading ? '◌' : '▷'}</span>{loading ? 'Finding route…' : 'Find path'}</button>
      <button type="button" className="button-secondary" onClick={onCompare} disabled={loading}><span>⇄</span>Compare Dijkstra &amp; A*</button>
    </div>
    <div className="utility-actions"><button type="button" onClick={onRandom} disabled={loading}>✳ Random walls</button><button type="button" onClick={onClear} disabled={loading}>Clear path</button><button type="button" onClick={onReset} disabled={loading}>Reset grid</button></div>
    <div className="control-footnote"><span>⌘</span> Tip: drag across cells to draw walls</div>
  </aside>
}
