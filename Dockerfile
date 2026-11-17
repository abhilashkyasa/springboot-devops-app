# Runtime image only: the jar is built by the Maven step of the pipeline (target/app.jar)
FROM amazoncorretto:17-alpine

# Run as a non-root user (container scanners flag root by default)
RUN addgroup -S app && adduser -S app -G app
WORKDIR /app

COPY --chown=app:app target/app.jar app.jar

USER app
EXPOSE 8080

# Respect container memory limits instead of host memory
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75.0"

HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget -qO- http://localhost:8080/actuator/health/liveness || exit 1

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar app.jar"]
