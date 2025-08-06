
## 🧩 Architecture Overview

               +------------------+
               |  API Gateway     |
               |  (Spring Cloud)  |
               +------------------+
                    |       |         |
     ---------------        ----------          ---------------
    |                             |             |             |
+-----------+         +----------------+    +---------------------+
| UserService|         | WalletService |    | TransactionService  |
+-----------+         +----------------+    +---------------------+
                                         |
                                +-------------------+
                                | AnalyticsService  |
                                |  (Elasticsearch)  |
                                +-------------------+
                                         |
                                +------------------+
                                | Orchestrator     |
                                | (Saga Manager)   |
                                +------------------+

            ⟳ Kafka Topics (event bus): transaction.initiated, wallet.debited, transaction.created

Folder Structure :

src
├── main
│ ├── java/com/bookstoreproject
│ │ ├── beans/ # All request and response payload DAOs (e.g., TransactionRequestRecord.java)
│ │ ├── controller/ # REST and gRPC controllers
│ │ ├── entity/ # JPA Entities
│ │ ├── event/ # Kafka Event Payloads
│ │ ├── exceptions/ # Custom exceptions and handlers
│ │ ├── repository/ # JPA Repositories
│ │ ├── service/ # Service Interfaces
│ │ ├── serviceImpl/ # Service Implementations
│ ├── proto/ # gRPC proto definitions
│ └── resources/
│ ├── application.yml # Spring Boot config
│ ├── bootstrap.yml # (Optional) Spring Cloud configs
│ └── ...

----------------------------------------------------------------------------------------------------------------------------------------------------------

## ⚙️ Technologies Used

| Tool/Framework     | Purpose                                   |
|--------------------|-------------------------------------------|
| Java 21            | Core language                             |
| Spring Boot        | Application framework                     |
| Spring Data JPA    | Database access layer                     |
| gRPC               | High-performance service communication    |
| Apache Kafka       | Messaging & event-driven architecture     |
| Redis              | Caching/Key-value store                   |
| Docker             | Containerization                          |
| Docker Compose     | Multi-service orchestration               |
| Elasticsearch      | Distributed search engine (Implemented    |
                     |           Kibana)                         |


----------------------------------------------------------------------------------------------------------------------------------------------------------

## 🚀 Setup & Run the Project Locally

### ✅ Prerequisites

- Java 21
- Maven 3.8+
- Docker & Docker Compose
- IntelliJ or VS Code (recommended)

----------------------------------------------------------------------------------------------------------------------------------------------------------

## 🔧 Step-by-Step Instructions

1. Clone the Repository

2. Install Dependencies
mvn clean install

3. Run Docker Services
This project uses Kafka, Zookeeper, and Redis through Docker. You can find the required docker-compose.yml file in the root directory .

To start all services:

docker-compose up -d
This will start:

Kafka on localhost:9092
Zookeeper on localhost:2181
Redis on localhost:6379

4. Run the Spring Boot Application
mvn spring-boot:run
Or run it from your IDE via FinEdgeApplication.java.

📡 gRPC Setup & Usage
Proto Location
The proto files are located under:

src/main/proto/wallet.proto
Compile gRPC Proto
Ensure that the protobuf plugin is configured in pom.xml. 
Run:
mvn clean compile
This will auto-generate gRPC stubs in target/generated-sources.

----------------------------------------------------------------------------------------------------------------------------------------------------------


🧪 How CQRS + SAGA Works
🔁 Saga Workflow (Wallet DEBIT Example)
POST /api/orchestrator/start
→ emits transaction.initiated

WalletService consumes →
→ checks + debits wallet → emits wallet.debited

TransactionService consumes →
→ creates transaction → emits transaction.created

OrchestratorService consumes →
→ logs saga completion

All messages flow through Kafka topics.

----------------------------------------------------------------------------------------------------------------------------------------------------------


🧪 How to Test Functionality

Kafka logs will also be printed in console if set up correctly.

🧰 Kafka Testing
You can verify Kafka integration by checking if event payloads are logged or stored when a wallet transaction is created.

Make sure to create a Kafka consumer or console listener (or add another microservice later in the roadmap).

📖 How to Continue Development
Upcoming modules to add (suggested order):
Transaction Service

Analytics & Dashboard Service

Email Notification via Kafka

gRPC Inter-service Communication

Elasticsearch integration

Distributed tracing (Zipkin/Jaeger)

CI/CD Pipelines

Kubernetes deployment

👨‍💻 Contribution Guidelines
Follow clean architecture and separation of concerns

Use beans/ folder for request/response DAOs only

Use event/ for all Kafka event classes

Use controller/, service/, serviceImpl/, repository/ as per standard layered architecture

Write tests after completing functional implementation

Document each module/service as you build

💡 Tips
Use Postman or Swagger UI to test your REST APIs

Kafka Manager UI or Kafdrop can help visualize Kafka topics

Use RedisInsight for Redis inspection (optional)

Don’t mix DTOs and Entities; keep layers clean

👤 Author
Piyush Barhanpurkar
Backend Developer | FinTech Learner
India 🇮🇳
