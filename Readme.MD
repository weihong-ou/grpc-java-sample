# Simple gRPC server and client

## Build the server jar
```bash
gradle serverJar
```

## Build the client jar
```bash
gradle clientJar
```

## Run in docker compose
```bash
docker compose up
```

## Run Server as standalone java
```bash
java -Dhostname=server1 -Dport=9000 -jar build/libs/server-1.0-SNAPSHOT.jar
```

## Run Client as standalone java
```bash
java -Dport=9000 -Dhostname=localhost -Dusername=dummy -jar build/libs/client-1.0-SNAPSHOT.jar
```