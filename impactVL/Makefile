export CLASSPATH="impact.jar;.;images"

impactVL: impact.jar
	java -classpath $(CLASSPATH) impact.Impact

4x4: impact.jar
	java -classpath $(CLASSPATH) impact.Cell 4x4

division: impact.jar
	java -classpath $(CLASSPATH) impact.Cell

impact.jar: impact/*java
	javac -classpath $(CLASSPATH) impact/*java
	jar -cf impact.jar impact
	(cd images; jar -uf ../impact.jar *gif)
