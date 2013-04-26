@ECHO OFF
SET A_PORT=9000
SET A_DBG=-Xdebug -Xnoagent -Xrunjdwp:transport=dt_socket,address=%A_PORT%,server=y,suspend=y
java %A_DBG% -cp . App