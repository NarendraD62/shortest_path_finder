# PathFinder – Route Planner & Algorithm Visualizer

A Java 21 / Spring Boot 3 REST backend that finds routes on a four-direction grid and reports the search order, path, node count, and measured execution time. All moves have unit cost; diagonal movement and weighted terrain are not supported.

## Architecture

- `controller`: HTTP endpoints only.
- `service`: request validation, graph creation, algorithm selection, and comparison orchestration.
- `service.algorithm`: one strategy per algorithm behind `PathfindingAlgorithm`.
- `datastructure`: adjacency-list `Graph` and custom binary `MinHeap` used by Dijkstra.
- `model`: request/response DTOs, grid, node, and algorithm results.
- `exception`: meaningful JSON errors via `@RestControllerAdvice`.

Each comparison run gets a fresh graph so mutable search state cannot leak between algorithms. CORS origins default to `http://localhost:3000` and `http://localhost:5173`; configure them with `PATHFINDER_CORS_ALLOWED_ORIGINS` as a comma-separated list.

## Run

Requires JDK 21 and Maven 3.9+ for the backend. The React frontend requires Node.js 20+ and npm.

```bash
mvn spring-boot:run
```

The server listens on `http://localhost:8080`. Build a jar with `mvn package`, then run `java -jar target/pathfinder-1.0.0.jar`.

In another terminal, start the frontend:

```bash
cd frontend
npm install
npm run dev
```

Vite serves the UI at `http://localhost:5173`. The frontend calls `http://localhost:8080` by default. Set `VITE_API_BASE_URL` in `frontend/.env` to change the API host (see `frontend/.env.example`).

## API

### `POST /api/pathfinding/find-path`

Request fields are `algorithm` (`DIJKSTRA`, `ASTAR`, `BFS`, `DFS`, or `FLOYD_WARSHALL`), `grid` (`rows`, `columns`, and obstacle coordinate pairs), `start`, and `end`.

```json
{
  "algorithm": "DIJKSTRA",
  "grid": { "rows": 5, "columns": 7, "obstacles": [[2, 3], [2, 4]] },
  "start": { "row": 0, "column": 0 },
  "end": { "row": 4, "column": 6 }
}
```

Response includes `algorithm`, `pathFound`, `visitedNodes` (search order), `path` (start through end), `pathLength` (number of moves, zero for no path or start=end), `visitedNodeCount`, and `executionTimeMs` (measured with `System.nanoTime()`). An unreachable target returns HTTP 200 with `pathFound: false` and an empty path.

### `POST /api/pathfinding/compare`

Uses the same `grid`, `start`, and `end` fields, with an `algorithms` array. Returns `{ "results": [ ... ] }`, with the full result metrics for each requested algorithm.

```json
{
  "algorithms": ["DIJKSTRA", "ASTAR", "BFS", "DFS"],
  "grid": { "rows": 5, "columns": 7, "obstacles": [[2, 3]] },
  "start": { "row": 0, "column": 0 },
  "end": { "row": 4, "column": 6 }
}
```

Rows and columns must each be between 1 and 200. Obstacles must be in bounds; start and end must be in bounds and unblocked. Floyd–Warshall is limited to grids of at most 400 cells because its time and memory costs are cubic and quadratic. Invalid input returns HTTP 400 with a JSON `message` and, where applicable, field `details`.

## Algorithms and complexity

Let `V = rows × columns` and `E ≤ 4V` for this grid graph.

| Algorithm | Behavior | Time | Space |
|---|---|---:|---:|
| Dijkstra | Shortest path with a custom binary min heap and adjacency list. | `O((V + E) log V)` | `O(V + E)` |
| A* | Shortest path using `f = g + h`, with Manhattan distance. | `O((V + E) log V)` worst case | `O(V + E)` |
| BFS | Shortest path for this unweighted grid using a queue. | `O(V + E)` | `O(V)` |
| DFS | Returns the first discovered route; it is not guaranteed shortest. | `O(V + E)` | `O(V)` |
| Floyd–Warshall | Computes all-pairs distances, then reports the requested pair's route. | `O(V³)` | `O(V²)` |

`executionTimeMs` is wall-clock algorithm execution time and varies by machine and JVM warm-up; it is not intended as a stable benchmark across separate requests.

## Tests

JUnit 5 coverage is in `src/test/java` for the custom heap, Dijkstra, A*, BFS, DFS, blocked routes, and start-equals-end behavior. Run with `mvn test`.
