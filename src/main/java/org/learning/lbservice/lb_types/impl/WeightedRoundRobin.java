package org.learning.lbservice.lb_types.impl;

import org.learning.lbservice.Node;
import org.learning.lbservice.lb_types.LBInterface;

import java.util.Comparator;

public class WeightedRoundRobin implements LBInterface {

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
}
