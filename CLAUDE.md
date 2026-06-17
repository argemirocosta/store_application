# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project overview

StoreApplication ("Sistema para Lojas") is a Java/JSF web app for managing store sales (clients,
sales, payment methods, users, stock reports). Built with Maven, JSF 2.2 (Glassfish/Mojarra) +
PrimeFaces 6.1, raw JDBC against PostgreSQL, packaged as a WAR, and deployed via webapp-runner
(see `Procfile`). All code, comments and commit messages are in Portuguese (pt-BR) — match that
convention when adding to existing files.

## Regras de segurança

@.claude/rules.md

## Common commands

```bash
# Run the full test suite (JUnit 5 + Mockito, with JaCoCo coverage)
mvn test

# Run a single test class
mvn test -Dtest=ClienteDAOTest

# Run a single test method
mvn test -Dtest=ClienteDAOTest#listarClientes

# Build the WAR (also copies webapp-runner.jar into target/dependency)
mvn package

# Run locally (after `mvn package`)
java -jar target/dependency/webapp-runner.jar --port 8080 target/*.war

# Run the business-focused integration tests (*IT) against a real Postgres
# (Testcontainers spins up postgres:15-alpine, requires Docker running)
mvn verify -P integration-tests
```

There is no linter configured. Java source/target level is 1.8.

The database is PostgreSQL. Create a `store_application` database and load `database.sql`
(schema `vendas`) before running locally.

## Architecture

Classic layered JSF/JDBC architecture, organized by feature under
`br.com.storeapplication`:

```
controller (*MB)  →  service (*Service)  →  dao (*DAO)  →  PostgreSQL (raw JDBC)
        ↑                                        ↓
   model/builder (*Builder)  ←──────────  shared/queries (*DAOQueries)
```

- **`controller`** — JSF `@ManagedBean` classes (e.g. `ClienteMB`, `VendaMB`, `UsuarioMB`,
  `FormaPagamentoMB`, `ReportJasperMB`). These back the `.xhtml` pages in `WebContent/pages`,
  hold view state, call into the service layer, and translate results into JSF dialogs/messages
  via `JSFUtil`, `Dialogs`, and `Mensagens`.
- **`service`** — thin pass-through layer between controllers and DAOs (one `*Service` per
  `*DAO`, mostly delegating 1:1). Business rules that don't belong in the controller or DAO
  live here.
- **`dao`** — JDBC data access. Each method opens its own `Connection` via
  `ConnectionFactory.getConnection()`, runs a `PreparedStatement` built from a constant in the
  matching `shared.queries.*DAOQueries` class, manually commits (`autoCommit` is `false`), and
  closes the connection in a `finally` block. SQL exceptions are wrapped in `ProjetoException`
  for write operations and merely logged (`printStackTrace`) for reads.
- **`shared.queries`** — SQL string constants only (one class per DAO, e.g.
  `ClienteDAOQueries`, `VendaDAOQueries`). Keep raw SQL out of the DAO classes themselves.
- **`model`** — plain data classes (`Cliente`, `Venda`, `Usuario`, `Endereco`,
  `FormaPagamento`, `BuscaRelatorio`).
- **`model.builder`** — fluent builders implementing `shared.Builder` (`construir()` /
  `mapear(ResultSet)`). These both construct objects fluently (`comNome(...).comCpf(...)`) and
  map `ResultSet` rows to model objects for the DAO layer — look here when changing what columns
  a DAO reads/writes.
- **`factory`** — `ConnectionFactory` opens JDBC connections using credentials selected by
  `Propriedades.Conexao` (`LOCALHOST` / `DEPLOY` / `PRODUCAO`) in `ConexaoBuilder`.
  **`Propriedades.Conexao` currently points at `PRODUCAO`** — switch it to `LOCALHOST` for local
  development and switch it back before committing/deploying. `LOCALHOST` still uses hardcoded,
  non-sensitive dev defaults (`postgres`/`post` on `localhost`); `DEPLOY` and `PRODUCAO` read
  `urlBanco`/`usuario`/`senha` from per-environment variables —
  `STORE_DB_URL_DEPLOY`/`DB_USUARIO_DEPLOY`/`DB_SENHA_DEPLOY` and
  `STORE_DB_URL_PRODUCAO`/`DB_USUARIO_PRODUCAO`/`DB_SENHA_PRODUCAO` respectively — via
  `util.AmbienteUtil.obterVariavelAmbiente`. These must be set on the hosting platform; the app
  fails fast with `IllegalStateException` if the relevant ones are missing when connecting.
- **`shared`** — cross-cutting constants/helpers: `Sessao` (session attribute keys),
  `Mensagens` (user-facing message keys), `Paginas` (page/navigation constants), `Dialogs`
  (PrimeFaces dialog widget IDs), `Builder` (interface for `model.builder.*`).
- **`util`** — stateless helpers: `SessaoUtil`/`JSFUtil` (JSF/session glue), `DataUtil`,
  `StringUtil`, `IntegerUtil`, `VerificadorUtil` (null/empty checks used pervasively in DAOs),
  `DocumentosUtil` (CPF/document formatting), `RelatorioUtil` (JasperReports report generation — templates in
  `WebContent/WEB-INF/relatorios`).
- **`exception.ProjetoException`** — single checked exception type used to wrap `SQLException`s
  thrown out of DAO write operations (insert/update/delete).

Each entity generally has a matching `*DAOQueries`, `*Builder`, `*Service`, `*DAO`, and `*MB`
across these packages — when adding a new entity or field, expect to touch all of them plus the
relevant `.xhtml` page under `WebContent/pages`.

## Session & multi-tenancy

The logged-in user (`Usuario`, stored under `Sessao.USUARIO_SESSAO`) scopes most data: DAOs read
`usuarioSessao` from the session (via `SessaoUtil.resgatarDaSessao`) and filter/insert queries by
`usuario = ?`. When adding queries or DAO methods for user-owned data, follow this same pattern
so data stays scoped to the current user.

## Tests

JUnit 5 (Jupiter) + Mockito (`mockito-inline` 4.x — needed for `Mockito.mockStatic`), located
under `src/test/br/com/storeapplication`, mirroring the main package structure:

- `model/builder/*Test` — `*Builder` tests covering both halves of the `shared.Builder` contract:
  `mapear(ResultSet)` (using `mock(ResultSet.class)` and Mockito `when`, no real DB connection)
  and the fluent `construir()` API.
- `validator/*Test` — pure validator tests, e.g. `CPFValidatorTest` calls `CPFValidator.validate`
  directly (its body never touches `FacesContext`/`UIComponent`, so no JSF mocking is needed).
- `util/*Test` — pure unit tests for the `util` helpers.
- `suites/*` — JUnit Platform `@Suite`/`@SelectClasses` groupings (e.g. `SuitePackageUtil` runs
  the `util` tests together).

When adding tests for a new entity, follow the existing pattern: add a `*BuilderTest` with
`umXxxTeste1()`/`umXxxTeste2()` fixture factories. These run via `mvn test` and never touch a
real database.

### Integration tests (`*IT`)

`dao/*IT` classes (e.g. `VendaDAOIT`, `ClienteDAOIT`) test real business rules — stock
calculation, discount aggregation, client normalization, multi-tenancy isolation — against a
real PostgreSQL instance:

- Run via `mvn verify -P integration-tests` (maven-failsafe-plugin, separate non-default
  profile). `mvn test`/`mvn package` skip them entirely and need no Docker.
- Requires Docker running: Testcontainers starts a `postgres:15-alpine` container, loads the
  `vendas` schema from `database.sql`, and points `Propriedades.Conexao` at it
  (`LOCALHOST`) for the duration of the JVM. The container is ephemeral and torn down
  automatically — nothing is written to a real database.
- Extend `integration.PostgresIntegrationTestBase`, which wires up the container and a mocked
  `FacesContext`/session (`integration.FacesContextTestSupport`).
- Each test creates its own user via `integration.UsuarioFixture.criarUsuario()` and calls
  `loginComoUsuario(usuario)`. Since `Cliente`/`Venda` queries filter by `usuario = ?`, each
  test's data is naturally isolated — no rollback or cleanup needed.

## Revisão de PRs

Quando o usuário pedir para "revisar"/"revisar o PR"/"revisar a branch atual", siga
este processo de três partes:

1. Rode em paralelo (via Agent tool) os dois agents do projeto:
   - `revisor-padrao` — aderência aos padrões de backend/frontend/SQL (skills
     `backend-pattern`/`frontend-pattern`/`sql-pattern`) e às regras de segurança em
     `.claude/rules.md`.
   - `qa` — `mvn test` + `mvn verify -P integration-tests` + cobertura JaCoCo do
     código novo/alterado via `.claude/scripts/check-coverage.sh`.
2. Faça você mesmo (o agente principal) uma análise complementar do diff, sem repetir
   o que os dois agents acima já cobrem:
   - **Test plan do PR**: os itens do checklist da descrição refletem o que foi
     verificado nesta revisão? Aponte itens que já podem ser marcados como feitos.
   - **Escopo e commits**: o PR tem escopo único e coerente? Mensagens de commit em
     pt-BR e descritivas, seguindo o estilo do histórico?
   - **Documentação**: a mudança introduz convenções, comandos ou agents novos que
     deveriam ser refletidos neste arquivo ou em algum skill?
   - Qualquer outro ponto de consistência/arquitetura fora do alcance dos dois agents.
3. Apresente um relatório consolidado com três seções — "revisor-padrao", "qa" e
   "Análise complementar" — terminando em um veredito geral (aprovado / aprovado com
   ressalvas / reprovado).

Use o `/code-review` (ou `/code-review ultra`) genérico apenas se o usuário pedir
explicitamente um outro tipo de revisão.

## Versioning convention

Commits that close out a release are titled `Fechando versão X.Y` and bump the `<version>` in
`pom.xml` (currently `1.14-202409302129`, format `MAJOR.MINOR-yyyyMMddHHmm`). Follow this
convention when asked to cut a release.
