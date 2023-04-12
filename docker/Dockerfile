FROM gradle:jdk17-alpine
WORKDIR /activity-command-service

COPY src /activity-command-service/src/
COPY build.gradle .
COPY lombok.config .

RUN gradle clean build -x test

ENTRYPOINT ["java", "-jar", "./build/libs/activity-command-service-1.0.0.jar"]