# How to run

## Compile

To compile run
```
javac Store.java
javac TestClient.java
```

## Running

A Store should be called using
```
java Store <IP_mcast_addr> <IP_mcast_port> <node_id>  <Store_port> 
for example
    java Store 224.0.0.251 80 127.0.0.1 600
```

A TestClient should be called using
```
java TestClient <node_ap> <operation> [<opnd>]
for example
    java TestClient 127.0.0.1:600 put test.txt
```

We created a class Called demonstration, it is helpful 
creating and operating several nodes.
Right now, it creates 5 nodes, joins all of them and then 
leaves and rejoins one of them from the cluster.
```
java Demonstrate
```
