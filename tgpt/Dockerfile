FROM bellsoft/liberica-openjre-alpine:25
RUN apk --no-cache add curl
COPY /build/install/tgpt /tgpt
ENTRYPOINT ["/tgpt/bin/tgpt"]
