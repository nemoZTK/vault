@echo on
set PWDEBUG=1
set PLAYWRIGHT_JAVA_SRC=%~dp0
echo "setted playwright dir in"
echo %PLAYWRIGHT_JAVA_SRC%
mvn test -Dtest=PlaywrightTest