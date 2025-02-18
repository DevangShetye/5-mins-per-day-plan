# Activity Progress Tracking API

## Overview
This is a Spring Boot REST API for managing **5-minute per day activity plans** as part of the **Prodigy Programs**. It provides endpoints to **fetch daily suggested activities** and **mark activities as completed** to track user progress. The API is designed for mobile and web applications.

## Features
- Fetch daily suggested activities for users.
- Mark activities as completed for progress tracking.
- Uses **Spring Boot**, **JPA**, and **Mockito** for testing.
- Includes **JUnit test cases** to ensure reliability.

## Technologies Used
- Java 1.8
- Spring Boot 2.6.4
- Spring Data JPA
- H2 Database (for testing)
- Mockito & JUnit
- Jackson (for JSON processing)

## API Endpoints
### 1. Fetch Activity Plan for a Day
```http
GET /api/program?day={day}
Headers: user_id (Integer)
```
#### Response:
```json
{
  "status": "success",
  "result": {
    "list": [
       {
                "id": 1,
                "category": "Athleticism",
                "activityTitle": "Advanced Mobility exercises",
                "frequency": "Maximize",
                "time": "Max.",
                "isCompleted": true
            },
            {
                "id": 3,
                "category": "Music",
                "activityTitle": "Visual Soflege",
                "frequency": "1x/Day",
                "time": "30 sec",
                "isCompleted": true
            }
    ]
  },
  "response_time": "2025-02-19 10:00:00"
}
```

### 2. Mark an Activity as Completed
```http
POST /api/confirm?activity_date={activity_date}&suggested_activity={activity_id}
Headers: user_id (Integer)
```
#### Response:
```json
{
  "status": "success",
  "result": { "result": "updated successfully" },
  "response_time": "2025-02-19 10:00:00"
}
```

## Setup & Run
### Prerequisites
- Java 1.8
- Maven

### Steps to Run
1. Clone the repository:
   ```sh
   git clone <repo-url>
   cd activity-progress-api
   ```
2. Build & Run:
   ```sh
   mvn spring-boot:run
   ```
3. Access the API at `http://localhost:8080/api`

## Running Tests
To execute unit tests:
```sh
mvn test
```

## License
This project is open-source and available for use under the MIT License.

