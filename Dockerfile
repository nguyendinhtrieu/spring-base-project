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
ENV APPLICATION_MAIL_ENABLE=true
ENV APPLICATION_MAIL_SERVICE_ENVIRONMENT=smtp
# SMTP Configuration (used when APPLICATION_MAIL_SERVICE_ENVIRONMENT is "smtp")
ENV APPLICATION_MAIL_SMTP_HOST=localhost
ENV APPLICATION_MAIL_SMTP_PORT=1025
ENV APPLICATION_MAIL_SMTP_USERNAME=sbp_test_email@example.com
ENV APPLICATION_MAIL_SMTP_PASSWORD=password
ENV APPLICATION_MAIL_SMTP_SENDER=sbp_test_email@example.com
ENV APPLICATION_MAIL_SMTP_SSL_ENABLE=false
# SES Configuration (used when APPLICATION_MAIL_SERVICE_ENVIRONMENT is "ses")
ENV APPLICATION_MAIL_AWS_SES_REGION=ap-southeast-1
ENV APPLICATION_MAIL_AWS_SES_SENDER=application_mail_aws_ses_sender
# SendGrid Configuration (used when APPLICATION_MAIL_SERVICE_ENVIRONMENT is "sendgrid")
ENV APPLICATION_MAIL_SENDGRID_API_KEY=application_mail_sendgrid_api_key
ENV APPLICATION_MAIL_SENDGRID_FROM_EMAIL=sbp_test_email@example.com
ENV APPLICATION_MAIL_SENDGRID_FROM_NAME=application_mail_sendgrid_from_name

RUN mkdir -p /usr/local/springbaseproject/

COPY ./target/springbaseproject-0.0.1-SNAPSHOT.jar /usr/local/springbaseproject/springbaseproject.jar

WORKDIR /usr/local/springbaseproject/
EXPOSE $PORT

ENTRYPOINT [ "sh", "-c", "java \
-Dspring.profiles.active=$SPRING_PROFILE \
-jar springbaseproject.jar \
" ]
