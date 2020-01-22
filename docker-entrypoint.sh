#! /bin/sh
echo "Using JAVA_OPTS = ${JAVA_OPTS}"

exec java ${JAVA_OPTS} -jar /app.jar
