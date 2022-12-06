FROM eclipse-temurin:11

### Add curl for health checks
RUN apt-get update \
    && apt-get install -y curl \
    && rm -rf /var/lib/apt/lists/*

### Add entrypoint as executable file
COPY entrypoint.sh /
RUN chmod +x /entrypoint.sh

### Set entrypoint to script running Java application
ENTRYPOINT ["/entrypoint.sh"]

### Add Java application artifact
COPY *.jar /application/application.jar