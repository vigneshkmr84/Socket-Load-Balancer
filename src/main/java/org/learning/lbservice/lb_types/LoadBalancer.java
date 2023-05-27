package org.learning.lbservice.lb_types;

import org.learning.lbservice.Node;

import java.util.Comparator;

public interface LoadBalancer {

    public Object getQueue();

    public Node getNextNode();

    public void insertNode(Node n);

    public Comparator<Node> getComparator();

    public void print();

    public String getType();

    public void updateNode(Node node);
}
