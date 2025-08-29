# Patient Management System

A microservice-based application designed for patient management processes in healthcare settings

## Architecture Overview
- **Patient Service:** Manages patient records.
- **Billing Service:** Handles billings and payment processes.
- **Analytics Service:** Provides analytical insights.
 
## Features
- Java
- SpringBoot
- Docker
- gRPC
- Kafka
- Kafka-UI

## Development Setup
- Build and run **patient-service**
  ```
  docker-compose up --build
  ```
- Build and run **billing-service**
  ```
  cd billing-service
  docker build -t billing-service:latest .
  docker run -d -p 4001:4001 -p 9001:9001 --name billing-service --network patient-service_patient-management-network billing-service:latest
  ```
- Build and run **analytics-service**
  ```
  cd analytics-service
  docker build -t analytics-service:latest .
  docker run -d -p 4001:4001 -p 9001:9001 --name analytics-service --network patient-service_patient-management-network analytics-service:latest
  ```
- Build and run **kafka**
   ```
  docker run -d --name kafka -p 9092:9092 -p 9094:9094 \
    -e KAFKA_CFG_ADVERTISED_LISTENERS=PLAINTEXT://kafka:9092,EXTERNAL://localhost:9094; \
    -e KAFKA_CFG_CONTROLLER_LISTENER_NAMES=CONTROLLER; \
    -e KAFKA_CFG_CONTROLLER_QUORUM_VOTERS=0@kafka:9093; \
    -e KAFKA_CFG_LISTENER_SECURITY_PROTOCOL_MAP=CONTROLLER:PLAINTEXT,EXTERNAL:PLAINTEXT,PLAINTEXT:PLAINTEXT; \
    -e KAFKA_CFG_LISTENERS=PLAINTEXT://:9092,CONTROLLER://:9093,EXTERNAL://:9094; \
    -e KAFKA_CFG_NODE_ID=0; \
    -e KAFKA_CFG_PROCESS_ROLES=controller,broker \
     --network patient-service_patient-management-network bitnami/kafka:latest
  ```


- Build and run **kafka-ui**
  ```
  docker run -d --name kafka-ui -p 8085:8080 --network patient-service_patient-management-network -e KAFKA_CLUSTERS_0_NAME=local -e KAFKA_CLUSTERS_0_BOOTSTRAPSERVERS=kafka:9092 provectuslabs/kafka-ui:latest
  ```
  
