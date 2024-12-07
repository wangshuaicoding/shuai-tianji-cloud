# 基础镜像
FROM eclipse-temurin:8-jre
# author
MAINTAINER shuai

# 挂载目录
VOLUME /home/shuai
# 创建目录
RUN mkdir -p /home/shuai
# 指定路径
WORKDIR /home/shuai
# 复制jar文件到路径
COPY ./jar/shuai-user-1.0.1.jar /home/shuai/app.jar
# 启动系统服务
# ENTRYPOINT ["java","-jar","shuai-user-1.0.1.jar"]

## 设置 JAVA_OPTS 环境变量，可通过 docker run -e "JAVA_OPTS=" 进行覆盖
# 设置激活环境
ENV JAVA_OPTS="-Xms512m -Xmx512m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=512m"
ENV SPRING_PROFILES_ACTIVE=prod

## 启动后端项目
CMD java ${JAVA_OPTS} -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE} -Djava.security.egd=file:/dev/./urandom -jar app.jar