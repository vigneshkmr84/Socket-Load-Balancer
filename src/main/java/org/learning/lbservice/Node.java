package org.learning.lbservice;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Node {

    String hostName;

//    Integer port;

    Integer weight;

    Integer requestCount;
}
