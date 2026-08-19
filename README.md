<img src="https://capsule-render.vercel.app/api?type=waving&color=0:0F2027,50:203A43,100:2C5364&height=180&section=header&text=Task%20Service&fontSize=40&fontColor=ffffff&animation=fadeIn&fontAlignY=36&desc=Task%20Management%20%C2%B7%20Status%20Workflow&descAlignY=54&descAlign=50" width="100%"/>

<div align="center">
  <a href="https://github.com/berat-erkul/TicketinApp"><img src="https://img.shields.io/badge/System_Overview-TicketinApp-2C5364?style=for-the-badge" /></a>
</div>

Task Service is a Spring Boot microservice responsible for task management within the TicketinApp system: creating, updating, assigning and tracking tasks (status: OPEN, IN_PROGRESS, COMPLETED), scoped to projects and employees.

This service is one component of a larger microservices system. See the main repository for the full architecture and links to all services: [TicketinApp](https://github.com/berat-erkul/TicketinApp).

## 🧰 Tech Stack

<div align="center">

![Java](https://img.shields.io/badge/Java_11-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot_2.3.4-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Spring Cloud](https://img.shields.io/badge/Spring_Cloud_Hoxton.SR8-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)
![Keycloak](https://img.shields.io/badge/Keycloak-4D4D4D?style=for-the-badge&logo=keycloak&logoColor=white)
![OpenFeign](https://img.shields.io/badge/OpenFeign-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Eureka](https://img.shields.io/badge/Eureka_Client-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)
![Zipkin](https://img.shields.io/badge/Sleuth_%2B_Zipkin-FF6600?style=for-the-badge&logo=zipkin&logoColor=white)
![Prometheus](https://img.shields.io/badge/Prometheus-E6522C?style=for-the-badge&logo=prometheus&logoColor=white)
![Swagger](https://img.shields.io/badge/SpringDoc_OpenAPI-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)

</div>

- Java 11
- Spring Boot 2.3.4 (Spring Cloud Hoxton.SR8)
- Spring Web, Spring Data JPA, Spring Validation
- Spring Security + Keycloak (`keycloak-spring-boot-starter`, `keycloak-admin-client`)
- Spring Cloud OpenFeign (inter-service calls) + Spring Cloud LoadBalancer
- Spring Cloud Netflix Eureka Client (service discovery)
- Spring Cloud Config Client (centralized config) + Spring Cloud Vault Config
- Spring Cloud Sleuth + Zipkin (distributed tracing)
- Spring Boot Actuator + Micrometer (Prometheus registry)
- PostgreSQL
- SpringDoc OpenAPI UI (Swagger)
- Lombok, ModelMapper, Log4j2

## ✨ Features

- **Task CRUD**: create, read (by task code or by project), update, and delete tasks
- **Status workflow**: update task status, move a task from OPEN to IN_PROGRESS, bulk-complete all tasks of a project
- **Project scoping**: tasks are linked to a project code; task counts can be queried per project
- **Employee views**: an employee can list their pending tasks and their archived (completed) tasks, and check if they have a task on a given project
- **Role-based access control**: endpoints are restricted by role (Manager, Employee, Admin) via `@RolesAllowed`, backed by Keycloak-issued JWTs
- **Inter-service communication via OpenFeign**:
  - `ProjectClient` calls `project-service` to validate a project code and retrieve the project's manager
  - `UserClient` calls `user-service` to validate a username and check that a user is an employee
  - Feign client calls carry the caller's JWT (token propagation) so downstream services can enforce their own authorization
- **Centralized configuration**: connects to a config server on startup (`bootstrap.yml`) instead of hardcoding DB/Keycloak values

## 🚀 Getting Started

### Prerequisites

- Java 11
- Maven
- A running PostgreSQL instance
- A running config server, Eureka server, and Keycloak instance (this service depends on them at startup)

### Run with Maven

```bash
mvn clean install
mvn spring-boot:run
```

### Run with Docker

```bash
docker build -t task-service .
docker run task-service
```

> The `server.port` and most other properties are pulled from the central config server rather than set in this repo.

## ⚙️ Configuration

This service does not read local `application.yml` values for its core settings — it fetches them from a Spring Cloud Config Server at startup (`bootstrap.yml`). The following environment variables are expected at runtime:

| Variable | Purpose |
| --- | --- |
| `CENTRAL_CONFIG_SERVER_URI` | URL of the config server |
| `TASK_DB_URL` | PostgreSQL JDBC URL |
| `TASK_DB_USERNAME` | PostgreSQL username |
| `TASK_DB_PASSWORD` | PostgreSQL password |
| `TASK_KEYCLOAK_CREDENTIALS` | Keycloak client secret |
| `TASK_KEYCLOAK_MASTER_USERNAME` | Keycloak admin username (used by keycloak-admin-client) |
| `TASK_KEYCLOAK_MASTER_PASSWORD` | Keycloak admin password |
| `TASK_KEYCLOAK_MASTER_REALM` | Keycloak master realm name |
| `TASK_KEYCLOAK_MASTER_CLIENT` | Keycloak admin client id |
| `VAULT_ROLE_ID` / `VAULT_SECRET_ID` | only needed if Vault-based config (`spring.cloud.vault.enabled`) is turned on; disabled by default |

No real values are stored in this repo — they are supplied via environment/config server per environment.

## 🔗 Related Services

Part of the TicketinApp microservices system: [github.com/berat-erkul/TicketinApp](https://github.com/berat-erkul/TicketinApp)

| Service | Description |
| --- | --- |
| [config-service-LAB](https://github.com/berat-erkul/config-service-LAB) | centralized configuration server |
| [discovery-service-LAB](https://github.com/berat-erkul/discovery-service-LAB) | Eureka service discovery |
| [gateway-service-LAB](https://github.com/berat-erkul/gateway-service-LAB) | API gateway |
| [user-service-LAB](https://github.com/berat-erkul/user-service-LAB) | user management |
| **task-service-LAB** | this service |
| [project-service-LAB](https://github.com/berat-erkul/project-service-LAB) | project management |
| [web-ui-service-LAB](https://github.com/berat-erkul/web-ui-service-LAB) | web front end |
| [mobile-ticketing-app-LAB](https://github.com/berat-erkul/mobile-ticketing-app-LAB) | mobile client |

<div align="center">

<a href="https://github.com/berat-erkul/TicketinApp"><img src="https://img.shields.io/badge/%E2%86%90_Back_to-TicketinApp-2C5364?style=for-the-badge" /></a>

</div>
