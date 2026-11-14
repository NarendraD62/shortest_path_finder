package com.pathfinder.service;

import com.pathfinder.datastructure.Graph;
import com.pathfinder.exception.PathfindingException;
import com.pathfinder.model.*;
import com.pathfinder.service.algorithm.PathfindingAlgorithm;
import com.pathfinder.service.algorithm.FloydWarshallAlgorithm;
import java.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class PathfindingService {
    private final Map<AlgorithmType, PathfindingAlgorithm> algorithms;
    public PathfindingService(List<PathfindingAlgorithm> strategies) {
        EnumMap<AlgorithmType,PathfindingAlgorithm> map=new EnumMap<>(AlgorithmType.class);
        strategies.forEach(a->map.put(a.type(),a)); algorithms=Map.copyOf(map);
    }
    public AlgorithmResult find(PathRequest request) {
        Set<Position> obstacles=validateAndGetObstacles(request.grid(),request.start(),request.end());
        validateAlgorithmGrid(request.algorithm(),request.grid());
        return algorithm(request.algorithm()).findPath(new Graph(request.grid().rows(),request.grid().columns(),obstacles),request.start(),request.end());
    }
    public CompareResponse compare(CompareRequest request) {
        Set<Position> obstacles=validateAndGetObstacles(request.grid(),request.start(),request.end());
        List<PathResponse> results=request.algorithms().stream().map(type -> {
            validateAlgorithmGrid(type,request.grid());
            Graph graph=new Graph(request.grid().rows(),request.grid().columns(),obstacles);
            return PathResponse.from(algorithm(type).findPath(graph,request.start(),request.end()));
        }).toList();
        return new CompareResponse(results);
    }
    private PathfindingAlgorithm algorithm(AlgorithmType type) {
        PathfindingAlgorithm found=algorithms.get(type);
        if(found==null) throw new PathfindingException(HttpStatus.BAD_REQUEST,"Unsupported algorithm: "+type);
        return found;
    }
    private void validateAlgorithmGrid(AlgorithmType type, GridRequest grid) {
        if(type==AlgorithmType.FLOYD_WARSHALL && (long)grid.rows()*grid.columns()>FloydWarshallAlgorithm.MAX_CELLS)
            throw bad("FLOYD_WARSHALL supports grids of at most "+FloydWarshallAlgorithm.MAX_CELLS+" cells (received "+((long)grid.rows()*grid.columns())+").");
        algorithm(type);
    }
    private Set<Position> validateAndGetObstacles(GridRequest grid, Position start, Position end) {
        if(grid.rows()<1||grid.columns()<1) throw bad("Grid rows and columns must be at least 1.");
        Set<Position> obstacles=new HashSet<>();
        for(int i=0;i<grid.obstacles().size();i++) {
            List<Integer> pair=grid.obstacles().get(i);
            if(pair==null||pair.size()!=2||pair.get(0)==null||pair.get(1)==null) throw bad("Obstacle at index "+i+" must be a [row, column] pair.");
            Position p=new Position(pair.get(0),pair.get(1)); validatePosition(p,grid,"Obstacle"); obstacles.add(p);
        }
        validatePosition(start,grid,"Start"); validatePosition(end,grid,"End");
        if(obstacles.contains(start)) throw bad("Start position cannot be an obstacle.");
        if(obstacles.contains(end)) throw bad("End position cannot be an obstacle.");
        return obstacles;
    }
    private void validatePosition(Position p, GridRequest grid, String label) {
        if(p.row()<0||p.row()>=grid.rows()||p.column()<0||p.column()>=grid.columns())
            throw bad(label+" position ("+p.row()+", "+p.column()+") is outside the grid.");
    }
    private PathfindingException bad(String message) { return new PathfindingException(HttpStatus.BAD_REQUEST,message); }
}
