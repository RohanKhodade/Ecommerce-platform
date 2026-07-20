# ShopStack — E-Commerce Microservices Platform

A Java Spring Boot microservices-based e-commerce backend, built to model a real-world checkout system: service discovery, an API gateway, JWT-secured inter-service calls, and Saga-style compensating transactions for stock rollback on payment failure.

Each service is independently deployable, containerized, and shipped to [Render](https://render.com) via its own Docker image and GitHub Actions pipeline.

---

## Architecture

```
                        ┌─────────────────┐
                        │   Eureka Server   │  (service registry)
                        └────────▲──────────┘
                                 │ registers
        ┌────────────────────────┼────────────────────────┐
        │                        │                        │
 ┌──────▼──────┐        ┌────────▼───────┐        ┌────────▼───────┐        ┌────────────────┐
 │  API Gateway │──────▶│  User Service   │        │ Order Service   │───────▶│ Inventory Service│
 │ (Spring Cloud│        │ (auth, users,   │◀───────│ (cart, orders,  │        │ (products, stock │
 │   Gateway)   │        │  addresses)     │  Feign  │  checkout Saga) │ Feign  │  reservations)   │
 └──────────────┘        └────────────────┘        └────────┬────────┘        └────────────────┘
        ▲                                                    │ Feign
        │ browser / frontend                                ▼
        │                                          ┌───────────────────┐
        └──────────────────────────────────────────│  Payment Service   │
                                                     └───────────────────┘
```

- The **API Gateway** is the single entry point for the browser/frontend and routes requests to the right service by path.
- All services register with **Eureka** for service discovery and talk to each other over Feign clients using load-balanced service names (no hardcoded URLs).
- The **Order Service** owns checkout: it calls Inventory to reserve stock, Payment to charge, and rolls back the inventory reservation (a compensating transaction) if payment fails.

---

## Services

| Service | Responsibility | Default Port | Base Path (via Gateway) |
|---|---|---|---|
| `eurekaServer` | Service registry / discovery | `8080` | — |
| `apiGateway` | Single entry point, routing, CORS | `8079` | `/api/**` |
| `userService` | Auth, user profile, addresses | `8081` | `/api/auth/**`, `/api/user/**` |
| `orderService` | Cart, checkout, order history | `8082` | `/api/cart/**`, `/api/order/**` |
| `inventoryService` | Products, categories, stock | `8083` | `/api/inventory/**` |
| `paymentService` | Payment processing | `8084` | `/api/payment/**` |

### API Gateway routing
The gateway (Spring Cloud Gateway, WebFlux) uses discovery-based load-balanced routes (`lb://serviceName`) and applies global CORS rules so only the configured frontend origin (`CORS_URL`, default `http://localhost:5173`) can call it with credentials.

---

## Tech Stack

- **Java 21**, **Spring Boot 4.1**
- **Spring Cloud** — Netflix Eureka (discovery), Gateway (routing), OpenFeign (inter-service calls)
- **Spring Security** + **JJWT** — stateless JWT authentication, propagated across services via a Feign `RequestInterceptor`
- **Spring Data JPA / Hibernate** — one PostgreSQL database per service (`ecom_users`, `ecom_orders`, `ecom_inventory`)
- **Lombok**, Bean Validation (`spring-boot-starter-validation`)
- **Docker** — multi-stage builds (Maven build stage → lightweight `eclipse-temurin` JRE runtime stage)
- **GitHub Actions** — one workflow per service, path-filtered, that builds and pushes a Docker image to Docker Hub and then triggers a Render deploy hook
- **Render** — hosting for all six services

---

## Key Design Highlights

- **JWT propagation across services** — a shared `JwtUtility` / `JwtAuthFilter` / `SecurityConfig` pattern in each service, with a Feign `RequestInterceptor` in Order Service forwarding the caller's JWT to Inventory, Payment, and User services so downstream services can independently verify the caller.
- **Saga-style checkout** — `OrderService` orchestrates checkout: reserve stock in `InventoryService` → charge via `PaymentService` → on payment failure, issue a compensating call back to Inventory to release the reserved stock, keeping stock counts consistent without a distributed transaction.
- **Optimistic locking with retry** — stock updates in `InventoryService` use JPA `@Version`, with the retry loop split across two Spring beans to avoid the classic Spring AOP proxy self-invocation problem.
- **Per-service databases** — each service owns its own PostgreSQL schema (`ecom_users`, `ecom_orders`, `ecom_inventory`), keeping services independently deployable and data-isolated.
- **Path-filtered CI/CD** — each GitHub Actions workflow only triggers when files under its own service directory change, so a change to `orderService/` doesn't rebuild and redeploy every other service.

---

## API Overview

### Auth & Users (`userService`)
| Method | Path | Description |
|---|---|---|
| POST | `/api/auth/register` | Register a new user |
| POST | `/api/auth/login` | Login, returns JWT |
| PUT | `/api/auth/update/password/{userId}` | Change password |
| PUT | `/api/auth/update/email/{userId}` | Change email |
| DELETE | `/api/auth/delete/{userId}` | Delete account |
| GET | `/api/user/me` | Get logged-in user |
| PUT | `/api/user/update/{userId}` | Update user details |
| POST/GET/PUT/DELETE | `/api/user/address/...` | Manage saved addresses |

### Inventory (`inventoryService`)
| Method | Path | Description |
|---|---|---|
| GET | `/api/inventory/all` | List all products |
| GET | `/api/inventory/{productId}` | Get a product |
| GET | `/api/inventory/seller/{sellerId}` | Products by seller |
| POST | `/api/inventory/add/{sellerId}` | Add a product |
| PUT | `/api/inventory/update/{productId}/{sellerId}` | Update a product |
| DELETE | `/api/inventory/delete/{productId}/{sellerId}` | Delete a product |
| POST | `/api/inventory/product/place/{productId}` | Reserve/decrement stock (internal, called by Order Service) |
| POST | `/api/inventory/product/cancel/{productId}` | Release stock (compensating action) |

### Cart & Orders (`orderService`)
| Method | Path | Description |
|---|---|---|
| GET | `/api/cart` | View cart |
| GET | `/api/cart/view/{productId}` | View one cart item |
| POST | `/api/cart/add` | Add item to cart |
| PUT | `/api/cart/update` | Update cart item quantity |
| DELETE | `/api/cart/remove/{productId}` | Remove item from cart |
| DELETE | `/api/cart/clearCart` | Empty the cart |
| POST | `/api/order/place` | Checkout (runs the Saga: reserve stock → pay → confirm/rollback) |
| POST | `/api/order/cancel/{orderId}` | Cancel an order |
| GET | `/api/order/view/{orderId}` | View an order |
| GET | `/api/order/all` | List all orders for the logged-in user |

### Payments (`paymentService`)
| Method | Path | Description |
|---|---|---|
| POST | `/api/payment/make` | Process a payment (called internally by Order Service) |

---

## Running Locally

Each service needs its own PostgreSQL database (skip this for `eurekaServer`, `apiGateway`, and `paymentService`, which don't require a DB).

1. **Start Eureka Server** first (default `localhost:8080`) — every other service registers with it.
2. **Start the databases** and create `ecom_users`, `ecom_orders`, `ecom_inventory` in PostgreSQL.
3. **Set environment variables** per service (see table below) or rely on the `localhost` defaults baked into each `application.properties`.
4. **Start `userService`, `inventoryService`, `orderService`, `paymentService`** in any order — they'll register with Eureka once up.
5. **Start `apiGateway`** last so it can discover the registered services.

### Common environment variables

| Variable | Used by | Purpose |
|---|---|---|
| `SERVER_PORT` | eurekaServer, apiGateway, orderService | Override default port |
| `EUREKA_SERVER_URL` | all services | Eureka registration URL |
| `POSTGRES_DATABASE_URL` | userService, orderService, inventoryService | JDBC URL |
| `POSTGRES_DATABASE_USERNAME` | userService, orderService, inventoryService | DB username |
| `POSTGRES_DATABASE_PASSWORD` | userService, orderService, inventoryService | DB password |
| `CORS_URL` | apiGateway | Allowed frontend origin for CORS |

### Building a single service

```bash
cd userService
./mvnw clean package
java -jar target/*.jar
```

### Running with Docker

Each service has its own multi-stage `Dockerfile` (Maven build → `eclipse-temurin` JRE runtime):

```bash
cd userService
docker build -t ecom-user-service .
docker run -p 8081:8080 --env-file .env ecom-user-service
```

---

## Deployment

Each service is deployed independently to **Render**. The pipeline for each service (`.github/workflows/<service>.yml`) is triggered only on changes to that service's directory:

1. Checkout code
2. Build and push a Docker image to Docker Hub
3. Trigger the corresponding Render deploy hook via `curl`

This means pushing a change to `orderService/` only rebuilds and redeploys the Order Service — the other five services are untouched.

---

## Project Structure

```
Ecommerce-platform/
├── eurekaServer/       # Service registry
├── apiGateway/         # Spring Cloud Gateway - single entry point
├── userService/        # Auth, users, addresses
├── inventoryService/   # Products, categories, stock
├── orderService/       # Cart, orders, checkout Saga
├── paymentService/     # Payment processing
├── BRD.pdf             # Business Requirements Document
└── .github/workflows/  # One CI/CD pipeline per service
```

---

## Notes

- This is a learning/portfolio project focused on getting core microservices patterns right — service discovery, gateway routing, JWT propagation, and Saga-style rollback — rather than a production-hardened payment system.
- `paymentService` currently simulates payment processing; no real payment gateway is integrated.
