FROM node:20 AS frontend-build
WORKDIR /app

COPY frontend/tickethub/package*.json ./
RUN npm install

COPY frontend/tickethub/ .
RUN npm run build


FROM maven:3.9-eclipse-temurin-21 AS backend-build
WORKDIR /app

COPY backend/tickethub/pom.xml .
COPY backend/tickethub/src ./src

COPY --from=frontend-build /app/build/client ./src/main/resources/static/new

RUN mvn clean package -DskipTests


FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=backend-build /app/target/*.jar app.jar

EXPOSE 8443
ENTRYPOINT ["java", "-jar", "app.jar"]