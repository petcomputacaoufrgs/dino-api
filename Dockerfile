FROM debian:stable-slim as build
WORKDIR /app
COPY . /app
RUN apt-get update -y && \
    apt-get upgrade -y && \
    mkdir -p /usr/share/man/man1mkdir -p /usr/share/man/man1 && \
    apt-get install openjdk-11-jdk -y && \
    apt-get install maven -y && \
    mvn package -Dmaven.test.skip=true && \
    mv target/*.jar ./app.jar

FROM debian:stable-slim
COPY --from=build /app/app.jar /app.jar
RUN apt-get update -y && \
    apt-get upgrade -y && \
    mkdir -p /usr/share/man/man1mkdir -p /usr/share/man/man1 && \
    apt-get install openjdk-11-jre -y
ENTRYPOINT ["java","-jar","/app.jar"]