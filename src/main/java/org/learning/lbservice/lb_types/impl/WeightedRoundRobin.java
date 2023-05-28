package org.learning.lbservice.lb_types.impl;

import org.learning.lbservice.Node;
import org.learning.lbservice.lb_types.LoadBalancer;

import java.util.Comparator;
import java.util.concurrent.LinkedBlockingDeque;

public class WeightedRoundRobin implements LoadBalancer {

    LinkedBlockingDeque<Node> queue = new LinkedBlockingDeque<>();

    @Override
    public Object getQueue() {
        return queue;
    }

    @Override
    public Node getNextNode() {
        return queue.peekFirst();
    }

    @Override
    public void insertNode(Node n) {
        queue.add(n);
    }

    @Override
    public Comparator<Node> getComparator() {

        return Comparator.comparing(Node::getWeight);
    }

    @Override
    public void print() {
        queue.forEach(System.out::println);
    }

    @Override
    public String getType() {
        return "Weighted Round Robin";
    }

    @Override
    public void updateNode(Node node) {
        // Remove the first element from queue
        queue.pollFirst();
        // if the request count has reached the weight, then add at the end
        // else add the updated node at the beginning itself
        if (node.getRequestCount() % node.getWeight() == 0){
            queue.add(node);
        }else{
            // need to update the element at the beginning itself
            queue.offerFirst(node);
        }
    }
}
