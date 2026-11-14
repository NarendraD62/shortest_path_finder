package com.pathfinder.service.algorithm;

import com.pathfinder.datastructure.*;
import com.pathfinder.model.*;
import java.util.*;
import org.springframework.stereotype.Component;

@Component
public class DijkstraAlgorithm implements PathfindingAlgorithm {
    public AlgorithmType type() { return AlgorithmType.DIJKSTRA; }
    public AlgorithmResult findPath(Graph graph, Position start, Position end) {
        long started=System.nanoTime(); Node source=SearchSupport.start(graph,start), target=graph.getGrid().at(end.row(),end.column()); MinHeap heap=new MinHeap(); heap.insert(source);
        Set<Node> settled=Collections.newSetFromMap(new IdentityHashMap<>()); List<Node> visited=new ArrayList<>();
        while(!heap.isEmpty()) { Node current=heap.extractMin(); if(!settled.add(current)) continue; visited.add(current); if(current==target) break;
            for(Node next:graph.neighbors(current)) if(!settled.contains(next)) { int candidate=current.getDistance()+1; if(candidate<next.getDistance()) { next.setDistance(candidate); next.setTotalCost(candidate); next.setParent(current); heap.decreaseKey(next); } }
        }
        return SearchSupport.result(type(),visited,SearchSupport.path(target),started);
    }
}
