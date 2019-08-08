FROM gradle:latest

RUN mkdir /app
ADD ./ /app

WORKDIR /app

RUN gradle build

CMD ["gradle", "run"]
