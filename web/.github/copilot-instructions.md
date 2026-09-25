# Copilot Instructions for this repository

You are working in the Java Spring Boot project under this repository.

## Core role and operating rules
- Act as a careful software engineer for this codebase.
- Read the relevant project docs before making significant changes, especially files under `docs/`.
- Prefer the guidance in `README.md` and the rule documents in `docs/*` over ad-hoc assumptions.
- Keep changes aligned with the existing architecture and naming conventions in this repo.

## Required project guidance
- Follow `docs/coding-rules.md` for Java, Spring Boot, naming, logging, dependency injection, and code quality expectations.
- Follow `docs/security-rules.md` for secrets handling, input validation, injection protection, and authorization checks.
- Follow `docs/api-rules.md` and `docs/api-spec.md` when changing API contracts or request/response models.
- Follow the domain model and work-order docs when making business logic changes.

## Security rules (must never be violated)
- Never commit secrets, API keys, tokens, passwords, certificates, private keys, or connection strings.
- Never hardcode sensitive data in code, comments, fixtures, or configuration files.
- Use environment variables or secure configuration instead of embedding values in source.
- Validate inputs at the boundary and do not trust client-provided data.
- Do not introduce SQL injection risks or unsafe string concatenation for database queries.
- Do not log sensitive data or PII.

## Working style
- Use clear, readable Java code with constructor injection and specific exceptions.
- Avoid generic `RuntimeException` / `Exception` unless strictly required by framework contracts.
- Keep changes minimal, targeted, and consistent with the current module structure.
- Do not invent features or silently bypass documented constraints.
- If requirements are unclear, ask clarifying questions instead of guessing.

## Before finalizing a change
- Check whether the request touches security, API contracts, or docs. If so, review the relevant rule files first.
- Make sure the final output does not include secrets or private environment values.
- If you need to propose code or configuration, prefer repository-safe patterns and local environment conventions.
