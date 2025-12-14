# Product Service

Product Service is a Spring Boot–based backend service responsible for managing products in an e-commerce system. It handles product creation, updates, deletion, variants, images, and optimized home-page product cards.

---

## Tech Stack
- Java 17
- Spring Boot
- Spring Data JPA (Hibernate)
- PostgreSQL
- REST APIs
- Maven
- Git & GitHub

---

## Features
- Product CRUD operations
- Product variants with dimensions and packages
- Product image upload (PRIMARY / SECONDARY)
- Home page optimized product cards
- Global exception handling
- Validation using Jakarta Validation
- Transaction management
- Clean layered architecture (Controller → Service → Repository)

---

## Project Structure
src/main/java/com/furniture/product
├── controller
├── service
│ └── impl
├── repository
├── entity
├── dto
├── mapper
├── exception
├── enums
└── config


---

## API Endpoints

### Create Product
POST /api/v1/products


---

### Upload Product Images
POST /api/v1/products/{productId}/images

---

### Get All Products
GET /api/v1/products

---

### Get Product By ID
GET /api/v1/products/{productId}


---

### Update Product
PUT /api/v1/products/{productId}


---

### Home Page Products
GET /api/v1/products/home


---

### Delete Product

DELETE /api/v1/products/{productId}

---

## Exception Handling
Centralized exception handling using `@RestControllerAdvice` with consistent error responses for:
- Product not found
- Validation errors
- Server errors