FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

COPY settings.gradle build.gradle gradle.* gradlew ./
COPY gradle ./gradle

RUN ./gradlew --no-daemon --quiet dependencies

COPY src ./src
RUN ./gradlew --no-daemon --quiet clean bootJar -x test

FROM eclipse-temurin:21-jdk-alpine AS runtime

COPY --from=build /app/build/libs/*.jar /app/app.jar

RUN apk add --no-cache tzdata && \
    ln -sf /usr/share/zoneinfo/Europe/Moscow /etc/localtime && \
    apk del tzdata

ENV JAVA_HOME=/usr/local/openjdk-21
ENV PATH="$JAVA_HOME/bin:$PATH"
ENV JAVA_OPTS="-XX:MaxRAM=128M -XX:MaxRAMPercentage=70 -XX:+UseSerialGC -XX:+TieredCompilation -XX:TieredStopAtLevel=1 -Xss512k -Djava.security.egd=file:/dev/./urandom"

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar"]
