FROM maven:3.9.9-amazoncorretto-21-al2023

WORKDIR /usr/src/app


COPY . .

EXPOSE 8080

RUN ["mvn", "clean", "install","-Dmaven.test.skip=true"]

CMD [ "mvn","spring-boot:run" ]
