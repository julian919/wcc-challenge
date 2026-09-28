# UK Postcode Distance Service

A microservice-based REST API that calculates straight-line distance between UK postcodes, built with Spring Boot (Java 21) and MongoDB behind an API Gateway.

```
  client ──► api-gateway :8765
               │
    ┌──────────┼─────────────────────┐
    │ /api/auth│ /api/user           │ /api/postcode
    ▼          ▼                     ▼
auth-service user-service     postcodes-service
   :8100        :8300               :8200
    └────────────┴─────────┬─────────┘
                       MongoDB :27017
```

---

## Requirements

- **Docker** with Docker Compose. That's all you need to run it.
- **Java 21**, only to run the tests or run the services from an IDE. Maven comes with the project (`./mvnw`).

## Steps to Run the Project

1. **Navigate to the Root Project Folder**
   Open your terminal and navigate to the project directory:
   ```bash
   cd /path/to/project
   ```

2. **Start the Services Using Docker Compose**
   Run the following command to build and launch all services:
   ```bash
   docker compose up --build
   ```

3. **Wait for the Services to Load**
   Allow the services a minute to start up. On first run, MongoDB will automatically populate the database with ~3,000 UK postcodes and initial authentication roles from `mongo-init/`.

4. **Check the Services are Up**
   Wait until all five containers are running: green in Docker Desktop, or `Up` in:
   ```bash
   docker compose ps
   ```
   The services need a few more seconds after that to finish starting. To confirm each one is ready, ask it directly. The backend services' ports are only reachable inside Docker, so those requests go through the gateway's container:
   ```bash
   curl -s -w '\n' localhost:8765/actuator/health/readiness
   docker compose exec api-gateway curl -s -w '\n' http://auth-service:8100/actuator/health/readiness
   docker compose exec api-gateway curl -s -w '\n' http://postcodes-service:8200/actuator/health/readiness
   docker compose exec api-gateway curl -s -w '\n' http://user-service:8300/actuator/health/readiness
   ```
   Each prints `{"status":"UP"}` on its own line when ready. An empty line means that service is still starting: wait a few seconds and try again.

---

# API Documentation

## Base URL
`http://localhost:8765`

## Authorization

Endpoints requiring authorization use standard Bearer JWT tokens in the `Authorization` header:

```http
Authorization: Bearer <JWT_TOKEN>
```

The system uses two token levels:
- **Client Token**: Authenticates the client app to register new users and log them in. Acquired via `/api/auth/token/client` with default dev credentials (`clientId`: `wcc-web`, `clientSecret`: `Test1234@`).
- **User Token**: Authenticates an individual user to calculate distance, lookup postcodes, update postcodes, and inspect their own profile. Acquired via `/api/auth/login`.

> **Note:** The client secret `Test1234@` and the key pair in `auth-service/src/main/resources/keys` are for local development only. A real deployment would load them from a secret manager.

> **Note:** Postcodes are UK outward codes (e.g., `B34`, `SW1A`), and lookups are case-insensitive.

---

## Endpoints

### 1. **Get Client Token**
- **Method:** `POST`
- **Endpoint:** `/api/auth/token/client`
- **Description:** Obtain a client JWT token required for user registration and login.
- **Authorization:** None
- **Request Body:**
  ```json
  {
    "clientId": "wcc-web",
    "clientSecret": "Test1234@"
  }
  ```
- **Response (`200 OK`):**
  ```json
  {
    "accessToken": "eyJhbGciOiJSUzI1NiIsIn...",
    "tokenType": "Bearer",
    "expiresIn": 900
  }
  ```

---

### 2. **Register User**
- **Method:** `POST`
- **Endpoint:** `/api/user/register`
- **Description:** Register a new user account with profile details.
- **Authorization:** **Required** (Client Token)
- **Request Body:**
  ```json
  {
    "username": "julian-test",
    "password": "password-test",
    "firstName": "Julian",
    "lastName": "Lieow"
  }
  ```
- **Response (`201 Created`):**
  ```json
  {
    "id": "673b18...",
    "principalId": "673b18...",
    "firstName": "Julian",
    "lastName": "Lieow",
    "createdAt": "2026-09-28T06:01:48.630Z"
  }
  ```

---

### 3. **Login User**
- **Method:** `POST`
- **Endpoint:** `/api/auth/login`
- **Description:** Authenticate user credentials and receive a User JWT token.
- **Authorization:** **Required** (Client Token)
- **Request Body:**
  ```json
  {
    "username": "julian-test",
    "password": "password-test"
  }
  ```
- **Response (`200 OK`):**
  ```json
  {
    "accessToken": "eyJhbGciOiJSUzI1NiIsIn...",
    "tokenType": "Bearer",
    "expiresIn": 900
  }
  ```

---

### 4. **Get Distance Between Postcodes**
- **Method:** `GET`
- **Endpoint:** `/api/postcode/distance`
- **Description:** Calculate the straight-line distance in kilometers between two UK postcodes.
- **Authorization:** **Required** (User Token)
- **Query Parameters:**
  - `from` *(string, required)*: Outward postcode 1 (e.g. `B34`)
  - `to` *(string, required)*: Outward postcode 2 (e.g. `SW1A`)
- **Example Request:**
  ```http
  GET /api/postcode/distance?from=B34&to=SW1A
  ```
- **Response (`200 OK`):**
  ```json
  {
    "from": {
      "postcode": "B34",
      "latitude": 52.4964133,
      "longitude": -1.7817039
    },
    "to": {
      "postcode": "SW1A",
      "latitude": 51.5044592,
      "longitude": -0.1321624
    },
    "distance": 157.84653946737518,
    "unit": "km"
  }
  ```

---

### 5. **Get Postcode Details**
- **Method:** `GET`
- **Endpoint:** `/api/postcode/{postcode}`
- **Description:** Retrieve coordinates for a specific UK outward postcode.
- **Authorization:** **Required** (User Token)
- **Example Request:**
  ```http
  GET /api/postcode/B34
  ```
- **Response (`200 OK`):**
  ```json
  {
    "postcode": "B34",
    "latitude": 52.4964133,
    "longitude": -1.7817039
  }
  ```

---

### 6. **Update Postcode Coordinates**
- **Method:** `PUT`
- **Endpoint:** `/api/postcode/{postcode}`
- **Description:** Update latitude and longitude coordinates for a given postcode.
- **Authorization:** **Required** (User Token)
- **Request Body:**
  ```json
  {
    "latitude": 52.4964133,
    "longitude": -1.7817039
  }
  ```
- **Response (`200 OK`):**
  ```json
  {
    "postcode": "B34",
    "latitude": 52.4964133,
    "longitude": -1.7817039
  }
  ```

---

### 7. **Get Own Profile**
- **Method:** `GET`
- **Endpoint:** `/api/user/me`
- **Description:** Retrieve details of the currently logged-in user.
- **Authorization:** **Required** (User Token)
- **Response (`200 OK`):**
  ```json
  {
    "id": "673b18...",
    "principalId": "673b18...",
    "firstName": "Julian",
    "lastName": "Lieow",
    "createdAt": "2026-09-28T06:01:48.630Z"
  }
  ```

---

## Development & Testing

### Building and Running Tests
Build every module and run the tests:
```bash
./mvnw package
```
Or run the tests only:
```bash
./mvnw test
```

### Running Locally with IDE
1. Start MongoDB:
   ```bash
   docker compose up -d mongo
   ```
2. Import the root `pom.xml` as a Maven project into your IDE.
3. Run each application class:
   - `AuthServiceApplication` (:8100)
   - `PostcodesServiceApplication` (:8200)
   - `UserServiceApplication` (:8300)
   - `ApiGatewayApplication` (:8765)

### Resetting Database
To wipe MongoDB data and trigger clean re-initialization scripts:
```bash
docker compose down -v
docker compose up --build
```
