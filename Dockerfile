FROM openjdk:22 AS build

COPY pom.xml mvnw ./
COPY .mvn .mvn

# Convert Windows line endings to Unix line endings
RUN sed -i 's/\r$//' mvnw && chmod +x mvnw

RUN ./mvnw dependency:resolve

COPY src src
RUN ./mvnw package

FROM openjdk:22
WORKDIR /BankOps
COPY --from=build target/*.jar markantbank-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","markantbank-0.0.1-SNAPSHOT.jar"]