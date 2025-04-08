> [!NOTE]
> This project is under development.
> Some new features coming soon

# 📊Financial Tracker
Microservice-based application for managing personal finances.

## 🚀TODO

- **Auth Service**
  - [x] **Entity & Database**: create `User` entity and setup PostgreSQL table `users` with (id, username, email, password, role)
  - [ ] **Security**: Configure security, implement JWT token generation and validation
  - [ ] **Service**: Implement `AuthService` with `registration` and `login`
  - [ ] **Controller**: Implement  `AuthController` with `registration` and `login` endpoints
- **Transaction Service**
  - [x] **Entity & Database**: create `Transaction` entity and configure PostgreSQL connection
  - [x] **Service & Repository**: implement `TransactionService` and `TransactionRepository` for CRUD operations
  - [x] **Controller**: implement `TransactionController`
  - [x] **Validation & Error Handling**: Implement data validation and global error handling
  - [x] **Documentation**: Integrate Swagger/OpenAPI for API documentation
  - [x] **Testing**: Write unit and integration tests
- **API Gateway & Service Discovery**
  - [ ] Configure Spring Cloud Gateway for routing
