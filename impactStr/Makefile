export CLASSPATH=impactstr.jar:lib/jnlp.jar:.:images

sortmachine: impactstr.jar
	java -classpath $(CLASSPATH) impact.Cell sortmachine

impactVL: impactstr.jar
	java -classpath $(CLASSPATH) impact.Impact

4x4: impactstr.jar
	java -classpath $(CLASSPATH) impact.Cell 4x4

division: impactstr.jar
	java -classpath $(CLASSPATH) impact.Cell

impactstr.jar: impact/*java
	javac -classpath $(CLASSPATH) impact/*java
	jar -cf impactstr.jar impact sort*
	(cd images; jar -uf ../impactstr.jar *gif)
