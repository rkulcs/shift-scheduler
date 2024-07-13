FROM alpine:latest

ADD . /opt
WORKDIR /opt

RUN apk update && apk add npm bash

RUN npm install && npm run build
RUN chmod +x env_setup.sh

CMD /bin/bash env_setup.sh && npm run preview
