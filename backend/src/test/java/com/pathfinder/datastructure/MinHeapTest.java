package com.pathfinder.datastructure;

import com.pathfinder.model.Node;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MinHeapTest {
    @Test void extractsInAscendingCostAndBecomesEmpty() {
        MinHeap heap=new MinHeap(); Node a=node(8),b=node(2),c=node(5);
        heap.insert(a);heap.insert(b);heap.insert(c);
        assertSame(b,heap.extractMin());assertSame(c,heap.extractMin());assertSame(a,heap.extractMin());assertTrue(heap.isEmpty());assertNull(heap.extractMin());
    }
    @Test void decreaseKeyRestoresHeapOrder() {
        MinHeap heap=new MinHeap();Node a=node(3),b=node(7),c=node(9);heap.insert(a);heap.insert(b);heap.insert(c);
        c.setTotalCost(1);heap.decreaseKey(c);assertSame(c,heap.extractMin());
    }
    private Node node(int cost){Node n=new Node(0,0,false);n.setTotalCost(cost);return n;}
}
