package com.pathfinder.service.algorithm;

import com.pathfinder.datastructure.Graph;
import com.pathfinder.model.*;
import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class PathfindingAlgorithmsTest {
    @Test void dijkstraFindsShortestPathAndVisitsStartAndEnd() {
        AlgorithmResult result=new DijkstraAlgorithm().findPath(new Graph(3,3,Set.of()),new Position(0,0),new Position(2,2));
        assertTrue(result.pathFound());assertEquals(4,result.pathLength());assertEquals(new Position(0,0),result.path().getFirst());assertEquals(new Position(2,2),result.path().getLast());assertTrue(result.visitedNodeCount()>=1);
    }
    @Test void aStarFindsManhattanShortestPath() {
        AlgorithmResult result=new AStarAlgorithm().findPath(new Graph(5,6,Set.of(new Position(2,2))),new Position(0,0),new Position(4,5));
        assertTrue(result.pathFound());assertEquals(9,result.pathLength());assertFalse(result.path().contains(new Position(2,2)));
    }
    @Test void bfsFindsShortestPathOnUnweightedGrid() {
        AlgorithmResult result=new BFSAlgorithm().findPath(new Graph(3,4,Set.of(new Position(1,1))),new Position(0,0),new Position(2,3));
        assertTrue(result.pathFound());assertEquals(5,result.pathLength());
    }
    @Test void dfsReturnsAValidDiscoveredPath() {
        AlgorithmResult result=new DFSAlgorithm().findPath(new Graph(3,3,Set.of()),new Position(0,0),new Position(2,2));
        assertTrue(result.pathFound());assertEquals(new Position(0,0),result.path().getFirst());assertEquals(new Position(2,2),result.path().getLast());
    }
    @Test void allSearchAlgorithmsReportNoPathWhenSeparatedByWall() {
        for(PathfindingAlgorithm algorithm:new PathfindingAlgorithm[]{new DijkstraAlgorithm(),new AStarAlgorithm(),new BFSAlgorithm(),new DFSAlgorithm(),new FloydWarshallAlgorithm()}) {
            Graph graph=new Graph(3,3,Set.of(new Position(0,1),new Position(1,1),new Position(2,1)));
            AlgorithmResult result=algorithm.findPath(graph,new Position(1,0),new Position(1,2));assertFalse(result.pathFound(),algorithm.type().name());assertTrue(result.path().isEmpty());assertEquals(0,result.pathLength());
        }
    }
    @Test void startEqualsEndHasZeroLengthPath() {
        AlgorithmResult result=new BFSAlgorithm().findPath(new Graph(1,1,Set.of()),new Position(0,0),new Position(0,0));
        assertTrue(result.pathFound());assertEquals(0,result.pathLength());assertEquals(1,result.path().size());
    }
}
