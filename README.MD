# Alien Clock with Alarm

This project is a web application that showcases an **Alien Clock** using a unique time concept and features an alarm system. The frontend is built with **Vue 3** using the **Composition API**, while the backend is powered by **Spring Boot**.

## Features

- **Alien Clock**: Displays alien time using a custom time format.
- **Earth Time**: Displays the current Earth time with time zone.
- **Alarm Management**: Users can add, update, enable and disable alarms.
- **Alarm Notification**: The application triggers a sound when an alarm goes off.
- **Responsive UI**: Built using TailwindCSS and custom scss/css for a modern and adaptive design.

## Tech Stack

### Frontend
- **Vue 3** with **Composition API**
- **Pinia** for state management
- **TailwindCSS/CSS/SCSS** for styling
- **Vite** for development and build tooling

### Backend
- **Spring Boot**
- **Spring Web** for RESTful APIs
- **Spring Data JPA** for database interactions
- **PostgreSQL Database** for local testing

## Getting Started

### Prerequisites
Make sure you have the following installed:
- **Node.js** (v18 or higher)
- **Java 17** or higher
- **Maven**

---

### Frontend Setup

1. Navigate to the `frontend` folder:
    ```bash
    cd frontend
    ```
2. Install dependencies:
    ```bash
    npm install
    ```
3. Start the development server:
    ```bash
    npm run dev
    ```
4. Access the frontend at `http://localhost:5173`.

---

### Backend Setup

1. Navigate to the `backend` folder:
    ```bash
    cd backend
    ```
2. Build the project using Maven:
    ```bash
    mvn clean install
    ```
3. Run the Spring Boot application:
    ```bash
    mvn spring-boot:run
    ```
4. Backend API will be available at `http://localhost:8080/api/v1/alarms`.

---

## API Endpoints

| Method  | Endpoint                     | Description                  |
|---------|------------------------------|------------------------------|
| GET     | `/api/v1/alarms`             | Get all alarms               |
| GET     | `/api/v1/alarms/{id}`        | Get alarm by ID              |
| POST    | `/api/v1/alarms`             | Create a new alarm           |
| PUT     | `/api/v1/alarms/update/{id}` | Update an existing alarm     |
| PUT     | `/api/v1/alarms/switch/{id}` | Enable or disable an alarm   |
| DELETE  | `/api/v1/alarms/{id}`        | Delete an alarm              |

## Environment Variables

Create a `.env` file for frontend and `application.properties` for backend directories with the following variables:

### Frontend `.env`
```env
VITE_API_BASE_URL=http://localhost:8080/api/v1
```

### Backend `.env`
```env
spring.application.name=alien-clock
spring.datasource.url=jdbc:postgresql://localhost:5432/alien_clock_db
spring.datasource.username=postgres
spring.datasource.password=123
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

## Conclusion

Enjoy the Alien Clock experience and manage your alarms seamlessly! If you encounter any issues, feel free to submit an issue or contribute to the project.

Happy Coding! 🚀

