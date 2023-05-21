package org.learning.lbservice.temp;

import org.learning.lbservice.Node;
import org.learning.lbservice.NodeComparator;

import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;

public class PriorityQueueMain {


    public static void main(String[] args){

        Comparator<Node> nodeComparator = new Comparator<Node>() {
            @Override
            public int compare(Node n1, Node n2) {
                if (n1.getRequestCount() > n2.getRequestCount()){
                    return 1;
                }else if (n1.getRequestCount()< n2.getRequestCount()){
                    return -1;
                }
                return 0;
            }
        };

        PriorityBlockingQueue<Node> queue = new PriorityBlockingQueue<>(2, nodeComparator);

        queue.add(new Node("h1:8080", 1, 12));
        queue.add(new Node("h2:8080", 1, 12));
        queue.add(new Node("h1:8080", 1, 10));
//        queue.add(new Node("h1", 8080, true, 15));
//        queue.add(new Node("h1", 8080, true, 13));

        while (!queue.isEmpty())
            System.out.println(queue.poll().toString());

    }
    // blocking priority for the weights + HM for the status
}
