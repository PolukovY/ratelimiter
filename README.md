# Getting Started

### Why we need rate limiter ?

1. **Preventing Abuse and Misuse**: Rate limiters prevent abusive behavior such as denial of service attacks, brute-force attacks, or simply overloading the system with too many requests.
2. **Protecting Server Resources**: By limiting the rate of incoming requests, rate limiters protect server resources from being overwhelmed, ensuring that the server can continue to function optimally for all users.
3. **Maintaining Quality of Service**: Without rate limiting, a small number of clients could monopolize server resources, leading to degraded service for other users. Rate limiting ensures fair access to server resources for all clients.
4. **Cost Control**: For services that operate under a pay-per-use model, rate limiting helps control costs by preventing excessive usage.
5. **Improving Performance**: By managing the rate of incoming requests, rate limiters can reduce server load, which can improve overall system performance and response times.
6. **Scaling**: Rate limiting helps in scaling the system by preventing sudden spikes in traffic that could overwhelm the system, allowing for more controlled growth.
7. **Stabilizing Traffic Patterns**: Rate limiters can help smooth out traffic patterns by spreading requests more evenly over time, reducing the likelihood of congestion during peak periods.
8. **Compliance and Security**: Rate limiting can be used to enforce security policies and compliance requirements. For example, limiting the number of login attempts can prevent brute-force attacks on user accounts.

Overall, rate limiting is a fundamental tool for ensuring the stability, security, and reliability of web services and APIs.

### Where we can put it?

1. Client side
2. Server side
- As a library inside api Gateway 
- As a library inside each service
- Dedicated service


### What Algorithms do we have?
- Token bucket
- Leaking bucket
- Fixed window counter
- Sliding window log
- Sliding window counter


### Requirement

**Functional Requirement:**
- client can send a limited number of requests to a server within a window
- client should get an error message if the defined threshold limit of request is crossed for a server single server or across different combinations of servers.

**Non-Functional Requirements:**

- The system should be highly available since it protects our service from external attacks.
- Performance is an important factor for any system. So, we need to be careful that rate limiter service should not add substantial latencies to the system.


Make redis up


docker pull redis:latest
docker run -d -p 6379:6379 redis
