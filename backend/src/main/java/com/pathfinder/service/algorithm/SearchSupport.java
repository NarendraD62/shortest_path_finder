package com.pathfinder.service.algorithm;

import com.pathfinder.model.*;
import java.util.*;

final class SearchSupport {
    private SearchSupport() { }
    static List<Position> positions(List<Node> nodes) { return nodes.stream().map(n -> new Position(n.getRow(), n.getColumn())).toList(); }
    static List<Node> path(Node end) {
        if (end == null || end.getDistance() == Integer.MAX_VALUE) return List.of();
        LinkedList<Node> result = new LinkedList<>(); for (Node n=end; n != null; n=n.getParent()) result.addFirst(n); return result;
    }
    static AlgorithmResult result(AlgorithmType type, List<Node> visited, List<Node> path, long started) {
        return new AlgorithmResult(type, !path.isEmpty(), positions(visited), positions(path), path.isEmpty()?0:path.size()-1,
                visited.size(), (System.nanoTime()-started)/1_000_000.0);
    }
    static Node start(com.pathfinder.datastructure.Graph graph, Position p) { Node n=graph.getGrid().at(p.row(),p.column()); n.setDistance(0); n.setTotalCost(0); return n; }
}
