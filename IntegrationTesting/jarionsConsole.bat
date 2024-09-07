@echo on
set PWDEBUG=1
set PLAYWRIGHT_JAVA_SRC=C:\Users\EmirCogorno\Documents\source\IntegrationTesting
cd %PLAYWRIGHT_JAVA_SRC%
mvn test -Dtest=PlaywrightTest