# Socket LoadBalancer (Layer 7)

The code demonstrates various Load balancer for REST APIs using socket implementation.  

Used TCP Socket connection to simulate the REST APIs (GET) from browser and a server pool in the backend to balance (written in `Spring Boot`)the load.
Based on the parameters, and the choice of load balancer the connections will be routed accordingly to the backend server.

When the backend server is started, it is configured with a discovery host and port to identify itself. 

Discovery thread will accept incoming connections from the backend server which will identify itself. 

Health Check thread will periodically check the health status (with a `TCP PING`) of the backend node. When a node goes down, it's marked `UNHEALTHY` and requests will not be served to it.  

Additionally, a printer thread will print the status of the node and it's details.

For the Core Implementation `Factory Design Pattern` is utilized to abstract the basic methods. Each Load Balancer type will have a different `Thread-safe Data Structure`.

Types of Load Balancers:
- Round Robin (classical)
- Weighted Round Robin
- Source IP Hash
