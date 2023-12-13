FROM maven:3.9.3
WORKDIR /app
COPY . .
RUN mvn package -DskipTests
EXPOSE ${PORT}
CMD ["java", "-jar", "target/matchapp-backend-0.0.1-SNAPSHOT.jar"]