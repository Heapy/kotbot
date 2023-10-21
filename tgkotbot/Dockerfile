FROM bellsoft/liberica-openjre-alpine:17.0.9
RUN apk --no-cache add curl
COPY /build/install/kotbot /kotbot
ENTRYPOINT /kotbot/bin/kotbot
