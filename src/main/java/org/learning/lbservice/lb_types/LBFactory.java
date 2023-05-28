package org.learning.lbservice.lb_types;

import org.learning.lbservice.lb_types.impl.LeastResponseTime;
import org.learning.lbservice.lb_types.impl.RandomSelector;
import org.learning.lbservice.lb_types.impl.RoundRobin;
import org.learning.lbservice.lb_types.impl.WeightedRoundRobin;

public class LBFactory {

    public LoadBalancer getLB(String lbType){

        switch (lbType.toUpperCase()){
            case "ROUND_ROBIN": return new RoundRobin();
            case "RANDOM" : return new RandomSelector();
            case "LEAST_RESPONSE_TIME": return new LeastResponseTime();
            case "WEIGHTED_ROUND_ROBIN" : return new WeightedRoundRobin();

            default: return new RoundRobin();
        }
    }
}
