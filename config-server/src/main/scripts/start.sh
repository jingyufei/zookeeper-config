#!/bin/sh
 
base_dir=$(cd `dirname $0` && pwd)
jarFile=`ls ${base_dir} -t| grep  "^config-server.*\.jar$" |head -n 1`
appName="config-server"
_LAUNCHER_DAEMON_OUT="server.out"

if [[ -z ${jarFile} ]]; then
  echo "can not  found jar file, failed to start server!"
  exit 1
fi

pid=`ps -ef | grep "appName=$appName" | grep -v "grep" | awk '{print $2}'`
if [ "$pid" = "" ]
then
	java -DappName=$appName -Djava.security.egd=file:/dev/./urandom -Xms512M -Xmx2048M  -Xbootclasspath/a:.: -jar ${jarFile} --spring.config.location=./application.properties > "$_LAUNCHER_DAEMON_OUT" 2>&1 < /dev/null &
else
	echo "$appName is running"
fi
