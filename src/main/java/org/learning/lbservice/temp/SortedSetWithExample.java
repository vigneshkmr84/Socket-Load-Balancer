package org.learning.lbservice.temp;

import org.learning.lbservice.Node;

import java.util.Comparator;
import java.util.TreeSet;

public class SortedSetWithExample {
    public static void main(String[] args) {

        Comparator<Node> nodeComparator = (n1, n2) -> {
            if (n1.getWeight() > n2.getWeight()) {
                return 1;
            } else if (n1.getWeight() < n2.getWeight()) {
                return -1;
            }
            return 0;
        };

        // Create a TreeSet of integers
        TreeSet<Node> sortedSet = new TreeSet<>(nodeComparator);

        // Add elements to the sorted set
        sortedSet.add(new Node("h1:8080", 1, 0));
        sortedSet.add(new Node("h1:8080", 1, 0));
        sortedSet.add(new Node("h1:8080", 1, 0));
        sortedSet.add(new Node("h2:8080", 1, 0));


        System.out.println("Original SortedSet: " + sortedSet);

        Node n = sortedSet.pollFirst();
        n.setWeight(n.getRequestCount() + 1);
        sortedSet.add(n);

        System.out.println("Updated SortedSet: " + sortedSet);
    }

}
