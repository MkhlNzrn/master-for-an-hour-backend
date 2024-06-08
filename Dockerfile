FROM amazoncorretto:17.0.11
ARG JAR_FILE=out/artifacts/master_for_an_hour_backend_jar/master-for-an-hour-backend.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]