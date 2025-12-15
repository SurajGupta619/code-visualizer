# Incubator

Short README for this workspace (frontend + Java backend).

## Project Overview

- Frontend: React app with Tailwind CSS, located in `frontend/`.
- Backend: Spring Boot Java service (Maven), located in `jv-Backend/`.
- `temp_user_code/` contains scratch code or user submissions.

## Prerequisites

- Node.js (>=16) and npm
- Java 17+ (or the version required by the backend)
- Maven (the project includes the Maven wrapper `mvnw` / `mvnw.cmd`)

## Frontend (development)

1. Open a terminal and run:

```bash
cd frontend
npm install
npm start
```

2. Other useful commands:

```bash
npm run build   # production build
npm test        # run tests
```

The React source is in `frontend/src/` (components, pages, auth, etc.).

## Backend (development)

From the project root run:

Windows:

```powershell
cd jv-Backend
.\mvnw.cmd spring-boot:run
```

Linux / macOS:

```bash
cd jv-Backend
./mvnw spring-boot:run
```

To build a jar and run it:

```bash
cd jv-Backend
./mvnw clean package   # or .\mvnw.cmd on Windows
java -jar target/*.jar
```

The backend Java sources are in `jv-Backend/src/main/java/`.

## Project structure (top-level)

- `frontend/` — React UI
- `jv-Backend/` — Spring Boot backend (Maven)
- `temp_user_code/` — temporary code / exercises

## Notes

- Use `mvnw` / `mvnw.cmd` to avoid requiring a system Maven installation.
- If you want, I can add environment variable instructions, API docs, or deployment steps.
