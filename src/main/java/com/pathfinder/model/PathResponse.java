package com.pathfinder.model;

import java.util.List;

public record PathResponse(AlgorithmType algorithm, boolean pathFound, List<Position> visitedNodes,
                           List<Position> path, int pathLength, int visitedNodeCount, double executionTimeMs) {
    public static PathResponse from(AlgorithmResult result) {
        return new PathResponse(result.algorithm(), result.pathFound(), result.visitedNodes(), result.path(),
                result.pathLength(), result.visitedNodeCount(), result.executionTimeMs());
    }
}
