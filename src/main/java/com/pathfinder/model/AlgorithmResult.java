package com.pathfinder.model;

import java.util.List;

public record AlgorithmResult(AlgorithmType algorithm, boolean pathFound, List<Position> visitedNodes,
                              List<Position> path, int pathLength, int visitedNodeCount, double executionTimeMs) { }
