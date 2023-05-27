package org.learning.lbservice.lb_types.impl;

import org.learning.lbservice.Node;
import org.learning.lbservice.lb_types.LoadBalancer;
import org.learning.lbservice.lb_types.enums.LBTypes;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;


public class RandomSelector implements LoadBalancer {

    // Synchronized List
    // alternate to Vectors
    CopyOnWriteArrayList<Node> queue = new CopyOnWriteArrayList<>();

    // in place update of the List
    AtomicInteger index = new AtomicInteger(0);

    @Override
    public CopyOnWriteArrayList<Node> getQueue() {
        return queue;
    }

    @Override
    public Node getNextNode() {
        Random rand = new Random();
        int i = rand.nextInt(queue.size());
        index.set(i);
        return queue.get(i);
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
        return LBTypes.Random.toString();
    }

    @Override
    public void updateNode(Node updatedNode) {
        queue.set(index.get(), updatedNode);
    }
}
