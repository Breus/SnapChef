# Contributing to the Project

## Local Development Environment Setup

### Prerequisites

Ensure you have the following tools installed:

* [Docker Desktop](https://docs.docker.com/get-docker/) - For running the PostgreSQL database
* [Java 21 JDK](https://www.oracle.com/java/technologies/downloads/#java21) - For the backend Quarkus application
* [Node.js](https://nodejs.org/en/download/) - For frontend development (version 22 or higher)
* [pnpm](https://pnpm.io/installation) - For frontend package management
* [Python 3.13](https://www.python.org/downloads/) - For local development scripts
* [pre-commit](https://pre-commit.com/#install) - For automatic code formatting

### Initial Setup

Run the setup script to prepare your development environment:

```bash
./local_setup.py
```

This script will:

* Install required pre-commit hooks
* Install frontend Node dependencies
* Generate a local RSA key pair for JWT authentication

## Development Workflow

### Starting the Development Environment

Run a single command to start the entire stack in development mode:

```bash
./dev_local.py
```

This script automatically:

* Launches the PostgreSQL database in Docker
* Starts the Quarkus backend in development mode
* Runs the frontend Vite development server
* Gracefully shuts down all components when interrupted

### Accessing the Application

Once the development environment is running, you can access the application via:

* [http://localhost:5173](http://localhost:5173) (frontend SPA)
* [http://localhost:8081](http://localhost:8081) (backend API)

## Building for Production

To build the entire application for production, run the following command:

```bash
cd app && ./gradlew build
```

This creates a runnable JAR file in `app/build/quarkus-app/` directory.

The resulting JAR is packaged in a container image in the `docker-compose.prod.yml` file created in the Github release
workflow ( see `.github/workflows/release.yml`)
