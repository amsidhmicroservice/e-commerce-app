# E-Commerce Microservices Application

Spring Boot 3.5.6 | Spring Cloud 2025.0.0 | Java 21

## Architecture

```
Client → Gateway (8080) → [Auth/Customer/Product/Order/Payment/Notification Services]
         ↓
         Auth Service (8099) - JWT Validation
         
Config Server (8888) ← All services load configuration
Eureka (8761) ← All services register here
```

## Services & Ports

| Service | Port | Description |
|---------|------|-------------|
| Config Server | 8888 | Centralized configuration |
| Eureka Discovery | 8761 | Service registry |
| Gateway | 8080 | API Gateway + JWT validation |
| Auth Service | 8099 | JWT authentication |
| Customer Service | 8090 | Customer management |
| Product Service | 8050 | Product catalog |
| Order Service | 8070 | Order processing |
| Payment Service | 8060 | Payment processing |
| Notification Service | 8085 | Email notifications |

## Quick Start

### 1. Start All Services (Proper Order)
```cmd
start-services-ordered.bat
```

### 2. Test Authentication
```bash
# Register
curl -X POST http://localhost:8080/api/v1/auth-service/auth/register \
  -H "Content-Type: application/json" \
  -d '{"name":"Test User","email":"test@test.com","password":"password123"}'

# Login (get JWT token)
curl -X POST http://localhost:8080/api/v1/auth-service/auth/token \
  -H "Content-Type: application/json" \
  -d '{"email":"test@test.com","password":"password123"}'

# Use token in subsequent requests
curl -X GET http://localhost:8080/api/v1/customer-service/customers \
  -H "Authorization: Bearer <YOUR_TOKEN>"
```

## Build & Deploy

### Build All
```cmd
mvn clean install
```

### Build Individual Service
```cmd
cd services\<service-name>
mvn clean package
```

## Key Features

- ✅ JWT Authentication (HS256, 24-hour tokens)
- ✅ API Gateway with centralized routing
- ✅ Centralized configuration (Config Server)
- ✅ Service discovery (Eureka)
- ✅ Distributed tracing (Zipkin on 9411)
- ✅ PostgreSQL (Auth/Customer/Product/Order/Payment)
- ✅ MongoDB (Notification)
- ✅ Kafka (Event streaming)

## Database Configuration

**PostgreSQL:** `jdbc:postgresql://localhost:5432/userinfodb`
- Username: `alibou`
- Password: `alibou`

**MongoDB:** `mongodb://localhost:27017/notification-db`

## Troubleshooting

### Service won't start
- Check if required services are running (Config Server, Eureka)
- Verify database connections
- Check port availability

### 404 on endpoints
- Ensure service registered in Eureka: http://localhost:8761
- Check Gateway routes: `curl http://localhost:8080/actuator/gateway/routes`
- Restart Config Server and Gateway if routes changed

### JWT token invalid
- Verify token not expired (24-hour validity)
- Check `jwt.secret` matches in Auth Service
- Ensure Authorization header: `Bearer <token>`

## Project Structure

```
e-commerce-app/
├── pom.xml (parent aggregator)
├── services/
│   ├── config-server/
│   ├── discovery-service/
│   ├── gateway-service/
│   ├── auth-service/
│   ├── customer-service/
│   ├── product-service/
│   ├── order-service/
│   ├── payment-service/
│   └── notification-service/
└── start-services-ordered.bat
```

## API Endpoints (via Gateway)

All requests go through Gateway: `http://localhost:8080/api/v1/<service>/<endpoint>`

### Auth Service
- `POST /api/v1/auth-service/auth/register` - Register user
- `POST /api/v1/auth-service/auth/token` - Get JWT token
- `POST /api/v1/auth-service/auth/validate` - Validate token (internal)

### Customer Service
- `GET /api/v1/customer-service/customers` - List customers
- `POST /api/v1/customer-service/customers` - Create customer

### Product Service
- `GET /api/v1/product-service/products` - List products
- `POST /api/v1/product-service/products` - Create product

### Order Service
- `GET /api/v1/order-service/orders` - List orders
- `POST /api/v1/order-service/orders` - Create order

### Payment Service
- `POST /api/v1/payment-service/payments` - Process payment

### Notification Service
- Event-driven (listens to Kafka)

---

**Author:** Amsidh Mohammed  
**Date:** October 31, 2025
