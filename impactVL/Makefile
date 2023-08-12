export CLASSPATH="build;impact.jar;src/main/resources"

impactVL: impact.jar
	java -cp $(CLASSPATH) net.coderextreme.impact.Impact

4x4: impact.jar
	java -cp $(CLASSPATH) net.coderextreme.impact.Cell 4x4

division: impact.jar
	java -cp $(CLASSPATH) net.coderextreme.impact.Cell

impact.jar: src/main/java/net/coderextreme/impact/*java
	(cd src/main/java; javac -cp $(CLASSPATH) net/coderextreme/impact/*java)
	(cd src/main/java; jar -cf ../../../impact.jar net)
	(cd src/main/resources; jar -uf ../../../impact.jar *gif)
