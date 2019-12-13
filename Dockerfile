# Container with application
FROM amazoncorretto:8u232
COPY /build/install/kotbot /kotbot
ENTRYPOINT /kotbot/bin/kotbot
