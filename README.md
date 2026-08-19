# Task Service - Microservice

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.3.4-brightgreen)

![Java](https://img.shields.io/badge/Java-11-orange)

![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Database-blue)

![Keycloak](https://img.shields.io/badge/Keycloak-OAuth2-red)

![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-Hoxton.SR8-blue)

![License](https://img.shields.io/badge/License-MIT-yellow)

Enterprise-grade **Task Management Microservice** built with Spring Boot, featuring OAuth2 authentication via Keycloak, inter-service communication with Feign Client, service discovery with Eureka, and production-ready architecture.

> **Architecture:** This is a **microservice** component of the Ticketing Application ecosystem, designed for independent deployment, scalability, and distributed system capabilities.

---

## Table of Contents

- [Overview](#overview)

- [Key Features](#key-features)

- [Technology Stack](#technology-stack)

- [Architecture](#architecture)

- [API Documentation](#api-documentation)

- [Getting Started](#getting-started)

- [Configuration](#configuration)

- [Security](#security)

- [Inter-Service Communication](#inter-service-communication)

- [Database Schema](#database-schema)

- [API Endpoints](#api-endpoints)

- [Microservices Ecosystem](#microservices-ecosystem)

---

## Overview

The **Task Service** is a dedicated microservice responsible for managing tasks within the Ticketing Application ecosystem. It provides comprehensive task lifecycle management, including creation, updates, status tracking, deletion, and integration with Project Service and User Service for task-related operations.

### Use Cases

- **Task Management:** Create, update, and manage tasks with detailed tracking

- **Status Management:** Track task status (Open, In Progress, Completed)

- **Project Integration:** Link tasks to projects and validate project access

- **Employee Assignment:** Assign tasks to employees and validate employee roles

- **Manager Operations:** Managers can create, update, and delete tasks for their projects

- **Employee Operations:** Employees can view and update status of their assigned tasks

- **Bulk Operations:** Complete or delete all tasks for a project

### Service Responsibilities

- **Task CRUD Operations:** Full lifecycle management of tasks

- **Status Tracking:** Manage task status transitions (Open → In Progress → Completed)

- **Project Service Integration:** Communicate with Project Service for project validation and manager verification

- **User Service Integration:** Communicate with User Service for employee validation

- **Access Control:** Enforce role-based and ownership-based access restrictions

- **Task Aggregation:** Provide task counts and statistics by project and employee

---

## Key Features

### Security & Authentication

- **OAuth2 Integration** with Keycloak for enterprise-grade authentication

- **Role-Based Access Control (RBAC)** with Manager and Employee roles

- **Ownership-Based Access Control** ensuring users can only access their own tasks/projects

- **JWT Token Validation** for stateless authentication

- **Token Propagation** via Feign Client Interceptor for inter-service calls

- **Access Control Enforcement** ensuring only authorized roles can perform operations

### Architecture & Design

- **Microservice Architecture** with independent deployment capabilities

- **RESTful API Design** following industry best practices

- **Clean Architecture** with clear separation of concerns

- **DTO Pattern** for decoupling domain models from API contracts

- **Global Exception Handling** for consistent error responses

- **Entity Auditing** with BaseEntity pattern

- **Soft Delete Pattern** for data retention

### Inter-Service Communication

- **Feign Client** for declarative REST client communication

- **Service Discovery** via Eureka for dynamic service location

- **Load Balancing** with Spring Cloud LoadBalancer

- **Token Propagation** for authenticated inter-service calls

- **Project Service Integration** for project validation and manager retrieval

- **User Service Integration** for employee validation

### Business Features

- **Task Management:** Complete CRUD operations for tasks

- **Status Workflow:** Track task progression through statuses (Open, In Progress, Completed)

- **Project-Based Operations:** Retrieve, complete, and delete tasks by project

- **Employee-Specific Views:** Employees can view their pending and archived tasks

- **Task Statistics:** Get task counts by project and employee

- **Bulk Operations:** Complete or delete all tasks for a project

- **Access Validation:** Ensure users can only access tasks they own or manage

### Technical Excellence

- **Spring Cloud OpenFeign** for service-to-service communication

- **Eureka Client** for service discovery

- **Swagger/OpenAPI 3.0** interactive API documentation

- **Bean Validation** for input validation

- **ModelMapper** for object mapping

- **Log4j2** for advanced logging capabilities

- **Spring Boot Actuator** for health checks and monitoring

---

## Technology Stack

### Core Framework

- **Spring Boot 2.3.4.RELEASE** - Application framework

- **Spring Web** - REST API development

- **Spring Data JPA** - Data persistence

- **Spring Security** - Security framework

- **Spring Cloud Hoxton.SR8** - Microservices framework

### Microservices & Communication

- **Spring Cloud OpenFeign** - Declarative REST client

- **Spring Cloud Netflix Eureka Client** - Service discovery

- **Spring Cloud LoadBalancer** - Client-side load balancing

### Database

- **PostgreSQL** - Primary database

- **Hibernate** - ORM framework

### Security & Authentication

- **Keycloak 18.0.0** - Identity and Access Management

- **Keycloak Spring Boot Adapter** - Keycloak integration

- **Keycloak Admin Client** - Keycloak administration

- **JWT** - Token-based authentication

### Documentation & Utilities

- **SpringDoc OpenAPI 3 (1.6.6)** - API documentation

- **Lombok 1.18.30** - Boilerplate code reduction

- **ModelMapper 3.1.0** - Object mapping

- **Log4j2** - Advanced logging

### Build & Development

- **Maven** - Dependency management

- **Java 11** - Programming language

---

## Architecture

### Microservice Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    API Gateway                              │
│              (Spring Cloud Gateway)                         │
└──────────────────────┬──────────────────────────────────────┘
                       │
        ┌──────────────┴──────────────┐
        │                             │
┌───────▼────────┐          ┌─────────▼──────────┐
│  Task Service  │          │  Project Service    │
│ (This Service) │          │                     │
└───────┬────────┘          └─────────────────────┘
        │
        │ 
        │
┌───────▼────────┐
│   PostgreSQL   │
│   (task-db)    │
└────────────────┘
        ┌──────────────┐
        │   Eureka     │
        │  Discovery   │
        └──────────────┘
        ┌──────────────┐
        │   Keycloak   │
        │  Auth Server │
        └──────────────┘
```

### Service Layer Architecture

```
┌─────────────────────────────────────────────┐
│         Controller Layer                    │
│      (TaskController)                       │
└─────────────────┬───────────────────────────┘
                  │
┌─────────────────▼───────────────────────────┐
│      Service Layer (Interface)              │
│      (TaskService, KeycloakService)        │
└─────────────────┬───────────────────────────┘
                  │
┌─────────────────▼───────────────────────────┐
│   Service Implementation Layer              │
│   (TaskServiceImpl, KeycloakServiceImpl)    │
│   • Business Logic                          │
│   • Keycloak Integration                    │
│   • Project Service Communication           │
│   • User Service Communication              │
│   • Access Control Validation               │
└─────────────────┬───────────────────────────┘
                  │
        ┌─────────┴─────────┐
        │                   │
┌───────▼────────┐  ┌───────▼────────┐
│   Repository   │  │  Feign Clients │
│    (JPA)       │  │  (ProjectClient,│
│                │  │   UserClient)  │
└───────┬────────┘  └────────────────┘
        │
┌───────▼───────┐
│  PostgreSQL   │
└───────────────┘
         Cross-Cutting Concerns
┌─────────────────────────────────────────────┐
│  • Global Exception Handling                │
│  • Security (Keycloak Integration)          │
│  • Token Propagation (Feign Interceptor)    │
│  • Entity Auditing                          │
│  • Logging (Log4j2)                         │
└─────────────────────────────────────────────┘
```

### Project Structure

```
com.cydeo
├── client                    # Feign clients
│   ├── interceptor
│   │   └── FeignClientInterceptor
│   ├── ProjectClient
│   └── UserClient
├── config                    # Configuration classes
│   ├── KeycloakProperties
│   ├── OpenAPI3Configuration
│   └── SecurityConfig
├── controller                # REST controllers
│   └── TaskController
├── dto                       # Data Transfer Objects
│   ├── TaskDTO
│   ├── Response
│   │   ├── ProjectResponse
│   │   └── UserResponse
│   └── wrapper
│       ├── ExceptionWrapper
│       ├── ResponseWrapper
│       └── ValidationExceptionWrapper
├── entity                    # JPA entities
│   ├── BaseEntity
│   └── Task
├── enums                     # Enumerations
│   └── Status
├── exception                 # Exception handling
│   ├── GlobalExceptionHandler
│   ├── EmployeeCheckFailedException
│   ├── EmployeeNotFoundException
│   ├── ManagerNotRetrievedException
│   ├── ProjectAccessDeniedException
│   ├── ProjectCheckFailedException
│   ├── ProjectNotFoundException
│   ├── TaskAccessDeniedException
│   ├── TaskAlreadyExistsException
│   ├── TaskNotFoundException
│   ├── UserNotEmployeeException
│   └── UserNotFoundException
├── repository                # Data repositories
│   └── TaskRepository
├── service                   # Business logic
│   ├── KeycloakService
│   ├── TaskService
│   └── impl
│       ├── KeycloakServiceImpl
│       └── TaskServiceImpl
└── util                      # Utilities
    ├── MapperUtil
    └── SwaggerExamples
```

---

## API Documentation

The API is fully documented using **Swagger/OpenAPI 3.0** with interactive documentation.

### Accessing Swagger UI

Once the application is running, navigate to:

```
http://localhost:8383/swagger-ui.html
```

### Features of API Documentation

- **Interactive Testing:** Try out API endpoints directly from the browser

- **OAuth2 Integration:** Authenticate using Keycloak within Swagger UI

- **Complete Schema Documentation:** View all request/response models

- **Role-Based Filtering:** See which endpoints are accessible to each role

---

## Getting Started

### Prerequisites

1. **Java 11** or higher

   ```bash
   java -version
   ```

2. **Maven 3.6+**

   ```bash
   mvn -version
   ```

3. **PostgreSQL 12+**

   ```bash
   psql --version
   ```

4. **Keycloak Server** (Running on port 9090)

   - Download from: https://www.keycloak.org/downloads

   - Or use Docker:

   ```bash
   docker run -p 9090:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin quay.io/keycloak/keycloak:18.0.0 start-dev
   ```

5. **Eureka Server** (Running on port 8761)

   - Service discovery server must be running

   - Other microservices in the ecosystem should be registered

6. **Project Service** (Running and registered with Eureka)

   - Required for inter-service communication

   - Must be accessible via service discovery

7. **User Service** (Running and registered with Eureka)

   - Required for inter-service communication

   - Must be accessible via service discovery

### Installation Steps

1. **Clone the repository**

   ```bash
   git clone <repository-url>
   cd task-service-master
   ```

2. **Create PostgreSQL Database**

   ```sql
   CREATE DATABASE task-db;
   ```

3. **Configure Keycloak**

   - Access Keycloak Admin Console: `http://localhost:9090`

   - Create a new realm: `cydeo-dev`

   - Create a client: `ticketing-app`

   - Configure client settings:

     - Client Protocol: `openid-connect`

     - Access Type: `confidential`

     - Valid Redirect URIs: `http://localhost:8383/*`

   - Create client roles: `Manager`, `Employee`

   - Note the client secret for configuration

4. **Update Application Configuration**

   Edit `src/main/resources/application.yml`:

   ```yaml
   spring:
     datasource:
       url: jdbc:postgresql://localhost:5432/task-db
       username: your_postgres_username
       password: your_postgres_password
   
   keycloak:
     realm: cydeo-dev
     auth-server-url: http://localhost:9090/auth
     resource: ticketing-app
     credentials:
       secret: your_client_secret
   
   eureka:
     client:
       service-url:
         defaultZone: http://localhost:8761/eureka
   ```

5. **Build the project**

   ```bash
   mvn clean install
   ```

6. **Run the application**

   ```bash
   mvn spring-boot:run
   ```

   Or run the JAR file:

   ```bash
   java -jar target/task-service-0.0.1-SNAPSHOT.jar
   ```

7. **Verify Installation**

   - Application: `http://localhost:8383`

   - Swagger UI: `http://localhost:8383/swagger-ui.html`

   - Health Check: `http://localhost:8383/actuator/health`

   - Eureka Registration: Check Eureka dashboard at `http://localhost:8761`

---

## Configuration

### Application Properties

| Property | Description | Default Value |

|----------|-------------|---------------|

| `server.port` | Application port | 8383 |

| `spring.application.name` | Service name for Eureka | task-service |

| `spring.datasource.url` | PostgreSQL connection URL | jdbc:postgresql://localhost:5432/task-db |

| `spring.jpa.hibernate.ddl-auto` | Hibernate DDL mode | create |

| `spring.jpa.show-sql` | Show SQL queries | true |

| `keycloak.realm` | Keycloak realm name | cydeo-dev |

| `keycloak.auth-server-url` | Keycloak server URL | http://localhost:9090/auth |

| `keycloak.resource` | Keycloak client ID | ticketing-app |

| `eureka.client.service-url.defaultZone` | Eureka server URL | http://localhost:8761/eureka |

### Environment-Specific Configuration

For production environments, consider:

- Using environment variables for sensitive data

- Setting `spring.jpa.hibernate.ddl-auto=validate`

- Enabling HTTPS

- Using external configuration management (Spring Cloud Config Server)

- Configuring proper Eureka instance settings for production

---

## Security

### Authentication Flow

1. **User Authentication**

   - Users authenticate via Keycloak

   - Keycloak issues JWT access token

   - Client includes JWT in Authorization header: `Bearer <token>`

2. **Token Validation**

   - Spring Security validates JWT signature

   - Extracts user roles from token claims

   - Enforces role-based access control

3. **Inter-Service Authentication**

   - Feign Client Interceptor automatically adds JWT token to requests

   - Token propagation ensures authenticated communication with Project Service and User Service

### Role-Based Access Control

| Role | Permissions |

|------|-------------|

| **Manager** | Full access to task management operations for their projects, can create, read, update, and delete tasks |

| **Employee** | Can view and update status of their assigned tasks, view archived and pending tasks |

### Access Control Rules

- **Managers** can only access tasks belonging to projects they manage

- **Employees** can only access tasks assigned to them

- Access is enforced at both method level (`@RolesAllowed`) and business logic level

- Project ownership is validated via Project Service integration

- Employee assignment is validated via User Service integration

### Secured Endpoints

```java
// Manager Only
POST   /api/v1/task/create
GET    /api/v1/task/read/all/{projectCode}
GET    /api/v1/task/count/project/{projectCode}
PUT    /api/v1/task/update/{taskCode}
PUT    /api/v1/task/complete/project/{projectCode}
DELETE /api/v1/task/delete/{taskCode}
DELETE /api/v1/task/delete/project/{projectCode}

// Manager or Employee
GET    /api/v1/task/read/{taskCode}

// Employee Only
GET    /api/v1/task/read/employee/archive
GET    /api/v1/task/read/employee/pending-tasks
PUT    /api/v1/task/update/employee/{taskCode}

// Admin Only
GET    /api/v1/task/count/employee/{assignedEmployee}
```

---

## Inter-Service Communication

### Feign Client Integration

The service communicates with **Project Service** and **User Service** using Spring Cloud OpenFeign:

```java
@FeignClient(name = "project-service")
public interface ProjectClient {
    @GetMapping("/api/v1/project/checks/{projectCode}")
    ResponseEntity<ProjectResponse> checkByProjectCode(@PathVariable String projectCode);

    @GetMapping("/api/v1/project/read/manager/{projectCode}")
    ResponseEntity<ProjectResponse> getManagerByProject(@PathVariable String projectCode);
}

@FeignClient(name = "user-service")
public interface UserClient {
    @GetMapping("/api/v1/user/check/{username}")
    ResponseEntity<UserResponse> checkByUsername(@PathVariable String username);
}
```

### Service Discovery

- **Eureka Client** enables automatic service discovery

- Service registers itself with Eureka on startup

- Feign Client uses service name (`project-service`, `user-service`) instead of hardcoded URLs

- Load balancing is handled automatically by Spring Cloud LoadBalancer

### Token Propagation

- **FeignClientInterceptor** automatically adds JWT token to all Feign requests

- Ensures authenticated communication between services

- Token is extracted from current security context

### Communication Flow

```
Task Service Request
         │
         ├─► Extract JWT Token
         │
         ├─► FeignClientInterceptor adds token to header
         │
         ├─► Eureka resolves service name to actual URL
         │
         ├─► Project Service receives authenticated request
         │
         └─► User Service receives authenticated request
```

---

## Database Schema

### Entity Relationship

```
┌─────────────────────────────────────┐
│           Task Entity               │
├─────────────────────────────────────┤
│ id (PK)                             │
│ taskCode (UNIQUE)                   │
│ taskSubject                         │
│ taskDetail                          │
│ taskStatus (ENUM)                   │
│ assignedDate (DATE)                │
│ projectCode                         │
│ assignedEmployee                    │
│ assignedManager                     │
│                                     │
│ (BaseEntity fields)                 │
│ insertDateTime                      │
│ lastUpdateDateTime                  │
│ insertUserId                        │
│ lastUpdateUserId                    │
│ isDeleted                           │
└─────────────────────────────────────┘
```

### Key Entities

1. **Task**: Core entity representing a task

   - Unique task code (5 characters)

   - Task subject and detail

   - Status enumeration (OPEN, IN_PROGRESS, COMPLETED)

   - Project code reference

   - Assigned employee and manager

   - Soft delete support

### Audit Fields (BaseEntity)

All entities inherit audit fields:

- `insertDateTime`: Record creation timestamp

- `lastUpdateDateTime`: Last modification timestamp

- `insertUserId`: Creator user ID

- `lastUpdateUserId`: Last modifier user ID

- `isDeleted`: Soft delete flag

### Status Enumeration

```java
public enum Status {
    OPEN("Open"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed");
}
```

---

## API Endpoints

### Task Management

| Method | Endpoint | Description | Role Required |

|--------|----------|-------------|---------------|

| POST | `/api/v1/task/create` | Create a new task | Manager |

| GET | `/api/v1/task/read/{taskCode}` | Get task by task code | Manager, Employee |

| GET | `/api/v1/task/read/all/{projectCode}` | Get all tasks by project code | Manager |

| GET | `/api/v1/task/read/employee/archive` | Get all archived (completed) tasks | Employee |

| GET | `/api/v1/task/read/employee/pending-tasks` | Get all pending (non-completed) tasks | Employee |

| GET | `/api/v1/task/count/project/{projectCode}` | Get task counts by project | Manager |

| GET | `/api/v1/task/count/employee/{assignedEmployee}` | Get non-completed task count by employee | Admin |

| PUT | `/api/v1/task/update/{taskCode}` | Update a task | Manager |

| PUT | `/api/v1/task/update/employee/{taskCode}` | Update task status | Employee |

| PUT | `/api/v1/task/complete/project/{projectCode}` | Complete all tasks of a project | Manager |

| DELETE | `/api/v1/task/delete/{taskCode}` | Delete a task | Manager |

| DELETE | `/api/v1/task/delete/project/{projectCode}` | Delete all tasks of a project | Manager |

### Response Format

All endpoints return a consistent response wrapper:

**Success Response:**

```json
{
  "success": true,
  "statusCode": 200,
  "message": "Task is successfully retrieved.",
  "data": { /* response data */ }
}
```

**Error Response:**

```json
{
  "success": false,
  "message": "Task does not exist.",
  "httpStatus": "NOT_FOUND",
  "localDateTime": "2024-01-05T10:30:00"
}
```

**Validation Error Response:**

```json
{
  "success": false,
  "message": "Invalid Input(s)",
  "httpStatus": "BAD_REQUEST",
  "localDateTime": "2024-01-05T10:30:00",
  "errorCount": 2,
  "validationExceptions": [
    {
      "errorField": "taskCode",
      "rejectedValue": "invalid",
      "reason": "Task code should include 5 characters."
    }
  ]
}
```

---

## Microservices Ecosystem

### Architecture Overview

This service is part of a larger microservices ecosystem:

```
                    ┌─────────────────────┐
                    │   API Gateway        │
                    │ (Spring Cloud Gateway)│
                    └──────────┬───────────┘
                               │
        ┌──────────────────────┼──────────────────────┐
        │                      │                      │
┌───────▼────────┐    ┌────────▼────────┐    ┌────────▼────────┐
│  Task Service  │    │ Project Service │    │  User Service  │
│  (This Service)│    │                 │    │                 │
└────────────────┘    └────────┬────────┘    └─────────────────┘
                               │
                    ┌──────────┴──────────┐
                    │                     │
            ┌───────▼────────┐    ┌───────▼────────┐
            │   PostgreSQL   │    │   PostgreSQL   │
            │   (task-db)    │    │  (project-db)  │
            └────────────────┘    └────────────────┘
                    ┌─────────────────────┐
                    │   Eureka Server     │
                    │  (Service Discovery)│
                    └─────────────────────┘
                    ┌─────────────────────┐
                    │   Keycloak Server   │
                    │  (Authentication)  │
                    └─────────────────────┘
```

### Service Dependencies

- **Eureka Server**: Required for service discovery

- **Project Service**: Required for project-related operations

  - Project existence validation

  - Manager retrieval for access control

- **User Service**: Required for user-related operations

  - Employee existence validation

  - User role verification

- **Keycloak Server**: Required for authentication and authorization

### Service Communication Patterns

1. **Synchronous Communication**

   - REST API calls via Feign Client

   - Used for immediate data retrieval and operations

2. **Service Discovery**

   - Eureka-based service location

   - Dynamic service resolution

3. **Load Balancing**

   - Client-side load balancing via Spring Cloud LoadBalancer

   - Automatic distribution of requests

### Benefits of Microservices Architecture

- **Independent Scalability:** Scale task service based on task management load

- **Technology Flexibility:** Use different tech stacks per service

- **Fault Isolation:** Failures in one service don't affect others

- **Independent Deployment:** Deploy task service without affecting other services

- **Team Autonomy:** Different teams can own different services

- **Database per Service:** Own PostgreSQL database for task data

---

## Project Status

**Current Version:** 0.0.1-SNAPSHOT

**Status:** Active Development

**Architecture:** Microservice (Part of Ticketing Application Ecosystem)

### Recent Updates

- OAuth2 integration with Keycloak

- Comprehensive API documentation with Swagger

- Role-based and ownership-based access control implementation

- Global exception handling

- Entity auditing system

- Feign Client integration with Project Service and User Service

- Eureka service discovery integration

- Token propagation for inter-service calls

- Task status workflow management

- Employee and Manager specific operations

- Keycloak role validation

### Upcoming Features

- Integration tests suite

- Docker containerization

- CI/CD pipeline setup

- Distributed tracing (Spring Cloud Sleuth)

- Circuit breaker pattern (Resilience4j)

- Event-driven communication (Kafka/RabbitMQ)

- Caching layer implementation (Redis)

- Task assignment notifications

- Task deadline management

---

## Troubleshooting

### Common Issues

1. **Service Not Registering with Eureka**

   - Verify Eureka server is running on port 8761

   - Check `eureka.client.service-url.defaultZone` configuration

   - Ensure network connectivity between services

2. **Project Service or User Service Communication Failures**

   - Verify services are running and registered with Eureka

   - Check service names match in Feign Clients (`project-service`, `user-service`)

   - Verify JWT token propagation in FeignClientInterceptor

3. **Keycloak Authentication Issues**

   - Verify Keycloak server is running

   - Check realm and client configuration

   - Verify client secret matches configuration

   - Ensure Keycloak admin credentials are correct

4. **Database Connection Issues**

   - Verify PostgreSQL is running

   - Check database credentials

   - Ensure database `task-db` exists

5. **Access Denied Errors**

   - Verify user has correct role (Manager or Employee)

   - Check that Manager is accessing tasks from their own projects

   - Check that Employee is accessing their own assigned tasks

   - Verify project ownership via Project Service

6. **Task Creation Failures**

   - Verify project exists in Project Service

   - Verify assigned employee exists and has Employee role

   - Check that Manager is the project manager

   - Verify task code is unique (5 characters)

### Health Checks

Access actuator endpoints for service health:

- Health: `http://localhost:8383/actuator/health`

- Info: `http://localhost:8383/actuator/info`

---

## License

This project is licensed under the MIT License.


