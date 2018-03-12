FROM fansin/oraclejdk
MAINTAINER FansinZhao <171388204@qq.com>

# Add the service itself
ARG JAR_FILE
ADD target/${JAR_FILE} /app/discovery-server.jar

CMD ["/app/discovery-server.jar"]

ENTRYPOINT ["java", "-jar"]