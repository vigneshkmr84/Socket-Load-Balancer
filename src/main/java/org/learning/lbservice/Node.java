package org.learning.lbservice;

import lombok.*;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Node implements Comparable<Node>{

    String hostName;

    Integer weight;

    Integer requestCount;

    Double avgResponseTime;

    String svcName;

    public Node(String hostName, Integer weight, Integer requestCount, String svcName){
        this.hostName = hostName;
        this.weight = weight;
        this.requestCount = requestCount;
        this.svcName = svcName;
        this.avgResponseTime = 0.0;
    }

    @Override
    public int compareTo(Node other) {
        // Compare based on request count (lower count has higher priority)
        return Integer.compare(this.requestCount, other.requestCount);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Node other = (Node) obj;
        return Objects.equals(hostName, other.hostName);
    }

//    @Override
//    public int hashCode() {
//        return Objects.hash(hostName);
//    }

    @Override
    public int hashCode() {
        return Objects.hash(hostName, weight, requestCount);
    }


}
