import type { PathResult } from '../types'
interface Props { result: PathResult | null; searching: boolean }
export function StatsPanel({ result, searching }: Props) {
  const metrics = [
    { icon: '↗', label: 'PATH LENGTH', value: result?.pathFound ? `${result.pathLength} steps` : result ? 'No route' : '—', tone: 'mint' },
    { icon: '◉', label: 'NODES VISITED', value: result ? result.visitedNodeCount.toLocaleString() : '—', tone: 'blue' },
    { icon: '◷', label: 'EXECUTION TIME', value: result ? `${result.executionTimeMs.toFixed(2)} ms` : '—', tone: 'violet' },
  ]
  return <section className="stats-panel">
    <div className="stats-title"><div><span className="eyebrow">LIVE RESULTS</span><h2>Search summary</h2></div><span className={`status-pill ${searching ? 'is-searching' : result?.pathFound ? 'is-found' : result ? 'is-empty' : 'is-idle'}`}><span />{searching ? 'Searching' : result?.pathFound ? 'Path found' : result ? 'No path' : 'Awaiting search'}</span></div>
    <div className="metric-grid">{metrics.map(m => <div className="metric-card" key={m.label}><span className={`metric-icon ${m.tone}`}>{m.icon}</span><span className="metric-label">{m.label}</span><strong>{m.value}</strong></div>)}</div>
  </section>
}
