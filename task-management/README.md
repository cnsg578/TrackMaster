# Task Management Backend

A secure and scalable RESTful backend application for managing tasks, teams, collaboration, comments, and file attachments.

Built using **Java, Spring Boot, Spring Security, JWT, Spring Data JPA, and MySQL**.

## 🚀 Features

* 🔐 User registration and login
* 🔑 JWT-based authentication and authorization
* 🔒 BCrypt password hashing
* 👤 User profile management
* 👥 Team creation and team member management
* 📋 Task CRUD operations
* 👨‍💻 Task assignment to users
* 🔄 Task status management
* ⭐ Task priority management
* 🔎 Task filtering and searching
* ↕️ Task sorting
* 💬 Task comments
* 📎 File attachments
* ✅ Request validation
* ⚠️ Global exception handling
* 📖 Swagger/OpenAPI API documentation

## 🛠️ Tech Stack

| Technology        | Purpose                       |
| ----------------- | ----------------------------- |
| Java 21           | Programming language          |
| Spring Boot 4.0.8 | Backend framework             |
| Spring Security   | Authentication & security     |
| JWT               | Stateless authentication      |
| Spring Data JPA   | Database access               |
| Hibernate         | ORM                           |
| MySQL 8           | Relational database           |
| Maven             | Build & dependency management |
| Lombok            | Boilerplate reduction         |
| Swagger / OpenAPI | API documentation             |
| Postman           | API testing                   |

## 🏗️ Architecture

The project follows a layered architecture:

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
Entity
    ↓
MySQL Database
```

### Main layers

* **Controller** – Handles HTTP requests and responses.
* **Service** – Contains business logic.
* **Repository** – Handles database operations using Spring Data JPA.
* **Entity** – Represents database tables.
* **DTO** – Controls request and response data.
* **Security** – Handles JWT authentication and Spring Security configuration.
* **Exception** – Provides centralized exception handling.

## 📂 Project Structure

```text
src/main/java/com/taskmanagement/taskmanagement
│
├── controller
│   ├── AuthController.java
│   ├── UserController.java
│   ├── TeamController.java
│   ├── TaskController.java
│   ├── CommentController.java
│   └── AttachmentController.java
│
├── dto
│   ├── RegisterRequest.java
│   ├── LoginRequest.java
│   ├── LoginResponse.java
│   ├── UserResponse.java
│   ├── TeamRequest.java
│   ├── TeamResponse.java
│   ├── TeamMemberRequest.java
│   ├── TaskRequest.java
│   ├── TaskResponse.java
│   ├── CommentRequest.java
│   ├── CommentResponse.java
│   └── AttachmentResponse.java
│
├── entity
│   ├── User.java
│   ├── Team.java
│   ├── TeamMember.java
│   ├── Task.java
│   ├── TaskStatus.java
│   ├── TaskPriority.java
│   ├── Comment.java
│   └── Attachment.java
│
├── repository
│   ├── UserRepository.java
│   ├── TeamRepository.java
│   ├── TeamMemberRepository.java
│   ├── TaskRepository.java
│   ├── CommentRepository.java
│   └── AttachmentRepository.java
│
├── service
│   ├── UserService.java
│   ├── TeamService.java
│   ├── TaskService.java
│   ├── CommentService.java
│   └── AttachmentService.java
│
├── security
│   ├── SecurityConfig.java
│   ├── JwtService.java
│   └── JwtAuthenticationFilter.java
│
└── exception
    └── GlobalExceptionHandler.java
```

## 🗄️ Database Design

The application uses MySQL.

Main tables:

```text
users
  │
  ├── teams
  │
  ├── team_members
  │
  ├── tasks
  │
  ├── comments
  │
  └── attachments

teams
  │
  ├── team_members
  └── tasks

tasks
  ├── comments
  └── attachments
```

### Core entities

* **User** – Application users
* **Team** – Collaborative teams
* **TeamMember** – Users belonging to teams
* **Task** – Tasks created within teams
* **Comment** – Comments associated with tasks
* **Attachment** – Files associated with tasks

## 🔐 Authentication

The application uses **JWT-based stateless authentication**.

### Registration

```http
POST /api/auth/register
```

Example request:

```json
{
  "name": "Rahul",
  "email": "rahul@example.com",
  "password": "Test@123"
}
```

Passwords are securely hashed using **BCrypt** before being stored in the database.

### Login

```http
POST /api/auth/login
```

Example:

```json
{
  "email": "rahul@example.com",
  "password": "Test@123"
}
```

The API returns a JWT token.

Protected APIs require:

```http
Authorization: Bearer <JWT_TOKEN>
```

## 📡 REST API Endpoints

### Authentication

| Method | Endpoint             | Description   |
| ------ | -------------------- | ------------- |
| POST   | `/api/auth/register` | Register user |
| POST   | `/api/auth/login`    | Login user    |

### Users

| Method | Endpoint             | Description                |
| ------ | -------------------- | -------------------------- |
| GET    | `/api/users/profile` | Get logged-in user profile |

### Teams

| Method | Endpoint                               | Description      |
| ------ | -------------------------------------- | ---------------- |
| POST   | `/api/teams`                           | Create team      |
| GET    | `/api/teams`                           | Get all teams    |
| GET    | `/api/teams/{teamId}`                  | Get team         |
| PUT    | `/api/teams/{teamId}`                  | Update team      |
| DELETE | `/api/teams/{teamId}`                  | Delete team      |
| POST   | `/api/teams/{teamId}/members`          | Add member       |
| GET    | `/api/teams/{teamId}/members`          | Get team members |
| DELETE | `/api/teams/{teamId}/members/{userId}` | Remove member    |

### Tasks

| Method | Endpoint                              | Description                  |
| ------ | ------------------------------------- | ---------------------------- |
| POST   | `/api/tasks`                          | Create task                  |
| GET    | `/api/tasks`                          | Get/filter/search/sort tasks |
| GET    | `/api/tasks/{taskId}`                 | Get task                     |
| PUT    | `/api/tasks/{taskId}`                 | Update task                  |
| DELETE | `/api/tasks/{taskId}`                 | Delete task                  |
| PUT    | `/api/tasks/{taskId}/assign/{userId}` | Assign task                  |
| PATCH  | `/api/tasks/{taskId}/status`          | Update status                |
| PATCH  | `/api/tasks/{taskId}/priority`        | Update priority              |

### Task filtering

Examples:

```http
GET /api/tasks?status=IN_PROGRESS
```

```http
GET /api/tasks?priority=HIGH
```

```http
GET /api/tasks?status=IN_PROGRESS&priority=HIGH
```

```http
GET /api/tasks?search=Authentication
```

Sorting:

```http
GET /api/tasks?sortBy=dueDate&direction=desc
```

Supported statuses:

```text
OPEN
IN_PROGRESS
COMPLETED
```

Supported priorities:

```text
LOW
MEDIUM
HIGH
```

### Comments

| Method | Endpoint                                   | Description       |
| ------ | ------------------------------------------ | ----------------- |
| POST   | `/api/tasks/{taskId}/comments`             | Add comment       |
| GET    | `/api/tasks/{taskId}/comments`             | Get task comments |
| PUT    | `/api/tasks/{taskId}/comments/{commentId}` | Update comment    |
| DELETE | `/api/tasks/{taskId}/comments/{commentId}` | Delete comment    |

### Attachments

| Method | Endpoint                                         | Description       |
| ------ | ------------------------------------------------ | ----------------- |
| POST   | `/api/tasks/{taskId}/attachments`                | Upload attachment |
| GET    | `/api/tasks/{taskId}/attachments`                | Get attachments   |
| DELETE | `/api/tasks/{taskId}/attachments/{attachmentId}` | Delete attachment |

Uploaded files are stored locally in the `uploads/` directory, while attachment metadata is stored in MySQL.

## ⚙️ Setup Instructions



### 2. Create the MySQL database

Open MySQL and run:

```sql
CREATE DATABASE task_management;
```

### 3. Configure environment variables

The application uses environment variables for sensitive configuration.

Set:

```text
DB_PASSWORD=<your-mysql-password>
JWT_SECRET=<your-long-jwt-secret>
```

Example in PowerShell:

```powershell
$env:DB_PASSWORD="your_mysql_password"
$env:JWT_SECRET="your_long_random_jwt_secret"
```

Do not commit passwords or secrets to GitHub.

### 4. Build the application

```powershell
.\mvnw.cmd clean install
```

### 5. Run the application

```powershell
.\mvnw.cmd spring-boot:run
```

The application runs on:

```text
http://localhost:8080
```

## 📖 Swagger API Documentation

After starting the application, open:

```text
http://localhost:8080/swagger-ui/index.html
```

Swagger provides an interactive interface for exploring and testing the REST APIs.

## 🧪 API Testing

The APIs were tested using **Postman**.

The testing flow includes:

1. User registration
2. User login
3. JWT authentication
4. Profile retrieval
5. Team creation
6. Team member management
7. Task creation
8. Task assignment
9. Task status and priority updates
10. Task filtering/searching/sorting
11. Comments
12. Attachments

## 🔒 Security

Security features include:

* BCrypt password hashing
* JWT authentication
* Stateless sessions
* Protected REST endpoints
* Input validation
* Centralized exception handling
* Sensitive credentials stored through environment variables

## 📌 Future Enhancements

Possible future improvements:

* WebSocket/SSE real-time notifications
* AI-powered task description generation
* Role-based team authorization
* Pagination for tasks and teams
* Cloud file storage such as AWS S3
* Refresh token mechanism
* Docker deployment
* Automated unit and integration tests

## Project Status

Completed RESTful backend implementation.


---

⭐ If you find this project useful, consider giving it a star.
