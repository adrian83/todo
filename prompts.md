Below, I've pasted the contents of the ai.md file.
Treat them as the primary, permanent instructions for the project.

If anything in subsequent commands conflicts with them,
ai.md always takes precedence.

--- ai.md ---

# AI Project Instructions

This file defines the global rules and expectations for AI assistance in this project.
All instructions in this file have higher priority than later prompts.

## Tech Stack
- Java 25
- Spring Boot 4.0.X
- Spring MVC (reactive)
- Spring Data JPA
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
- No security, authentication, or user accounts unless explicitly requested.
- No advanced frameworks or libraries beyond the defined stack.

## Definition of Done
- The application starts without errors.
- Features work end-to-end.
- Code is understandable for a beginner/intermediate Java developer.

--- end ---

add ccs styles to note_list.html. make it minimalistic. change visualization of notes from table to separate rectangles.  