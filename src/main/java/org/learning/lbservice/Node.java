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

//    Integer port;

    Integer weight;

    Integer requestCount;

    public void incrementRequest() {
        this.requestCount++;
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

    @Override
    public int hashCode() {
        return Objects.hash(hostName);
    }
}
