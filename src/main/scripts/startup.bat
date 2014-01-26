@echo off
set jarFileName=littcore-deploy

setlocal ENABLEDELAYEDEXPANSION
if defined CLASSPATH (set CLASSPATH=%CLASSPATH%) else (set CLASSPATH=.)
FOR /R ..\lib %%G IN (*.jar) DO set CLASSPATH=!CLASSPATH!;%%G

echo The Classpath definition is %CLASSPATH%...

set JAVA_OPTS=%JAVA_OPTS% -Xms64m -Xmx256m -XX:MaxPermSize=128m 
echo %JAVA_OPTS%

echo %jarFileName%...

java %JAVA_OPTS% -cp "../conf;%jarFileName%.jar" com.litt.core.deploy.gui.Gui