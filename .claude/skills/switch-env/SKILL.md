---
name: switch-env
description: Switches the database connection environment (LOCALHOST/DEPLOY/PRODUCAO) configured in Propriedades.java, builds the project with Maven, and copies the generated WAR file to the Downloads directory. Use when asked to switch/change/toggle the connection environment, or to point Propriedades.Conexao at localhost, deploy, or producao and produce a build.
allowed-tools: Read Edit Bash(mvn *) Bash(cp *) Bash(ls *) Bash(find *)
---

Switch the active database connection environment for this project (`Propriedades.Conexao` in
`src/main/br/com/storeapplication/factory/Propriedades.java`), build the WAR, and copy it to
`~/Downloads`.

Target environment: $ARGUMENTS — accepts `localhost`, `deploy`, or `producao`/`production`
(case-insensitive), mapping to the `Conexoes` enum constants `LOCALHOST`, `DEPLOY`, `PRODUCAO`.
If no argument was given, or it doesn't match one of these, ask the user which environment to
switch to before doing anything else.

Steps:

1. Read `src/main/br/com/storeapplication/factory/Propriedades.java` and find the line
   `public static Conexoes Conexao = Conexoes.<CURRENT>;`.
2. If `<CURRENT>` already equals the requested target, tell the user it's already set to that
   environment, and ask whether to proceed with the build anyway (skip straight to step 4 if so)
   or stop.
3. Edit only that line, changing the enum constant to the target:
   `public static Conexoes Conexao = Conexoes.<TARGET>;`. Do not touch anything else in the
   file, and leave the hardcoded credentials in `ConexaoBuilder` untouched.
4. Build the project: `mvn clean install`. This compiles, runs the test suite, and packages the
   WAR (the `maven-war-plugin` is configured against `WebContent`). If the build fails, report
   the failure to the user and stop — do not copy a stale or missing artifact.
5. Locate the generated WAR under `target/` (e.g. `target/store_application-*.war` — the exact
   name includes the project version from `pom.xml`).
6. Copy it to the user's Downloads directory: `cp target/<warfile>.war ~/Downloads/`.
7. Confirm to the user which environment is now active and the full path of the copied WAR file
   in `~/Downloads`.

Notes:
- This skill does not commit or push anything — it only edits the working tree, builds, and
  copies the artifact.
- Switching to `PRODUCAO` is the checked-in default; switching to `LOCALHOST` is typically for
  local development/testing builds.
