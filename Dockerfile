FROM amazoncorretto:17

ENV SPRING_PROFILE=dev
ENV PORT=8080
ENV TZ=GMT+9

ENV DATABASE_HOST=localhost
ENV DATABASE_PORT=5432
ENV DATABASE_NAME=sbp_postgres_database
ENV DATABASE_USERNAME=sbp
ENV DATABASE_PASSWORD=sbp
ENV APPLICATION_JWT_SECRET=c2RmS0o4NzgmXmpma3Nkaio3Zjc4RkpLRGoqN2ZkanNoKjc4N0tGS0Y
ENV APPLICATION_JWT_EXPIRATION_IN_SECOND=43200
ENV MIGRATE_DATABASE=false
ENV APPLICATION_AWS_REGION=ap-southeast-1
ENV APPLICATION_AWS_ACCESS_KEY=application_aws_access_key
ENV APPLICATION_AWS_SECRET_KEY=application_aws_secret_key
ENV APPLICATION_AWS_BUCKET=sbp-bucket

RUN mkdir -p /usr/local/springbaseproject/

COPY ./target/springbaseproject-0.0.1-SNAPSHOT.jar /usr/local/springbaseproject/springbaseproject.jar

WORKDIR /usr/local/springbaseproject/
EXPOSE $PORT

ENTRYPOINT [ "sh", "-c", "java \
-Dspring.profiles.active=$SPRING_PROFILE \
-jar springbaseproject.jar \
" ]
