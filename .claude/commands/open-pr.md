---
description: Stage changes, commit, push the branch, and open a PR against main
---

Do the following in order, stopping to ask the user if anything is ambiguous:

1. Run `git status`, `git diff`, and `git log` (recent commits) in parallel to see what changed
   and to learn this repo's commit message style. All commit messages in this repo are in
   Portuguese (pt-BR) — match that.
2. Stage the relevant files by name (never `git add -A` or `git add .`). Skip anything that
   looks like a secret (`.env`, credentials, etc.) and warn the user if such a file appears
   modified.
3. Draft a concise commit message (1-2 sentences, in Portuguese, focused on *why* not *what*)
   and create the commit. Do not add a `Co-Authored-By` trailer.
4. Push the current branch to `origin`, setting upstream with `-u` if it isn't tracked yet.
5. Open a PR with `gh pr create` against `main`:
   - Title under 70 characters, in Portuguese.
   - Body with a `## Summary` (bullet points of what changed and why) and a `## Test plan`
     (checklist of how to verify), passed via a HEREDOC. Do not add a Claude/Anthropic
     attribution footer.
6. Report the PR URL back to the user.

Confirm with the user before pushing if the branch has diverged from `origin` in a way that
would require a force-push — never force-push without explicit confirmation.
