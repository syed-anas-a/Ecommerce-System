# 🛍️ TeraMart : E-Commerce Management System

A backend system covering the core workflows of an e-commerce platform - authentication, product catalog, cart management, order processing, and payment verification.

The design prioritises system correctness over feature breadth: trust boundaries are explicit, state transitions are deliberate, and each concern is handled independently.

![Java](https://img.shields.io/badge/Java_21-ED8B00?style=flat&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=flat&logo=springboot&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=flat&logo=mysql&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=flat&logo=apachemaven&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=flat&logo=docker&logoColor=white)

---

## 📋 Overview

| Area | Approach |
|---|---|
| 🔐 Authentication | Stateful JWT via HttpOnly cookies with database-backed invalidation |
| 🛡️ Authorisation | Role-based — Customer and Admin separated at the filter level |
| 🏗️ Architecture | Strict layered — Controller → Service → Repository → DB |
| 💳 Payments | Razorpay integration with HMAC-SHA256 backend signature verification |
| ⚙️ Configuration | Environment-driven — no hardcoded credentials |

---

## 🏗️ System Design

### 🔐 Authentication

JWT tokens are stored in the database rather than kept purely stateless. This is a deliberate tradeoff, it adds one DB lookup per request, but makes logout a real operation: the token is deleted and subsequent requests are rejected. A stateless token cannot be invalidated before expiry; this one can.

Tokens are issued via `HttpOnly` cookies, keeping them inaccessible to JavaScript. A custom authentication filter validates every request to `/api/*` and `/admin/*` before it reaches the controller layer.

Passwords are hashed with BCrypt. Role separation : `CUSTOMER` and `ADMIN` is enforced at the routing level, not inside business logic.

---

### 🛒 Order & Payment Flow

Cart totals are calculated server-side. The client sends items; the server determines the price.

When an order is created, product prices at that moment are copied into the order record rather than referenced by foreign key. This means a catalog price change does not alter historical orders an intentional consistency decision.

Payment verification is implemented via Razorpay's HMAC-SHA256 signature mechanism. The backend creates the Razorpay order server-side and exposes a verification endpoint that expects the signature payload generated after user payment completion. The backend recomputes the expected HMAC value and compares, a mismatch keeps the order in a pending state.

End-to-end verification requires a frontend-triggered payment flow to generate the signature. The backend logic is complete; the trust boundary holds once that integration is in place. Payment success is never assumed from client input alone.

---

### 🧱 Architecture

```text
Controller → Service → Repository → MySQL
```

| Layer | Responsibility |
|---|---|
| Controller | Request handling, response shaping, input validation |
| Service | Business logic, workflow orchestration |
| Repository | Data access via JPA |
| Filter | Token validation, user context resolution |
| DTO | Request/response models isolated from entity layer |

Authentication, cart, orders, and payments are independent service boundaries, each manages its own state and failure modes.

---

## 📡 API Reference

### 🔐 Auth

| Method | Endpoint |
|---|---|
| `POST` | `/api/auth/register` |
| `POST` | `/api/auth/login` |
| `POST` | `/api/auth/logout` |

### 👤 Customer

| Method | Endpoint |
|---|---|
| `GET` | `/api/products` |
| `POST` | `/api/cart/add` |
| `GET` | `/api/cart/items` |
| `PUT` | `/api/cart/update` |
| `DELETE` | `/api/cart/delete` |
| `POST` | `/api/orders/create` |
| `GET` | `/api/orders` |
| `POST` | `/api/payments/verify` |

### 🛠️ Admin

| Method | Endpoint |
|---|---|
| `POST` | `/admin/products/add` |
| `DELETE` | `/admin/products/delete` |

---

## 📂 Project Structure

```text
src/main/java/com/syed/ecommerce
├── config          → Security and application configuration
├── controller      → API endpoints
├── dto             → Request/response models
├── entity          → JPA entities
├── filter          → Authentication layer
├── repository      → Database access
└── service         → Business logic
```

---

## 🛠️ Tech Stack

| Category | Technology |
|---|---|
| Language | ![Java](https://img.shields.io/badge/Java_21-ED8B00?style=flat&logo=openjdk&logoColor=white) |
| Framework | ![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=flat&logo=springboot&logoColor=white) ![Spring MVC](https://img.shields.io/badge/Spring_MVC-6DB33F?style=flat&logo=spring&logoColor=white) |
| Persistence | ![JPA](https://img.shields.io/badge/Spring_Data_JPA-6DB33F?style=flat&logo=spring&logoColor=white) ![Hibernate](https://img.shields.io/badge/Hibernate-59666C?style=flat&logo=hibernate&logoColor=white) |
| Database | ![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=flat&logo=mysql&logoColor=white) |
| Auth | ![JWT](https://img.shields.io/badge/JWT-000000?style=flat&logo=jsonwebtokens&logoColor=white) ![BCrypt](https://img.shields.io/badge/BCrypt-003A70?style=flat&logoColor=white) |
| Payments | ![Razorpay](https://img.shields.io/badge/Razorpay-02042B?style=flat&logo=razorpay&logoColor=3395FF) |
| Build | ![Maven](https://img.shields.io/badge/Maven-C71A36?style=flat&logo=apachemaven&logoColor=white) |

---

## 🚀 Running Locally

### Prerequisites

- ☕ Java 21
- 📦 Maven
- 🗄️ MySQL
- 🔑 Razorpay test keys

### Setup

**1. Clone the repository**

```bash
git clone https://github.com/syed-anas-a/Ecommerce-System.git
cd Ecommerce-System
```

**2. Create a `.env` file in the project root**

```env
DB_URL=jdbc:mysql://localhost:3306/your_database_name
DB_USERNAME=your_mysql_username
DB_PASSWORD=your_mysql_password

JWT_SECRET=your_jwt_secret
JWT_EXPIRATION=86400000

RAZORPAY_KEY_ID=your_razorpay_key_id
RAZORPAY_KEY_SECRET=your_razorpay_key_secret
```

**3. Create the database**

```sql
CREATE DATABASE your_database_name;
```

**4. Run the application**

```bash
mvn spring-boot:run
```

Backend runs at `http://localhost:8080`

---

## ✅ Implementation Highlights

- JWT authentication with database-backed invalidation and logout support
- Role-based access control - Customer and Admin flows fully separated
- Cart management with server-side total calculation
- Order creation with price freezing at purchase time
- Razorpay order creation and backend verification endpoint implemented
- All endpoints tested via Postman

---

## 🔭 Future Development

- React.js frontend with full checkout flow and Razorpay payment integration
- Docker + Docker Compose support for single-command local setup
- Advanced product search and filtering capabilities

---
