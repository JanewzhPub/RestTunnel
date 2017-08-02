set JAVA_HOME="C:\Program Files\Java\jdk1.8.0_92"
%JAVA_HOME%\bin\java ^
	-Djava.security.manager -Djava.security.policy=security.policy ^
	-classpath .;client-main.jar com.quest.forge.rest.tunnel.client.Main ^
	ws://localhost:8180/api/tunnel http://localhost:8080/api ^
	Quest YSGZlrb7+sbrEWm/pFs6dfLZZircL6hIjFQJfohtX0OaVgbrUuGN8Fo47fSt35Ii54i6mHD+/c13XtKzVVXf3+1nCvlih1oqdT4SZIHxH/A+2z/EtIq50gN0Li2KAdNt+FanN1YUzXyStF5DtJ9lwkucAHd1aw3vPkqxFupozf4= ^
	> client.log