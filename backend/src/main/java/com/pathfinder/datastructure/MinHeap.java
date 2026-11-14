package com.pathfinder.datastructure;

import com.pathfinder.model.Node;
import java.util.*;

/** Binary min heap keyed by Node.totalCost, with O(log n) decrease-key support. */
public class MinHeap {
    private final List<Node> heap = new ArrayList<>();
    private final Map<Node, Integer> positions = new IdentityHashMap<>();
    public boolean isEmpty() { return heap.isEmpty(); }
    public boolean contains(Node node) { return positions.containsKey(node); }
    public void insert(Node node) {
        if (positions.containsKey(node)) { decreaseKey(node); return; }
        heap.add(node); int index = heap.size() - 1; positions.put(node, index); heapifyUp(index);
    }
    public Node extractMin() {
        if (heap.isEmpty()) return null;
        Node min = heap.get(0), last = heap.remove(heap.size() - 1); positions.remove(min);
        if (!heap.isEmpty()) { heap.set(0, last); positions.put(last, 0); heapifyDown(0); }
        return min;
    }
    /** Call after lowering a node's total cost. If absent, the node is inserted. */
    public void decreaseKey(Node node) {
        Integer index = positions.get(node);
        if (index == null) insert(node); else heapifyUp(index);
    }
    private void heapifyUp(int index) {
        while (index > 0) { int parent = (index - 1) / 2; if (compare(heap.get(parent), heap.get(index)) <= 0) break; swap(parent, index); index = parent; }
    }
    private void heapifyDown(int index) {
        while (true) {
            int left = index * 2 + 1, right = left + 1, smallest = index;
            if (left < heap.size() && compare(heap.get(left), heap.get(smallest)) < 0) smallest = left;
            if (right < heap.size() && compare(heap.get(right), heap.get(smallest)) < 0) smallest = right;
            if (smallest == index) return; swap(index, smallest); index = smallest;
        }
    }
    private int compare(Node a, Node b) { int c = Integer.compare(a.getTotalCost(), b.getTotalCost()); return c != 0 ? c : Integer.compare(a.getDistance(), b.getDistance()); }
    private void swap(int a, int b) { Node tmp = heap.get(a); heap.set(a, heap.get(b)); heap.set(b, tmp); positions.put(heap.get(a), a); positions.put(heap.get(b), b); }
}
