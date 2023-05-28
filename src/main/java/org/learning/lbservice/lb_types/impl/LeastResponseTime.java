package org.learning.lbservice.lb_types.impl;

import org.learning.lbservice.Node;
import org.learning.lbservice.lb_types.LoadBalancer;

import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;

public class LeastResponseTime implements LoadBalancer {

    PriorityBlockingQueue<Node> queue = new PriorityBlockingQueue<>(3, getComparator());

    @Override
    public Object getQueue() {
        return queue;
    }

    @Override
    public Node getNextNode() {
        return queue.peek();
    }

    @Override
    public void insertNode(Node n) {
        queue.add(n);
    }

    @Override
    public Comparator<Node> getComparator() {
        return Comparator.comparing(Node::getAvgResponseTime);
    }

    @Override
    public void print() {
        queue.forEach(System.out::println);
    }

    @Override
    public String getType() {
        return "Least Response Time";
    }

    @Override
    public void updateNode(Node node) {
        queue.poll();
        queue.offer(node);
    }
}
