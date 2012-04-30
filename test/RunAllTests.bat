javac -cp .;..\src\java;..\lib\junit-4.10.jar;..\lib\mockito-all-1.9.0.jar;..\lib\rabbitmq-client.jar;..\lib\commons-io-1.2.jar;..\lib\commons-collections-3.2.1.jar;..\lib\commons-cli-1.2.jar -d build -Xlint:unchecked -Xlint:deprecation RunAllTests.java

java -cp build;..\lib\junit-4.10.jar;..\lib\mockito-all-1.9.0.jar;..\lib\rabbitmq-client.jar;..\lib\commons-io-1.2.jar;..\lib\commons-collections-3.2.1.jar;..\lib\commons-cli-1.2.jar junit.textui.TestRunner RunAllTests
pause