# Container with application
FROM amazoncorretto:11.0.3
COPY /build/install/kotbot /kotbot
ENTRYPOINT /kotbot/bin/kotbot
