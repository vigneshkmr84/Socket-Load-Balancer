package org.learning.lbservice.lb_types.impl;

import org.learning.lbservice.Node;
import org.learning.lbservice.lb_types.LoadBalancer;

import java.util.Comparator;

public class WeightedRoundRobin implements LoadBalancer {

    @Override
    public Object getQueue() {
        return null;
    }

    @Override
    public Node getNextNode() {
        return null;
    }

    @Override
    public void insertNode(Node n) {

    }

    @Override
    public Comparator<Node> getComparator() {
        return null;
    }

    @Override
    public void print() {

    }

    @Override
    public String getType() {
        return "Weighted Round Robin";
    }
}
