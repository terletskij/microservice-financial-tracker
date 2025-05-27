> [!NOTE]
> This project is under development.
> Some new features coming soon

# 📊Financial Tracker
Microservice-based application for managing personal finances.
## 🛠 Tech Stack
  - **Language & Framework**: Java 17, Spring Boot, Spring Security, Spring Data JPA
  - **Database**: PostgreSQL
  - **Testing**: JUnit, Mockito
  - **DevTools**: Maven, Docker
  - **API documentation**: OpenAPI (Swagger)

## 📑 Table of Contents
- [About the Project](#-financial-tracker)
- [Features](#-features)
- [Installation & Run](#how-to-install--run)
- [Roadmap](#-roadmap)

## 🚀 Features
  - User registration & login (JWT-based authentication) <br>
  - Track income and expenses <br>
  - Modular microservices architecture <br>
  - Unit & integration testing <br>
  - API documentation via Swagger / OpenAPI
## How to install & run
### 1. Clone the repository
    git clone https://github.com/terletskij/microservice-financial-tracker.git
    cd microservice-financial-tracker
### 2.Create and configure .env file:
In the root directory of the project, create a file named `.env` and add the following environment variables:

    # === Database URLs ===
    TRANSACTION_DATASOURCE_URL=jdbc:postgresql://transaction-db:5432/transaction_db
    AUTH_DATASOURCE_URL=jdbc:postgresql://auth-db:5432/auth_db
    
    # === Common Database Credentials ===
    DB_USERNAME=your_db_username
    DB_PASSWORD=your_db_password
    
    # === Ports Configuration ===
    POSTGRES_PORT=5432
    TRANSACTION_SERVICE_PORT=8082
    AUTH_SERVICE_PORT=8081
    
    # === Database Names ===
    TRANSACTION_DB_NAME=transaction_db
    AUTH_DB_NAME=auth_db
    
    # === JWT Configuration ===
    JWT_SECRET=your_jwt_secret_key
    JWT_EXPIRATION=3600000              # in milliseconds (e.g., 1 hour)
    JWT_REFRESH_EXPIRATION=86400000     # in milliseconds (e.g., 24 hours)
> [!NOTE] 
> Replace the values (e.g., your_db_username, your_jwt_secret_key, etc.) with your own configuration.
> These values are used by Docker containers and Spring Boot services during startup.
### 3. Build services
> [!IMPORTANT]
> **Temporary Step**! <br>
> We're working on simplifying the build process by introducing a parent pom.xml.
> Automatic build coming soon!
To build service manually for now:

**Step 1**: navigate to service directory
For example, to build transaction-service.

    cd transaction-service
**Step 2**: use the following command

    mvn clean package
Repeat these steps for each microservice (auth-service, transaction-service, gateway-service, discovery-service).
> [!IMPORTANT]
> 🙏 Sorry for the extra steps right now — we’re working on making the setup fully automatic soon.
### 4. Start the application (Docker)
Start the application by using the following command

    docker-compose up --build

## 🚀 ROADMAP
> [!NOTE]
> New features soon
- **GitHub Repository**:
  - [ ] Provide clear and beginner-friendly documentation (installation, usage, environment setup)
  - [ ] Add architecture diagram and service overview
  - [ ] Simplify project startup with improved docker-compose automation
- **Development Goals**:
  - [ ] Refactor .env variables, remove redundant 
  - [ ] Implement centralized logging (e.g., ELK stack or simple logger config)
  - [ ] Add rate limiting and request validation in API Gateway
  - [ ] Implement role-based access control (RBAC)
  - [ ] Add email verification and password reset flow in Auth Service
- **Discovery Service**
  - [x] Initialize service with Spring Cloud Netflix Eureka
  - [x] Configure application.yml
  - [x] Update docker-compose.yml to include the discovery-service with port mapping.
  - [x] Configure application.yml in each service to point to http://discovery-service:8761/eureka/ as the defaultZone.
  - [x] Verify that all services register with Eureka and that API calls work through the gateway.
- **API Gateway**
  - [x] Initialize Spring Cloud Gateway 
  - [X] Configure basic routes to `auth-service` and `transaction-service`
  - [ ] Implement `JWT` validation in `API Gateway`
  - [ ] Extract `userId` from token and forward to services via `headers`
  - [ ] Add `CORS` configuration for frontend
        
- **Auth Service**
  - [x] **Entity & Database**: create `User` entity and setup PostgreSQL table `users` with (id, username, email, password, role)
  - [x] **Security**: Configure security, implement JWT token generation and validation
  - [x] **Service**: Implement `AuthService` with `registration` and `login`
  - [x] **Controller**: Implement  `AuthController` with `registration` and `login` endpoints

- **Transaction Service**
  - [x] **Entity & Database**: create `Transaction` entity and configure PostgreSQL connection
  - [x] **Service & Repository**: implement `TransactionService` and `TransactionRepository` for CRUD operations
  - [x] **Controller**: implement `TransactionController`
  - [x] **Validation & Error Handling**: Implement data validation and global error handling
  - [x] **Documentation**: Integrate Swagger/OpenAPI for API documentation
  - [x] **Testing**: Write unit and integration tests
