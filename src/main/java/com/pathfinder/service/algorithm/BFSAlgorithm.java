package com.pathfinder.service.algorithm;

import com.pathfinder.datastructure.Graph;
import com.pathfinder.model.*;
import java.util.*;
import org.springframework.stereotype.Component;

@Component
public class BFSAlgorithm implements PathfindingAlgorithm {
    public AlgorithmType type() { return AlgorithmType.BFS; }
    public AlgorithmResult findPath(Graph graph,Position start,Position end) {
        long started=System.nanoTime(); Node source=SearchSupport.start(graph,start),target=graph.getGrid().at(end.row(),end.column()); Queue<Node> q=new ArrayDeque<>(); Set<Node> seen=Collections.newSetFromMap(new IdentityHashMap<>()); List<Node> visited=new ArrayList<>(); q.add(source);seen.add(source);
        while(!q.isEmpty()) { Node current=q.remove();visited.add(current);if(current==target)break;for(Node next:graph.neighbors(current))if(seen.add(next)){next.setDistance(current.getDistance()+1);next.setParent(current);q.add(next);} }
        return SearchSupport.result(type(),visited,SearchSupport.path(target),started);
    }
}
