package org.learning.lbservice.lb_types.impl;

import org.learning.lbservice.Node;
import org.learning.lbservice.lb_types.LBInterface;

import java.util.*;


public class RandomSelector implements LBInterface {

    // Synchronized List
    // alternate to Vectors
    List<Node> queue = Collections.synchronizedList(new ArrayList<>());

    @Override
    public Node getNextNode() {
        Random rand = new Random();
        return queue.get(rand.nextInt(queue.size()));
    }

    @Override
    public void insertNode(Node n) {
        queue.add(n);
    }

    @Override
    public Comparator<Node> getComparator() {
        return null;
    }

    @Override
    public void print() {
        queue.forEach(System.out::println);
    }
}
