# Sử dụng OpenJDK 21 làm base image
FROM openjdk:24-ea-24-jdk-slim

# Thiết lập thông tin metadata
LABEL maintainer="your-email@example.com"
LABEL description="Cloud Gateway Service"
LABEL version="0.0.1-SNAPSHOT"

# Cài đặt curl cho health check
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Tạo thư mục làm việc trong container
WORKDIR /app

# Copy file JAR từ target vào container
COPY target/CloudGateWay-0.0.1-SNAPSHOT.jar cloud-gateway.jar

# Expose port 8082 (port từ application.yml)
EXPOSE 8082

# Thiết lập JVM options và Spring Profile cho Docker
ENV JAVA_OPTS="-Xms512m -Xmx1024m -Djava.security.egd=file:/dev/./urandom"
ENV SPRING_PROFILES_ACTIVE=docker

# Tạo user non-root để bảo mật
RUN addgroup --system spring && adduser --system spring --ingroup spring
USER spring:spring

# Health check để kiểm tra trạng thái của Cloud Gateway
HEALTHCHECK --interval=30s --timeout=3s --start-period=30s --retries=3 \
    CMD curl -f http://localhost:8082/actuator/health || exit 1

# Chạy ứng dụng với Docker profile
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dspring.profiles.active=docker -jar cloud-gateway.jar"]
