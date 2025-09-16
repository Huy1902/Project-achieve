
# 1. API
- insert
- delMax/delMin
- isEmpty

# 2. Implementation
![[Pasted image 20241222114628.png]]
Another way follow binary heap:
- Sink
- Swim
- Insert: cost at most 1 + lg N compares (swim)
- delMax/delMin: exchange root with node at end, then sink it down (at most 2lg N compares)