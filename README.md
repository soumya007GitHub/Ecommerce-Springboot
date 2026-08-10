# Ecommerce SpringBoot

A Spring Boot backend for an ecommerce platform — catalog browsing, cart, checkout, orders and reviews, with JWT-based authentication and role-based authorization.

## Tech Stack

- Java 17, Spring Boot 4.1.0
- Spring Web MVC, Spring Data JPA (Hibernate), MySQL
- Spring Security 7 + JJWT (JWT auth)
- Bean Validation (Jakarta Validation)
- springdoc-openapi (Swagger UI)
- JUnit 5, Mockito, AssertJ, H2 (tests)
- Lombok

## Getting Started

### Prerequisites

- JDK 17+
- MySQL 8+ running locally (or reachable via `DB_URL`)
- Maven (or use the bundled `mvnw` / `mvnw.cmd`)

### Configuration

The app reads these from environment variables, falling back to local defaults in `application.properties`:

| Variable | Default | Purpose |
|---|---|---|
| `DB_URL` | `jdbc:mysql://localhost:3306/ecommerce?createDatabaseIfNotExist=true` | MySQL connection URL |
| `DB_USERNAME` | `root` | MySQL username |
| `DB_PASSWORD` | `root` | MySQL password |
| `JWT_SECRET` | (dev default, change in prod) | HMAC signing key for JWTs |
| `JWT_EXPIRATION_MS` | `86400000` (24h) | JWT token lifetime |
| `SERVER_PORT` | `8080` | HTTP port |
| `admin.default-email` / `admin.default-password` | `admin@ecommerce.com` / `Admin@12345` | Seeded admin account created on first boot |

### Run

```bash
./mvnw spring-boot:run
```

Schema is auto-created/updated by Hibernate (`spring.jpa.hibernate.ddl-auto=update`). On first startup, a default `ADMIN` user is seeded (see table above) so you can immediately create categories/products without manually promoting an account.

### API Docs

Once running, Swagger UI is available at:

```
http://localhost:8080/swagger-ui.html
```

Use the "Authorize" button with a bearer token obtained from `/api/auth/login` to call protected endpoints from the UI.

### Tests

```bash
./mvnw test
```

Tests run against an in-memory H2 database (`src/test/resources/application.properties`), so no MySQL instance is needed to run the suite.

## Authentication & Roles

- `POST /api/auth/register` — creates a `CUSTOMER` account, returns a JWT
- `POST /api/auth/login` — returns a JWT for an existing account
- Send the token as `Authorization: Bearer <token>` on subsequent requests
- Two roles: `CUSTOMER` (default on self-registration) and `ADMIN` (catalog and order management). There's no self-service admin signup — promote a user directly in the database, or use the seeded admin account.

## API Overview

| Resource | Endpoints | Auth |
|---|---|---|
| Auth | `POST /api/auth/register`, `POST /api/auth/login` | Public |
| Products | `GET /api/products`, `GET /api/products/{id}` | Public |
| Products | `POST/PUT/DELETE /api/products/**` | Admin |
| Categories | `GET /api/categories/**` | Public |
| Categories | `POST/PUT/DELETE /api/categories/**` | Admin |
| Category Types | `GET /api/category-types/**` | Public |
| Category Types | `POST/PUT/DELETE /api/category-types/**` | Admin |
| Addresses | `GET/POST/PUT/DELETE /api/addresses/**` | Authenticated (scoped to caller) |
| Cart | `GET/POST/PUT/DELETE /api/cart/**` | Authenticated (scoped to caller) |
| Orders | `POST /api/orders`, `GET /api/orders`, `GET /api/orders/{id}`, `POST /api/orders/{id}/cancel` | Authenticated (scoped to caller) |
| Orders (admin) | `GET /api/orders/admin`, `PATCH /api/orders/admin/{id}/status` | Admin |
| Reviews | `GET /api/reviews/product/{productId}` | Public |
| Reviews | `POST /api/reviews`, `DELETE /api/reviews/{id}` | Authenticated |

`GET /api/products` supports query params: `categoryId`, `categoryTypeId`, `brand`, `minPrice`, `maxPrice`, `keyword`, `page`, `size`, `sortBy`, `direction`.

## Design Notes

- **Layering**: `controller` → `service` → `repository`, with a dedicated `mapper` package converting between entities and DTOs (kept out of the service interfaces so the API contract stays about business operations, not object translation).
- **Filtering**: product search uses a JPA `Specification` (`ProductSpecification`) instead of a pile of derived query methods, since filters are optional and combinable.
- **Order items are snapshotted**, not foreign-keyed to `ProductVariant` — `productName`, `color`, `size` and `unitPrice` are copied onto the `OrderItem` at checkout time so historical orders stay accurate even if a product is later renamed, repriced, or deleted.
- **Stock is decremented at checkout** and restored if an order is cancelled while still `PENDING`/`CONFIRMED`.
- **Reviews require a verified purchase**: `ReviewService` checks the user has an order containing that product before allowing a review, and a product's `rating` is recalculated as the live average whenever a review is added or removed.
- **Security**: stateless JWT auth — `JwtAuthenticationFilter` runs once per request, resolves the user from the token, and populates the `SecurityContext`; method-level `@PreAuthorize("hasRole('ADMIN')")` guards admin-only endpoints on top of the URL-level rules in `SecurityConfig`.
- **Errors**: a single `GlobalExceptionHandler` maps domain exceptions (`ResourceNotFoundException`, `DuplicateResourceException`, `InsufficientStockException`, `BadRequestException`) and validation failures to a consistent `ApiError` JSON shape.

## Known Limitations

- No real payment gateway integration — `paymentMethod`/`paymentStatus` are tracked but payment is not actually processed (order status is advanced manually via the admin endpoint).
- No image/file upload; `ProductResourceDTO.url` expects an already-hosted URL.
- No refresh-token flow — clients re-login once the JWT expires.
