FROM openjdk:8-jre-alpine

COPY build/libs/producer-0.0.1-SNAPSHOT.jar /app.jar
COPY docker-entrypoint.sh /
EXPOSE 8080

CMD ["/docker-entrypoint.sh"]
