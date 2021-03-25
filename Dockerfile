FROM debian:stable-slim as build
WORKDIR /app
COPY . /app
RUN export DEBIAN_FRONTEND=noninteractive
RUN apt-get update -y 
RUN apt-get upgrade -y 
RUN mkdir -p /usr/share/man/man1mkdir -p /usr/share/man/man1 
RUN apt-get install openjdk-11-jdk -y 
RUN apt-get install openjdk-11-jre -y 
RUN apt-get install maven -y 
RUN mvn package -Dmaven.test.skip=true 
RUN mkdir jar 
RUN mv target/*.jar ./jar

FROM debian:stable-slim
WORKDIR /app
RUN export DEBIAN_FRONTEND=noninteractive && \
    apt-get update -y && \
    apt-get upgrade -y && \
    mkdir -p /usr/share/man/man1mkdir -p /usr/share/man/man1 && \
    apt-get install openjdk-11-jdk -y && \
    apt-get install openjdk-11-jre -y
COPY --from=build /app/jar/ /app
ENTRYPOINT ["java", "-jar", "*.jar"]