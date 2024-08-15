# skt-task

## Technologies

- SpringBoot 1.5.7-RELEASE
- JDK 1.8
- Maven 3
- Kafka

## How to run
```bash
# From the kafka source code
# Start the ZooKeeper service
bin/zookeeper-server-start.sh config/zookeeper.properties
# Start the Kafka broker service
bin/kafka-server-start.sh config/server.properties

# Create a topics for communication
bin/kafka-topics.sh --bootstrap-server localhost:9092 --create --replication-factor 1 --partitions 1 --topic newProduct
bin/kafka-topics.sh --bootstrap-server localhost:9092 --create --replication-factor 1 --partitions 1 --topic listAllProducts

mvn clean package
java -jar productsManagement/target/productsManagement-1.0-SNAPSHOT.war
java -jar productsService/target/productsService-1.0-SNAPSHOT.jar 

# Manual monitoring of communication
bin/kafka-console-consumer.sh --topic newProduct --from-beginning --bootstrap-server localhost:9092
bin/kafka-console-consumer.sh --topic listAllProducts --from-beginning --bootstrap-server localhost:9092
```

Front app: http://localhost:8881
- GET  /product
- GET  /product/new
- POST /product/new


