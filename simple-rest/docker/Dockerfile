FROM eclipse-temurin:11

### Add entrypoint as executable file
COPY entrypoint.sh /
RUN chmod +x /entrypoint.sh

### Set entrypoint to script running Java application
ENTRYPOINT ["/entrypoint.sh"]

### Add Java application artifact
COPY *.jar /application/application.jar