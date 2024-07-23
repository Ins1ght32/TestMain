FROM node:alpine

RUN apk add --no-cache tini git \
    && npm install -g git-http-server \
    && adduser -D -g git git

USER git
WORKDIR /home/git

RUN which git-http-server

RUN git init --bare repository.git \
    && git config --global user.name "Lim Guan Quan Joseph" \
    && git config --global user.email "2200648@sit.singaporetech.edu.sg"


ENTRYPOINT ["tini", "--", "git-http-server", "-p", "3000", "/home/git"]

CMD ["git-http-server", "/home/git/repository.git"]