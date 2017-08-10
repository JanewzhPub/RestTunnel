#!/bin/sh
export JAVA_HOME=/home/usr/jdk1.8.0_111
$JAVA_HOME/bin/java ^
	-Djava.security.manager -Djava.security.policy=security.policy ^
	-classpath .:client-main.jar com.quest.forge.rest.tunnel.client.Main ^
	ws://mfoglight.azurewebsites.net/api/tunnel http://10.30.154.102:8080/api/v1 ^
	Quest YSGZlrb7+sbrEWm/pFs6dfLZZircL6hIjFQJfohtX0OaVgbrUuGN8Fo47fSt35Ii54i6mHD+/c13XtKzVVXf3+1nCvlih1oqdT4SZIHxH/A+2z/EtIq50gN0Li2KAdNt+FanN1YUzXyStF5DtJ9lwkucAHd1aw3vPkqxFupozf4= ^
	> client.log