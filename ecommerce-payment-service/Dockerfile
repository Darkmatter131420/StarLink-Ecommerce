FROM openjdk:17-jdk-slim

# 指定工作目录
WORKDIR /app

# 将jar包添加到工作目录
ADD target/ecommerce-payment-service-1.0-SNAPSHOT.jar /app/ecommerce-payment-service-1.0-SNAPSHOT.jar

# 暴露服务端口
EXPOSE 8086

# 启动命令
ENTRYPOINT ["java", "-jar", "/app/ecommerce-payment-service-1.0-SNAPSHOT.jar"]