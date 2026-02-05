# TODO

Application for managing notes. Application written in Java with: Spring Boot, Spring Security, Spring Reactive MVC, Thymeleaf and Postgres DB


## Running

### Running with docker compose

#### Prerequisites
- Docker
- Docker Compose

#### Steps
1. Run `docker-compose up`
2. Navigate in browser to `localhost:8080`

### Running locally

#### Prerequisites
- Docker
- Java 8
- Gradle (version < 6)
- Dart
- Webdev

#### Steps
1. Start infrastructure (Elasticsearch and Postgres): `make deps`
2. Start backend: `make be-all`
3. Start frontend: `make fe-all`
4. Navigate in browser to `localhost:8080`


### Misc
1. Backend formated and linted with [ktlint](https://ktlint.github.io/)
- Lint: `gradle ktlint`
- Format: `gradle ktlintFormat`
