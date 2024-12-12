#! /bin/sh
cd /opt/docker/script || exit 1
BASE_PATH='/opt/docker/jenkins/home/jenkins_home/workspace/shuai-dev-build'
PROJECT_NAME=""
PROJECT_PATH=''
CONTAINER_NAME=""
JAVA_OPTS="-Xms512m -Xmx512m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=512m"
SPRING_PROFILES_ACTIVE=dev
PORT=8080
DEBUG_PORT=0

while getopts "c:n:d:p:o:a:" opt; do
    case $opt in
         c)
            CONTAINER_NAME=$OPTARG
          ;;
         n)
            PROJECT_NAME=$OPTARG
          ;;
         d)
            PROJECT_PATH=$OPTARG
          ;;
         p)
            PORT=$OPTARG
          ;;
         o)
            [ -n "$OPTARG" ] && JAVA_OPTS=$OPTARG
          ;;
         a)
            [ -n "$OPTARG" ] && DEBUG_PORT=$OPTARG
          ;;
         ?)
            echo "unkonw argument"
            exit 1
          ;;
    esac
done
# 如果 DEBUG_PORT 不为 0，则设置 JAVA_OPTS 为调试模式的 JVM 选项。
if [ "$DEBUG_PORT" = "0" ]; then
  JAVA_OPTS=$JAVA_OPTS
else
  JAVA_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"
fi
IMAGE_NAME="${CONTAINER_NAME}:latest"
echo "copy xx.jar from ${BASE_PATH}/${PROJECT_PATH}"
rm -f app.jar
cp ${BASE_PATH}/${PROJECT_PATH}/target/${PROJECT_NAME}.jar ./app.jar ||  exit 1

echo "begin to build ${PROJECT_NAME} image ！！"

[ -n "`docker ps -a | grep ${CONTAINER_NAME}`" ] && docker rm -f ${CONTAINER_NAME}
[ -n "`docker images | grep ${CONTAINER_NAME}`" ] && docker rmi ${IMAGE_NAME}

docker build -t ${IMAGE_NAME} . || exit 1
echo "${PROJECT_NAME} image build success，java_opts = $JAVA_OPTS ！！^_^"

echo "begin to create container ${CONTAINER_NAME}，port: ${PORT} ！！"

if [ "$DEBUG_PORT" = "0" ]; then
  echo "run in normal mode"
  docker run -d --name ${CONTAINER_NAME} \
   -p "${PORT}:${PORT}" \
   -e JAVA_OPTS="${JAVA_OPTS}" \
   -e SPRING_PROFILES_ACTIVE="${SPRING_PROFILES_ACTIVE}" \
   --memory 300m --memory-swap -1 \
   --network shuai-network ${IMAGE_NAME} \
  || exit 1
else
  echo "run in debug mode"
  docker run -d --name ${CONTAINER_NAME} \
   -p "${PORT}:${PORT}" \
   -p ${DEBUG_PORT}:5005 \
   -e JAVA_OPTS="${JAVA_OPTS}" \
   -e SPRING_PROFILES_ACTIVE="${SPRING_PROFILES_ACTIVE}" \
   --network shuai-network ${IMAGE_NAME} \
  || exit 1
fi
echo "container is running now !! ^_^"
exit 0