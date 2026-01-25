# AI Project Instructions

This file defines the global rules and expectations for AI assistance in this project.
All instructions in this file have higher priority than later prompts.

## Tech Stack
- Java 25
- Spring Boot 4.0.X
- Spring MVC (reactive)
- Spring Data JPA
- Spring Security
- Thymeleaf
- H2 database (in-memory, for development)
- Maven

## Architecture Rules
- Use classic MVC architecture:
  Controller → Service → Repository
- Controllers must be thin (no business logic).
- Business logic belongs in Services.
- Repositories use Spring Data JPA only.

## Coding Rules
- Do NOT use Lombok.
- Write explicit constructors, getters, and setters.
- Prefer readability over cleverness.
- Keep classes and methods small.
- One class = one responsibility.
- Use new Java language features (such as Streams, Records, Record Patterns, Lambdas, Pattern Matching, Sealed Classes, Virtual Threads, Structured Concurrency, Scoped Values) where it makes sense.
- Do not attach dependencies to servlet-based Spring MVC.
- Do not import anything from servlet-based Spring MVC.

## Frontend Rules
- Use Thymeleaf for server-side rendering.
- Use plain HTML and minimal CSS.
- No JavaScript unless explicitly requested.

## Development Style
- Work iteratively: each step must compile and run.
- Avoid overengineering.
- Prefer simple, explicit solutions.
- Explain reasoning when introducing new concepts.

## Constraints
- No advanced frameworks or libraries beyond the defined stack.

## Definition of Done
- The application starts without errors.
- Features work end-to-end.
- Code is understandable for a beginner/intermediate Java developer.


