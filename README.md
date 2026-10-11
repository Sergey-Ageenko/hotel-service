# Hotel REST API

RESTful API application for managing hotel information, built with Java 21, Spring Boot, Spring Data JPA, and Liquibase. The application uses an H2 database and provides endpoints for retrieving, searching, creating, and managing hotels.

## Technologies

* **Java 21**
* **Maven**
* **Spring Boot**
* **Spring Data JPA / Hibernate**
* **Liquibase** — database schema migrations
* **H2 Database**
* **Bean Validation**
* **MapStruct** — DTO mapping
* **Swagger / OpenAPI** — API documentation
* **JUnit 5, Mockito, MockMvc** — testing

## Requirements

Make sure the following tools are installed:

* JDK 21
* Maven 3.9+ (or use the Maven Wrapper, if available)

Verify your Java version:

```bash
java -version
```

## Getting Started

### Run the application

Start the application using Maven:

```bash
mvn spring-boot:run
```

The application starts on port **8092**.

Base URL:

```text
http://localhost:8092/property-view
```

Liquibase applies the database migrations during application startup.

## API Endpoints

### 1. Get all hotels

**GET** `/property-view/hotels`

Returns a list of hotels with brief information.

Example response:

```json
[
  {
    "id": 1,
    "name": "DoubleTree by Hilton Minsk",
    "description": "The DoubleTree by Hilton Hotel Minsk offers luxurious rooms in Minsk.",
    "address": "9 Pobediteley Avenue, Minsk, 220004, Belarus",
    "phone": "+375 17 309-80-00"
  }
]
```

### 2. Get hotel details

**GET** `/property-view/hotels/{id}`

Returns detailed information about a hotel, including its address, contacts, arrival times, and amenities.

Example:

```text
GET /property-view/hotels/1
```

Example response:

```json
{
  "id": 1,
  "name": "DoubleTree by Hilton Minsk",
  "description": "The DoubleTree by Hilton Hotel Minsk offers luxurious rooms in Minsk.",
  "brand": "Hilton",
  "address": {
    "houseNumber": 9,
    "street": "Pobediteley Avenue",
    "city": "Minsk",
    "country": "Belarus",
    "postCode": "220004"
  },
  "contacts": {
    "phone": "+375 17 309-80-00",
    "email": "doubletreeminsk.info@hilton.com"
  },
  "arrivalTime": {
    "checkIn": "14:00",
    "checkOut": "12:00"
  },
  "amenities": [
    "Free parking",
    "Free WiFi",
    "Non-smoking rooms",
    "Fitness center"
  ]
}
```

### 3. Search hotels

**GET** `/property-view/search`

Searches hotels by one or more optional parameters:

| Parameter   | Description     |
| ----------- | --------------- |
| `name`      | Hotel name      |
| `brand`     | Hotel brand     |
| `city`      | City            |
| `country`   | Country         |
| `amenities` | Hotel amenities |

Example requests:

```text
GET /property-view/search?city=Minsk
GET /property-view/search?brand=Hilton
GET /property-view/search?country=Belarus
GET /property-view/search?name=DoubleTree&city=Minsk
GET /property-view/search?amenities=Free%20WiFi
```

Returns a list of hotels in the same format as `GET /property-view/hotels`.

### 4. Create a hotel

**POST** `/property-view/hotels`

Creates a new hotel.

Example request body:

```json
{
  "name": "DoubleTree by Hilton Minsk",
  "description": "A hotel in the center of Minsk.",
  "brand": "Hilton",
  "address": {
    "houseNumber": 9,
    "street": "Pobediteley Avenue",
    "city": "Minsk",
    "country": "Belarus",
    "postCode": "220004"
  },
  "contacts": {
    "phone": "+375 17 309-80-00",
    "email": "doubletreeminsk.info@hilton.com"
  },
  "arrivalTime": {
    "checkIn": "14:00",
    "checkOut": "12:00"
  }
}
```

The `description` and `arrivalTime` fields are optional according to the assignment.

The response contains the created hotel's brief information.

### 5. Add amenities to a hotel

**POST** `/property-view/hotels/{id}/amenities`

Adds amenities to the specified hotel.

Example:

```text
POST /property-view/hotels/1/amenities
Content-Type: application/json
```

Request body:

```json
[
  "Free parking",
  "Free WiFi",
  "Non-smoking rooms",
  "Concierge",
  "Fitness center",
  "Room service"
]
```

Existing amenities are not added again.

### 6. Get hotel histogram

**GET** `/property-view/histogram/{param}`

Returns the number of hotels grouped by a specified parameter.

Supported parameters:

* `brand`
* `city`
* `country`
* `amenities`

Example:

```text
GET /property-view/histogram/city
```

Example response:

```json
{
  "Minsk": 1,
  "Moscow": 2,
  "Mogilev": 1
}
```

For amenities, the response contains the number of hotels associated with each amenity.

## API Documentation

If Swagger UI is enabled in the application, it is available at:

```text
http://localhost:8092/property-view/swagger-ui/index.html
```

OpenAPI specification:

```text
http://localhost:8092/property-view/v3/api-docs
```

## Database

The application uses an H2 in-memory database by default.

Liquibase manages database schema changes and initial data through changelog files.

Database settings can be configured in the application configuration files.

## Testing

Run the test suite with:

```bash
mvn test
```

The project includes tests for application logic and REST endpoints.

## Project Structure

The application follows a layered architecture:

* **Controller** — handles HTTP requests and responses.
* **Service** — contains business logic.
* **Repository** — performs database operations using Spring Data JPA.
* **Entity** — represents database entities.
* **DTO** — defines request and response models.
* **Mapper** — converts entities to DTOs and vice versa.
* **Exception** — handles application errors.
* **Liquibase changelogs** — manage database schema migrations.

## Configuration

The default application port is `8092`, and the API context path is `/property-view`.

To change the port or database configuration, update the relevant application configuration file.

## License

This project was created as a technical assignment.
