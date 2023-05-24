package org.learning.lbservice.lb_types.impl;

import org.learning.lbservice.Node;
import org.learning.lbservice.lb_types.LoadBalancer;

import java.util.*;


public class RandomSelector implements LoadBalancer {

    // Synchronized List
    // alternate to Vectors
    List<Node> queue = Collections.synchronizedList(new ArrayList<>());

    @Override
    public List<Node> getQueue() {
        return queue;
    }

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

    @Override
    public String getType() {
        return "Random";
    }
}
