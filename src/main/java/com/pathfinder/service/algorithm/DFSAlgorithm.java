package com.pathfinder.service.algorithm;

import com.pathfinder.datastructure.Graph;
import com.pathfinder.model.*;
import java.util.*;
import org.springframework.stereotype.Component;

@Component
public class DFSAlgorithm implements PathfindingAlgorithm {
    public AlgorithmType type() { return AlgorithmType.DFS; }
    public AlgorithmResult findPath(Graph graph,Position start,Position end) {
        long started=System.nanoTime();Node source=SearchSupport.start(graph,start),target=graph.getGrid().at(end.row(),end.column());Deque<Node> stack=new ArrayDeque<>();Set<Node> seen=Collections.newSetFromMap(new IdentityHashMap<>());List<Node> visited=new ArrayList<>();stack.push(source);seen.add(source);
        while(!stack.isEmpty()){Node current=stack.pop();visited.add(current);if(current==target)break;List<Node> ns=graph.neighbors(current);for(int i=ns.size()-1;i>=0;i--){Node next=ns.get(i);if(seen.add(next)){next.setDistance(current.getDistance()+1);next.setParent(current);stack.push(next);}}}
        return SearchSupport.result(type(),visited,SearchSupport.path(target),started);
    }
}
