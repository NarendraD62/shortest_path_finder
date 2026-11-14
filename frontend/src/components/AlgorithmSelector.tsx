import { ALGORITHMS, type Algorithm } from '../types'

interface Props { value: Algorithm; onChange: (value: Algorithm) => void }
export function AlgorithmSelector({ value, onChange }: Props) {
  return <label className="field-label" htmlFor="algorithm-select">PATHFINDING ALGORITHM
    <select id="algorithm-select" value={value} onChange={e => onChange(e.target.value as Algorithm)}>
      {ALGORITHMS.map(a => <option key={a.value} value={a.value}>{a.label}</option>)}
    </select>
  </label>
}
