# Store Application

Sales system for stores ("Sistema para Lojas") — a Java/JSF web app for managing clients,
sales, payment methods, users, and stock reports.

## Tech stack

- Java 8, Maven
- JSF 2.2 (Mojarra) + PrimeFaces 6.1
- Raw JDBC against PostgreSQL
- Packaged as a WAR, run via `webapp-runner`
- JUnit 5 + Mockito for unit tests, Testcontainers for integration tests

## Getting started

Clone the project:

```shell
git clone https://github.com/argemirocosta/store_application.git
```

### Database setup

Create a `store_application` database in PostgreSQL and load the schema/seed data from
`database.sql` (creates the `vendas` schema).

### Configure the connection

`src/main/br/com/storeapplication/factory/Propriedades.java` selects which environment
`ConexaoBuilder` uses (`LOCALHOST`, `DEPLOY`, or `PRODUCAO`). For local development, set it to
`LOCALHOST` and export the matching environment variables before building/running:

```shell
export STORE_DB_URL_LOCALHOST="jdbc:postgresql://localhost:5432/store_application"
export DB_USUARIO_LOCALHOST="postgres"
export DB_SENHA_LOCALHOST="<sua-senha>"
```

`DEPLOY` and `PRODUCAO` read the equivalent `STORE_DB_URL_*`/`DB_USUARIO_*`/`DB_SENHA_*`
variables for their respective environments. The app fails fast with an
`IllegalStateException` if the variables required by the selected environment are missing.

> **Important:** `Propriedades.Conexao` must be set back to `PRODUCAO` before committing.

## Build

```shell
mvn package
```

This also copies `webapp-runner.jar` into `target/dependency`.

## Run

```shell
java -jar target/dependency/webapp-runner.jar --port 8080 target/*.war
```

Then open `http://localhost:8080`.

## Tests

Run the full unit test suite (JUnit 5 + Mockito, with JaCoCo coverage):

```shell
mvn test
```

Run a single test class or method:

```shell
mvn test -Dtest=ClienteDAOTest
mvn test -Dtest=ClienteDAOTest#listarClientes
```

### Integration tests

Business-rule integration tests (`*IT`) run against a real PostgreSQL instance spun up with
Testcontainers (requires Docker running):

```shell
mvn verify -P integration-tests
```

`mvn test`/`mvn package` skip these and need no Docker.
