# Shift Scheduler

Shift Scheduler is a web application can generate shift schedules for workplaces where employees work flexible hours.
Employees can specify the periods of the week during which they are available to work, as well as the range of hours that they expect to work per week.
Managers can set the hours of operation of their businesses, as well as the number of employees they need per hour, then request the generation of schedules 
that meet their needs, while also respecting their employees' availabilities.

# Sample Users

There are eight sample users available for trying out the application:

| Username      | Password    |
| ------------- | ----------- |
| sampleManager | password123 |
| employee1     | password123 |
| employee2     | password123 |
| employee3     | password123 |
| employee4     | password123 |
| employee5     | password123 |
| employee6     | password123 |
| employee7     | password123 |

# Running the Application with Docker Compose

1. Build the images: `docker compose build`
2. Start the application: `docker compose up`
3. Visit https://10.5.0.2:8443/api/ and trust the self-signed certificate of the backend
4. Visit the frontend application at http://10.5.0.4:4173

# Running the Application in a Development Environment

## Requirements

- A *nix system or WSL is recommended to be able to create a keystore with the Bash script in the `backend` directory
- [Java 21 or above](https://www.oracle.com/ca-en/java/technologies/downloads/)
- [Node.js](https://nodejs.org/en)
- [Redis](https://redis.io/downloads/)

## Steps

1. Start a local Redis server, which will be used to cache valid and usable JWTs
2. Open `backend/src/main/resources/application.properties`, and update the following settings, as needed:
    - `load.sample.data`: set this to `true` to populate the database with sample data when the application is started. Note that this should only be set to `true` when the application is started for the first time (or when the shiftscheduler database is empty).
    - `spring.datasource.url`: ensure that the URL matches the URL of the PostgreSQL server.
    - `redis.host`: the hostname or IP address of the Redis server.
    - `redis.port`: the port of the Redis server.
    - `application.security.jwt.secret-key`: change this as needed to a 384-bit (or larger) key.
    - `application.security.jwt.expiration`: JWT expiration time in milliseconds.
    - `server.ssl.key-store-password`: change this if the keystore password in `backend/keystore_gen.sh` is modified.
3. Open `backend/keystore_gen.sh`, and edit the values of `STOREPASS` and `KEYPASS` if needed.
4. Generate the keystore from the `backend` directory:
```
chmod +x keystore_gen.sh
./keystore_gen.sh
```
5. Start the backend:
```
./mvnw spring-boot:run
```
6. Navigate to the `frontend` directory, install all dependencies, then start the application:
```
npm install
npx vite
```

# Swagger UI

The OpenAPI documentation of the backend API endpoints can be viewed at `https://localhost:8443/api/swagger-ui/index.html`. To use any endpoints which require authorization, click on the "Authorize" button above the list of endpoints, and provide a valid JWT, which can be obtained by sending a request to `/user/login` or `/user/register`.

# Screenshots

<details>
<summary>View</summary>

| ![shift_scheduler_home](https://github.com/user-attachments/assets/b2703575-851d-4f2b-bcf2-e8d4a0bf1cd8) |
|:--:| 
| *Home page* |

| ![shift_scheduler_login](https://github.com/user-attachments/assets/5eee573c-d3e0-4012-8870-d0a31cc723ad) |
|:--:| 
| *Login* |

| ![shift_scheduler_manager_home](https://github.com/user-attachments/assets/54a6f6bd-4ff5-4662-8c6a-b5b3657762d7) |
|:--:| 
| *Manager dashboard* |

| ![shift_scheduler_hours_of_operation](https://github.com/user-attachments/assets/238378c4-1055-4ce7-a141-72f037d7e776) |
|:--:| 
| *Company hours of operation settings* |

| ![shift_scheduler_schedule_generation](https://github.com/user-attachments/assets/04429246-9626-4116-9a02-9f7c736fde53) |
|:--:| 
| *Schedule generation* |

| ![shift_scheduler_schedule_selection](https://github.com/user-attachments/assets/dd79fbcf-178e-4bfa-b80e-071782dcbc55) |
|:--:| 
| *Generated schedule selection* |

| ![shift_scheduler_manager_schedule_browser](https://github.com/user-attachments/assets/b1bbf403-1320-4c0e-a732-0f156adbd267) |
|:--:| 
| *Schedule browser (manager)* |

| ![shift_scheduler_employee_home](https://github.com/user-attachments/assets/15e0499c-da88-48ee-bd20-e12088a9f6e9) |
|:--:| 
| *Employee dashboard* |

| ![shift_scheduler_employee_settings](https://github.com/user-attachments/assets/7f81d8b6-dffc-4689-9917-0a6437777a4a) |
|:--:| 
| *Employee settings* |

| ![shift_scheduler_employee_schedule_browser](https://github.com/user-attachments/assets/396e6dfe-b2be-4e2a-8061-f30a39e84aa0) |
|:--:| 
| *Schedule browser (employee)* |

| ![shift_scheduler_manager_registration](https://github.com/user-attachments/assets/74f537b4-ef99-4e39-8a2c-9a3da8110335) |
|:--:| 
| *Company registration* |

| ![shift_scheduler_employee_registration](https://github.com/user-attachments/assets/e363c6ef-463c-4123-b727-c6f521c8238b) |
|:--:| 
| *Employee registration* |

</details>
