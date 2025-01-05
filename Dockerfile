# 基础镜像
FROM eclipse-temurin:8-jre
# author
MAINTAINER shuai

# 创建目录，并使用它作为工作目录
RUN mkdir -p /home/shuai
WORKDIR /home/shuai

# 设定时区
ENV TZ=Asia/Shanghai
ENV JAVA_OPTS=""
ENV SPRING_PROFILES_ACTIVE=""

RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

ADD app.jar /home/shuai/app.jar

ENTRYPOINT ["sh","-c","java -jar $JAVA_OPTS -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE} /home/shuai/app.jar"]