package com.pathfinder.service.algorithm;

import com.pathfinder.datastructure.Graph;
import com.pathfinder.model.*;

public interface PathfindingAlgorithm {
    AlgorithmType type();
    AlgorithmResult findPath(Graph graph, Position start, Position end);
}
