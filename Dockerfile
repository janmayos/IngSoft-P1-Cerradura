FROM maven:3.9.9-amazoncorretto-21-al2023

WORKDIR /usr/src/app

COPY pom.xml .
COPY . .

EXPOSE 8008


CMD [ "mvn","clean","install","spring-boot:run" ]
