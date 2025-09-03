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
- [ ] Add resiliency patterns such as rate limiters on endpoints
- [ ] Set-up continuous integration / deployment (Github actions)

## Tech Stack

### Infrastructure

* [Docker-compose](https://docs.docker.com/compose/)
* [Pre-commit](https://pre-commit.com/)
* [Spotless](https://spotless.dev/)

### Persistence 

* [PostgreSQL](https://www.postgresql.org/)
* [PgAdmin](https://www.pgadmin.org/)

### Backend

* [Java](https://www.oracle.com/java/) 
* [Quarkus](https://quarkus.io/)
* [Gradle](https://gradle.org/)
* [Hibernate ORM](https://hibernate.org/orm/)
* [Hibernate Validator](https://hibernate.org/validator/)

### Frontend

* [Tailwind CSS](https://tailwindcss.com/)
* [Vue.js](https://vuejs.org/)
* [Vite](https://vitejs.dev/)
* [pnpm](https://pnpm.io/)
* [Node.js](https://nodejs.org/en/)
* [Biome](https://biomejs.dev/)
* [ESLint](https://eslint.org/)

## Getting Started

### Local Development Environment Setup

1. Install the required tools:

   - [Docker Desktop (for Docker Compose)](https://docs.docker.com/get-docker/)
   - [Java 21 JDK](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html)
   - [Node.js](https://nodejs.org/en/download/) (version 22 or higher)
   - [pnpm](https://pnpm.io/installation) 
   - [pre-commit](https://pre-commit.com/#install) (for required code formatting)

2. Install the required pre-commit hooks by running:
```shell
pre-commit install
```

3. Copy the example `.env` file to create your own: `cp .env.example .env`

   This file contains environment variables used by the application. It is essential to set up the database connection and other configurations.
```shell script
cp .env.example .env
```
4. Modify the `.env` by entering the correct credentials. The other default values should work for local development.

5. For easier database access, copy the example `.pgpass` to create your own:
```shell script
cp .pgpass.example .pgpass
```

6. Modify the `.pgpass` file with the correct database credentials.

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

1. Uncomment `quarkus_app: !reset null` in `docker compose.override.yml` to disable the Docker container for the Quarkus 
   app, allowing you to run it directly on your host machine in development mode.

2. Make sure the PostgreSQL database is running (now `docker compose up` will only start the database and PgAdmin):

3. Navigate to the `app` directory and run:

```shell script
cd app
./gradlew quarkusDev
```

or you can use `quarkus dev` if you have the Quarkus CLI installed.

The backend will be available at http://localhost:8080.

> **_Note:_**  Quarkus ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

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

> **_Note:_** The UI application is configured to use environment variables from the `.env` file in the root directory of the project. This means you only need to maintain a single `.env` file at the project root, which will be used by both the backend and frontend applications.

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