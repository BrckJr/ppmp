# Expects the Quarkus fast-jar produced by `mvn -f backend/pom.xml package` (CI builds it once).
FROM eclipse-temurin:25-jre-alpine

RUN addgroup -S ppmp && adduser -S ppmp -G ppmp
WORKDIR /app

COPY --chown=ppmp:ppmp app/target/quarkus-app/ /app/

USER ppmp
EXPOSE 8080
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75 -XX:+UseSerialGC -Djava.util.logging.manager=org.jboss.logmanager.LogManager"
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/quarkus-run.jar"]
