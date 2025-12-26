FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /build
COPY . .

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21
WORKDIR /agroshop
COPY --from=build /build/target/*.jar ./agroshop.jar

ENTRYPOINT ["java", "-jar", "agroshop.jar"]