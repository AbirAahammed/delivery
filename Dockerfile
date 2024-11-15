# Use the official OpenJDK 17 image as a base
FROM mcr.microsoft.com/devcontainers/java:0-17

# Set the working directory
WORKDIR /workspace

# Copy build files and install dependencies if you're using Gradle or Maven
COPY build.gradle ./  

# Run a build to cache dependencies
RUN gradle dependencies --no-daemon || true  # or `mvn dependency:resolve`

# Copy the rest of your project files
COPY . .

# Expose the port your application will run on
EXPOSE 9001

# Command to run your application (change as needed)
CMD ["./gradlew", "bootRun", "--continuous"]  
# CMD ["mvn", "spring-boot:run"]  # Maven example, if using Spring Boot
