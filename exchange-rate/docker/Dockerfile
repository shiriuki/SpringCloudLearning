FROM eclipse-temurin:11

### Install app to validate gRPC service status
RUN GRPC_HEALTH_PROBE_VERSION=v0.3.2 && \
    wget -qO/bin/grpc_health_probe https://github.com/grpc-ecosystem/grpc-health-probe/releases/download/${GRPC_HEALTH_PROBE_VERSION}/grpc_health_probe-linux-amd64 && \
    chmod +x /bin/grpc_health_probe

### Add entrypoint as executable file
COPY entrypoint.sh /
RUN chmod +x /entrypoint.sh

### Set entrypoint to script running Java application
ENTRYPOINT ["/entrypoint.sh"]

### Add Java application artifact
COPY *.jar /application/application.jar