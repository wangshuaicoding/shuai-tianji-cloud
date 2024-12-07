#!/bin/sh

# 复制项目的文件到对应docker路径，便于一键生成镜像。
usage() {
	echo "Usage: sh copy.sh"
	exit 1
}


# copy sql
#echo "begin copy sql "
#cp ../sql/ry_20240629.sql ./mysql/db
#cp ../sql/ry_config_20240902.sql ./mysql/db

# copy html
#echo "begin copy html "
#cp -r ../shuai-ui/dist/** ./nginx/html/dist


# copy jar
echo "begin copy shuai-gateway "
cp ../shuai-gateway/target/shuai-gateway-1.0.1.jar ./shuai/gateway/jar

echo "begin copy shuai-auth "
cp ../shuai-auth/target/shuai-auth-1.0.1.jar ./shuai/auth/jar

echo "begin copy shuai-user "
cp ../shuai-user/target/shuai-user-1.0.1.jar ./shuai/user/jar

echo "begin copy shuai-message-service "
cp ../shuai-message/shuai-message-service/target/shuai-message-service-1.0.1.jar ./shuai/message/jar


