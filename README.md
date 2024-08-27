# skt-task

## Technologies

- SpringBoot 1.5.7-RELEASE
- JDK 1.8
- Maven 3
- Apache Kafka
- MS SQL Server
- Docker version 26.0.0

## How to run

```bash
# Start the MS SQL Server Database and Apache Kafka
docker compose up -d

mvn clean package
java -jar config-server/target/config-server-1.0-SNAPSHOT.jar       
java -jar product-management-webapp/target/product-management-webapp-1.0-SNAPSHOT.war          
java -jar product-service/target/product-service-1.0-SNAPSHOT.jar   

# For stopping the containers
docker compose down
```

Front app: http://localhost:8881
- GET  /product
- GET  /product/new
- POST /product/new


