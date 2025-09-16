# Sử dụng OpenJDK 24 làm base image (match với compiled JAR)
FROM openjdk:24-ea-24-jdk-slim

# Thiết lập thông tin metadata
LABEL maintainer="your-email@example.com"
LABEL description="Product Service"
LABEL version="0.0.1-SNAPSHOT"

# Cài đặt curl cho health check
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Tạo thư mục làm việc trong container
WORKDIR /app

# Copy file JAR từ target vào container
COPY target/ProductService-0.0.1-SNAPSHOT.jar product-service.jar

# Expose dynamic ports for both instances
EXPOSE 8085
EXPOSE 8086

# Thiết lập JVM options và Spring Profile cho Docker
ENV JAVA_OPTS="-Xms512m -Xmx1024m -Djava.security.egd=file:/dev/./urandom"
ENV SPRING_PROFILES_ACTIVE=docker

# Tạo user non-root để bảo mật
RUN addgroup --system spring && adduser --system spring --ingroup spring
USER spring:spring

# Health check động theo profile instance
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=5 \
    CMD curl -f http://localhost:8085/actuator/health || curl -f http://localhost:8086/actuator/health || exit 1

# Chạy ứng dụng với profile được set từ docker-compose
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE} -jar product-service.jar"]
