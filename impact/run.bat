set CLASSPATH="src/main/java;src/main/resources"
javac src\main\java\impact\*java
java -cp %CLASSPATH% impact.Cell 4x4
