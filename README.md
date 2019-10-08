# TODO

Application for managing todo list (with authentication). Application written with: Kotlin, Spring Boot 2.1, Spring 5 (Core, MVC, Security), H2 DB, Dart 2.5, AngularDart 5.


## Running

#### Short version
1. Run `docker-compose up`
2. Navigate in brawser to `localhost:8080`

#### Longer version
1. Start backend `cd todo-be && gradle bootRun`
2. Start frontend `cd todo-fe && npm start`
3. Navigate in brawser to `localhost:8080`


## Misc

### Backend formated and linted with [ktlint](https://ktlint.github.io/)
1. Lint: `gradle ktlint`
2. Format: `gradle ktlintFormat`
