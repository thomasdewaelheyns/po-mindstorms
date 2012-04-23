javac -cp .;..\src\java;..\lib\junit-4.10.jar;..\lib\mockito-all-1.9.0.jar -d build -Xlint:unchecked -Xlint:deprecation RunAllTests.java

java -cp build;..\lib\junit-4.10.jar;..\lib\mockito-all-1.9.0.jar junit.textui.TestRunner RunAllTests
pause