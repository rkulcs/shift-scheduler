FROM alpine:latest

ARG OPENJDK=openjdk21

ADD . /opt
WORKDIR /opt

RUN apk update && apk add ${OPENJDK} bash

RUN rm src/main/resources/keystore*
RUN chmod +x keystore_gen.sh; /bin/bash keystore_gen.sh
RUN ./mvnw install -DskipTests

CMD ./mvnw spring-boot:run
