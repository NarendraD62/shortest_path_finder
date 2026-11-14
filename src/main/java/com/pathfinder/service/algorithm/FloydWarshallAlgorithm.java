package com.pathfinder.service.algorithm;

import com.pathfinder.datastructure.Graph;
import com.pathfinder.exception.PathfindingException;
import com.pathfinder.model.*;
import java.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class FloydWarshallAlgorithm implements PathfindingAlgorithm {
    public static final int MAX_CELLS = 400;
    public AlgorithmType type() { return AlgorithmType.FLOYD_WARSHALL; }
    public AlgorithmResult findPath(Graph graph, Position start, Position end) {
        long began=System.nanoTime(); int rows=graph.getGrid().rows(), cols=graph.getGrid().columns(), n=rows*cols;
        if(n>MAX_CELLS) throw new PathfindingException(HttpStatus.BAD_REQUEST,"FLOYD_WARSHALL supports grids of at most "+MAX_CELLS+" cells (received "+n+").");
        final int inf=1_000_000; int[][] dist=new int[n][n], next=new int[n][n];
        for(int i=0;i<n;i++){Arrays.fill(dist[i],inf);Arrays.fill(next[i],-1);dist[i][i]=0;next[i][i]=i;}
        for(int r=0;r<rows;r++)for(int c=0;c<cols;c++){Node node=graph.getGrid().at(r,c);if(node.isObstacle())continue;int from=r*cols+c;for(Node nb:graph.neighbors(node)){int to=nb.getRow()*cols+nb.getColumn();dist[from][to]=1;next[from][to]=to;}}
        for(int k=0;k<n;k++)for(int i=0;i<n;i++)if(dist[i][k]<inf)for(int j=0;j<n;j++)if(dist[k][j]<inf&&dist[i][j]>dist[i][k]+dist[k][j]){dist[i][j]=dist[i][k]+dist[k][j];next[i][j]=next[i][k];}
        int source=start.row()*cols+start.column(), target=end.row()*cols+end.column();
        List<Node> path=new ArrayList<>();if(next[source][target]!=-1){int at=source;path.add(graph.getGrid().at(at/cols,at%cols));while(at!=target){at=next[at][target];path.add(graph.getGrid().at(at/cols,at%cols));}}
        List<Node> visited=new ArrayList<>();for(int i=0;i<n;i++)if(dist[source][i]<inf)visited.add(graph.getGrid().at(i/cols,i%cols));
        // Keep visited order deterministic and use the all-pairs distance for the reported path length.
        List<Position> v=SearchSupport.positions(visited), p=SearchSupport.positions(path);
        return new AlgorithmResult(type(),!path.isEmpty(),v,p,path.isEmpty()?0:dist[source][target],v.size(),(System.nanoTime()-began)/1_000_000.0);
    }
}
