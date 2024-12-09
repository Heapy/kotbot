FROM bellsoft/liberica-openjre-alpine:21.0.5
RUN apk --no-cache add curl
COPY /build/install/kotbot /kotbot
ENTRYPOINT /kotbot/bin/kotbot
