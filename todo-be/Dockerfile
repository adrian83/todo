FROM gradle:6.6.1-jdk14

ADD . /todo-be
WORKDIR /todo-be



RUN gradle build --no-daemon

RUN pwd

RUN ls -al

RUN cd build && ls -al

RUN cd build/libs && ls -al

CMD ["java", "-jar", "build/libs/todo-0.0.1-SNAPSHOT.jar"]
