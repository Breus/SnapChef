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

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./gradlew quarkusDev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

## Packaging and running the application

The application can be packaged using:

```shell script
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