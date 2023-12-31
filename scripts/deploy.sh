REPOSITORY=/home/ec2-user

cd &REPOSITORY

APP_NAME=your_weather
JAR_NAME=$(ls $REPOSITORY/build/libs/ | grep 'SNAPSHOT.jar' | tail -n 1)
JAR_PATH=$REPOSITORY/build/libs/$JAR_NAME

CURRENT_PID=$(pgrep -f $APP_NAME)

if [ -z $CURRENT_PID ]
then
  echo "> No application to stop."
else
  echo "> kill -15 $CURRENT_PID"
  sudo kill -15 $CURRENT_PID
  sleep 5
fi

echo "> Redis Server Start"
nohup redis-server &

echo "> Deploying - $JAR_PATH"
nohup java -jar $JAR_PATH > /dev/null 2> /dev/null < /dev/null &
