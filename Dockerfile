FROM amazoncorretto:21 

COPY target/pi_zambom-0.0.1-SNAPSHOT.jar /app.jar 

ENTRYPOINT [ "java", "-jar", "/app.jar" ]