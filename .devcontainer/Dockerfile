# Use an OpenJDK image with Gradle pre-installed
FROM gradle:7.3.3-jdk17 AS build

# Set the working directory
WORKDIR /workspace

# Copy build.gradle and settings.gradle files to cache dependencies
COPY build.gradle settings.gradle ./

# Download and cache dependencies
RUN gradle dependencies --no-daemon || true

# Copy the rest of your project files
COPY . .

# Build the project
RUN gradle build --no-daemon --offline || true

# Set the entry point to start your application (replace 'YourMainClass' with the main class for your app)
CMD ["gradle", "run", "--no-daemon"]
