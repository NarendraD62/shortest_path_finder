package com.pathfinder.datastructure;

import com.pathfinder.model.*;
import java.util.*;

/** Four-direction, unit-weight graph representation of a grid. */
public class Graph {
    private final Grid grid;
    private final Map<Node, List<Node>> adjacency = new IdentityHashMap<>();
    private static final int[][] DIRECTIONS = {{-1,0},{1,0},{0,-1},{0,1}};
    public Graph(int rows, int columns, Set<Position> obstacles) {
        List<List<Node>> nodes = new ArrayList<>();
        for (int r = 0; r < rows; r++) { List<Node> row = new ArrayList<>(); for (int c = 0; c < columns; c++) row.add(new Node(r, c, obstacles.contains(new Position(r,c)))); nodes.add(row); }
        grid = new Grid(rows, columns, nodes);
        for (List<Node> row : nodes) for (Node node : row) {
            List<Node> neighbors = new ArrayList<>();
            if (!node.isObstacle()) for (int[] d : DIRECTIONS) {
                int nr = node.getRow()+d[0], nc = node.getColumn()+d[1];
                if (nr >= 0 && nr < rows && nc >= 0 && nc < columns) { Node neighbor = grid.at(nr,nc); if (!neighbor.isObstacle()) neighbors.add(neighbor); }
            }
            adjacency.put(node, neighbors);
        }
    }
    public Grid getGrid() { return grid; }
    public List<Node> neighbors(Node node) { return adjacency.getOrDefault(node, List.of()); }
}
