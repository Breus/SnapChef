## TODO short term

- [ ] Add local JWT key generation to CONTRIBUTING.md
- [ ] Add SonarQube integration
- [ ] Deploy on quicoock.blaauwendraad.dev manually and prepare entire environment
- [ ] Get rid of Spotless but keep code formatter and corresponding pre-commit hooks
- [ ] Replace Biome with Prettier for code formatting
- [ ] Add unit and integration tests for backend and frontend
- [ ] Finish favorite functionality
- [ ] Finish shopping list functionality

## TODO longer term

- [ ] Add resiliency patterns such as rate limiters on endpoints
- [ ] Set-up continuous integration / deployment using Github actions

## Technical Details

### Infrastructure

* [docker compose](https://docs.docker.com/compose/)
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

1. Install the required, non-prepackaged tools:

    - [Docker Desktop](https://docs.docker.com/get-docker/) (for Docker Compose)
    - [Java 21 JDK](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html) (to build the backend
      Quarkus application)
    - [Node.js](https://nodejs.org/en/download/) (for frontend development tooling, version 22 or higher)
    - [pnpm](https://pnpm.io/installation) (for frontend package management)
    - [Python 3.13](https://www.python.org/downloads/) (for local dev environment auto-setup scripts)
    - [pre-commit](https://pre-commit.com/#install) (for automatic, required code formatting)
2. Run the `setup.py`, which will:
    1. Install the pre-commit hooks
    2. Generate a local RSA key pair for user authentication (JWT signing)
    3. Copy the example `.env.example` file to `.env`. This file contains environment variables used by the application.
       **Note**: You still need to modify the `.env` file with the correct credentials.
    4. Copy the example `.pgpass.example` file to `.pgpass`. **Note**: You still need to modify the `.pgpass` file with
       the
       correct database credentials.
3. Modify the generated `.env` and `.pgpass` files by entering the correct credentials. The other default values should
   work for local development.

### Running with Docker Compose

The easiest way to run the entire application stack (database, backend, and database admin tool) is using Docker
Compose:

```shell script
docker compose up -d
```

This will start:

- PostgreSQL database on port 5432
- PgAdmin on port 8081 (accessible at http://localhost:8081)
- Quarkus backend on port 8080 (accessible at http://localhost:8080)

To stop all services:

```shell script
docker compose down
```

**Note:** By default, the `docker-compose-override.yml` file configures the Quarkus application to be ignored by Docker
Compose because for local development it is more convenient to run it directly on your host machine using quarkusDev.

### Running the Backend in Development Mode

If you prefer to run the backend in development mode for live coding:

1. Uncomment `quarkus_app: !reset null` in `docker compose.override.yml` to disable the Docker container for the Quarkus
   app, allowing you to run it directly on your host machine in development mode.

2. Make sure the PostgreSQL database is running (now `docker compose up` will only start the database and PgAdmin):

3. From to the `app` directory run:

```shell script
./gradlew quarkusDev
```

or you can use `quarkus dev` if you have the Quarkus CLI installed.

The backend will be available at http://localhost:8080.

> **_Note:_**  Quarkus ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

### Running the Frontend

To run the frontend development server, from the `ui` directory:

1. Install the frontend dependencies (if you haven't already):

```shell script
pnpm install
```

2. Start the development server:

```shell script
pnpm dev
```

The frontend will be available at http://localhost:5173.

> **_Note:_** The UI application is configured to use environment variables from the `.env` file in the root directory
> of the project. This means you only need to maintain a single `.env` file at the project root, which will be used by
> both the backend and frontend applications.

## Packaging and Running the Application

### Backend

The backend application can be packaged using:

```shell script
cd app
./gradlew build
```

It produces the `quarkus-run.jar` file in the `build/quarkus-app/` directory.
Be aware that it’s not a _über-jar_ as the dependencies are copied into the `build/quarkus-app/lib/` directory.

The application is now runnable using `java -jar build/quarkus-app/quarkus-run.jar`.

If you want to build a _über-jar_, execute the following command:

```shell script
./gradlew build -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as a _über-jar_, is now runnable using `java -jar build/*-runner.jar`.

### Frontend

To build the frontend for production:

```shell script
cd ui
pnpm build
```

This will generate static files in the `ui/dist` directory that can be served by any web server.