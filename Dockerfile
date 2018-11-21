# Container with application
FROM openjdk:11.0.1-jre-sid
COPY /build/install/kotbot /kotbot
ENTRYPOINT /kotbot/bin/kotbot
