FROM arm64v8/eclipse-temurin:17 AS BUILDER

RUN mkdir app_source
COPY . app_source

WORKDIR /app_source

RUN chmod +x ./gradlew
RUN ./gradlew :app:api:bootJar

FROM arm64v8/eclipse-temurin:17 AS RUNNER

RUN mkdir /app

COPY --from=BUILDER /app_source/app/api/build/libs /app

WORKDIR /app

ENV TZ=Asia/Seoul

EXPOSE 8080
USER nobody

ARG PHASE
ENV ENV_PHASE=${PHASE}

ENTRYPOINT java -jar \
  -Dspring.profiles.active=${ENV_PHASE:-dev} \
  /app/*.jar
