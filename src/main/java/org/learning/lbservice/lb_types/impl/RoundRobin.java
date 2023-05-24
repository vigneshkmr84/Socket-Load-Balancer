package org.learning.lbservice.lb_types.impl;

import org.learning.lbservice.Node;
import org.learning.lbservice.lb_types.LBInterface;

import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class RoundRobin implements LBInterface {

    BlockingQueue<Node> queue = new LinkedBlockingQueue<>();

    @Override
    public Node getNextNode() {
        return queue.poll();
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
