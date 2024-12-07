# 基础镜像
FROM redis:7.4.1
# author
MAINTAINER shuai

# 挂载目录
VOLUME /home/shuai/redis
# 创建目录
RUN mkdir -p /home/shuai/redis
# 指定路径
WORKDIR /home/shuai/redis
# 复制conf文件到路径
COPY ./conf/redis.conf /home/shuai/redis/redis.conf
