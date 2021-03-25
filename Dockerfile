FROM debian:stable-slim as build
WORKDIR /app
COPY . /app
RUN apt-get update -y && \
    mkdir -p /usr/share/man/man1mkdir -p /usr/share/man/man1 && \
    apt-get install openjdk-11-jdk -y && \
    apt-get install maven -y && \
    mvn package -Dmaven.test.skip=true && \
    mkdir jar && \
    mv target/*.jar ./jar

FROM debian:stable-slim
WORKDIR /app
RUN apt-get update -y && \
    mkdir -p /usr/share/man/man1mkdir -p /usr/share/man/man1 && \
    apt-get install openjdk-11-jre -y
COPY --from=build /app/jar/ /app
ENTRYPOINT ["java", "-jar", "*.jar"]