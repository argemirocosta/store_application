---
description: Bump the MINOR version in pom.xml, commit "Fechando versão X.Y", and push
---

Do the following in order, stopping to ask the user if anything is ambiguous:

1. Read the current `<version>` in `pom.xml`. It follows the format `MAJOR.MINOR-yyyyMMddHHmm`
   (e.g. `1.14-202409302129`, where the suffix is the release timestamp: year, month, day, hour,
   minute).
2. Compute the new version:
   - Increment MINOR by 1 (e.g. `14` → `15`).
   - Replace the timestamp suffix with the current date/time in `yyyyMMddHHmm` format.
   - Example: `1.14-202409302129` → `1.15-202606071435`.
3. Update the `<version>` element in `pom.xml` to the new version (only that element — don't
   touch other version-looking strings such as dependency versions).
4. Stage `pom.xml`, create a commit titled `Fechando versão MAJOR.NEW_MINOR` (e.g. `Fechando
   versão 1.15`), and push the current branch to `origin`, setting upstream with `-u` if it
   isn't tracked yet. Do not add a `Co-Authored-By` trailer.
5. Report the new version string and the push result back to the user.

Confirm with the user before pushing if the branch has diverged from `origin` in a way that
would require a force-push — never force-push without explicit confirmation.
