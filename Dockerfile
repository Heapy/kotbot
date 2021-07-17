# Container with application
FROM amazoncorretto:11.0.11
COPY /build/install/kotbot /kotbot
ENTRYPOINT /kotbot/bin/kotbot
