package com.pathfinder.model;

import java.util.List;

public record Grid(int rows, int columns, List<List<Node>> nodes) {
    public Node at(int row, int column) { return nodes.get(row).get(column); }
}
