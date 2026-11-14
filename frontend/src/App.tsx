import { useRef, useState } from 'react'
import { comparePaths, findPath } from './api'
import { ComparisonTable } from './components/ComparisonTable'
import { ControlPanel } from './components/ControlPanel'
import { Grid, keyOf, type CellState, type Tool } from './components/Grid'
import { StatsPanel } from './components/StatsPanel'
import { ALGORITHMS, type Algorithm, type PathRequest, type PathResult, type Position } from './types'

const ROWS = 20, COLUMNS = 20
const initialState = (): CellState => ({ walls: new Set(), start: { row: 9, column: 5 }, end: { row: 9, column: 14 } })
const wait = (ms: number) => new Promise(resolve => window.setTimeout(resolve, ms))

export default function App() {
  const [cells, setCells] = useState<CellState>(initialState)
  const [algorithm, setAlgorithm] = useState<Algorithm>('ASTAR')
  const [tool, setTool] = useState<Tool>('wall')
  const [speed, setSpeed] = useState(1)
  const [visited, setVisited] = useState<Set<string>>(new Set())
  const [path, setPath] = useState<Set<string>>(new Set())
  const [result, setResult] = useState<PathResult | null>(null)
  const [comparison, setComparison] = useState<PathResult[]>([])
  const [loading, setLoading] = useState(false)
  const [comparing, setComparing] = useState(false)
  const [error, setError] = useState('')
  const runId = useRef(0)
  const delay = [24, 8, 2][speed]

  const gridRequest = () => ({ rows: ROWS, columns: COLUMNS, obstacles: [...cells.walls].map(key => key.split(':').map(Number) as [number, number]) })
  const request = (selected: Algorithm): PathRequest => ({ algorithm: selected, grid: gridRequest(), start: cells.start, end: cells.end })

  async function animate(found: PathResult) {
    const thisRun = ++runId.current
    setVisited(new Set()); setPath(new Set())
    const visitedSet = new Set<string>()
    for (let i = 0; i < found.visitedNodes.length; i++) {
      if (runId.current !== thisRun) return
      visitedSet.add(keyOf(found.visitedNodes[i]))
      if (i % 3 === 0 || i === found.visitedNodes.length - 1) { setVisited(new Set(visitedSet)); await wait(delay) }
    }
    if (!found.pathFound || runId.current !== thisRun) return
    const pathSet = new Set<string>()
    for (let i = 0; i < found.path.length; i++) {
      if (runId.current !== thisRun) return
      pathSet.add(keyOf(found.path[i]))
      setPath(new Set(pathSet)); await wait(Math.max(12, delay * 2))
    }
  }

  async function runSearch() {
    setLoading(true); setError(''); setComparison([]); setComparing(false); setResult(null)
    runId.current++; setVisited(new Set()); setPath(new Set())
    try { const found = await findPath(request(algorithm)); setResult(found); void animate(found) }
    catch (e) { setError(e instanceof Error ? e.message : 'The pathfinding request failed.') }
    finally { setLoading(false) }
  }

  async function runComparison() {
    setComparing(true); setLoading(true); setError(''); setComparison([]); setResult(null)
    runId.current++; setVisited(new Set()); setPath(new Set())
    try {
      const response = await comparePaths({ algorithms: ['DIJKSTRA', 'ASTAR'], grid: gridRequest(), start: cells.start, end: cells.end })
      setComparison(response.results)
      const selected = response.results.find(item => item.algorithm === algorithm) ?? response.results[0]
      if (selected) { setResult(selected); void animate(selected) }
    } catch (e) { setError(e instanceof Error ? e.message : 'The comparison request failed.') }
    finally { setLoading(false); setComparing(false) }
  }

  function clearPath() { runId.current++; setVisited(new Set()); setPath(new Set()); setResult(null); setError('') }
  function resetGrid() { clearPath(); setCells(initialState()); setComparison([]) }
  function randomWalls() {
    const walls = new Set<string>()
    for (let row = 0; row < ROWS; row++) for (let column = 0; column < COLUMNS; column++) {
      const p: Position = { row, column }
      if (keyOf(p) !== keyOf(cells.start) && keyOf(p) !== keyOf(cells.end) && Math.random() < 0.17) walls.add(keyOf(p))
    }
    clearPath(); setCells(current => ({ ...current, walls })); setComparison([])
  }
  const algo = ALGORITHMS.find(item => item.value === algorithm)!

  return <div className="app-shell">
    <header className="topbar">
      <a className="brand" href="#top" aria-label="PathFinder home"><span className="brand-mark"><svg viewBox="0 0 32 32" aria-hidden="true"><path d="M7 23 14 9l5 10 6-9"/><circle cx="7" cy="23" r="2.5"/><circle cx="25" cy="10" r="2.5"/></svg></span><span className="brand-name">pathfinder<span>.</span></span></a>
      <div className="topbar-center"><span className="topbar-dot" />Interactive algorithm lab</div>
      <div className="topbar-right"><span className="api-indicator"><i />API configured</span><span className="avatar">PF</span></div>
    </header>

    <main id="top" className="page-content">
      <section className="hero"><div><div className="hero-kicker"><span>ROUTE PLANNER</span><i /> ALGORITHM VISUALIZER</div><h1>Every route has a <em>story.</em></h1><p>Explore how algorithms think. Draw a map, choose a strategy, and watch the shortest path unfold.</p></div><div className="hero-meta"><span className="meta-chip"><i /> {ROWS} × {COLUMNS} grid</span><span className="meta-chip">4-way movement</span></div></section>

      {error && <div className="error-banner" role="alert"><span className="error-symbol">!</span><div><strong>Something went wrong</strong><span>{error}</span></div><button type="button" aria-label="Dismiss error" onClick={() => setError('')}>×</button></div>}

      <div className="workspace-layout">
        <ControlPanel algorithm={algorithm} onAlgorithm={value => { setAlgorithm(value); setComparison([]); clearPath() }} tool={tool} onTool={setTool} speed={speed} onSpeed={setSpeed} loading={loading} onFind={runSearch} onReset={resetGrid} onClear={clearPath} onRandom={randomWalls} onCompare={runComparison} />

        <div className="main-column">
          <section className="grid-card">
            <div className="grid-card-heading"><div><span className="eyebrow">YOUR MAP</span><h2>Interactive grid</h2></div><div className="grid-heading-right"><span className="edit-hint"><span className="cursor-icon">↖</span> Click or drag to edit</span><button className="icon-button" type="button" aria-label="Reset grid" title="Reset grid" onClick={resetGrid}>↻</button></div></div>
            <div className="grid-stage"><Grid rows={ROWS} columns={COLUMNS} state={cells} tool={tool} visited={visited} path={path} onChange={setCells} disabled={loading} /></div>
            <div className="grid-legend"><span><i className="legend-start">S</i> Start</span><span><i className="legend-end">E</i> Destination</span><span><i className="legend-wall" /> Wall</span><span><i className="legend-visited" /> Visited</span><span><i className="legend-path" /> Shortest path</span><span className="legend-count">{cells.walls.size} walls</span></div>
          </section>

          <StatsPanel result={result} searching={loading && !comparing} />
        </div>
      </div>

      <ComparisonTable results={comparison} loading={comparing} />
      <footer className="page-footer"><span><span className="footer-mark">⌁</span> Built for curious minds.</span><span>PathFinder <i>·</i> Find your way through the algorithm.</span><span className="footer-algorithm"><i style={{ background: algo.color }} />{algo.short} selected</span></footer>
    </main>
  </div>
}
