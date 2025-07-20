# Recipe Book application

The Recipe Book will be a web application where users can save their recipes along with the corresponding ingredient
list. We're not aiming to compete with existing recipe apps or services. Instead, the main goal is to learn the
technologies involved in building a modern, full-featured web application. If the app turns out to be useful to others,
that’s simply a nice bonus. We chose the recipe book concept because it's a domain everyone can understand and relate
to, and it offers flexibility: it can be very simple but can also become highly complex.

## Planned Features

* Any user can see all recipes
* A recipe consists of two things:
    * A (shopping) list of ingredients
    * A list of steps to prepare the meal
* A logged-in user can easily insert a new recipe
* A logged-in user can easily edit an existing recipe

## Future Features Ideas

* A user can select what they crave, e.g. “something with rice, chicken, …” and all matching recipes will be shown
* A user can easily share the recipe's ingredient list with someone via WhatsApp for grocery shopping

## TODO short term
- [ ] Add static code analysis
  - [ ] Add SonarQube integration
- [ ] Add input validation on endpoints (Hibernate bean validation)
- [ ] Add authentication/authorization

## TODO longer term
- [ ] Add JavaFormatting (pre-commit hook?)
- [ ] Add resiliency patterns such as rate limiters on endpoints
- [ ] Set-up continuous integration / deployment (Github actions)

## Used Technologies

### Infrastructure

* Container orchestration: Docker-compose

### Database

* PostgreSQL

### Backend

* Java
* Web stack: Quarkus imperative, HotSpot
* Serializer: Jackson
* Build tool: Gradle
* ORM: Hibernate ORM, and Panache

### Frontend

* TypeScript
* JavaScript Framework: VueJs
* CSS Framework: Tailwind
* Asset building: Vite
* Node package manager: pnpm

## Repository layout

```plain text
project-root/
├── app/ ← Quarkus application
│ ├── build.gradle
│ ├── settings.gradle
│ ├── src/
│ │ ├── main/
│ │ │ ├── java/
│ │ │ ├── resources/
│ │ │ └── docker/ ← Quarkus-specific Docker files
│ │ └── test/
│ └── db/ ← Database schema and data
│ ├── schema/
│ └── data/
│
├── ui/ ← The headless Vue.js frontend
│ ├── src/
│ │ ├── api/ ← API clients talking to Quarkus
│ │ ├── assets/
│ │ ├── components/
│ │ ├── models/ 
│ │ ├── router/ 
│ │ ├── App.vue
│ │ ├── main.ts
│ │ ├── style.css
│ │ └── vite-env.d.ts ← TypeScript environment definitions
│ ├── .editorconfig
│ ├── .gitignore
│ ├── .prettierignore
│ ├── .prettierrc
│ ├── .eslint.config.js
│ ├── index.html
│ ├── package.json
│ ├── pnpm-lock.yaml
│ ├── pnpm-workspace.yaml
│ ├── stylelint.config.js
│ ├── tsconfig.app.json ← TypeScript configuration for the app
│ ├── tsconfig.json ← TypeScript configuration for the entire project
│ ├── tsconfig.node.json ← TypeScript configuration for Node.js
│ └── vite.config.ts
│
├── docker-compose.override.yml
├── docker-compose.yml ← Compose backend/frontend/db
└── README.md
```

## Getting Started

### Environment Setup

1. Copy the example environment file to create your own:

```shell script
cp .env.example .env
```

2. Modify the `.env` file with your preferred settings if needed. The default values should work for local development.

### Running with Docker Compose

The easiest way to run the entire application stack (database, backend, and database admin tool) is using Docker Compose:

```shell script
docker-compose up -d
```

This will start:
- PostgreSQL database on port 5432
- PgAdmin on port 8081 (accessible at http://localhost:8081)
- Quarkus backend on port 8080 (accessible at http://localhost:8080)

To stop all services:

```shell script
docker-compose down
```

### Running the Backend in Development Mode

If you prefer to run the backend in development mode for live coding:

1. Make sure the PostgreSQL database is running (you can use Docker Compose for just the database):

```shell script
# Uncomment the quarkus_app service in docker-compose.override.yml first
docker-compose up -d postgres pg_admin
```

2. Navigate to the app directory and run:

```shell script
cd app
./gradlew quarkusDev
```

The backend will be available at http://localhost:8080.

> **_NOTE:_**  Quarkus ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

### Running the Frontend

To run the frontend development server:

1. Navigate to the ui directory:

```shell script
cd ui
```

2. Install dependencies (if you haven't already):

```shell script
pnpm install
```

3. Start the development server:

```shell script
pnpm dev
```

The frontend will be available at http://localhost:5173.

## Packaging and Running the Application

### Backend

The backend application can be packaged using:

```shell script
cd app
./gradlew build
```

It produces the `quarkus-run.jar` file in the `build/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `build/quarkus-app/lib/` directory.

The application is now runnable using `java -jar build/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./gradlew build -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar build/*-runner.jar`.

### Frontend

To build the frontend for production:

```shell script
cd ui
pnpm build
```

This will generate static files in the `ui/dist` directory that can be served by any web server.