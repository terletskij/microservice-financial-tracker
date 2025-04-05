> [!NOTE]
> This project is under development.
> Some new features coming soon

# 📊Financial Tracker
Microservice-based application for managing personal finances.

## 🚀TODO

- **Auth Service**
  - [ ] **Entity & Database**: create `User` entity and setup PostgreSQL table users with (id, username, email, password, role)
  - [ ] Implement user registration and login
  - [ ] JWT token generation and validation
- **Transaction Service**
  - [x] **Entity & Database**: create `Transaction` entity and configure PostgreSQL connection
  - [x] **Service & Repository**: implement `TransactionService` and `TransactionRepository` for CRUD operations
  - [x] **Controller**: implement `TransactionController`
  - [x] **Validation & Error Handling**: Implement data validation and global error handling
  - [x] **Documentation**: Integrate Swagger/OpenAPI for API documentation
  - [x] **Testing**: Write unit and integration tests
- **API Gateway & Service Discovery**
  - [ ] Configure Spring Cloud Gateway for routing
