package com.pathfinder.model;

/** Mutable search state for one grid cell. Identity equality is intentional for heap bookkeeping. */
public class Node {
    private final int row;
    private final int column;
    private int distance = Integer.MAX_VALUE;
    private int heuristic;
    private int totalCost = Integer.MAX_VALUE;
    private Node parent;
    private final boolean obstacle;

    public Node(int row, int column, boolean obstacle) {
        this.row = row; this.column = column; this.obstacle = obstacle;
    }
    public int getRow() { return row; }
    public int getColumn() { return column; }
    public int getDistance() { return distance; }
    public void setDistance(int distance) { this.distance = distance; }
    public int getHeuristic() { return heuristic; }
    public void setHeuristic(int heuristic) { this.heuristic = heuristic; }
    public int getTotalCost() { return totalCost; }
    public void setTotalCost(int totalCost) { this.totalCost = totalCost; }
    public Node getParent() { return parent; }
    public void setParent(Node parent) { this.parent = parent; }
    public boolean isObstacle() { return obstacle; }
}
