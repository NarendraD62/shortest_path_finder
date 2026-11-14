import { ALGORITHMS, type PathResult } from '../types'
interface Props { results: PathResult[]; loading: boolean }
export function ComparisonTable({ results, loading }: Props) {
  const winner = results.filter(r => r.pathFound).reduce<PathResult | null>((best, r) => !best || r.visitedNodeCount < best.visitedNodeCount ? r : best, null)
  return <section className="comparison-card">
    <div className="comparison-heading"><div><span className="eyebrow">BENCHMARK</span><h2>Algorithm comparison</h2></div><span className="compare-caption">Same map, different strategies</span></div>
    {loading ? <div className="table-message"><span className="spinner" />Running both algorithms on your grid…</div> : results.length === 0 ? <div className="table-message empty-table"><span className="table-empty-icon">⇄</span><span>Run a comparison to see how each algorithm explores your map.</span></div> : <div className="table-scroll"><table><thead><tr><th>ALGORITHM</th><th>PATH LENGTH</th><th>NODES VISITED</th><th>TIME</th><th>RESULT</th></tr></thead><tbody>{results.map(result => {
      const algorithm = ALGORITHMS.find(a => a.value === result.algorithm)
      return <tr key={result.algorithm} className={winner?.algorithm === result.algorithm ? 'winner-row' : ''}><td><span className="table-algorithm"><i style={{ background: algorithm?.color }} />{algorithm?.short ?? result.algorithm}{winner?.algorithm === result.algorithm && <em>Efficient</em>}</span></td><td>{result.pathFound ? `${result.pathLength} steps` : '—'}</td><td>{result.visitedNodeCount.toLocaleString()}</td><td>{result.executionTimeMs.toFixed(2)} ms</td><td><span className={`table-result ${result.pathFound ? 'yes' : 'no'}`}>{result.pathFound ? '✓ Found' : '× No path'}</span></td></tr>
    })}</tbody></table></div>}
    <div className="comparison-footer"><span className="info-icon">i</span>Execution time is measured server-side and may vary between runs.</div>
  </section>
}
