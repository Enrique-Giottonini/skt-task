# skt-task

## Technologies

- SpringBoot 1.5.7-RELEASE
- JDK 1.8
- Maven 3
- Apache Kafka
- MS SQL Server
- Docker version 26.0.0

## How to run

You will need a configuration file for your DB
```env
/sqlserver.env

ACCEPT_EULA=Y
MSSQL_PID=Developer
MSSQL_SA_PASSWORD=Sg.0s5+39?D)
MSSQL_TCP_PORT=1433
```

And for Kafka
```env
KAFKA_BROKER_ID: 1
KAFKA_ZOOKEEPER_CONNECT: stk-task-zookeeper:2181
KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://stk-task-kafka:9092,PLAINTEXT_HOST://localhost:1113
KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT
KAFKA_INTER_BROKER_LISTENER_NAME: PLAINTEXT
KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
```

```bash
# Start the MS SQL Server Database and Apache Kafka
docker compose up -d

mvn clean package
java -jar productsManagement/target/productsManagement-1.0-SNAPSHOT.war
java -jar productsService/target/productsService-1.0-SNAPSHOT.jar 

# For stopping the containers
docker compose down
```

Front app: http://localhost:8881
- GET  /product
- GET  /product/new
- POST /product/new


