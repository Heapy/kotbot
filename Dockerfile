# Container with application
FROM openjdk:10-jre-slim
COPY /build/install/kotbot /kotbot
ENTRYPOINT /kotbot/bin/kotbot
