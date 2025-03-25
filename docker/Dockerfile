FROM docker.io/eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /build

COPY ../.mvn /build/.mvn
COPY ../mvnw /build/mvnw
COPY ../pom.xml /build/pom.xml

COPY ../application /build/application
COPY ../domain /build/domain
COPY ../persistence /build/persistence
COPY ../infrastructure /build/infrastructure
COPY ../web /build/web


RUN --mount=type=cache,target=/root/.m2 /build/mvnw -B -e -fn -C -T 1C dependency:go-offline -DexcludeArtifactIds=application,domain,persistence,infrastructure
RUN --mount=type=cache,target=/root/.m2 /build/mvnw -B -e -T 1C -DskipTests verify


FROM docker.io/eclipse-temurin:21-jre-alpine AS runner

WORKDIR /app

COPY --from=builder  /build/web/target/*.jar /app/web.jar

LABEL MAINTAINER="Attaporn_J <alienx1step@gmail.com>"
LABEL org.opencontainers.image.source="https://github.com/alienx1/java-project-base"
LABEL version="1.0"
LABEL description="Java Project Base"


EXPOSE 8443

ENTRYPOINT ["java", "-jar", "/app/web.jar" ]
