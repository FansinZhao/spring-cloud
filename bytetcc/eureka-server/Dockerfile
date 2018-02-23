FROM fansin/oraclejdk
MAINTAINER FansinZhao <171388204@qq.com>

# Add the service itself
ARG JAR_FILE
ADD target/${JAR_FILE} /app/eureka-server.jar

ENTRYPOINT ["java", "-jar", "/app/eureka-server.jar"]