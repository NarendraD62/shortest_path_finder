package com.pathfinder.service.algorithm;

import com.pathfinder.datastructure.Graph;
import com.pathfinder.model.*;
import java.util.*;
import org.springframework.stereotype.Component;

@Component
public class AStarAlgorithm implements PathfindingAlgorithm {
    public AlgorithmType type() { return AlgorithmType.ASTAR; }
    public AlgorithmResult findPath(Graph graph, Position start, Position end) {
        long started=System.nanoTime(); Node source=SearchSupport.start(graph,start), target=graph.getGrid().at(end.row(),end.column());
        source.setHeuristic(h(source,target)); source.setTotalCost(source.getHeuristic());
        record Entry(Node node, int gCost, int fCost) { }
        PriorityQueue<Entry> open=new PriorityQueue<>(Comparator.comparingInt(Entry::fCost).thenComparingInt(Entry::gCost)); Set<Node> closed=Collections.newSetFromMap(new IdentityHashMap<>()); List<Node> visited=new ArrayList<>(); open.add(new Entry(source,0,source.getTotalCost()));
        while(!open.isEmpty()) { Entry entry=open.poll(); Node current=entry.node(); if(entry.gCost()!=current.getDistance()||!closed.add(current)) continue; visited.add(current); if(current==target) break;
            for(Node next:graph.neighbors(current)) if(!closed.contains(next)) { int candidate=current.getDistance()+1; if(candidate<next.getDistance()) { next.setDistance(candidate); next.setHeuristic(h(next,target)); next.setTotalCost(candidate+next.getHeuristic()); next.setParent(current); open.add(new Entry(next,candidate,next.getTotalCost())); } }
        }
        return SearchSupport.result(type(),visited,SearchSupport.path(target),started);
    }
    private int h(Node a,Node b) { return Math.abs(a.getRow()-b.getRow())+Math.abs(a.getColumn()-b.getColumn()); }
}
